package za.co.quantive.app.data.remote.api

import za.co.quantive.app.domain.entities.*

/**
 * Contact API interface for backend communication
 */
interface ContactApi {
    suspend fun getContacts(
        page: Int = 0,
        limit: Int = 50,
        filter: ContactFilter? = null
    ): ApiResponse<PaginatedResponse<BusinessContact>>

    suspend fun getContact(id: String): ApiResponse<BusinessContact>
    
    suspend fun createContact(request: CreateContactRequest): ApiResponse<BusinessContact>
    
    suspend fun updateContact(id: String, request: UpdateContactRequest): ApiResponse<BusinessContact>
    
    suspend fun deleteContact(id: String): ApiResponse<Unit>
    
    suspend fun getContactSummary(dateRange: DateRange? = null): ApiResponse<ContactSummary>
}

// Request models
@kotlinx.serialization.Serializable
data class CreateContactRequest(
    val businessId: String,
    val type: ContactType,
    val name: String,
    val displayName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val website: String? = null,
    val address: BusinessAddress? = null,
    val taxDetails: TaxDetails? = null,
    val bankingDetails: BankingDetails? = null,
    val notes: String? = null,
    val tags: List<String> = emptyList()
)

@kotlinx.serialization.Serializable
data class UpdateContactRequest(
    val name: String? = null,
    val displayName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val website: String? = null,
    val address: BusinessAddress? = null,
    val taxDetails: TaxDetails? = null,
    val bankingDetails: BankingDetails? = null,
    val notes: String? = null,
    val tags: List<String>? = null,
    val isActive: Boolean? = null
)