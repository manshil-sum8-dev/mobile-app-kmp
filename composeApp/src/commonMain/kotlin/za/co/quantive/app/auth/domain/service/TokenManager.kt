package za.co.quantive.app.auth.domain.service

import za.co.quantive.app.auth.domain.model.AuthTokens
import za.co.quantive.app.auth.domain.model.UserSession

/**
 * Secure token management service for handling authentication tokens,
 * automatic refresh, and token lifecycle management.
 */
interface TokenManager {

    /**
     * Get current valid access token, refreshing if necessary.
     * @return Valid access token or null if authentication failed
     */
    suspend fun getValidAccessToken(): String?

    /**
     * Store authentication tokens securely.
     * @param tokens The authentication tokens to store
     */
    suspend fun storeTokens(tokens: AuthTokens)

    /**
     * Clear all stored tokens.
     */
    suspend fun clearTokens()

    /**
     * Check if tokens are expired or will expire soon.
     * @param bufferSeconds Buffer time in seconds before expiry (default 5 minutes)
     * @return true if tokens need refresh
     */
    suspend fun needsRefresh(bufferSeconds: Long = 300): Boolean

    /**
     * Refresh tokens if possible.
     * @return true if refresh was successful
     */
    suspend fun refreshTokens(): Boolean

    /**
     * Get current stored tokens.
     * @return Current tokens or null if not available
     */
    suspend fun getCurrentTokens(): AuthTokens?

    /**
     * Validate token format and basic properties.
     * @param token The token to validate
     * @return true if token appears valid
     */
    fun isValidTokenFormat(token: String): Boolean
}

/**
 * Implementation of TokenManager with secure storage and automatic refresh.
 */
class TokenManagerImpl(
    private val getSession: suspend () -> UserSession?,
    private val saveSession: suspend (UserSession) -> Unit,
    private val refreshSession: suspend () -> UserSession?,
    private val clearSession: suspend () -> Unit,
) : TokenManager {

    override suspend fun getValidAccessToken(): String? {
        val session = getSession() ?: return null

        // Check if token needs refresh
        if (session.tokens.isExpired()) {
            val refreshed = refreshTokens()
            if (!refreshed) {
                clearTokens()
                return null
            }
            // Get refreshed session
            val newSession = getSession() ?: return null
            return newSession.tokens.accessToken
        }

        return session.tokens.accessToken
    }

    override suspend fun storeTokens(tokens: AuthTokens) {
        // This is handled by the session storage in the repository
        // The tokens are part of the UserSession
    }

    override suspend fun clearTokens() {
        clearSession()
    }

    override suspend fun needsRefresh(bufferSeconds: Long): Boolean {
        val session = getSession() ?: return false
        return session.tokens.isExpired(bufferSeconds)
    }

    override suspend fun refreshTokens(): Boolean {
        return try {
            val refreshedSession = refreshSession()
            refreshedSession != null
        } catch (e: Exception) {
            println("SECURITY: Token refresh failed: ${e.message}")
            false
        }
    }

    override suspend fun getCurrentTokens(): AuthTokens? {
        return getSession()?.tokens
    }

    override fun isValidTokenFormat(token: String): Boolean {
        // Basic JWT format validation
        val parts = token.split(".")
        return parts.size == 3 && // Header.Payload.Signature
            parts.all { it.isNotEmpty() } &&
            token.length > 50 // Reasonable minimum length
    }
}

/**
 * Token refresh strategy interface for different refresh approaches.
 */
interface TokenRefreshStrategy {
    /**
     * Determine if tokens should be refreshed based on expiry and usage patterns.
     */
    fun shouldRefresh(tokens: AuthTokens): Boolean

    /**
     * Get the buffer time before expiry to trigger refresh.
     */
    fun getRefreshBuffer(): Long
}

/**
 * Conservative token refresh strategy - refreshes tokens well before expiry.
 */
class ConservativeRefreshStrategy : TokenRefreshStrategy {
    override fun shouldRefresh(tokens: AuthTokens): Boolean {
        return tokens.isExpired(getRefreshBuffer())
    }

    override fun getRefreshBuffer(): Long = 600L // 10 minutes before expiry
}

/**
 * Aggressive token refresh strategy - refreshes tokens closer to expiry.
 */
class AggressiveRefreshStrategy : TokenRefreshStrategy {
    override fun shouldRefresh(tokens: AuthTokens): Boolean {
        return tokens.isExpired(getRefreshBuffer())
    }

    override fun getRefreshBuffer(): Long = 300L // 5 minutes before expiry
}

/**
 * Adaptive token refresh strategy - adjusts refresh timing based on usage patterns.
 */
class AdaptiveRefreshStrategy(
    private var baseBuffer: Long = 600L,
    private val minBuffer: Long = 300L,
    private val maxBuffer: Long = 1800L, // 30 minutes
) : TokenRefreshStrategy {

    private var consecutiveFailures = 0

    override fun shouldRefresh(tokens: AuthTokens): Boolean {
        return tokens.isExpired(getRefreshBuffer())
    }

    override fun getRefreshBuffer(): Long {
        // Adjust buffer based on failure rate
        return when {
            consecutiveFailures > 3 -> maxBuffer
            consecutiveFailures > 1 -> baseBuffer + (consecutiveFailures * 300)
            else -> baseBuffer
        }.coerceIn(minBuffer, maxBuffer)
    }

    fun recordFailure() {
        consecutiveFailures++
    }

    fun recordSuccess() {
        consecutiveFailures = 0
    }
}
