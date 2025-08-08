package za.co.quantive.app.domain.profile

import kotlinx.serialization.Serializable

@Serializable
data class BusinessProfile(
    val id: String? = null,
    val owner_id: String? = null,
    val name: String,
    val currency: String = "USD",
    val tax_id: String? = null,
    val terms: String? = null,
    val brand_color: String? = null,
    val logo_path: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
)
