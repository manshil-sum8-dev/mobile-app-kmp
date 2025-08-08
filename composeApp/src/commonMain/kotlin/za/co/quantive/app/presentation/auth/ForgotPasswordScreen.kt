package za.co.quantive.app.presentation.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import za.co.quantive.app.presentation.theme.QuantiveDesignTokens

/**
 * Forgot Password Screen for password reset functionality.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    uiState: AuthUiState,
    onSendResetEmail: (email: String) -> Unit,
    onNavigateToSignIn: () -> Unit,
    onClearError: () -> Unit,
    onClearSuccessMessage: () -> Unit,
) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(QuantiveDesignTokens.Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Header
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.XLarge),
        ) {
            Column(
                modifier = Modifier.padding(QuantiveDesignTokens.Spacing.Large),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(bottom = QuantiveDesignTokens.Spacing.Medium),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                )

                Text(
                    text = "Forgot Password?",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = "Enter your email address and we'll send you a link to reset your password",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = QuantiveDesignTokens.Spacing.Small),
                )
            }
        }

        // Success Message
        AnimatedVisibility(visible = uiState.successMessage != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = QuantiveDesignTokens.Spacing.Medium),
            ) {
                Row(
                    modifier = Modifier.padding(QuantiveDesignTokens.Spacing.Medium),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(end = QuantiveDesignTokens.Spacing.Small),
                    )
                    Text(
                        text = uiState.successMessage ?: "",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        // Error Message
        AnimatedVisibility(visible = uiState.error != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = QuantiveDesignTokens.Spacing.Medium),
            ) {
                Row(
                    modifier = Modifier.padding(QuantiveDesignTokens.Spacing.Medium),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(end = QuantiveDesignTokens.Spacing.Small),
                    )
                    Text(
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                if (uiState.error != null) onClearError()
                if (uiState.successMessage != null) onClearSuccessMessage()
            },
            label = { Text("Email Address") },
            placeholder = { Text("your@email.com") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                )
            },
            isError = uiState.hasFieldError("email"),
            supportingText = {
                uiState.getFieldError("email")?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                    )
                } ?: if (email.isNotEmpty()) {
                    Text(
                        text = "We'll send a password reset link to this email",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    null
                }
            },
            enabled = !uiState.isLoading,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = QuantiveDesignTokens.Spacing.Large),
        )

        // Send Reset Email Button
        Button(
            onClick = { onSendResetEmail(email.trim()) },
            enabled = !uiState.isLoading && email.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier.padding(end = QuantiveDesignTokens.Spacing.Small),
                )
                Text("Send Reset Email")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Security Note
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = QuantiveDesignTokens.Spacing.Medium),
        ) {
            Column(
                modifier = Modifier.padding(QuantiveDesignTokens.Spacing.Medium),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.Small),
                )
                Text(
                    text = "For security reasons, if this email is not associated with a Quantive account, you will not receive a reset email.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }

        // Back to Sign In
        TextButton(
            onClick = onNavigateToSignIn,
            modifier = Modifier.padding(top = QuantiveDesignTokens.Spacing.Medium),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier.padding(end = QuantiveDesignTokens.Spacing.Small),
            )
            Text("Back to Sign In")
        }

        Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Large))
    }
}
