package za.co.quantive.app.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import za.co.quantive.app.auth.Session

/**
 * Enterprise-grade secure storage implementation using EncryptedSharedPreferences
 * with Android Keystore hardware-backed encryption.
 */
actual object SecureStore {
    private const val PREF_FILE = "quantive_secure_store"
    private const val KEY_SESSION = "session_json"
    private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_ROLE = "user_role"

    private lateinit var encryptedPrefs: SharedPreferences
    private var isInitialized = false

    fun init(context: Context) {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            encryptedPrefs = EncryptedSharedPreferences.create(
                context,
                PREF_FILE,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
            isInitialized = true
        } catch (e: Exception) {
            // Log security event - initialization failure
            println("SECURITY: SecureStore initialization failed: ${e.javaClass.simpleName}")
            throw SecurityException("Failed to initialize secure storage", e)
        }
    }

    actual suspend fun saveSession(session: Session?) {
        if (!isInitialized) {
            println("SECURITY: Attempted to save session before initialization")
            return
        }

        try {
            val edit = encryptedPrefs.edit()
            if (session == null) {
                edit.remove(KEY_SESSION)
                println("SECURITY: Session cleared from secure storage")
            } else {
                val json = Json.encodeToString(session)
                edit.putString(KEY_SESSION, json)
                println("SECURITY: Session saved to encrypted storage")
            }
            edit.apply()
        } catch (e: Exception) {
            println("SECURITY: Failed to save session: ${e.javaClass.simpleName}")
            throw SecurityException("Failed to save session securely", e)
        }
    }

    actual suspend fun getSession(): Session? {
        if (!isInitialized) {
            println("SECURITY: Attempted to get session before initialization")
            return null
        }

        return try {
            val json = encryptedPrefs.getString(KEY_SESSION, null) ?: return null
            Json.decodeFromString<Session>(json)
        } catch (e: Exception) {
            println("SECURITY: Failed to retrieve session: ${e.javaClass.simpleName}")
            // Clear corrupted session data
            clearSession()
            null
        }
    }

    actual suspend fun clearSession() {
        if (!isInitialized) return

        try {
            encryptedPrefs.edit().remove(KEY_SESSION).apply()
            println("SECURITY: Session cleared from encrypted storage")
        } catch (e: Exception) {
            println("SECURITY: Failed to clear session: ${e.javaClass.simpleName}")
        }
    }

    actual suspend fun setOnboardingCompleted(completed: Boolean) {
        if (!isInitialized) return

        try {
            encryptedPrefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
        } catch (e: Exception) {
            println("SECURITY: Failed to save onboarding status: ${e.javaClass.simpleName}")
        }
    }

    actual suspend fun isOnboardingCompleted(): Boolean {
        if (!isInitialized) return false

        return try {
            encryptedPrefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        } catch (e: Exception) {
            println("SECURITY: Failed to get onboarding status: ${e.javaClass.simpleName}")
            false
        }
    }

    actual suspend fun saveUserProfile(name: String, role: String) {
        if (!isInitialized) return

        try {
            encryptedPrefs.edit()
                .putString(KEY_USER_NAME, name)
                .putString(KEY_USER_ROLE, role)
                .apply()
        } catch (e: Exception) {
            println("SECURITY: Failed to save user profile: ${e.javaClass.simpleName}")
        }
    }

    actual suspend fun getUserProfile(): Pair<String, String>? {
        if (!isInitialized) return null

        return try {
            val name = encryptedPrefs.getString(KEY_USER_NAME, null)
            val role = encryptedPrefs.getString(KEY_USER_ROLE, null)
            if (name != null && role != null) Pair(name, role) else null
        } catch (e: Exception) {
            println("SECURITY: Failed to get user profile: ${e.javaClass.simpleName}")
            null
        }
    }
}
