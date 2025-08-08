package za.co.quantive.app.data.remote.api

import za.co.quantive.app.domain.contact.ContactType
import za.co.quantive.app.domain.contact.CreateContactRequest
import za.co.quantive.app.domain.contact.PatchContactRequest
import za.co.quantive.app.domain.contact.UpdateContactRequest
import za.co.quantive.app.domain.entities.BusinessContact
import za.co.quantive.app.domain.entities.ContactFilter

/**
 * REST-first Contact API interface for backend communication
 * CRUD operations use standard HTTP methods/endpoints
 */
interface ContactApi {

    // === STANDARD REST CRUD OPERATIONS ===

    /**
     * GET /contacts - Get paginated list of contacts
     */
    suspend fun getContacts(
        page: Int = 0,
        limit: Int = 50,
        filter: ContactFilter? = null,
    ): ApiResponse<PaginatedResponse<BusinessContact>>

    /**
     * GET /contacts/{id} - Get single contact by ID
     */
    suspend fun getContact(id: String): ApiResponse<BusinessContact>

    /**
     * POST /contacts - Create new contact
     */
    suspend fun createContact(request: CreateContactRequest): ApiResponse<BusinessContact>

    /**
     * PUT /contacts/{id} - Update existing contact (full update)
     */
    suspend fun updateContact(id: String, request: UpdateContactRequest): ApiResponse<BusinessContact>

    /**
     * PATCH /contacts/{id} - Partial contact update
     */
    suspend fun patchContact(id: String, request: PatchContactRequest): ApiResponse<BusinessContact>

    /**
     * DELETE /contacts/{id} - Soft delete contact
     */
    suspend fun deleteContact(id: String): ApiResponse<Unit>

    // === CONTACT SUB-RESOURCES ===

    /**
     * GET /contacts/search - Search contacts with query
     */
    suspend fun searchContacts(
        query: String,
        type: ContactType? = null,
        limit: Int = 20,
    ): ApiResponse<List<BusinessContact>>

    /**
     * GET /contacts/recent - Get recently accessed contacts
     */
    suspend fun getRecentContacts(
        type: ContactType? = null,
        limit: Int = 10,
    ): ApiResponse<List<BusinessContact>>
}
