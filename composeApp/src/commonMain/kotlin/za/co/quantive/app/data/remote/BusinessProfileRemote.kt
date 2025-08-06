package za.co.quantive.app.data.remote

import za.co.quantive.app.domain.profile.BusinessProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
private data class BusinessProfileDto(
    val id: String? = null,
    @SerialName("owner_id") val owner_id: String? = null,
    val name: String,
    val currency: String = "USD",
    @SerialName("tax_id") val tax_id: String? = null,
    val terms: String? = null,
    @SerialName("brand_color") val brand_color: String? = null,
    @SerialName("logo_path") val logo_path: String? = null,
    @SerialName("created_at") val created_at: String? = null,
    @SerialName("updated_at") val updated_at: String? = null
)

private fun BusinessProfileDto.toDomain() = BusinessProfile(
    id = id,
    owner_id = owner_id,
    name = name,
    currency = currency,
    tax_id = tax_id,
    terms = terms,
    brand_color = brand_color,
    logo_path = logo_path,
    created_at = created_at,
    updated_at = updated_at
)

private fun BusinessProfile.toDto(ownerId: String) = BusinessProfileDto(
    id = id,
    owner_id = owner_id ?: ownerId,
    name = name,
    currency = currency,
    tax_id = tax_id,
    terms = terms,
    brand_color = brand_color,
    logo_path = logo_path,
    created_at = created_at,
    updated_at = updated_at
)

class BusinessProfileRemote(private val client: SupabaseClient, private val ownerIdProvider: suspend () -> String?) {
    suspend fun get(): BusinessProfile? {
        val ownerId = ownerIdProvider() ?: return null
        // GET /rest/v1/business_profiles?owner_id=eq.{uid}&select=*&limit=1
        val list: List<BusinessProfileDto> = client.get(
            path = "rest/v1/business_profiles",
            params = mapOf(
                "owner_id" to "eq.$ownerId",
                "select" to "*",
                "limit" to "1"
            )
        )
        return list.firstOrNull()?.toDomain()
    }

    suspend fun upsert(profile: BusinessProfile): BusinessProfile? {
        val ownerId = ownerIdProvider() ?: return null
        // POST with Prefer: resolution=merge-duplicates to upsert
        val dto = profile.toDto(ownerId)
        val result: List<BusinessProfileDto> = client.post(
            path = "rest/v1/business_profiles",
            body = listOf(dto),
            params = mapOf(
                "select" to "*"
            )
        )
        return result.firstOrNull()?.toDomain()
    }
}
