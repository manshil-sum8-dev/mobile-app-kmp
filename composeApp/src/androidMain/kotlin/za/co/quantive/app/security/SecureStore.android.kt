package za.co.quantive.app.security

import android.content.Context
import android.content.SharedPreferences
import za.co.quantive.app.auth.Session
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// NOTE: For MVP we use SharedPreferences. TODO: Switch to EncryptedSharedPreferences before production.
actual object SecureStore {
    private const val PREF = "quantive_secure_store"
    private const val KEY_SESSION = "session_json"
    private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_ROLE = "user_role"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
    }

    actual suspend fun saveSession(session: Session?) {
        if (!::prefs.isInitialized) return
        val edit = prefs.edit()
        if (session == null) {
            edit.remove(KEY_SESSION)
        } else {
            val json = Json.encodeToString(session)
            edit.putString(KEY_SESSION, json)
        }
        edit.apply()
    }

    actual suspend fun getSession(): Session? {
        if (!::prefs.isInitialized) return null
        val json = prefs.getString(KEY_SESSION, null) ?: return null
        return runCatching { Json.decodeFromString<Session>(json) }.getOrNull()
    }

    actual suspend fun clearSession() {
        if (!::prefs.isInitialized) return
        prefs.edit().remove(KEY_SESSION).apply()
    }

    actual suspend fun setOnboardingCompleted(completed: Boolean) {
        if (!::prefs.isInitialized) return
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }

    actual suspend fun isOnboardingCompleted(): Boolean {
        if (!::prefs.isInitialized) return false
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    actual suspend fun saveUserProfile(name: String, role: String) {
        if (!::prefs.isInitialized) return
        prefs.edit()
            .putString(KEY_USER_NAME, name)
            .putString(KEY_USER_ROLE, role)
            .apply()
    }

    actual suspend fun getUserProfile(): Pair<String, String>? {
        if (!::prefs.isInitialized) return null
        val name = prefs.getString(KEY_USER_NAME, null)
        val role = prefs.getString(KEY_USER_ROLE, null)
        return if (name != null && role != null) Pair(name, role) else null
    }
}
