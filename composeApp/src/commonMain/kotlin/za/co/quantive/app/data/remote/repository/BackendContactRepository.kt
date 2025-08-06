package za.co.quantive.app.data.remote.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import za.co.quantive.app.data.remote.api.*
import za.co.quantive.app.data.local.ContactCacheImpl
import za.co.quantive.app.domain.entities.*

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
    private val cache: ContactCache
) : ContactRepository {

    override suspend fun getContacts(
        filter: ContactFilter?,
        forceRefresh: Boolean
    ): Flow<Result<List<BusinessContact>>> = flow {
        try {
            // Emit cached data first for better UX
            if (!forceRefresh) {
                val cachedContacts = cache.getContacts(filter)
                if (cachedContacts.isNotEmpty()) {
                    emit(Result.success(cachedContacts))
                }
            }

            // Fetch from backend
            val response = api.getContacts(
                page = 0,
                limit = 100, // TODO: Implement proper pagination
                filter = filter
            )

            if (response.isSuccess()) {
                val contacts = response.data!!.data
                cache.saveContacts(contacts)
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

    override suspend fun getContactSummary(dateRange: DateRange?): Result<ContactSummary> {
        return try {
            val response = api.getContactSummary(dateRange)
            if (response.isSuccess()) {
                Result.success(response.data!!)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch contact summary"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Backend-driven contact repository interface
 */
interface ContactRepository {
    suspend fun getContacts(filter: ContactFilter? = null, forceRefresh: Boolean = false): Flow<Result<List<BusinessContact>>>
    suspend fun getContact(id: String): Result<BusinessContact>
    suspend fun createContact(request: CreateContactRequest): Result<BusinessContact>
    suspend fun updateContact(id: String, request: UpdateContactRequest): Result<BusinessContact>
    suspend fun deleteContact(id: String): Result<Unit>
    suspend fun getContactSummary(dateRange: DateRange? = null): Result<ContactSummary>
}

