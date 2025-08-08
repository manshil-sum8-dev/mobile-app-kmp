package za.co.quantive.app.auth.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain entity representing an authenticated user.
 * Contains core user information without platform-specific details.
 */
@Serializable
data class AuthUser(
    val id: String,
    val email: String,
    val emailVerified: Boolean = false,
    val phone: String? = null,
    val phoneVerified: Boolean = false,
    val role: String = "authenticated",
    val lastSignInAt: String? = null,
    val createdAt: String,
    val updatedAt: String,
)

/**
 * Domain entity representing authentication credentials.
 */
data class AuthCredentials(
    val email: String,
    val password: String,
)

/**
 * Domain entity representing authentication tokens.
 */
@Serializable
data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long,
    val tokenType: String = "Bearer",
) {
    /**
     * Check if the access token is expired or will expire soon.
     * @param bufferSeconds Buffer time in seconds before actual expiry (default 5 minutes)
     */
    fun isExpired(bufferSeconds: Long = 300): Boolean {
        val currentTime = kotlinx.datetime.Clock.System.now().epochSeconds
        return currentTime >= (expiresAt - bufferSeconds)
    }

    /**
     * Check if the token is valid (not expired).
     */
    fun isValid(): Boolean = !isExpired()
}

/**
 * Domain entity representing a complete user session.
 */
@Serializable
data class UserSession(
    val user: AuthUser,
    val tokens: AuthTokens,
    val deviceId: String? = null,
    val lastActivity: String? = null,
) {
    /**
     * Check if the session is valid.
     */
    fun isValid(): Boolean = tokens.isValid()

    /**
     * Check if the session requires refresh.
     */
    fun requiresRefresh(): Boolean = tokens.isExpired(bufferSeconds = 600) // 10 minutes buffer
}
