package za.co.quantive.app.auth.domain.usecase

import za.co.quantive.app.auth.domain.model.AuthError
import za.co.quantive.app.auth.domain.repository.AuthRepository
import za.co.quantive.app.auth.domain.repository.Result

/**
 * Use case for changing user password while authenticated.
 */
class ChangePasswordUseCase(
    private val authRepository: AuthRepository,
) {

    /**
     * Change password for authenticated user.
     */
    suspend fun execute(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): Result<Unit, AuthError> {
        // Validate input
        val validationError = validateInput(currentPassword, newPassword, confirmPassword)
        if (validationError != null) {
            return Result.Error(validationError)
        }

        // Check if user is authenticated
        if (!authRepository.isAuthenticated()) {
            return Result.Error(AuthError.SessionInvalid())
        }

        return authRepository.changePassword(currentPassword, newPassword)
    }

    /**
     * Validate password change input.
     */
    private fun validateInput(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ): AuthError? {
        return when {
            currentPassword.isBlank() -> AuthError.ValidationError(
                field = "currentPassword",
                message = "Current password is required",
            )

            newPassword.isBlank() -> AuthError.ValidationError(
                field = "newPassword",
                message = "New password is required",
            )

            currentPassword == newPassword -> AuthError.ValidationError(
                field = "newPassword",
                message = "New password must be different from current password",
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
     * Password strength validation.
     */
    private fun isStrongPassword(password: String): Boolean {
        return password.length >= 8 &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { it.isDigit() }
    }
}
