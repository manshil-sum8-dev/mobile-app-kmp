package za.co.quantive.app.data.remote.api

import kotlinx.serialization.Serializable
import za.co.quantive.app.domain.entities.Invoice
import za.co.quantive.app.domain.entities.InvoiceFilter
import za.co.quantive.app.domain.entities.InvoiceStatus
import za.co.quantive.app.domain.entities.RecurringInfo
import za.co.quantive.app.domain.entities.TemplateInfo

/**
 * REST-first API interface for invoice operations
 * CRUD operations use standard HTTP methods/endpoints
 * Business logic handled by backend with proper REST patterns
 */
interface InvoiceApi {

    // === STANDARD REST CRUD OPERATIONS ===

    /**
     * GET /invoices - Get paginated list of invoices with backend-applied filters
     */
    suspend fun getInvoices(
        page: Int = 0,
        limit: Int = 20,
        filter: InvoiceFilter? = null,
    ): ApiResponse<PaginatedResponse<Invoice>>

    /**
     * GET /invoices/{id} - Get single invoice by ID with all backend calculations
     */
    suspend fun getInvoice(id: String): ApiResponse<Invoice>

    /**
     * POST /invoices - Create new invoice with backend calculations
     */
    suspend fun createInvoice(request: CreateInvoiceRequest): ApiResponse<Invoice>

    /**
     * PUT /invoices/{id} - Update existing invoice with recalculations
     */
    suspend fun updateInvoice(id: String, request: UpdateInvoiceRequest): ApiResponse<Invoice>

    /**
     * PATCH /invoices/{id} - Partial update (status changes, etc)
     */
    suspend fun patchInvoice(id: String, request: PatchInvoiceRequest): ApiResponse<Invoice>

    /**
     * DELETE /invoices/{id} - Soft delete invoice
     */
    suspend fun deleteInvoice(id: String): ApiResponse<Unit>

    // === INVOICE SUB-RESOURCES (REST nested resources) ===

    /**
     * POST /invoices/{id}/send - Send invoice to customer
     */
    suspend fun sendInvoice(id: String, request: SendInvoiceRequest): ApiResponse<Invoice>

    /**
     * POST /invoices/{id}/payments - Record new payment
     */
    suspend fun recordPayment(id: String, request: RecordPaymentRequest): ApiResponse<Invoice>

    /**
     * POST /invoices/{id}/duplicate - Create duplicate invoice
     */
    suspend fun duplicateInvoice(id: String): ApiResponse<Invoice>

    /**
     * GET /invoices/{id}/pdf - Generate and download PDF
     */
    suspend fun generatePdf(id: String, options: PdfGenerationOptions): ApiResponse<PdfResponse>

    // === TEMPLATE RESOURCES ===

    /**
     * POST /invoice-templates - Convert invoice to template
     */
    suspend fun convertToTemplate(id: String, templateName: String): ApiResponse<InvoiceTemplate>

    /**
     * POST /invoices/from-template - Create invoice from template
     */
    suspend fun createFromTemplate(templateId: String, request: CreateFromTemplateRequest): ApiResponse<Invoice>
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
    val metadata: Map<String, String> = emptyMap(),
)

@Serializable
data class CreateInvoiceItemRequest(
    val description: String,
    val quantity: Double,
    val unitPrice: Double, // Raw amount - backend applies currency formatting
    val notes: String? = null,
    val productId: String? = null,
)

/**
 * Request to update existing invoice (PUT - full update)
 */
@Serializable
data class UpdateInvoiceRequest(
    val customerId: String,
    val items: List<CreateInvoiceItemRequest>,
    val dueDate: String? = null,
    val notes: String? = null,
    val termsAndConditions: String? = null,
    val paymentTerms: String? = null,
    val metadata: Map<String, String> = emptyMap(),
)

/**
 * Request for partial invoice updates (PATCH)
 */
@Serializable
data class PatchInvoiceRequest(
    val status: InvoiceStatus? = null,
    val dueDate: String? = null,
    val notes: String? = null,
    val termsAndConditions: String? = null,
    val paymentTerms: String? = null,
    val metadata: Map<String, String>? = null,
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
    val scheduleDateTime: String? = null, // For scheduled sending
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
    val notes: String? = null,
)

/**
 * Request to create invoice from template
 */
@Serializable
data class CreateFromTemplateRequest(
    val customerId: String,
    val dueDate: String? = null,
    val notes: String? = null,
    val customizations: Map<String, String> = emptyMap(),
)

/**
 * PDF generation options
 */
@Serializable
data class PdfGenerationOptions(
    val includePaymentStub: Boolean = true,
    val watermark: String? = null,
    val locale: String = "en-ZA",
    val template: String = "default",
)

/**
 * PDF response from backend
 */
@Serializable
data class PdfResponse(
    val url: String, // Temporary download URL
    val filename: String,
    val sizeBytes: Long,
    val expiresAt: String, // ISO timestamp
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
    val updatedAt: String,
)

@Serializable
data class InvoiceTemplateItem(
    val description: String,
    val quantity: Double,
    val unitPrice: Double,
    val notes: String? = null,
    val productId: String? = null,
)
