package za.co.quantive.app.auth.domain.usecase

import za.co.quantive.app.auth.domain.model.AuthError
import za.co.quantive.app.auth.domain.model.UserSession
import za.co.quantive.app.auth.domain.repository.AuthRepository
import za.co.quantive.app.auth.domain.repository.Result

/**
 * Use case for user sign-in with validation and error handling.
 */
class SignInUseCase(
    private val authRepository: AuthRepository,
) {

    /**
     * Execute sign-in with email and password validation.
     */
    suspend fun execute(email: String, password: String): Result<UserSession, AuthError> {
        // Validate input
        val validationError = validateInput(email, password)
        if (validationError != null) {
            return Result.Error(validationError)
        }

        // Attempt sign-in
        return authRepository.signIn(email.trim(), password)
    }

    /**
     * Validate sign-in input parameters.
     */
    private fun validateInput(email: String, password: String): AuthError? {
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

            password.length < 6 -> AuthError.ValidationError(
                field = "password",
                message = "Password must be at least 6 characters",
            )

            else -> null
        }
    }

    /**
     * Basic email validation.
     */
    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") &&
            email.contains(".") &&
            email.trim() == email &&
            email.length >= 5
    }
}
