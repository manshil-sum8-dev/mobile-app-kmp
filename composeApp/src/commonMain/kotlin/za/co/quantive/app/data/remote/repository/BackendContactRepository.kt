package za.co.quantive.app.data.remote.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import za.co.quantive.app.data.remote.api.ContactApi
import za.co.quantive.app.domain.contact.BatchUpdateResult
import za.co.quantive.app.domain.contact.ContactAnalytics
import za.co.quantive.app.domain.contact.ContactBatchUpdate
import za.co.quantive.app.domain.contact.ContactRepository
import za.co.quantive.app.domain.contact.ContactSummary
import za.co.quantive.app.domain.contact.ContactType
import za.co.quantive.app.domain.contact.CreateContactRequest
import za.co.quantive.app.domain.contact.ExportFormat
import za.co.quantive.app.domain.contact.ExportResult
import za.co.quantive.app.domain.contact.ImportOptions
import za.co.quantive.app.domain.contact.ImportResult
import za.co.quantive.app.domain.contact.PatchContactRequest
import za.co.quantive.app.domain.contact.UpdateContactRequest
import za.co.quantive.app.domain.entities.BusinessContact
import za.co.quantive.app.domain.entities.ContactFilter
import za.co.quantive.app.domain.shared.DateRange

// Add the interface definition here for now
interface ContactCache {
    suspend fun getContacts(filter: ContactFilter? = null): List<BusinessContact>
    suspend fun getContact(id: String): BusinessContact?
    suspend fun saveContacts(contacts: List<BusinessContact>)
    suspend fun saveContact(contact: BusinessContact)
    suspend fun deleteContact(id: String)
    suspend fun clearCache()
}

/**
 * Backend-driven contact repository
 * All business logic and calculations handled by backend
 */
class BackendContactRepository(
    private val api: ContactApi,
    private val cache: ContactCache,
) : ContactRepository {

    override suspend fun getContacts(
        filter: ContactFilter?,
        forceRefresh: Boolean,
    ): Flow<Result<List<BusinessContact>>> = flow {
        try {
            // Check cache first
            if (!forceRefresh) {
                val cachedContacts = cache.getContacts(filter)
                if (cachedContacts.isNotEmpty()) {
                    emit(Result.success(cachedContacts)) // Fast cache response
                }
            }

            // Fetch from backend
            val response = api.getContacts(
                page = 0,
                limit = 100, // TODO: Implement proper pagination
                filter = filter,
            )

            if (response.isSuccess()) {
                val contacts = response.data!!.data

                // Smart caching with appropriate TTL based on filter
                cache.saveContacts(contacts)

                // Cache is automatically saved by saveContacts

                emit(Result.success(contacts))
            } else {
                emit(Result.failure(Exception(response.message ?: "Failed to fetch contacts")))
            }
        } catch (e: Exception) {
            // Emit cached data as fallback on network error
            val cachedContacts = cache.getContacts(filter)
            if (cachedContacts.isNotEmpty()) {
                emit(Result.success(cachedContacts))
            } else {
                emit(Result.failure(e))
            }
        }
    }

    override suspend fun getContact(id: String): Result<BusinessContact> {
        return try {
            val response = api.getContact(id)
            if (response.isSuccess()) {
                val contact = response.data!!
                cache.saveContact(contact)
                Result.success(contact)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch contact"))
            }
        } catch (e: Exception) {
            // Try cache fallback
            val cachedContact = cache.getContact(id)
            if (cachedContact != null) {
                Result.success(cachedContact)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun createContact(request: CreateContactRequest): Result<BusinessContact> {
        return try {
            val response = api.createContact(request)
            if (response.isSuccess()) {
                val contact = response.data!!
                cache.saveContact(contact)
                Result.success(contact)
            } else {
                Result.failure(Exception(response.message ?: "Failed to create contact"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateContact(id: String, request: UpdateContactRequest): Result<BusinessContact> {
        return try {
            val response = api.updateContact(id, request)
            if (response.isSuccess()) {
                val contact = response.data!!
                cache.saveContact(contact)
                Result.success(contact)
            } else {
                Result.failure(Exception(response.message ?: "Failed to update contact"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteContact(id: String): Result<Unit> {
        return try {
            val response = api.deleteContact(id)
            if (response.isSuccess()) {
                cache.deleteContact(id)
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Failed to delete contact"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get recent customers for invoice creation autocomplete
     * Uses smart caching for frequently accessed data
     */
    suspend fun getRecentCustomers(limit: Int = 20): Result<List<BusinessContact>> {
        return try {
            // Check cache first
            val cachedCustomers = cache.getContacts(ContactFilter(type = ContactType.CUSTOMER))
            if (cachedCustomers.isNotEmpty()) {
                return Result.success(cachedCustomers.take(limit)) // Instant response
            }

            // Fetch from backend using new REST endpoint
            val response = api.getContacts(
                page = 0,
                limit = limit,
                filter = ContactFilter(type = ContactType.CUSTOMER),
            )

            if (response.isSuccess()) {
                val customers = response.data!!.data
                cache.saveContacts(customers)
                Result.success(customers)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch recent customers"))
            }
        } catch (e: Exception) {
            // Fallback to cache on network error
            val cachedCustomers = cache.getContacts(ContactFilter(type = ContactType.CUSTOMER))
            if (cachedCustomers.isNotEmpty()) {
                Result.success(cachedCustomers.take(limit))
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun patchContact(id: String, request: PatchContactRequest): Result<BusinessContact> {
        return try {
            // TODO: Implement patch contact via API
            Result.failure(Exception("Patch contact not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchContacts(query: String, type: ContactType?, limit: Int): Result<List<BusinessContact>> {
        return try {
            val response = api.searchContacts(query, type, limit)
            if (response.isSuccess()) {
                Result.success(response.data!!)
            } else {
                Result.failure(Exception(response.message ?: "Search failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecentContacts(type: ContactType?, limit: Int): Result<List<BusinessContact>> {
        return try {
            val response = api.getRecentContacts(type, limit)
            if (response.isSuccess()) {
                Result.success(response.data!!)
            } else {
                Result.failure(Exception(response.message ?: "Failed to get recent contacts"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFrequentCustomers(limit: Int): Result<List<BusinessContact>> {
        return try {
            // Use recent customers as frequent customers for now
            getRecentContacts(ContactType.CUSTOMER, limit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getContactSummary(dateRange: DateRange?): Result<ContactSummary> {
        return try {
            // TODO: Implement contact summary via RPC service
            Result.failure(Exception("Contact summary not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getContactAnalytics(contactId: String, dateRange: DateRange?): Result<ContactAnalytics> {
        return try {
            // TODO: Implement contact analytics via RPC service
            Result.failure(Exception("Contact analytics not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun importContacts(contacts: List<CreateContactRequest>, options: ImportOptions): Result<ImportResult> {
        return try {
            // TODO: Implement bulk import
            Result.failure(Exception("Contact import not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun exportContacts(filter: ContactFilter?, format: ExportFormat): Result<ExportResult> {
        return try {
            // TODO: Implement export functionality
            Result.failure(Exception("Contact export not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateContactsBatch(updates: List<ContactBatchUpdate>): Result<BatchUpdateResult> {
        return try {
            // TODO: Implement batch update
            Result.failure(Exception("Batch update not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
