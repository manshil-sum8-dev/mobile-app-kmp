package za.co.quantive.app.data.remote.api

import kotlinx.serialization.Serializable

/**
 * Business profile and settings API interface
 */
interface BusinessApi {
    suspend fun getBusinessProfile(id: String): ApiResponse<BusinessProfile>
    suspend fun updateBusinessProfile(id: String, request: UpdateBusinessProfileRequest): ApiResponse<BusinessProfile>
    suspend fun getBusinessSettings(id: String): ApiResponse<BusinessSettings>
    suspend fun updateBusinessSettings(id: String, request: UpdateBusinessSettingsRequest): ApiResponse<BusinessSettings>
    suspend fun validateTaxCompliance(id: String): ApiResponse<TaxComplianceStatus>
}

// Business models
@Serializable
data class BusinessProfile(
    val id: String,
    val name: String,
    val ownerName: String,
    val email: String,
    val phone: String?,
    val website: String?,
    val registrationNumber: String?,
    val taxNumber: String?,
    val vatNumber: String?,
    val address: BusinessAddress?,
    val logo: String?,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class BusinessSettings(
    val id: String,
    val businessId: String,
    val defaultCurrency: String,
    val defaultPaymentTerms: String,
    val autoSendInvoices: Boolean,
    val sendPaymentReminders: Boolean,
    val reminderFrequencyDays: Int,
    val invoiceNumberPrefix: String,
    val nextInvoiceNumber: Int,
    val defaultVatRate: Double,
    val timezone: String,
    val dateFormat: String,
    val updatedAt: String
)

// Request models
@Serializable
data class UpdateBusinessProfileRequest(
    val name: String? = null,
    val ownerName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val website: String? = null,
    val registrationNumber: String? = null,
    val taxNumber: String? = null,
    val vatNumber: String? = null,
    val address: BusinessAddress? = null,
    val logo: String? = null
)

@Serializable
data class UpdateBusinessSettingsRequest(
    val defaultCurrency: String? = null,
    val defaultPaymentTerms: String? = null,
    val autoSendInvoices: Boolean? = null,
    val sendPaymentReminders: Boolean? = null,
    val reminderFrequencyDays: Int? = null,
    val invoiceNumberPrefix: String? = null,
    val defaultVatRate: Double? = null,
    val timezone: String? = null,
    val dateFormat: String? = null
)

@Serializable
data class BusinessAddress(
    val streetAddress: String,
    val city: String,
    val province: String?,
    val postalCode: String,
    val country: String = "South Africa"
)