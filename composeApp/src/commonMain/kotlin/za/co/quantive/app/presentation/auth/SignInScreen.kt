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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import za.co.quantive.app.presentation.theme.QuantiveDesignTokens

/**
 * Enhanced Sign In Screen with Material 3 design and enterprise UX patterns.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    uiState: AuthUiState,
    onSignIn: (email: String, password: String) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onClearError: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    // Clear field errors when user starts typing
    LaunchedEffect(email) {
        if (uiState.hasFieldError("email")) onClearError()
    }
    LaunchedEffect(password) {
        if (uiState.hasFieldError("password")) onClearError()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(QuantiveDesignTokens.Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Brand Header
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.XLarge),
        ) {
            Column(
                modifier = Modifier.padding(QuantiveDesignTokens.Spacing.Large),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "📊",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.Small),
                )

                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = "Sign in to your Quantive account",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = QuantiveDesignTokens.Spacing.Small),
                )
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
            onValueChange = { email = it },
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
                }
            },
            enabled = !uiState.isLoading,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = QuantiveDesignTokens.Spacing.Medium),
        )

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            placeholder = { Text("Enter your password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { showPassword = !showPassword },
                ) {
                    Icon(
                        imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (showPassword) "Hide password" else "Show password",
                    )
                }
            },
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            isError = uiState.hasFieldError("password"),
            supportingText = {
                uiState.getFieldError("password")?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            enabled = !uiState.isLoading,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = QuantiveDesignTokens.Spacing.Large),
        )

        // Sign In Button
        Button(
            onClick = { onSignIn(email.trim(), password) },
            enabled = !uiState.isLoading && email.isNotBlank() && password.isNotBlank(),
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
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.padding(end = QuantiveDesignTokens.Spacing.Small),
                )
                Text("Sign In")
            }
        }

        // Forgot Password
        TextButton(
            onClick = onNavigateToForgotPassword,
            modifier = Modifier.padding(top = QuantiveDesignTokens.Spacing.Medium),
        ) {
            Text("Forgot your password?")
        }

        Spacer(modifier = Modifier.weight(1f))

        // Sign Up Option
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = QuantiveDesignTokens.Spacing.Large),
        ) {
            Column(
                modifier = Modifier.padding(QuantiveDesignTokens.Spacing.Medium),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "New to Quantive?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                TextButton(
                    onClick = onNavigateToSignUp,
                    modifier = Modifier.padding(top = QuantiveDesignTokens.Spacing.Small),
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(end = QuantiveDesignTokens.Spacing.Small),
                    )
                    Text("Create an Account")
                }
            }
        }

        Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Large))
    }
}
