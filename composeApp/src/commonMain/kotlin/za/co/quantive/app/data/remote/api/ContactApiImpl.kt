package za.co.quantive.app.data.remote.api

import za.co.quantive.app.data.remote.SupabaseClient
import za.co.quantive.app.domain.entities.*

/**
 * Contact API implementation using Supabase backend
 */
class ContactApiImpl(
    private val client: SupabaseClient
) : ContactApi {

    override suspend fun getContacts(
        page: Int,
        limit: Int,
        filter: ContactFilter?
    ): ApiResponse<PaginatedResponse<BusinessContact>> {
        return try {
            // Build query parameters for filtering and pagination
            val params = mutableMapOf<String, String?>(
                "offset" to (page * limit).toString(),
                "limit" to limit.toString(),
                "select" to "*"
            )
            
            // Apply filters if provided
            filter?.let { f ->
                f.type?.let { params["type"] = "eq.${it.name}" }
                f.searchQuery?.let { params["or"] = "(name.ilike.*$it*,email.ilike.*$it*,phone.ilike.*$it*)" }
            }
            
            // Call Supabase REST API
            val contacts: List<BusinessContact> = client.get("rest/v1/contacts", params)
            
            // Get total count for pagination (separate query)
            val countParams = params.filterKeys { it != "offset" && it != "limit" && it != "select" }
                .toMutableMap().apply { put("select", "count") }
            val countResponse: List<Map<String, Int>> = client.get("rest/v1/contacts", countParams)
            val total = countResponse.firstOrNull()?.get("count") ?: 0
            
            ApiResponse.success(
                PaginatedResponse(
                    data = contacts,
                    pagination = PaginationInfo(
                        page = page,
                        limit = limit,
                        total = total
                    )
                )
            )
        } catch (e: Exception) {
            ApiResponse.error("Failed to fetch contacts: ${e.message}")
        }
    }

    override suspend fun getContact(id: String): ApiResponse<BusinessContact> {
        return try {
            val params = mapOf(
                "select" to "*",
                "id" to "eq.$id"
            )
            
            val contacts: List<BusinessContact> = client.get("rest/v1/contacts", params)
            val contact = contacts.firstOrNull()
            
            if (contact != null) {
                ApiResponse.success(contact)
            } else {
                ApiResponse.error("Contact not found")
            }
        } catch (e: Exception) {
            ApiResponse.error("Failed to fetch contact: ${e.message}")
        }
    }

    override suspend fun createContact(request: CreateContactRequest): ApiResponse<BusinessContact> {
        return try {
            // Create contact via Supabase stored procedure or direct insert
            val contactData = mapOf<String, Any?>(
                "business_id" to request.businessId,
                "type" to request.type.name,
                "name" to request.name,
                "display_name" to request.displayName,
                "email" to request.email,
                "phone" to request.phone,
                "website" to request.website,
                "notes" to request.notes,
                "tags" to request.tags,
                "address" to request.address?.let { addr ->
                    mapOf<String, String?>(
                        "street_address" to addr.streetAddress,
                        "city" to addr.city,
                        "province" to addr.province,
                        "postal_code" to addr.postalCode,
                        "country" to addr.country
                    )
                },
                "tax_details" to request.taxDetails?.let { tax ->
                    mapOf<String, Any?>(
                        "vat_number" to tax.vatNumber,
                        "company_registration" to tax.companyRegistration,
                        "tax_number" to tax.taxNumber,
                        "is_vat_registered" to tax.isVatRegistered,
                        "vat_rate" to tax.vatRate
                    )
                },
                "banking_details" to request.bankingDetails?.let { bank ->
                    mapOf<String, Any?>(
                        "bank_name" to bank.bankName,
                        "branch_code" to bank.branchCode,
                        "account_number" to bank.accountNumber,
                        "account_type" to bank.accountType.name,
                        "account_holder_name" to bank.accountHolderName
                    )
                }
            )
            
            // Call backend function that creates contact with validation
            val createdContact: BusinessContact = client.post(
                "rest/v1/rpc/create_contact",
                contactData
            )
            
            ApiResponse.success(createdContact)
        } catch (e: Exception) {
            ApiResponse.error("Failed to create contact: ${e.message}")
        }
    }

    override suspend fun updateContact(id: String, request: UpdateContactRequest): ApiResponse<BusinessContact> {
        return try {
            // Update contact via backend function
            val updateData = mutableMapOf<String, Any?>(
                "contact_id" to id
            ).apply {
                request.name?.let { put("name", it) }
                request.displayName?.let { put("display_name", it) }
                request.email?.let { put("email", it) }
                request.phone?.let { put("phone", it) }
                request.website?.let { put("website", it) }
                request.notes?.let { put("notes", it) }
                request.tags?.let { put("tags", it) }
                request.isActive?.let { put("is_active", it) }
                request.address?.let { addr ->
                    put("address", mapOf<String, String?>(
                        "street_address" to addr.streetAddress,
                        "city" to addr.city,
                        "province" to addr.province,
                        "postal_code" to addr.postalCode,
                        "country" to addr.country
                    ))
                }
                request.taxDetails?.let { tax ->
                    put("tax_details", mapOf<String, Any?>(
                        "vat_number" to tax.vatNumber,
                        "company_registration" to tax.companyRegistration,
                        "tax_number" to tax.taxNumber,
                        "is_vat_registered" to tax.isVatRegistered,
                        "vat_rate" to tax.vatRate
                    ))
                }
                request.bankingDetails?.let { bank ->
                    put("banking_details", mapOf<String, Any?>(
                        "bank_name" to bank.bankName,
                        "branch_code" to bank.branchCode,
                        "account_number" to bank.accountNumber,
                        "account_type" to bank.accountType.name,
                        "account_holder_name" to bank.accountHolderName
                    ))
                }
            }
            
            // Call backend function that updates contact with validation
            val updatedContact: BusinessContact = client.post(
                "rest/v1/rpc/update_contact",
                updateData
            )
            
            ApiResponse.success(updatedContact)
        } catch (e: Exception) {
            ApiResponse.error("Failed to update contact: ${e.message}")
        }
    }

    override suspend fun deleteContact(id: String): ApiResponse<Unit> {
        return try {
            val params = mapOf("id" to "eq.$id")
            
            // Soft delete - update status to inactive or mark as deleted
            val updateData = mapOf("is_active" to false, "deleted_at" to "now()")
            client.post<Unit, Map<String, Any>>("rest/v1/contacts", updateData, params)
            
            ApiResponse.success(Unit)
        } catch (e: Exception) {
            ApiResponse.error("Failed to delete contact: ${e.message}")
        }
    }

    override suspend fun getContactSummary(dateRange: DateRange?): ApiResponse<ContactSummary> {
        return try {
            // Get contact summary via backend aggregation function
            val params = mutableMapOf<String, String?>()
            dateRange?.let {
                params["start_date"] = it.start
                params["end_date"] = it.end
            }
            
            // Call backend function that calculates contact metrics
            val summary: ContactSummary = client.post(
                "rest/v1/rpc/get_contact_summary",
                params
            )
            
            ApiResponse.success(summary)
        } catch (e: Exception) {
            ApiResponse.error("Failed to fetch contact summary: ${e.message}")
        }
    }
}