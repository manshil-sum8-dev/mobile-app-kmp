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

    // Extended onboarding fields
    val business_type: BusinessType? = null,
    val company_registration_number: String? = null,
    val website: String? = null,
    val industry: String? = null,
    val employee_count: EmployeeCount? = null,
    val business_address: BusinessAddress? = null,
    val country_id: String? = null,
    val province_id: String? = null,
    val phone: String? = null,
    val description: String? = null,

    val created_at: String? = null,
    val updated_at: String? = null,
)

@Serializable
enum class BusinessType {
    INDIVIDUAL,
    COMPANY,
}

@Serializable
enum class EmployeeCount(val displayName: String) {
    SOLO("Just me"),
    SMALL("2-10 employees"),
    MEDIUM("11-50 employees"),
    LARGE("51-200 employees"),
    ENTERPRISE("200+ employees"),
}

@Serializable
data class BusinessAddress(
    val street: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postal_code: String? = null,
    val country: String? = null,
)

@Serializable
data class Country(
    val id: String,
    val iso_code: String,
    val iso3_code: String,
    val name: String,
    val currency_code: String?,
    val phone_prefix: String?,
    val continent: String,
    val region: String?,
    val subregion: String?,
)

@Serializable
data class Province(
    val id: String,
    val country_id: String,
    val code: String,
    val name: String,
    val type: String,
)
