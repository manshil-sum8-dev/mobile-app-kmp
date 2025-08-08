package za.co.quantive.app.domain.contact

import kotlinx.coroutines.flow.Flow
import za.co.quantive.app.domain.entities.BusinessContact
import za.co.quantive.app.domain.entities.ContactFilter
import za.co.quantive.app.domain.shared.DateRange

/**
 * Domain repository interface for business contact management
 * Follows backend-driven architecture with smart caching for performance
 */
interface ContactRepository {

    // === CORE CRUD OPERATIONS ===

    /**
     * Get paginated list of contacts with optional filtering
     * Returns Flow for reactive UI updates with caching strategy
     */
    suspend fun getContacts(
        filter: ContactFilter? = null,
        forceRefresh: Boolean = false,
    ): Flow<Result<List<BusinessContact>>>

    /**
     * Get single contact by ID
     */
    suspend fun getContact(id: String): Result<BusinessContact>

    /**
     * Create new contact with backend validation
     */
    suspend fun createContact(request: CreateContactRequest): Result<BusinessContact>

    /**
     * Update existing contact (full update)
     */
    suspend fun updateContact(id: String, request: UpdateContactRequest): Result<BusinessContact>

    /**
     * Partial update for specific contact fields
     */
    suspend fun patchContact(id: String, request: PatchContactRequest): Result<BusinessContact>

    /**
     * Soft delete contact
     */
    suspend fun deleteContact(id: String): Result<Unit>

    // === CONTACT SEARCH AND DISCOVERY ===

    /**
     * Search contacts with query string
     */
    suspend fun searchContacts(
        query: String,
        type: ContactType? = null,
        limit: Int = 50,
    ): Result<List<BusinessContact>>

    /**
     * Get recently accessed contacts for quick selection
     */
    suspend fun getRecentContacts(
        type: ContactType? = null,
        limit: Int = 20,
    ): Result<List<BusinessContact>>

    /**
     * Get frequent customers for invoice creation autocomplete
     */
    suspend fun getFrequentCustomers(limit: Int = 10): Result<List<BusinessContact>>

    // === ANALYTICS AND REPORTING ===

    /**
     * Get contact summary with backend-calculated metrics
     */
    suspend fun getContactSummary(dateRange: DateRange? = null): Result<ContactSummary>

    /**
     * Get contact interaction analytics
     */
    suspend fun getContactAnalytics(
        contactId: String,
        dateRange: DateRange? = null,
    ): Result<ContactAnalytics>

    // === BULK OPERATIONS ===

    /**
     * Import contacts from various sources
     */
    suspend fun importContacts(
        contacts: List<CreateContactRequest>,
        options: ImportOptions = ImportOptions(),
    ): Result<ImportResult>

    /**
     * Export contacts to various formats
     */
    suspend fun exportContacts(
        filter: ContactFilter? = null,
        format: ExportFormat = ExportFormat.CSV,
    ): Result<ExportResult>

    /**
     * Update multiple contacts with batch operation
     */
    suspend fun updateContactsBatch(
        updates: List<ContactBatchUpdate>,
    ): Result<BatchUpdateResult>
}

/**
 * Contact analytics data
 */
data class ContactAnalytics(
    val contactId: String,
    val totalInvoices: Int,
    val totalRevenue: Double,
    val averageInvoiceValue: Double,
    val paymentTrends: List<PaymentTrend>,
    val lastInteractionDate: String?,
    val communicationPreferences: Map<String, Any>,
)

/**
 * Payment trend data for contact analytics
 */
data class PaymentTrend(
    val period: String,
    val invoiceCount: Int,
    val totalAmount: Double,
    val averagePaymentTime: Int,
)

/**
 * Contact import options
 */
data class ImportOptions(
    val skipDuplicates: Boolean = true,
    val updateExisting: Boolean = false,
    val validateData: Boolean = true,
    val dryRun: Boolean = false,
)

/**
 * Contact import result
 */
data class ImportResult(
    val totalProcessed: Int,
    val successCount: Int,
    val skipCount: Int,
    val errorCount: Int,
    val errors: List<ImportError> = emptyList(),
)

/**
 * Import error details
 */
data class ImportError(
    val rowNumber: Int,
    val contactName: String?,
    val errorMessage: String,
    val errorField: String?,
)

/**
 * Export result
 */
data class ExportResult(
    val fileName: String,
    val fileUrl: String,
    val recordCount: Int,
    val exportedAt: String,
)

/**
 * Export format options
 */
enum class ExportFormat {
    CSV,
    XLSX,
    JSON,
    VCARD,
}

/**
 * Batch update operation
 */
data class ContactBatchUpdate(
    val contactId: String,
    val updates: PatchContactRequest,
)

/**
 * Batch update result
 */
data class BatchUpdateResult(
    val totalRequested: Int,
    val successCount: Int,
    val failureCount: Int,
    val errors: List<BatchUpdateError> = emptyList(),
)

/**
 * Batch update error
 */
data class BatchUpdateError(
    val contactId: String,
    val errorMessage: String,
    val errorCode: String? = null,
)
