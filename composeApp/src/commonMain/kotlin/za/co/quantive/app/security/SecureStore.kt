package za.co.quantive.app.security

import za.co.quantive.app.auth.Session

expect object SecureStore {
    suspend fun saveSession(session: Session?)
    suspend fun getSession(): Session?
    suspend fun clearSession()
    
    // Onboarding and user preferences
    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun isOnboardingCompleted(): Boolean
    suspend fun saveUserProfile(name: String, role: String)
    suspend fun getUserProfile(): Pair<String, String>? // name, role
}
