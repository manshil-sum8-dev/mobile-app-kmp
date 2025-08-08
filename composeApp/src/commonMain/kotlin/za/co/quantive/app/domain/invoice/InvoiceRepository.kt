package za.co.quantive.app.domain.invoice

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import za.co.quantive.app.data.remote.api.CreateFromTemplateRequest
import za.co.quantive.app.data.remote.api.CreateInvoiceRequest
import za.co.quantive.app.data.remote.api.InvoiceTemplate
import za.co.quantive.app.data.remote.api.PatchInvoiceRequest
import za.co.quantive.app.data.remote.api.PdfGenerationOptions
import za.co.quantive.app.data.remote.api.PdfResponse
import za.co.quantive.app.data.remote.api.RecordPaymentRequest
import za.co.quantive.app.data.remote.api.SendInvoiceRequest
import za.co.quantive.app.data.remote.api.UpdateInvoiceRequest
import za.co.quantive.app.domain.entities.Invoice
import za.co.quantive.app.domain.entities.InvoiceFilter
import za.co.quantive.app.domain.entities.InvoiceStatus
import za.co.quantive.app.domain.entities.InvoiceSummary
import za.co.quantive.app.domain.entities.Money
import za.co.quantive.app.domain.shared.DateRange
import za.co.quantive.app.domain.shared.ExportFormat
import za.co.quantive.app.domain.shared.ExportResponse

/**
 * Comprehensive invoice repository interface following enterprise architecture patterns
 * Provides complete invoice management including templates, recurring invoices, and analytics
 */
interface InvoiceRepository {

    // === CORE CRUD OPERATIONS ===

    /**
     * Get paginated list of invoices with optional filtering
     * Returns Flow for reactive UI updates with caching strategy
     */
    suspend fun getInvoices(
        filter: InvoiceFilter? = null,
        forceRefresh: Boolean = false,
    ): Flow<Result<List<Invoice>>>

    /**
     * Get single invoice by ID with full backend calculations
     */
    suspend fun getInvoice(id: String): Result<Invoice>

    /**
     * Create new invoice with backend-driven calculations
     */
    suspend fun createInvoice(request: CreateInvoiceRequest): Result<Invoice>

    /**
     * Update existing invoice with recalculations
     */
    suspend fun updateInvoice(id: String, request: UpdateInvoiceRequest): Result<Invoice>

    /**
     * Partial update for status changes and minor modifications
     */
    suspend fun patchInvoice(id: String, request: PatchInvoiceRequest): Result<Invoice>

    /**
     * Soft delete invoice
     */
    suspend fun deleteInvoice(id: String): Result<Unit>

    // === INVOICE LIFECYCLE OPERATIONS ===

    /**
     * Send invoice to customer via email
     */
    suspend fun sendInvoice(id: String, request: SendInvoiceRequest): Result<Invoice>

    /**
     * Record payment against invoice
     */
    suspend fun recordPayment(id: String, request: RecordPaymentRequest): Result<Invoice>

    /**
     * Create duplicate invoice from existing one
     */
    suspend fun duplicateInvoice(id: String): Result<Invoice>

    /**
     * Generate PDF for invoice with customization options
     */
    suspend fun generatePdf(id: String): Result<String>

    /**
     * Generate PDF with custom options
     */
    suspend fun generatePdfWithOptions(id: String, options: PdfGenerationOptions): Result<PdfResponse>

    // === TEMPLATE OPERATIONS ===

    /**
     * Get all available invoice templates
     */
    suspend fun getInvoiceTemplates(): Result<List<InvoiceTemplate>>

    /**
     * Get specific invoice template by ID
     */
    suspend fun getInvoiceTemplate(templateId: String): Result<InvoiceTemplate>

    /**
     * Convert existing invoice to template
     */
    suspend fun convertToTemplate(invoiceId: String, templateName: String): Result<InvoiceTemplate>

    /**
     * Create invoice from template with customizations
     */
    suspend fun createFromTemplate(templateId: String, request: CreateFromTemplateRequest): Result<Invoice>

    /**
     * Update template information
     */
    suspend fun updateTemplate(templateId: String, request: UpdateTemplateRequest): Result<InvoiceTemplate>

    /**
     * Delete invoice template
     */
    suspend fun deleteTemplate(templateId: String): Result<Unit>

    // === RECURRING INVOICE OPERATIONS ===

    /**
     * Set up recurring invoice schedule
     */
    suspend fun setupRecurringInvoice(invoiceId: String, recurringInfo: RecurringInfo): Result<Unit>

    /**
     * Get all active recurring invoices
     */
    suspend fun getRecurringInvoices(activeOnly: Boolean = true): Result<List<Invoice>>

    /**
     * Get recurring invoice schedule preview
     */
    suspend fun getRecurringSchedulePreview(
        invoiceId: String,
        recurringInfo: RecurringInfo,
    ): Result<RecurringSchedulePreview>

    /**
     * Update recurring invoice settings
     */
    suspend fun updateRecurringSettings(invoiceId: String, recurringInfo: RecurringInfo): Result<Unit>

    /**
     * Cancel/deactivate recurring invoice
     */
    suspend fun cancelRecurringInvoice(invoiceId: String): Result<Unit>

    /**
     * Generate next recurring invoice manually
     */
    suspend fun generateRecurringInvoice(recurringInvoiceId: String): Result<Invoice>

    // === ANALYTICS AND REPORTING ===

    /**
     * Get invoice summary with backend-calculated metrics
     * Uses DateRange from domain.shared for consistency
     */
    suspend fun getInvoiceSummary(dateRange: DateRange? = null): Result<InvoiceSummary>

    /**
     * Get invoice trends and analytics
     */
    suspend fun getInvoiceAnalytics(dateRange: DateRange? = null): Result<InvoiceAnalyticsData>

    /**
     * Export invoices to various formats
     */
    suspend fun exportInvoices(
        filter: InvoiceFilter? = null,
        format: ExportFormat = ExportFormat.PDF,
        dateRange: DateRange? = null,
    ): Result<ExportResponse>

    // === BATCH OPERATIONS ===

    /**
     * Send multiple invoices in batch
     */
    suspend fun sendInvoicesBatch(
        invoiceIds: List<String>,
        request: SendInvoiceRequest,
    ): Result<BatchOperationResult>

    /**
     * Delete multiple invoices in batch
     */
    suspend fun deleteInvoicesBatch(invoiceIds: List<String>): Result<BatchOperationResult>

    /**
     * Update status for multiple invoices
     */
    suspend fun updateInvoicesStatusBatch(
        invoiceIds: List<String>,
        status: InvoiceStatus,
    ): Result<BatchOperationResult>
}

/**
 * Request for updating invoice template
 */
@Serializable
data class UpdateTemplateRequest(
    val templateName: String,
    val description: String? = null,
    val isDefault: Boolean = false,
    val isActive: Boolean = true,
    val customFields: Map<String, String> = emptyMap(),
    val brandingColors: Map<String, String> = emptyMap(),
    val termsAndConditions: String? = null,
    val footerText: String? = null,
)

/**
 * Invoice analytics data for reporting
 */
@Serializable
data class InvoiceAnalyticsData(
    val totalRevenue: Money,
    val totalInvoicesCount: Int,
    val paidInvoicesCount: Int,
    val overdueInvoicesCount: Int,
    val averageInvoiceValue: Money,
    val averagePaymentTime: Int, // in days
    val paymentTrends: List<PaymentTrendData>,
    val statusDistribution: Map<InvoiceStatus, Int>,
    val monthlyRevenue: List<MonthlyRevenueData>,
    val topCustomers: List<CustomerRevenueData>,
)

/**
 * Payment trend data for analytics
 */
@Serializable
data class PaymentTrendData(
    val period: String, // ISO date string
    val totalAmount: Money,
    val invoiceCount: Int,
    val averagePaymentTime: Int,
)

/**
 * Monthly revenue data for trends
 */
@Serializable
data class MonthlyRevenueData(
    val month: String, // YYYY-MM format
    val revenue: Money,
    val invoiceCount: Int,
    val averageInvoiceValue: Money,
)

/**
 * Customer revenue data for top customers analysis
 */
@Serializable
data class CustomerRevenueData(
    val customerId: String,
    val customerName: String,
    val totalRevenue: Money,
    val invoiceCount: Int,
    val averageInvoiceValue: Money,
    val lastInvoiceDate: String? = null,
)

/**
 * Result for batch operations
 */
@Serializable
data class BatchOperationResult(
    val totalRequested: Int,
    val successCount: Int,
    val failureCount: Int,
    val errors: List<BatchOperationError> = emptyList(),
)

/**
 * Error details for batch operations
 */
@Serializable
data class BatchOperationError(
    val invoiceId: String,
    val errorMessage: String,
    val errorCode: String? = null,
)
