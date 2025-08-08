package za.co.quantive.app.auth.domain.usecase

import za.co.quantive.app.auth.domain.model.UserSession
import za.co.quantive.app.auth.domain.repository.AuthRepository

/**
 * Use case for getting current user session with automatic refresh handling.
 */
class GetCurrentSessionUseCase(
    private val authRepository: AuthRepository,
) {

    /**
     * Get current session, refreshing if needed.
     */
    suspend fun execute(): UserSession? {
        val currentSession = authRepository.getCurrentSession()

        // If session exists but requires refresh, attempt refresh
        if (currentSession?.requiresRefresh() == true) {
            val refreshResult = authRepository.refreshSession()
            return refreshResult.getOrNull() ?: currentSession
        }

        return currentSession
    }

    /**
     * Check if user is authenticated with a valid session.
     */
    suspend fun isAuthenticated(): Boolean {
        return execute() != null
    }
}
