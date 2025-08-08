package za.co.quantive.app.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import za.co.quantive.app.data.remote.SupabaseClient

class AuthService(private val client: SupabaseClient) {

    @Serializable
    private data class SignUpRequest(val email: String, val password: String)

    @Serializable
    private data class PasswordGrantRequest(
        @SerialName("grant_type") val grantType: String = "password",
        val email: String,
        val password: String,
    )

    @Serializable
    private data class RefreshGrantRequest(
        @SerialName("grant_type") val grantType: String = "refresh_token",
        @SerialName("refresh_token") val refreshToken: String,
    )

    @Serializable
    private data class TokenResponse(
        @SerialName("access_token") val accessToken: String,
        @SerialName("token_type") val tokenType: String? = null,
        @SerialName("expires_in") val expiresIn: Long? = null,
        @SerialName("expires_at") val expiresAt: Long? = null,
        @SerialName("refresh_token") val refreshToken: String,
        val user: User,
    )

    @Serializable
    private data class User(
        val id: String,
        val aud: String? = null,
        val role: String? = null,
        val email: String? = null,
        @SerialName("email_confirmed_at") val emailConfirmedAt: String? = null,
        val phone: String? = null,
        @SerialName("last_sign_in_at") val lastSignInAt: String? = null,
        @SerialName("app_metadata") val appMetadata: kotlinx.serialization.json.JsonElement? = null,
        @SerialName("user_metadata") val userMetadata: kotlinx.serialization.json.JsonElement? = null,
        val identities: kotlinx.serialization.json.JsonElement? = null,
        @SerialName("created_at") val createdAt: String? = null,
        @SerialName("updated_at") val updatedAt: String? = null,
        @SerialName("is_anonymous") val isAnonymous: Boolean? = null,
    )

    suspend fun signUp(email: String, password: String): Session {
        // Supabase signup returns a session depending on email confirmation settings.
        // For local dev default, it usually returns a session immediately.
        val respJson = client.post<JsonObject, SignUpRequest>(
            path = "auth/v1/signup",
            body = SignUpRequest(email, password),
        )

        // Secure logging - only log non-sensitive response metadata
        println("DEBUG: Signup response contains keys: ${respJson.keys.joinToString(", ")}")

        // Check if this is an error response (Supabase error format)
        if (respJson.contains("code") || respJson.contains("error_code") || respJson.contains("msg")) {
            val code = respJson["code"]?.jsonPrimitive?.content
            val errorCode = respJson["error_code"]?.jsonPrimitive?.content
            val message = respJson["msg"]?.jsonPrimitive?.content ?: "Unknown error"
            val errorDetails = listOfNotNull(code, errorCode).joinToString(", ")
            val fullMessage = if (errorDetails.isNotEmpty()) "$message ($errorDetails)" else message
            throw IllegalStateException("Signup failed: $fullMessage")
        }

        // Check if this is a standard error response
        if (respJson.contains("error")) {
            val errorMessage = respJson["error"]?.jsonPrimitive?.content ?: "Unknown error"
            val errorDescription = respJson["error_description"]?.jsonPrimitive?.content ?: ""
            throw IllegalStateException("Signup failed: $errorMessage - $errorDescription")
        }

        val accessToken = respJson["access_token"]?.jsonPrimitive?.content
            ?: throw IllegalStateException("Missing access_token in response. Available keys: ${respJson.keys}")
        val refreshToken = respJson["refresh_token"]?.jsonPrimitive?.content
            ?: throw IllegalStateException("Missing refresh_token in response")
        val expiresIn = respJson["expires_in"]?.jsonPrimitive?.long
        val expiresAt = respJson["expires_at"]?.jsonPrimitive?.long
            ?: (za.co.quantive.app.core.time.Clock.currentTimeMillis() / 1000L) + (expiresIn ?: 3600L)
        val userId = respJson["user"]?.jsonObject?.get("id")?.jsonPrimitive?.content
            ?: throw IllegalStateException("Missing user.id in response")

        return Session(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresAt = expiresAt,
            userId = userId,
        )
    }

    suspend fun signIn(email: String, password: String): Session {
        val respJson = client.post<JsonObject, PasswordGrantRequest>(
            path = "auth/v1/token?grant_type=password",
            body = PasswordGrantRequest(email = email, password = password),
        )

        val accessToken = respJson["access_token"]?.jsonPrimitive?.content
            ?: throw IllegalStateException("Missing access_token in response")
        val refreshToken = respJson["refresh_token"]?.jsonPrimitive?.content
            ?: throw IllegalStateException("Missing refresh_token in response")
        val expiresIn = respJson["expires_in"]?.jsonPrimitive?.long
        val expiresAt = respJson["expires_at"]?.jsonPrimitive?.long
            ?: (za.co.quantive.app.core.time.Clock.currentTimeMillis() / 1000L) + (expiresIn ?: 3600L)
        val userId = respJson["user"]?.jsonObject?.get("id")?.jsonPrimitive?.content
            ?: throw IllegalStateException("Missing user.id in response")

        return Session(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresAt = expiresAt,
            userId = userId,
        )
    }

    suspend fun refresh(refreshToken: String): Session {
        val respJson = client.post<JsonObject, RefreshGrantRequest>(
            path = "auth/v1/token?grant_type=refresh_token",
            body = RefreshGrantRequest(refreshToken = refreshToken),
        )

        val accessToken = respJson["access_token"]?.jsonPrimitive?.content
            ?: throw IllegalStateException("Missing access_token in response")
        val newRefreshToken = respJson["refresh_token"]?.jsonPrimitive?.content
            ?: throw IllegalStateException("Missing refresh_token in response")
        val expiresIn = respJson["expires_in"]?.jsonPrimitive?.long
        val expiresAt = respJson["expires_at"]?.jsonPrimitive?.long
            ?: (za.co.quantive.app.core.time.Clock.currentTimeMillis() / 1000L) + (expiresIn ?: 3600L)
        val userId = respJson["user"]?.jsonObject?.get("id")?.jsonPrimitive?.content
            ?: throw IllegalStateException("Missing user.id in response")

        return Session(
            accessToken = accessToken,
            refreshToken = newRefreshToken,
            expiresAt = expiresAt,
            userId = userId,
        )
    }
}
