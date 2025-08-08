package za.co.quantive.app.auth.domain.service

import za.co.quantive.app.auth.domain.model.AuthError
import za.co.quantive.app.auth.domain.model.AuthUser
import za.co.quantive.app.auth.domain.model.UserSession
import za.co.quantive.app.auth.domain.model.toAuthError
import za.co.quantive.app.auth.domain.repository.AuthRepository
import za.co.quantive.app.auth.domain.repository.Result
import za.co.quantive.app.auth.domain.usecase.ChangePasswordUseCase
import za.co.quantive.app.auth.domain.usecase.EmailVerificationUseCase
import za.co.quantive.app.auth.domain.usecase.ForgotPasswordUseCase
import za.co.quantive.app.auth.domain.usecase.GetCurrentSessionUseCase
import za.co.quantive.app.auth.domain.usecase.SignInUseCase
import za.co.quantive.app.auth.domain.usecase.SignOutUseCase
import za.co.quantive.app.auth.domain.usecase.SignUpUseCase
import za.co.quantive.app.auth.domain.usecase.UpdateProfileUseCase

/**
 * High-level authentication service that coordinates all auth-related operations.
 * Provides a clean API for the presentation layer with comprehensive error handling.
 */
class AuthService(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,

    // Use cases
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getCurrentSessionUseCase: GetCurrentSessionUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val emailVerificationUseCase: EmailVerificationUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) {

    // Authentication operations

    /**
     * Sign in user with email and password.
     */
    suspend fun signIn(email: String, password: String): Result<UserSession, AuthError> {
        return signInUseCase.execute(email, password)
    }

    /**
     * Sign up new user with email and password.
     */
    suspend fun signUp(email: String, password: String, confirmPassword: String): Result<UserSession, AuthError> {
        return signUpUseCase.execute(email, password, confirmPassword)
    }

    /**
     * Sign out current user.
     */
    suspend fun signOut(): Result<Unit, AuthError> {
        return signOutUseCase.execute()
    }

    // Session management

    /**
     * Get current user session with automatic refresh.
     */
    suspend fun getCurrentSession(): UserSession? {
        return getCurrentSessionUseCase.execute()
    }

    /**
     * Check if user is authenticated.
     */
    suspend fun isAuthenticated(): Boolean {
        return getCurrentSessionUseCase.isAuthenticated()
    }

    /**
     * Get current user information.
     */
    suspend fun getCurrentUser(): AuthUser? {
        return getCurrentSession()?.user
    }

    /**
     * Get valid access token, handling refresh automatically.
     */
    suspend fun getValidAccessToken(): String? {
        return tokenManager.getValidAccessToken()
    }

    // Password management

    /**
     * Send password reset email.
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit, AuthError> {
        return forgotPasswordUseCase.execute(email)
    }

    /**
     * Reset password using token.
     */
    suspend fun resetPassword(token: String, newPassword: String, confirmPassword: String): Result<Unit, AuthError> {
        return forgotPasswordUseCase.resetPassword(token, newPassword, confirmPassword)
    }

    /**
     * Change password for authenticated user.
     */
    suspend fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String): Result<Unit, AuthError> {
        return changePasswordUseCase.execute(currentPassword, newPassword, confirmPassword)
    }

    // Email verification

    /**
     * Send email verification.
     */
    suspend fun sendEmailVerification(email: String? = null): Result<Unit, AuthError> {
        return emailVerificationUseCase.sendVerification(email)
    }

    /**
     * Verify email using token.
     */
    suspend fun verifyEmail(token: String): Result<Unit, AuthError> {
        return emailVerificationUseCase.verifyEmail(token)
    }

    // Profile management

    /**
     * Update user profile.
     */
    suspend fun updateProfile(updates: Map<String, Any>): Result<AuthUser, AuthError> {
        return updateProfileUseCase.execute(updates)
    }

    /**
     * Update user email.
     */
    suspend fun updateEmail(newEmail: String): Result<AuthUser, AuthError> {
        return updateProfileUseCase.updateEmail(newEmail)
    }

    /**
     * Update user phone.
     */
    suspend fun updatePhone(newPhone: String): Result<AuthUser, AuthError> {
        return updateProfileUseCase.updatePhone(newPhone)
    }

    // Token management

    /**
     * Force token refresh.
     */
    suspend fun refreshTokens(): Boolean {
        return tokenManager.refreshTokens()
    }

    /**
     * Clear all authentication data.
     */
    suspend fun clearAuthData() {
        tokenManager.clearTokens()
    }

    // Security utilities

    /**
     * Validate token format.
     */
    fun isValidTokenFormat(token: String): Boolean {
        return tokenManager.isValidTokenFormat(token)
    }

    /**
     * Check if session requires refresh.
     */
    suspend fun sessionRequiresRefresh(): Boolean {
        return tokenManager.needsRefresh()
    }

    // Helper methods for UI state

    /**
     * Get authentication state for UI.
     */
    suspend fun getAuthState(): AuthState {
        return try {
            val session = getCurrentSession()
            if (session != null) {
                if (session.isValid()) {
                    AuthState.Authenticated(session)
                } else {
                    AuthState.TokenExpired
                }
            } else {
                AuthState.Unauthenticated
            }
        } catch (e: Exception) {
            AuthState.Error(e.toAuthError())
        }
    }
}

/**
 * Authentication state for UI binding.
 */
sealed class AuthState {
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Authenticated(val session: UserSession) : AuthState()
    object TokenExpired : AuthState()
    data class Error(val error: AuthError) : AuthState()
}
