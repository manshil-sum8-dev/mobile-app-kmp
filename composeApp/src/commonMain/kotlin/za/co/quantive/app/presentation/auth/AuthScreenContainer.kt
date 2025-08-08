package za.co.quantive.app.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import za.co.quantive.app.auth.domain.service.AuthState
import za.co.quantive.app.presentation.theme.QuantiveDesignTokens

/**
 * Container for all authentication screens with navigation and state management.
 */
@Composable
fun AuthScreenContainer(
    viewModel: AuthViewModel,
    onAuthSuccess: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val authState by viewModel.authState.collectAsState()

    // Handle auth success
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> onAuthSuccess()
            else -> { /* Handle other states as needed */ }
        }
    }

    // Show loading while checking auth status
    when (authState) {
        is AuthState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.Medium),
                    )
                    Text(
                        text = "Loading...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        else -> {
            // Show appropriate auth screen based on current screen type
            when (uiState.currentScreen) {
                AuthScreenType.SignIn -> {
                    SignInScreen(
                        uiState = uiState,
                        onSignIn = viewModel::signIn,
                        onNavigateToSignUp = { viewModel.setCurrentScreen(AuthScreenType.SignUp) },
                        onNavigateToForgotPassword = { viewModel.setCurrentScreen(AuthScreenType.ForgotPassword) },
                        onClearError = viewModel::clearError,
                    )
                }

                AuthScreenType.SignUp -> {
                    SignUpScreen(
                        uiState = uiState,
                        onSignUp = viewModel::signUp,
                        onNavigateToSignIn = { viewModel.setCurrentScreen(AuthScreenType.SignIn) },
                        onClearError = viewModel::clearError,
                    )
                }

                AuthScreenType.ForgotPassword -> {
                    ForgotPasswordScreen(
                        uiState = uiState,
                        onSendResetEmail = viewModel::sendPasswordResetEmail,
                        onNavigateToSignIn = { viewModel.setCurrentScreen(AuthScreenType.SignIn) },
                        onClearError = viewModel::clearError,
                        onClearSuccessMessage = viewModel::clearSuccessMessage,
                    )
                }

                AuthScreenType.ResetPassword -> {
                    // TODO: Implement reset password screen
                    // This would typically be accessed via deep link with reset token
                }

                AuthScreenType.EmailVerification -> {
                    // TODO: Implement email verification screen
                    // This would be shown after sign up or accessed via deep link
                }
            }
        }
    }
}
