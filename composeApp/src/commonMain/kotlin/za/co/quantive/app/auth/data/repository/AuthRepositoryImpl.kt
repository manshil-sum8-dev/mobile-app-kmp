package za.co.quantive.app.auth.data.repository

import za.co.quantive.app.auth.AuthService
import za.co.quantive.app.auth.Session
import za.co.quantive.app.auth.domain.model.AuthError
import za.co.quantive.app.auth.domain.model.AuthTokens
import za.co.quantive.app.auth.domain.model.AuthUser
import za.co.quantive.app.auth.domain.model.UserSession
import za.co.quantive.app.auth.domain.model.toAuthError
import za.co.quantive.app.auth.domain.repository.AuthRepository
import za.co.quantive.app.auth.domain.repository.Result
import za.co.quantive.app.auth.domain.repository.error
import za.co.quantive.app.auth.domain.repository.success
import za.co.quantive.app.security.SecureStore

/**
 * Implementation of AuthRepository using Supabase backend and secure local storage.
 * Handles the mapping between domain models and data layer entities.
 */
class AuthRepositoryImpl(
    private val authService: AuthService,
    private val secureStore: SecureStore,
) : AuthRepository {

    override suspend fun signUp(email: String, password: String): Result<UserSession, AuthError> {
        return try {
            val session = authService.signUp(email, password)
            val userSession = session.toDomainUserSession(email)

            // Save session to secure storage
            secureStore.saveSession(session)

            success(userSession)
        } catch (e: Exception) {
            error(mapToAuthError(e))
        }
    }

    override suspend fun signIn(email: String, password: String): Result<UserSession, AuthError> {
        return try {
            val session = authService.signIn(email, password)
            val userSession = session.toDomainUserSession(email)

            // Save session to secure storage
            secureStore.saveSession(session)

            success(userSession)
        } catch (e: Exception) {
            error(mapToAuthError(e))
        }
    }

    override suspend fun signOut(): Result<Unit, AuthError> {
        return try {
            // Clear session from secure storage
            secureStore.clearSession()

            // TODO: Call backend logout endpoint when available
            // authService.signOut()

            success(Unit)
        } catch (e: Exception) {
            error(mapToAuthError(e))
        }
    }

    override suspend fun refreshSession(): Result<UserSession, AuthError> {
        return try {
            val currentSession = secureStore.getSession()
                ?: return error(AuthError.SessionInvalid())

            val newSession = authService.refresh(currentSession.refreshToken)
            val userSession = newSession.toDomainUserSession()

            // Save updated session
            secureStore.saveSession(newSession)

            success(userSession)
        } catch (e: Exception) {
            // Clear invalid session
            secureStore.clearSession()
            error(mapToAuthError(e))
        }
    }

    override suspend fun getCurrentSession(): UserSession? {
        return try {
            val session = secureStore.getSession() ?: return null

            // Check if session is expired
            val currentTime = kotlinx.datetime.Clock.System.now().epochSeconds
            if (session.isExpired(currentTime)) {
                secureStore.clearSession()
                return null
            }

            session.toDomainUserSession()
        } catch (e: Exception) {
            println("SECURITY: Failed to get current session: ${e.message}")
            null
        }
    }

    override suspend fun isAuthenticated(): Boolean {
        return getCurrentSession() != null
    }

    override suspend fun getCurrentUser(): AuthUser? {
        return getCurrentSession()?.user
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit, AuthError> {
        // TODO: Implement password reset with Supabase Auth API
        return error(AuthError.UnknownError("Password reset not yet implemented"))
    }

    override suspend fun resetPassword(token: String, newPassword: String): Result<Unit, AuthError> {
        // TODO: Implement password reset with Supabase Auth API
        return error(AuthError.UnknownError("Password reset not yet implemented"))
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit, AuthError> {
        // TODO: Implement password change with Supabase Auth API
        return error(AuthError.UnknownError("Password change not yet implemented"))
    }

    override suspend fun sendEmailVerification(email: String?): Result<Unit, AuthError> {
        // TODO: Implement email verification with Supabase Auth API
        return error(AuthError.UnknownError("Email verification not yet implemented"))
    }

    override suspend fun verifyEmail(token: String): Result<Unit, AuthError> {
        // TODO: Implement email verification with Supabase Auth API
        return error(AuthError.UnknownError("Email verification not yet implemented"))
    }

    override suspend fun updateProfile(updates: Map<String, Any>): Result<AuthUser, AuthError> {
        // TODO: Implement profile update with Supabase Auth API
        return error(AuthError.UnknownError("Profile update not yet implemented"))
    }

    override suspend fun deleteAccount(): Result<Unit, AuthError> {
        // TODO: Implement account deletion with Supabase Auth API
        return error(AuthError.UnknownError("Account deletion not yet implemented"))
    }

    /**
     * Convert data layer Session to domain UserSession.
     */
    private fun Session.toDomainUserSession(email: String? = null): UserSession {
        val user = AuthUser(
            id = userId,
            email = email ?: "unknown@example.com", // TODO: Get from token or API
            emailVerified = false, // TODO: Get from API
            createdAt = kotlinx.datetime.Clock.System.now().toString(),
            updatedAt = kotlinx.datetime.Clock.System.now().toString(),
        )

        val tokens = AuthTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresAt = expiresAt,
        )

        return UserSession(
            user = user,
            tokens = tokens,
        )
    }

    /**
     * Map exceptions to domain-specific authentication errors.
     */
    private fun mapToAuthError(exception: Exception): AuthError {
        return when {
            exception.message?.contains("Invalid email or password", ignoreCase = true) == true ||
                exception.message?.contains("Invalid login credentials", ignoreCase = true) == true ->
                AuthError.InvalidCredentials()

            exception.message?.contains("Email already exists", ignoreCase = true) == true ||
                exception.message?.contains("User already registered", ignoreCase = true) == true ->
                AuthError.EmailAlreadyExists()

            exception.message?.contains("Weak password", ignoreCase = true) == true ||
                exception.message?.contains("Password should be at least", ignoreCase = true) == true ->
                AuthError.WeakPassword()

            exception.message?.contains("Invalid email", ignoreCase = true) == true ->
                AuthError.InvalidEmail()

            exception.message?.contains("Email not confirmed", ignoreCase = true) == true ->
                AuthError.EmailNotVerified()

            exception.message?.contains("Too many requests", ignoreCase = true) == true ->
                AuthError.TooManyAttempts()

            exception.message?.contains("Token expired", ignoreCase = true) == true ||
                exception.message?.contains("JWT expired", ignoreCase = true) == true ->
                AuthError.TokenExpired()

            exception.message?.contains("refresh_token", ignoreCase = true) == true ->
                AuthError.RefreshTokenInvalid()

            else -> exception.toAuthError()
        }
    }
}
