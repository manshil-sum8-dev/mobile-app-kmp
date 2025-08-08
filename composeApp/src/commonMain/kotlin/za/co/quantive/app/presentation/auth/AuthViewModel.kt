package za.co.quantive.app.presentation.auth

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import za.co.quantive.app.auth.domain.model.AuthError
import za.co.quantive.app.auth.domain.repository.Result
import za.co.quantive.app.auth.domain.service.AuthService
import za.co.quantive.app.auth.domain.service.AuthState

/**
 * ViewModel for authentication screens using Clean Architecture.
 * Handles all authentication-related UI state and business logic.
 */
class AuthViewModel(
    private val authService: AuthService,
) {

    // Create our own scope for KMP compatibility
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthStatus()
    }

    // Auth state management

    fun checkAuthStatus() {
        scope.launch {
            _authState.value = AuthState.Loading
            _authState.value = authService.getAuthState()
        }
    }

    // Sign In

    fun signIn(email: String, password: String) {
        scope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                fieldErrors = emptyMap(),
            )

            when (val result = authService.signIn(email, password)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _authState.value = AuthState.Authenticated(result.value)
                }
                is Result.Error -> {
                    handleAuthError(result.error)
                }
            }
        }
    }

    // Sign Up

    fun signUp(email: String, password: String, confirmPassword: String) {
        scope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                fieldErrors = emptyMap(),
            )

            when (val result = authService.signUp(email, password, confirmPassword)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _authState.value = AuthState.Authenticated(result.value)
                }
                is Result.Error -> {
                    handleAuthError(result.error)
                }
            }
        }
    }

    // Sign Out

    fun signOut() {
        scope.launch {
            when (authService.signOut()) {
                is Result.Success -> {
                    _authState.value = AuthState.Unauthenticated
                    clearUiState()
                }
                is Result.Error -> {
                    // Handle error if needed, but still treat as signed out
                    _authState.value = AuthState.Unauthenticated
                    clearUiState()
                }
            }
        }
    }

    // Password Reset

    fun sendPasswordResetEmail(email: String) {
        scope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
            )

            when (val result = authService.sendPasswordResetEmail(email)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Password reset email sent. Please check your inbox.",
                    )
                }
                is Result.Error -> {
                    handleAuthError(result.error)
                }
            }
        }
    }

    fun resetPassword(token: String, newPassword: String, confirmPassword: String) {
        scope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                fieldErrors = emptyMap(),
            )

            when (val result = authService.resetPassword(token, newPassword, confirmPassword)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Password reset successfully. You can now sign in with your new password.",
                    )
                }
                is Result.Error -> {
                    handleAuthError(result.error)
                }
            }
        }
    }

    // Email Verification

    fun sendEmailVerification(email: String? = null) {
        scope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
            )

            when (val result = authService.sendEmailVerification(email)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Verification email sent. Please check your inbox.",
                    )
                }
                is Result.Error -> {
                    handleAuthError(result.error)
                }
            }
        }
    }

    fun verifyEmail(token: String) {
        scope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
            )

            when (val result = authService.verifyEmail(token)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Email verified successfully!",
                    )
                    checkAuthStatus() // Refresh auth state
                }
                is Result.Error -> {
                    handleAuthError(result.error)
                }
            }
        }
    }

    // UI State Management

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }

    fun clearFieldError(field: String) {
        val newErrors = _uiState.value.fieldErrors.toMutableMap()
        newErrors.remove(field)
        _uiState.value = _uiState.value.copy(fieldErrors = newErrors)
    }

    fun setCurrentScreen(screen: AuthScreenType) {
        _uiState.value = _uiState.value.copy(
            currentScreen = screen,
            error = null,
            successMessage = null,
            fieldErrors = emptyMap(),
        )
    }

    // Private helpers

    private fun handleAuthError(error: AuthError) {
        _uiState.value = _uiState.value.copy(isLoading = false)

        when (error) {
            is AuthError.ValidationError -> {
                val fieldErrors = _uiState.value.fieldErrors.toMutableMap()
                fieldErrors[error.field] = error.message
                _uiState.value = _uiState.value.copy(fieldErrors = fieldErrors)
            }
            else -> {
                _uiState.value = _uiState.value.copy(error = error.message)
            }
        }
    }

    private fun clearUiState() {
        _uiState.value = AuthUiState()
    }
}

/**
 * UI state for authentication screens.
 */
data class AuthUiState(
    val currentScreen: AuthScreenType = AuthScreenType.SignIn,
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
) {
    fun getFieldError(field: String): String? = fieldErrors[field]
    fun hasFieldError(field: String): Boolean = fieldErrors.containsKey(field)
}

/**
 * Types of authentication screens.
 */
enum class AuthScreenType {
    SignIn,
    SignUp,
    ForgotPassword,
    ResetPassword,
    EmailVerification,
}
