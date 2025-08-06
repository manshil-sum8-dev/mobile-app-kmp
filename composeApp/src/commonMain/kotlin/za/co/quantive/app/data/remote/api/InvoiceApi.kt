package za.co.quantive.app.data.remote.api

import kotlinx.serialization.Serializable
import za.co.quantive.app.domain.entities.*

/**
 * Backend API interface for invoice operations
 * All business logic, calculations, and validations are handled by the backend
 */
interface InvoiceApi {
    
    /**
     * Get paginated list of invoices with backend-applied filters
     */
    suspend fun getInvoices(
        page: Int = 0,
        limit: Int = 20,
        filter: InvoiceFilter? = null
    ): ApiResponse<PaginatedResponse<Invoice>>
    
    /**
     * Get single invoice by ID with all backend calculations
     */
    suspend fun getInvoice(id: String): ApiResponse<Invoice>
    
    /**
     * Create invoice - backend handles all calculations and validations
     */
    suspend fun createInvoice(request: CreateInvoiceRequest): ApiResponse<Invoice>
    
    /**
     * Update invoice - backend recalculates and revalidates
     */
    suspend fun updateInvoice(id: String, request: UpdateInvoiceRequest): ApiResponse<Invoice>
    
    /**
     * Delete invoice
     */
    suspend fun deleteInvoice(id: String): ApiResponse<Unit>
    
    /**
     * Send invoice to customer - backend handles delivery and status updates
     */
    suspend fun sendInvoice(id: String, request: SendInvoiceRequest): ApiResponse<Invoice>
    
    /**
     * Record payment - backend updates calculations and status
     */
    suspend fun recordPayment(id: String, request: RecordPaymentRequest): ApiResponse<Invoice>
    
    /**
     * Duplicate invoice - backend creates new invoice with calculations
     */
    suspend fun duplicateInvoice(id: String): ApiResponse<Invoice>
    
    /**
     * Convert invoice to template
     */
    suspend fun convertToTemplate(id: String, templateName: String): ApiResponse<InvoiceTemplate>
    
    /**
     * Create invoice from template - backend applies current rates and calculations
     */
    suspend fun createFromTemplate(templateId: String, request: CreateFromTemplateRequest): ApiResponse<Invoice>
    
    /**
     * Get invoice PDF - backend generates with all current data
     */
    suspend fun generatePdf(id: String, options: PdfGenerationOptions): ApiResponse<PdfResponse>
    
    /**
     * Get invoice summary/analytics - all calculations done by backend
     */
    suspend fun getInvoiceSummary(dateRange: DateRange? = null): ApiResponse<InvoiceSummary>
}

/**
 * Request to create new invoice
 * Client provides raw input, backend handles all calculations
 */
@Serializable
data class CreateInvoiceRequest(
    val customerId: String,
    val items: List<CreateInvoiceItemRequest>,
    val dueDate: String? = null, // Optional - backend can set default
    val notes: String? = null,
    val termsAndConditions: String? = null,
    val paymentTerms: String? = null,
    val templateInfo: TemplateInfo? = null,
    val recurringInfo: RecurringInfo? = null,
    val metadata: Map<String, String> = emptyMap()
)

@Serializable
data class CreateInvoiceItemRequest(
    val description: String,
    val quantity: Double,
    val unitPrice: Double, // Raw amount - backend applies currency formatting
    val notes: String? = null,
    val productId: String? = null
)

/**
 * Request to update existing invoice
 */
@Serializable
data class UpdateInvoiceRequest(
    val customerId: String? = null,
    val items: List<CreateInvoiceItemRequest>? = null,
    val dueDate: String? = null,
    val notes: String? = null,
    val termsAndConditions: String? = null,
    val paymentTerms: String? = null,
    val metadata: Map<String, String>? = null
)

/**
 * Request to send invoice
 */
@Serializable
data class SendInvoiceRequest(
    val emailAddresses: List<String>,
    val subject: String? = null,
    val message: String? = null,
    val attachPdf: Boolean = true,
    val scheduleDateTime: String? = null // For scheduled sending
)

/**
 * Request to record payment
 */
@Serializable
data class RecordPaymentRequest(
    val amount: Double,
    val currency: String = "ZAR",
    val paymentDate: String,
    val paymentMethod: String,
    val reference: String? = null,
    val notes: String? = null
)

/**
 * Request to create invoice from template
 */
@Serializable
data class CreateFromTemplateRequest(
    val customerId: String,
    val dueDate: String? = null,
    val notes: String? = null,
    val customizations: Map<String, String> = emptyMap()
)

/**
 * PDF generation options
 */
@Serializable
data class PdfGenerationOptions(
    val includePaymentStub: Boolean = true,
    val watermark: String? = null,
    val locale: String = "en-ZA",
    val template: String = "default"
)

/**
 * PDF response from backend
 */
@Serializable
data class PdfResponse(
    val url: String, // Temporary download URL
    val filename: String,
    val sizeBytes: Long,
    val expiresAt: String // ISO timestamp
)

/**
 * Invoice template entity
 */
@Serializable
data class InvoiceTemplate(
    val id: String,
    val name: String,
    val description: String? = null,
    val items: List<InvoiceTemplateItem>,
    val defaultTerms: String? = null,
    val defaultPaymentTerms: String? = null,
    val defaultDueDays: Int? = null,
    val isActive: Boolean = true,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class InvoiceTemplateItem(
    val description: String,
    val quantity: Double,
    val unitPrice: Double,
    val notes: String? = null,
    val productId: String? = null
)

// API response models are defined in ApiResponse.kt