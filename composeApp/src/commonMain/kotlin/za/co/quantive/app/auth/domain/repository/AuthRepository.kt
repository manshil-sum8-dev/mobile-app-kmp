package za.co.quantive.app.auth.domain.repository

import za.co.quantive.app.auth.domain.model.AuthError
import za.co.quantive.app.auth.domain.model.AuthUser
import za.co.quantive.app.auth.domain.model.UserSession

/**
 * Domain repository interface for authentication operations.
 * Defines the contract for authentication data access without implementation details.
 */
interface AuthRepository {

    // Core authentication operations

    /**
     * Sign up a new user with email and password.
     * @param email User's email address
     * @param password User's password
     * @return Result containing UserSession or AuthError
     */
    suspend fun signUp(email: String, password: String): Result<UserSession, AuthError>

    /**
     * Sign in an existing user with email and password.
     * @param email User's email address
     * @param password User's password
     * @return Result containing UserSession or AuthError
     */
    suspend fun signIn(email: String, password: String): Result<UserSession, AuthError>

    /**
     * Sign out the current user and clear all session data.
     * @return Result indicating success or AuthError
     */
    suspend fun signOut(): Result<Unit, AuthError>

    /**
     * Refresh the current session using the refresh token.
     * @return Result containing updated UserSession or AuthError
     */
    suspend fun refreshSession(): Result<UserSession, AuthError>

    // Session management

    /**
     * Get the current user session if valid.
     * @return Current UserSession or null if not authenticated
     */
    suspend fun getCurrentSession(): UserSession?

    /**
     * Check if user is currently authenticated with a valid session.
     * @return true if authenticated, false otherwise
     */
    suspend fun isAuthenticated(): Boolean

    /**
     * Get the current authenticated user.
     * @return Current AuthUser or null if not authenticated
     */
    suspend fun getCurrentUser(): AuthUser?

    // Password management

    /**
     * Send password reset email to the user.
     * @param email User's email address
     * @return Result indicating success or AuthError
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit, AuthError>

    /**
     * Reset password using reset token.
     * @param token Password reset token
     * @param newPassword New password
     * @return Result indicating success or AuthError
     */
    suspend fun resetPassword(token: String, newPassword: String): Result<Unit, AuthError>

    /**
     * Change password for authenticated user.
     * @param currentPassword Current password
     * @param newPassword New password
     * @return Result indicating success or AuthError
     */
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit, AuthError>

    // Email verification

    /**
     * Send email verification to the user.
     * @param email User's email address (optional, uses current user if null)
     * @return Result indicating success or AuthError
     */
    suspend fun sendEmailVerification(email: String? = null): Result<Unit, AuthError>

    /**
     * Verify email using verification token.
     * @param token Email verification token
     * @return Result indicating success or AuthError
     */
    suspend fun verifyEmail(token: String): Result<Unit, AuthError>

    // User profile management

    /**
     * Update user profile information.
     * @param updates Map of profile fields to update
     * @return Result containing updated AuthUser or AuthError
     */
    suspend fun updateProfile(updates: Map<String, Any>): Result<AuthUser, AuthError>

    /**
     * Delete user account and all associated data.
     * @return Result indicating success or AuthError
     */
    suspend fun deleteAccount(): Result<Unit, AuthError>
}

/**
 * Result wrapper for authentication operations.
 * Provides a clean way to handle success and error states.
 */
sealed class Result<out T, out E> {
    data class Success<T>(val value: T) : Result<T, Nothing>()
    data class Error<E>(val error: E) : Result<Nothing, E>()

    inline fun <R> map(transform: (T) -> R): Result<R, E> {
        return when (this) {
            is Success -> Success(transform(value))
            is Error -> Error(error)
        }
    }

    inline fun <R> mapError(transform: (E) -> R): Result<T, R> {
        return when (this) {
            is Success -> Success(value)
            is Error -> Error(transform(error))
        }
    }

    inline fun onSuccess(action: (T) -> Unit): Result<T, E> {
        if (this is Success) action(value)
        return this
    }

    inline fun onError(action: (E) -> Unit): Result<T, E> {
        if (this is Error) action(error)
        return this
    }

    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Error -> null
    }

    fun getErrorOrNull(): E? = when (this) {
        is Success -> null
        is Error -> error
    }
}

/**
 * Helper function to create success result.
 */
fun <T> success(value: T): Result<T, Nothing> = Result.Success(value)

/**
 * Helper function to create error result.
 */
fun <E> error(error: E): Result<Nothing, E> = Result.Error(error)
