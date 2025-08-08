package za.co.quantive.app.security

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.CoreFoundation.CFDataCreate
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFDataRef
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFDictionarySetValue
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFStringCreateWithCString
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFBooleanTrue
import platform.CoreFoundation.kCFStringEncodingUTF8
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.errSecItemNotFound
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccessible
import platform.Security.kSecAttrAccessibleWhenUnlockedThisDeviceOnly
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData
import za.co.quantive.app.auth.Session

/**
 * Enterprise-grade secure storage implementation using iOS Keychain Services
 * with hardware-backed security (Secure Enclave when available).
 */
actual object SecureStore {
    private const val SERVICE_NAME = "za.co.quantive.app"
    private const val SESSION_ACCOUNT = "user_session"
    private const val ONBOARDING_ACCOUNT = "onboarding_status"
    private const val USER_NAME_ACCOUNT = "user_name"
    private const val USER_ROLE_ACCOUNT = "user_role"
    actual suspend fun saveSession(session: Session?) {
        if (session == null) {
            clearSession()
            return
        }

        try {
            val json = Json.encodeToString<Session>(session)
            val success = saveToKeychain(SESSION_ACCOUNT, json)
            if (success) {
                println("SECURITY: Session saved to iOS Keychain")
            } else {
                println("SECURITY: Failed to save session to Keychain")
                throw RuntimeException("Failed to save session to Keychain")
            }
        } catch (e: Throwable) {
            println("SECURITY: Session serialization failed: ${e.message}")
            throw RuntimeException("Failed to save session securely", e)
        }
    }

    actual suspend fun getSession(): Session? {
        return try {
            val json = getFromKeychain(SESSION_ACCOUNT) ?: return null
            Json.decodeFromString<Session>(json)
        } catch (e: Throwable) {
            println("SECURITY: Failed to retrieve session: ${e.message}")
            // Clear corrupted session data
            clearSession()
            null
        }
    }

    actual suspend fun clearSession() {
        val success = deleteFromKeychain(SESSION_ACCOUNT)
        if (success) {
            println("SECURITY: Session cleared from iOS Keychain")
        } else {
            println("SECURITY: Failed to clear session from Keychain")
        }
    }

    actual suspend fun setOnboardingCompleted(completed: Boolean) {
        try {
            val value = if (completed) "true" else "false"
            saveToKeychain(ONBOARDING_ACCOUNT, value)
        } catch (e: Throwable) {
            println("SECURITY: Failed to save onboarding status: ${e.message}")
        }
    }

    actual suspend fun isOnboardingCompleted(): Boolean {
        return try {
            val value = getFromKeychain(ONBOARDING_ACCOUNT) ?: "false"
            value == "true"
        } catch (e: Throwable) {
            println("SECURITY: Failed to get onboarding status: ${e.message}")
            false
        }
    }

    actual suspend fun saveUserProfile(name: String, role: String) {
        try {
            saveToKeychain(USER_NAME_ACCOUNT, name)
            saveToKeychain(USER_ROLE_ACCOUNT, role)
        } catch (e: Throwable) {
            println("SECURITY: Failed to save user profile: ${e.message}")
        }
    }

    actual suspend fun getUserProfile(): Pair<String, String>? {
        return try {
            val name = getFromKeychain(USER_NAME_ACCOUNT)
            val role = getFromKeychain(USER_ROLE_ACCOUNT)
            if (name != null && role != null) Pair(name, role) else null
        } catch (e: Throwable) {
            println("SECURITY: Failed to get user profile: ${e.message}")
            null
        }
    }

    /**
     * Save data to iOS Keychain with hardware-backed security
     */
    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun saveToKeychain(account: String, value: String): Boolean {
        // First delete any existing item
        deleteFromKeychain(account)

        return memScoped {
            val query = CFDictionaryCreateMutable(null, 0, null, null)

            // Set keychain item attributes
            CFDictionarySetValue(query, kSecClass, kSecClassGenericPassword)
            CFDictionarySetValue(query, kSecAttrService, CFStringCreateWithCString(null, SERVICE_NAME, kCFStringEncodingUTF8))
            CFDictionarySetValue(query, kSecAttrAccount, CFStringCreateWithCString(null, account, kCFStringEncodingUTF8))
            val data = value.encodeToByteArray()
            data.usePinned { pinned ->
                CFDictionarySetValue(query, kSecValueData, CFDataCreate(null, pinned.addressOf(0).reinterpret(), data.size.convert()))
            }

            // Use hardware-backed security when available
            CFDictionarySetValue(query, kSecAttrAccessible, kSecAttrAccessibleWhenUnlockedThisDeviceOnly)

            val result = SecItemAdd(query, null)
            CFRelease(query)

            result == errSecSuccess
        }
    }

    /**
     * Retrieve data from iOS Keychain
     */
    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun getFromKeychain(account: String): String? {
        return memScoped {
            val query = CFDictionaryCreateMutable(null, 0, null, null)

            CFDictionarySetValue(query, kSecClass, kSecClassGenericPassword)
            CFDictionarySetValue(query, kSecAttrService, CFStringCreateWithCString(null, SERVICE_NAME, kCFStringEncodingUTF8))
            CFDictionarySetValue(query, kSecAttrAccount, CFStringCreateWithCString(null, account, kCFStringEncodingUTF8))
            CFDictionarySetValue(query, kSecReturnData, kCFBooleanTrue)
            CFDictionarySetValue(query, kSecMatchLimit, kSecMatchLimitOne)

            val result = alloc<CFTypeRefVar>()
            val status = SecItemCopyMatching(query, result.ptr)
            CFRelease(query)

            if (status == errSecSuccess) {
                val data = result.value as CFDataRef?
                if (data != null) {
                    val length = CFDataGetLength(data).toInt()
                    val bytes = CFDataGetBytePtr(data)
                    if (bytes != null && length > 0) {
                        val byteArray = bytes.readBytes(length)
                        CFRelease(data)
                        byteArray.decodeToString()
                    } else {
                        CFRelease(data)
                        null
                    }
                } else {
                    null
                }
            } else {
                null
            }
        }
    }

    /**
     * Delete data from iOS Keychain
     */
    @OptIn(ExperimentalForeignApi::class)
    private fun deleteFromKeychain(account: String): Boolean {
        return memScoped {
            val query = CFDictionaryCreateMutable(null, 0, null, null)

            CFDictionarySetValue(query, kSecClass, kSecClassGenericPassword)
            CFDictionarySetValue(query, kSecAttrService, CFStringCreateWithCString(null, SERVICE_NAME, kCFStringEncodingUTF8))
            CFDictionarySetValue(query, kSecAttrAccount, CFStringCreateWithCString(null, account, kCFStringEncodingUTF8))

            val result = SecItemDelete(query)
            CFRelease(query)

            result == errSecSuccess || result == errSecItemNotFound
        }
    }
}
