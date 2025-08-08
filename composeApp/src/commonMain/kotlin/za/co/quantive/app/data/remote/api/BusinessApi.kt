package za.co.quantive.app.data.remote.api

import kotlinx.serialization.Serializable

/**
 * REST-first Business profile and settings API interface
 * CRUD operations for business data
 */
interface BusinessApi {

    // === BUSINESS PROFILE REST ENDPOINTS ===

    /**
     * GET /business/{id}/profile - Get business profile
     */
    suspend fun getBusinessProfile(id: String): ApiResponse<BusinessProfile>

    /**
     * PUT /business/{id}/profile - Update business profile
     */
    suspend fun updateBusinessProfile(id: String, request: UpdateBusinessProfileRequest): ApiResponse<BusinessProfile>

    /**
     * PATCH /business/{id}/profile - Partial profile update
     */
    suspend fun patchBusinessProfile(id: String, request: PatchBusinessProfileRequest): ApiResponse<BusinessProfile>

    // === BUSINESS SETTINGS REST ENDPOINTS ===

    /**
     * GET /business/{id}/settings - Get business settings
     */
    suspend fun getBusinessSettings(id: String): ApiResponse<BusinessSettings>

    /**
     * PUT /business/{id}/settings - Update business settings
     */
    suspend fun updateBusinessSettings(id: String, request: UpdateBusinessSettingsRequest): ApiResponse<BusinessSettings>

    /**
     * PATCH /business/{id}/settings - Partial settings update
     */
    suspend fun patchBusinessSettings(id: String, request: PatchBusinessSettingsRequest): ApiResponse<BusinessSettings>
}

/**
 * RPC functions for heavy computational operations on business data
 */
interface BusinessComplianceRpc {

    /**
     * RPC: Validate tax compliance - complex validation with SARS integration
     */
    suspend fun validateTaxCompliance(id: String): ApiResponse<TaxComplianceStatus>

    /**
     * RPC: Generate compliance report
     * TODO: Re-enable when compliance models are implemented
     */
    // suspend fun generateComplianceReport(request: ComplianceReportRequest): ApiResponse<ComplianceReportResponse>

    /**
     * RPC: Audit business configuration
     * TODO: Re-enable when compliance models are implemented
     */
    // suspend fun auditBusinessConfig(id: String): ApiResponse<BusinessAuditReport>
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
    val updatedAt: String,
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
    val updatedAt: String,
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
    val logo: String? = null,
)

@Serializable
data class UpdateBusinessSettingsRequest(
    val defaultCurrency: String,
    val defaultPaymentTerms: String,
    val autoSendInvoices: Boolean,
    val sendPaymentReminders: Boolean,
    val reminderFrequencyDays: Int,
    val invoiceNumberPrefix: String,
    val defaultVatRate: Double,
    val timezone: String,
    val dateFormat: String,
)

@Serializable
data class PatchBusinessProfileRequest(
    val name: String? = null,
    val ownerName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val website: String? = null,
    val registrationNumber: String? = null,
    val taxNumber: String? = null,
    val vatNumber: String? = null,
    val address: BusinessAddress? = null,
    val logo: String? = null,
)

@Serializable
data class PatchBusinessSettingsRequest(
    val defaultCurrency: String? = null,
    val defaultPaymentTerms: String? = null,
    val autoSendInvoices: Boolean? = null,
    val sendPaymentReminders: Boolean? = null,
    val reminderFrequencyDays: Int? = null,
    val invoiceNumberPrefix: String? = null,
    val defaultVatRate: Double? = null,
    val timezone: String? = null,
    val dateFormat: String? = null,
)

@Serializable
data class BusinessAddress(
    val streetAddress: String,
    val city: String,
    val province: String?,
    val postalCode: String,
    val country: String = "South Africa",
)
