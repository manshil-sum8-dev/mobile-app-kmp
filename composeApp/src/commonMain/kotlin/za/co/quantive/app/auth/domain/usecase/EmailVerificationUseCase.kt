package za.co.quantive.app.auth.domain.usecase

import za.co.quantive.app.auth.domain.model.AuthError
import za.co.quantive.app.auth.domain.repository.AuthRepository
import za.co.quantive.app.auth.domain.repository.Result

/**
 * Use case for email verification functionality.
 */
class EmailVerificationUseCase(
    private val authRepository: AuthRepository,
) {

    /**
     * Send email verification to user.
     */
    suspend fun sendVerification(email: String? = null): Result<Unit, AuthError> {
        // If email is provided, validate it
        if (email != null) {
            val validationError = validateEmail(email)
            if (validationError != null) {
                return Result.Error(validationError)
            }
        }

        return authRepository.sendEmailVerification(email?.trim())
    }

    /**
     * Verify email using token.
     */
    suspend fun verifyEmail(token: String): Result<Unit, AuthError> {
        val validationError = validateToken(token)
        if (validationError != null) {
            return Result.Error(validationError)
        }

        return authRepository.verifyEmail(token.trim())
    }

    /**
     * Validate email format.
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
     * Validate verification token.
     */
    private fun validateToken(token: String): AuthError? {
        return when {
            token.isBlank() -> AuthError.ValidationError(
                field = "token",
                message = "Verification token is required",
            )

            token.length < 10 -> AuthError.ValidationError(
                field = "token",
                message = "Invalid verification token format",
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
}
