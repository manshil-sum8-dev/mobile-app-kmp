package za.co.quantive.app.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import za.co.quantive.app.app.AppServices
import za.co.quantive.app.auth.Session
import za.co.quantive.app.presentation.components.*
import za.co.quantive.app.presentation.theme.QuantiveDesignTokens
import kotlinx.coroutines.launch

/**
 * Quantive Authentication Screen
 * Modern, professional login/signup flow
 */
@Composable
fun QuantiveAuthScreen(onSignUpSuccess: (Session) -> Unit) {
    var isSignUp by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Validation functions
    fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".") && email.length > 5
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    fun getPasswordStrengthMessage(password: String): String? {
        return when {
            password.isEmpty() -> null
            password.length < 8 -> "Password must be at least 8 characters"
            !password.any { it.isDigit() } -> "Password should contain at least one number"
            !password.any { it.isLetter() } -> "Password should contain at least one letter"
            else -> null
        }
    }

    fun validateInputs(): Boolean {
        var isValid = true
        
        // Email validation
        emailError = when {
            email.isBlank() -> {
                isValid = false
                "Email is required"
            }
            !isValidEmail(email) -> {
                isValid = false
                "Please enter a valid email address"
            }
            else -> null
        }
        
        // Password validation
        passwordError = when {
            password.isBlank() -> {
                isValid = false
                "Password is required"
            }
            isSignUp && !isValidPassword(password) -> {
                isValid = false
                getPasswordStrengthMessage(password)
            }
            isSignUp && password != confirmPassword -> {
                isValid = false
                "Passwords do not match"
            }
            else -> null
        }
        
        return isValid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(QuantiveDesignTokens.Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Header
        Text(
            text = "Quantive",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Professional Invoicing & Business Management",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                top = QuantiveDesignTokens.Spacing.Small,
                bottom = QuantiveDesignTokens.Spacing.XXLarge
            )
        )

        // Toggle between Sign In / Sign Up
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            QuantiveTextButton(
                text = "Sign In",
                onClick = { 
                    isSignUp = false
                    errorMessage = null
                    emailError = null
                    passwordError = null
                },
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (!isSignUp) Modifier else Modifier
                    )
            )
            
            QuantiveTextButton(
                text = "Sign Up",
                onClick = { 
                    isSignUp = true
                    errorMessage = null
                    emailError = null
                    passwordError = null
                },
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (isSignUp) Modifier else Modifier
                    )
            )
        }
        
        Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Large))

        // Email Input
        QuantiveTextField(
            value = email,
            onValueChange = { 
                email = it
                if (emailError != null) emailError = null
            },
            label = "Email Address",
            placeholder = "your@email.com",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            },
            error = emailError,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = QuantiveDesignTokens.Spacing.Medium)
        )

        // Password Input
        QuantiveTextField(
            value = password,
            onValueChange = { 
                password = it
                if (passwordError != null) passwordError = null
                // Show real-time password strength feedback for sign up
                if (isSignUp && password.isNotEmpty()) {
                    passwordError = getPasswordStrengthMessage(password)
                }
            },
            label = "Password",
            placeholder = if (isSignUp) "At least 8 characters" else "Your password",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            error = passwordError,
            enabled = !isLoading,
            isPassword = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (isSignUp) QuantiveDesignTokens.Spacing.Medium else QuantiveDesignTokens.Spacing.Large)
        )

        // Confirm Password Input (Sign Up only)
        if (isSignUp) {
            QuantiveTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                placeholder = "Re-enter your password",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                },
                enabled = !isLoading,
                isPassword = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = QuantiveDesignTokens.Spacing.Large)
            )
        }

        // Error Message
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.Medium)
            )
        }

        // Action Button
        QuantivePrimaryButton(
            text = if (isSignUp) "Create Account" else "Sign In",
            onClick = {
                if (validateInputs()) {
                    scope.launch {
                        try {
                            isLoading = true
                            errorMessage = null
                            
                            val session = if (isSignUp) {
                                AppServices.auth.signUp(email, password)
                            } else {
                                AppServices.auth.signIn(email, password)
                            }
                            
                            AppServices.setSession(session)
                            onSignUpSuccess(session)
                        } catch (e: Exception) {
                            errorMessage = "${if (isSignUp) "Sign up" else "Sign in"} failed: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                }
            },
            loading = isLoading,
            enabled = email.isNotBlank() && password.isNotBlank() && 
                     (!isSignUp || confirmPassword.isNotBlank()) &&
                     emailError == null && (passwordError == null || !isSignUp),
            modifier = Modifier.fillMaxWidth()
        )

        // Additional Info for Sign Up
        if (isSignUp) {
            Text(
                text = "By creating an account, you agree to our Terms of Service and Privacy Policy",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = QuantiveDesignTokens.Spacing.Large)
            )
        }
    }
}

/**
 * Quantive Error Screen
 */
@Composable
fun QuantiveErrorScreen(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        QuantiveEmptyState(
            title = "Something went wrong",
            subtitle = error,
            icon = Icons.Default.Warning,
            actionText = "Try Again",
            onActionClick = onRetry
        )
    }
}

/**
 * Quantive Onboarding Flow (Simplified)
 */
@Composable
fun QuantiveOnboardingFlow(onOnboardingComplete: () -> Unit) {
    var currentStep by remember { mutableStateOf(0) }
    
    when (currentStep) {
        0 -> QuantiveWelcomeScreen(onNext = { currentStep = 1 })
        1 -> QuantiveBusinessSetupScreen(onNext = { currentStep = 2 })
        2 -> QuantiveOnboardingCompleteScreen(onComplete = onOnboardingComplete)
        else -> onOnboardingComplete()
    }
}

@Composable
fun QuantiveWelcomeScreen(onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(QuantiveDesignTokens.Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎉",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.Large)
        )
        
        QuantiveSectionHeader(
            title = "Welcome to Quantive!",
            subtitle = "Your professional invoicing and business management platform",
            modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.XXLarge)
        )
        
        QuantiveCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "✓ Create professional invoices",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.Small)
            )
            Text(
                text = "✓ Manage customers and suppliers",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.Small)
            )
            Text(
                text = "✓ Track payments and analytics",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.Small)
            )
            Text(
                text = "✓ South African tax compliance",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        QuantivePrimaryButton(
            text = "Get Started",
            onClick = onNext,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun QuantiveBusinessSetupScreen(onNext: () -> Unit) {
    var businessName by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(QuantiveDesignTokens.Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        QuantiveSectionHeader(
            title = "Set Up Your Business",
            subtitle = "Tell us about your business to personalize your experience",
            modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.XXLarge)
        )
        
        QuantiveTextField(
            value = businessName,
            onValueChange = { businessName = it },
            label = "Business Name",
            placeholder = "Your Business Name",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = QuantiveDesignTokens.Spacing.Medium)
        )
        
        QuantiveTextField(
            value = ownerName,
            onValueChange = { ownerName = it },
            label = "Your Name",
            placeholder = "Business Owner Name",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = QuantiveDesignTokens.Spacing.Large)
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        QuantivePrimaryButton(
            text = "Continue",
            onClick = {
                scope.launch {
                    // Save business profile
                    za.co.quantive.app.security.SecureStore.saveUserProfile(
                        ownerName.ifBlank { "Business Owner" },
                        businessName.ifBlank { "My Business" }
                    )
                    onNext()
                }
            },
            enabled = businessName.isNotBlank() || ownerName.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun QuantiveOnboardingCompleteScreen(onComplete: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(QuantiveDesignTokens.Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🚀",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.Large)
        )
        
        QuantiveSectionHeader(
            title = "You're all set!",
            subtitle = "Ready to start managing your business with Quantive",
            modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.XXLarge)
        )
        
        QuantivePrimaryButton(
            text = "Enter Quantive",
            onClick = onComplete,
            modifier = Modifier.fillMaxWidth()
        )
    }
}