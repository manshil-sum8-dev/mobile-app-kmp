package za.co.quantive.app.auth.domain.usecase

import za.co.quantive.app.auth.domain.model.AuthError
import za.co.quantive.app.auth.domain.model.UserSession
import za.co.quantive.app.auth.domain.repository.AuthRepository
import za.co.quantive.app.auth.domain.repository.Result

/**
 * Use case for user sign-up with comprehensive validation.
 */
class SignUpUseCase(
    private val authRepository: AuthRepository,
) {

    /**
     * Execute sign-up with email and password validation.
     */
    suspend fun execute(email: String, password: String, confirmPassword: String): Result<UserSession, AuthError> {
        // Validate input
        val validationError = validateInput(email, password, confirmPassword)
        if (validationError != null) {
            return Result.Error(validationError)
        }

        // Attempt sign-up
        return authRepository.signUp(email.trim(), password)
    }

    /**
     * Validate sign-up input parameters.
     */
    private fun validateInput(email: String, password: String, confirmPassword: String): AuthError? {
        return when {
            email.isBlank() -> AuthError.ValidationError(
                field = "email",
                message = "Email is required",
            )

            !isValidEmail(email) -> AuthError.InvalidEmail()

            password.isBlank() -> AuthError.ValidationError(
                field = "password",
                message = "Password is required",
            )

            !isStrongPassword(password) -> AuthError.WeakPassword(
                message = "Password must be at least 8 characters with uppercase, lowercase, and number",
            )

            password != confirmPassword -> AuthError.ValidationError(
                field = "confirmPassword",
                message = "Passwords do not match",
            )

            else -> null
        }
    }

    /**
     * Email validation with comprehensive checks.
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")
        return emailRegex.matches(email.trim())
    }

    /**
     * Password strength validation.
     * Requires: 8+ characters, uppercase, lowercase, digit
     */
    private fun isStrongPassword(password: String): Boolean {
        return password.length >= 8 &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { it.isDigit() }
    }
}
