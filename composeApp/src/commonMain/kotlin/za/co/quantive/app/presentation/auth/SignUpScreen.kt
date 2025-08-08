package za.co.quantive.app.presentation.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import za.co.quantive.app.presentation.theme.QuantiveDesignTokens

/**
 * Enhanced Sign Up Screen with Material 3 design and password strength indicators.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    uiState: AuthUiState,
    onSignUp: (email: String, password: String, confirmPassword: String) -> Unit,
    onNavigateToSignIn: () -> Unit,
    onClearError: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    // Password strength state - compute within @Composable context
    val passwordStrength = calculatePasswordStrength(password)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(QuantiveDesignTokens.Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Brand Header
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            ),
            modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.XLarge),
        ) {
            Column(
                modifier = Modifier.padding(QuantiveDesignTokens.Spacing.Large),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "🚀",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.Small),
                )

                Text(
                    text = "Join Quantive",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = "Create your professional invoicing account",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
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

        // Password Input with Strength Indicator
        Column {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Choose a strong password") },
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
                modifier = Modifier.fillMaxWidth(),
            )

            // Password Strength Indicator
            if (password.isNotEmpty()) {
                Column(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = QuantiveDesignTokens.Spacing.Small,
                        bottom = QuantiveDesignTokens.Spacing.Medium,
                    ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.Small),
                    ) {
                        Text(
                            text = "Password Strength: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = passwordStrength.label,
                            style = MaterialTheme.typography.bodySmall,
                            color = animateColorAsState(passwordStrength.color).value,
                        )
                    }

                    LinearProgressIndicator(
                        progress = { passwordStrength.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = passwordStrength.color,
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Medium))
            }
        }

        // Confirm Password Input
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            placeholder = { Text("Re-enter your password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { showConfirmPassword = !showConfirmPassword },
                ) {
                    Icon(
                        imageVector = if (showConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (showConfirmPassword) "Hide password" else "Show password",
                    )
                }
            },
            visualTransformation = if (showConfirmPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            isError = uiState.hasFieldError("confirmPassword") || (confirmPassword.isNotEmpty() && password != confirmPassword),
            supportingText = {
                when {
                    uiState.hasFieldError("confirmPassword") -> {
                        Text(
                            text = uiState.getFieldError("confirmPassword") ?: "",
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    confirmPassword.isNotEmpty() && password != confirmPassword -> {
                        Text(
                            text = "Passwords do not match",
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    confirmPassword.isNotEmpty() && password == confirmPassword -> {
                        Text(
                            text = "Passwords match",
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            },
            enabled = !uiState.isLoading,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = QuantiveDesignTokens.Spacing.Large),
        )

        // Sign Up Button
        Button(
            onClick = { onSignUp(email.trim(), password, confirmPassword) },
            enabled = !uiState.isLoading &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                password == confirmPassword &&
                passwordStrength.isStrong,
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
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(end = QuantiveDesignTokens.Spacing.Small),
                )
                Text("Create Account")
            }
        }

        // Terms and Privacy
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = QuantiveDesignTokens.Spacing.Medium),
        ) {
            Text(
                text = "By creating an account, you agree to our Terms of Service and Privacy Policy",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(QuantiveDesignTokens.Spacing.Medium),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Sign In Option
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = QuantiveDesignTokens.Spacing.Medium),
        ) {
            Column(
                modifier = Modifier.padding(QuantiveDesignTokens.Spacing.Medium),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Already have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                TextButton(
                    onClick = onNavigateToSignIn,
                    modifier = Modifier.padding(top = QuantiveDesignTokens.Spacing.Small),
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.padding(end = QuantiveDesignTokens.Spacing.Small),
                    )
                    Text("Sign In")
                }
            }
        }

        Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Large))
    }
}

/**
 * Password strength data class
 */
private data class PasswordStrength(
    val progress: Float,
    val label: String,
    val color: androidx.compose.ui.graphics.Color,
    val isStrong: Boolean,
)

/**
 * Calculate password strength
 */
@Composable
private fun calculatePasswordStrength(password: String): PasswordStrength {
    val colorScheme = MaterialTheme.colorScheme

    if (password.isEmpty()) {
        return PasswordStrength(
            progress = 0f,
            label = "",
            color = colorScheme.outline,
            isStrong = false,
        )
    }

    var score = 0
    val checks = listOf(
        password.length >= 8,
        password.any { it.isLowerCase() },
        password.any { it.isUpperCase() },
        password.any { it.isDigit() },
        password.any { !it.isLetterOrDigit() },
    )

    score = checks.count { it }

    return when (score) {
        0, 1 -> PasswordStrength(
            progress = 0.2f,
            label = "Very Weak",
            color = colorScheme.error,
            isStrong = false,
        )
        2 -> PasswordStrength(
            progress = 0.4f,
            label = "Weak",
            color = colorScheme.error.copy(alpha = 0.7f),
            isStrong = false,
        )
        3 -> PasswordStrength(
            progress = 0.6f,
            label = "Fair",
            color = colorScheme.tertiary,
            isStrong = false,
        )
        4 -> PasswordStrength(
            progress = 0.8f,
            label = "Good",
            color = colorScheme.primary,
            isStrong = true,
        )
        else -> PasswordStrength(
            progress = 1.0f,
            label = "Strong",
            color = colorScheme.primary,
            isStrong = true,
        )
    }
}
