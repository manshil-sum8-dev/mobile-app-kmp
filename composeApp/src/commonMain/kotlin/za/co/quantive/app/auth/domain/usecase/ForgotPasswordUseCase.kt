package za.co.quantive.app.auth.domain.usecase

import za.co.quantive.app.auth.domain.model.AuthError
import za.co.quantive.app.auth.domain.repository.AuthRepository
import za.co.quantive.app.auth.domain.repository.Result

/**
 * Use case for password reset functionality.
 */
class ForgotPasswordUseCase(
    private val authRepository: AuthRepository,
) {

    /**
     * Send password reset email with validation.
     */
    suspend fun execute(email: String): Result<Unit, AuthError> {
        // Validate email
        val validationError = validateEmail(email)
        if (validationError != null) {
            return Result.Error(validationError)
        }

        return authRepository.sendPasswordResetEmail(email.trim())
    }

    /**
     * Reset password using token.
     */
    suspend fun resetPassword(token: String, newPassword: String, confirmPassword: String): Result<Unit, AuthError> {
        // Validate input
        val validationError = validatePasswordReset(token, newPassword, confirmPassword)
        if (validationError != null) {
            return Result.Error(validationError)
        }

        return authRepository.resetPassword(token, newPassword)
    }

    /**
     * Validate email for password reset.
     */
    private fun validateEmail(email: String): AuthError? {
        return when {
            email.isBlank() -> AuthError.ValidationError(
                field = "email",
                message = "Email is required",
            )

            !isValidEmail(email) -> AuthError.InvalidEmail()

            else -> null
        }
    }

    /**
     * Validate password reset parameters.
     */
    private fun validatePasswordReset(token: String, newPassword: String, confirmPassword: String): AuthError? {
        return when {
            token.isBlank() -> AuthError.ValidationError(
                field = "token",
                message = "Reset token is required",
            )

            newPassword.isBlank() -> AuthError.ValidationError(
                field = "password",
                message = "New password is required",
            )

            !isStrongPassword(newPassword) -> AuthError.WeakPassword(
                message = "Password must be at least 8 characters with uppercase, lowercase, and number",
            )

            newPassword != confirmPassword -> AuthError.ValidationError(
                field = "confirmPassword",
                message = "Passwords do not match",
            )

            else -> null
        }
    }

    /**
     * Email validation.
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")
        return emailRegex.matches(email.trim())
    }

    /**
     * Password strength validation.
     */
    private fun isStrongPassword(password: String): Boolean {
        return password.length >= 8 &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { it.isDigit() }
    }
}
