package za.co.quantive.app.auth.domain.usecase

import za.co.quantive.app.auth.domain.model.AuthError
import za.co.quantive.app.auth.domain.model.AuthUser
import za.co.quantive.app.auth.domain.repository.AuthRepository
import za.co.quantive.app.auth.domain.repository.Result

/**
 * Use case for updating user profile information.
 */
class UpdateProfileUseCase(
    private val authRepository: AuthRepository,
) {

    /**
     * Update user profile with validation.
     */
    suspend fun execute(updates: Map<String, Any>): Result<AuthUser, AuthError> {
        // Check if user is authenticated
        if (!authRepository.isAuthenticated()) {
            return Result.Error(AuthError.SessionInvalid())
        }

        // Validate updates
        val validationError = validateUpdates(updates)
        if (validationError != null) {
            return Result.Error(validationError)
        }

        return authRepository.updateProfile(updates)
    }

    /**
     * Update user email with validation.
     */
    suspend fun updateEmail(newEmail: String): Result<AuthUser, AuthError> {
        val validationError = validateEmail(newEmail)
        if (validationError != null) {
            return Result.Error(validationError)
        }

        return execute(mapOf("email" to newEmail.trim()))
    }

    /**
     * Update user phone with validation.
     */
    suspend fun updatePhone(newPhone: String): Result<AuthUser, AuthError> {
        val validationError = validatePhone(newPhone)
        if (validationError != null) {
            return Result.Error(validationError)
        }

        return execute(mapOf("phone" to newPhone.trim()))
    }

    /**
     * Validate profile updates.
     */
    private fun validateUpdates(updates: Map<String, Any>): AuthError? {
        for ((field, value) in updates) {
            when (field) {
                "email" -> {
                    val email = value as? String ?: return AuthError.ValidationError(
                        field = field,
                        message = "Email must be a string",
                    )
                    val emailError = validateEmail(email)
                    if (emailError != null) return emailError
                }

                "phone" -> {
                    val phone = value as? String ?: return AuthError.ValidationError(
                        field = field,
                        message = "Phone must be a string",
                    )
                    val phoneError = validatePhone(phone)
                    if (phoneError != null) return phoneError
                }

                // Add validation for other fields as needed
                else -> {
                    // Allow other fields for now
                }
            }
        }

        return null
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
     * Validate phone format.
     */
    private fun validatePhone(phone: String): AuthError? {
        return when {
            phone.isBlank() -> AuthError.ValidationError(
                field = "phone",
                message = "Phone number is required",
            )

            !isValidPhone(phone) -> AuthError.ValidationError(
                field = "phone",
                message = "Please enter a valid phone number",
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
     * Basic phone validation.
     */
    private fun isValidPhone(phone: String): Boolean {
        val cleanPhone = phone.replace("[^\\d+]".toRegex(), "")
        return cleanPhone.length >= 10 && cleanPhone.length <= 15
    }
}
