package za.co.quantive.app.auth.domain.usecase

import za.co.quantive.app.auth.domain.model.AuthError
import za.co.quantive.app.auth.domain.repository.AuthRepository
import za.co.quantive.app.auth.domain.repository.Result

/**
 * Use case for user sign-out with secure session cleanup.
 */
class SignOutUseCase(
    private val authRepository: AuthRepository,
) {

    /**
     * Execute sign-out with complete session cleanup.
     */
    suspend fun execute(): Result<Unit, AuthError> {
        return authRepository.signOut()
    }
}
