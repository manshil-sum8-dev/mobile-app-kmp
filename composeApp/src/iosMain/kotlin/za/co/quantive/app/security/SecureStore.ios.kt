package za.co.quantive.app.security

import za.co.quantive.app.auth.Session
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

actual object SecureStore {
    private const val SESSION_KEY = "secure_session"
    private const val ONBOARDING_KEY = "onboarding_completed"
    private const val USER_NAME_KEY = "user_name"
    private const val USER_ROLE_KEY = "user_role"
    
    // TODO: Replace with proper Keychain implementation
    // For now using NSUserDefaults as a temporary solution
    actual suspend fun saveSession(session: Session?) {
        if (session == null) {
            clearSession()
            return
        }
        
        try {
            val json = Json.encodeToString<Session>(session)
            NSUserDefaults.standardUserDefaults.setObject(json, SESSION_KEY)
        } catch (e: Throwable) {
            // Handle serialization error
        }
    }

    actual suspend fun getSession(): Session? {
        return try {
            val json = NSUserDefaults.standardUserDefaults.stringForKey(SESSION_KEY) ?: return null
            Json.decodeFromString<Session>(json)
        } catch (e: Throwable) {
            null
        }
    }

    actual suspend fun clearSession() {
        NSUserDefaults.standardUserDefaults.removeObjectForKey(SESSION_KEY)
    }

    actual suspend fun setOnboardingCompleted(completed: Boolean) {
        NSUserDefaults.standardUserDefaults.setBool(completed, ONBOARDING_KEY)
    }

    actual suspend fun isOnboardingCompleted(): Boolean {
        return NSUserDefaults.standardUserDefaults.boolForKey(ONBOARDING_KEY)
    }

    actual suspend fun saveUserProfile(name: String, role: String) {
        NSUserDefaults.standardUserDefaults.setObject(name, USER_NAME_KEY)
        NSUserDefaults.standardUserDefaults.setObject(role, USER_ROLE_KEY)
    }

    actual suspend fun getUserProfile(): Pair<String, String>? {
        val name = NSUserDefaults.standardUserDefaults.stringForKey(USER_NAME_KEY)
        val role = NSUserDefaults.standardUserDefaults.stringForKey(USER_ROLE_KEY)
        return if (name != null && role != null) Pair(name, role) else null
    }
}