package za.co.quantive.app.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    val expiresAt: Long,
    val userId: String
) {
    fun isExpired(nowEpochSeconds: Long): Boolean = nowEpochSeconds >= expiresAt
}
