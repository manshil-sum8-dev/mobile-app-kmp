package za.co.quantive.app.auth.domain.model

/**
 * Domain-specific authentication errors with user-friendly messages and recovery options.
 */
sealed class AuthError(
    open val message: String,
    open val cause: Throwable? = null,
) {

    // Network and connectivity errors
    data class NetworkError(
        override val message: String = "Network connection failed. Please check your internet connection.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    data class ServerError(
        override val message: String = "Server is temporarily unavailable. Please try again later.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    // Authentication errors
    data class InvalidCredentials(
        override val message: String = "Invalid email or password. Please check your credentials.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    data class AccountNotFound(
        override val message: String = "Account not found. Please check your email or sign up.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    data class AccountDisabled(
        override val message: String = "Your account has been disabled. Please contact support.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    data class EmailNotVerified(
        override val message: String = "Please verify your email address before signing in.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    // Registration errors
    data class EmailAlreadyExists(
        override val message: String = "An account with this email already exists. Please sign in instead.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    data class WeakPassword(
        override val message: String = "Password is too weak. Please choose a stronger password.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    data class InvalidEmail(
        override val message: String = "Please enter a valid email address.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    // Session and token errors
    data class TokenExpired(
        override val message: String = "Your session has expired. Please sign in again.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    data class RefreshTokenInvalid(
        override val message: String = "Session refresh failed. Please sign in again.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    data class SessionInvalid(
        override val message: String = "Your session is invalid. Please sign in again.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    // Security errors
    data class TooManyAttempts(
        override val message: String = "Too many failed attempts. Please try again later.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    data class SecurityError(
        override val message: String = "Security verification failed. Please try again.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    data class CertificatePinningError(
        override val message: String = "Security certificate validation failed. Please check your connection.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    // Storage errors
    data class StorageError(
        override val message: String = "Failed to save authentication data. Please try again.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    // Generic errors
    data class UnknownError(
        override val message: String = "An unexpected error occurred. Please try again.",
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)

    // Validation errors
    data class ValidationError(
        val field: String,
        override val message: String,
        override val cause: Throwable? = null,
    ) : AuthError(message, cause)
}

/**
 * Recovery options available for authentication errors.
 */
sealed class RecoveryOption {
    object Retry : RecoveryOption()
    object SignIn : RecoveryOption()
    object SignUp : RecoveryOption()
    object ForgotPassword : RecoveryOption()
    object VerifyEmail : RecoveryOption()
    object ContactSupport : RecoveryOption()
    object CheckConnection : RecoveryOption()
    data class WaitAndRetry(val seconds: Int) : RecoveryOption()
}

/**
 * Maps exceptions to domain-specific auth errors.
 */
fun Throwable.toAuthError(): AuthError {
    return when {
        // Network errors
        message?.contains("ConnectException", ignoreCase = true) == true ||
            message?.contains("SocketTimeoutException", ignoreCase = true) == true ||
            message?.contains("UnknownHostException", ignoreCase = true) == true ->
            AuthError.NetworkError(cause = this)

        // Certificate pinning errors
        message?.contains("Certificate pinning failure", ignoreCase = true) == true ||
            message?.contains("javax.net.ssl.SSLPeerUnverifiedException", ignoreCase = true) == true ->
            AuthError.CertificatePinningError(cause = this)

        // Storage errors
        message?.contains("EncryptedSharedPreferences", ignoreCase = true) == true ||
            message?.contains("Keychain", ignoreCase = true) == true ||
            message?.contains("SecureStore", ignoreCase = true) == true ->
            AuthError.StorageError(cause = this)

        // Generic security errors
        this is IllegalStateException && message?.contains("security", ignoreCase = true) == true ->
            AuthError.SecurityError(cause = this)

        // Default to unknown error
        else -> AuthError.UnknownError(
            message = message ?: "An unexpected error occurred",
            cause = this,
        )
    }
}
