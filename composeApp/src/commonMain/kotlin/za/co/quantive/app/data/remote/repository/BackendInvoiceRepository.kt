package za.co.quantive.app.data.remote.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import za.co.quantive.app.data.remote.api.CreateFromTemplateRequest
import za.co.quantive.app.data.remote.api.CreateInvoiceRequest
import za.co.quantive.app.data.remote.api.InvoiceApi
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
import za.co.quantive.app.domain.invoice.BatchOperationResult
import za.co.quantive.app.domain.invoice.InvoiceAnalyticsData
import za.co.quantive.app.domain.invoice.InvoiceCache
import za.co.quantive.app.domain.invoice.InvoiceRepository
import za.co.quantive.app.domain.invoice.RecurringSchedulePreview
import za.co.quantive.app.domain.invoice.UpdateTemplateRequest
import za.co.quantive.app.domain.shared.DateRange
import za.co.quantive.app.domain.shared.ExportFormat
import za.co.quantive.app.domain.shared.ExportResponse
import za.co.quantive.app.domain.invoice.RecurringInfo as EnhancedRecurringInfo

/**
 * Backend-driven invoice repository
 * All business logic, calculations, and validations handled by backend
 */
class BackendInvoiceRepository(
    private val api: InvoiceApi,
    private val cache: InvoiceCache,
) : InvoiceRepository {

    /**
     * Get invoices with backend-applied filtering and calculations
     */
    override suspend fun getInvoices(
        filter: InvoiceFilter?,
        forceRefresh: Boolean,
    ): Flow<Result<List<Invoice>>> = flow {
        try {
            // Emit cached data first for better UX
            if (!forceRefresh) {
                val cachedInvoices = cache.getInvoices(filter)
                if (cachedInvoices.isNotEmpty()) {
                    emit(Result.success(cachedInvoices))
                }
            }

            // Fetch from backend - all calculations done server-side
            val response = api.getInvoices(
                page = 0,
                limit = 100, // TODO: Implement proper pagination
                filter = filter,
            )

            if (response.isSuccess()) {
                val invoices = response.data!!.data
                // Cache backend-calculated data
                cache.saveInvoices(invoices)
                emit(Result.success(invoices))
            } else {
                emit(Result.failure(Exception(response.message ?: "Failed to fetch invoices")))
            }
        } catch (e: Exception) {
            // Emit cached data as fallback on network error
            val cachedInvoices = cache.getInvoices(filter)
            if (cachedInvoices.isNotEmpty()) {
                emit(Result.success(cachedInvoices))
            } else {
                emit(Result.failure(e))
            }
        }
    }

    /**
     * Get single invoice by ID - backend provides all calculations
     */
    override suspend fun getInvoice(id: String): Result<Invoice> {
        return try {
            val response = api.getInvoice(id)
            if (response.isSuccess()) {
                val invoice = response.data!!
                cache.saveInvoice(invoice)
                Result.success(invoice)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch invoice"))
            }
        } catch (e: Exception) {
            // Try cache fallback
            val cachedInvoice = cache.getInvoice(id)
            if (cachedInvoice != null) {
                Result.success(cachedInvoice)
            } else {
                Result.failure(e)
            }
        }
    }

    /**
     * Create invoice - backend handles all calculations and validations
     */
    override suspend fun createInvoice(request: CreateInvoiceRequest): Result<Invoice> {
        return try {
            val response = api.createInvoice(request)
            if (response.isSuccess()) {
                val invoice = response.data!!
                cache.saveInvoice(invoice)
                Result.success(invoice)
            } else {
                Result.failure(Exception(response.message ?: "Failed to create invoice"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update invoice - backend recalculates everything
     */
    override suspend fun updateInvoice(id: String, request: UpdateInvoiceRequest): Result<Invoice> {
        return try {
            val response = api.updateInvoice(id, request)
            if (response.isSuccess()) {
                val invoice = response.data!!
                cache.saveInvoice(invoice)
                Result.success(invoice)
            } else {
                Result.failure(Exception(response.message ?: "Failed to update invoice"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Partial update for invoice status changes and minor modifications
     */
    override suspend fun patchInvoice(id: String, request: PatchInvoiceRequest): Result<Invoice> {
        return try {
            val response = api.patchInvoice(id, request)
            if (response.isSuccess()) {
                val invoice = response.data!!
                cache.saveInvoice(invoice)
                Result.success(invoice)
            } else {
                Result.failure(Exception(response.message ?: "Failed to patch invoice"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete invoice
     */
    override suspend fun deleteInvoice(id: String): Result<Unit> {
        return try {
            val response = api.deleteInvoice(id)
            if (response.isSuccess()) {
                cache.deleteInvoice(id)
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Failed to delete invoice"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Send invoice - backend handles delivery and status management
     */
    override suspend fun sendInvoice(id: String, request: SendInvoiceRequest): Result<Invoice> {
        return try {
            val response = api.sendInvoice(id, request)
            if (response.isSuccess()) {
                val invoice = response.data!!
                cache.saveInvoice(invoice)
                Result.success(invoice)
            } else {
                Result.failure(Exception(response.message ?: "Failed to send invoice"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Record payment - backend updates all calculations
     */
    override suspend fun recordPayment(id: String, request: RecordPaymentRequest): Result<Invoice> {
        return try {
            val response = api.recordPayment(id, request)
            if (response.isSuccess()) {
                val invoice = response.data!!
                cache.saveInvoice(invoice)
                Result.success(invoice)
            } else {
                Result.failure(Exception(response.message ?: "Failed to record payment"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Duplicate invoice - backend creates new invoice with current calculations
     */
    override suspend fun duplicateInvoice(id: String): Result<Invoice> {
        return try {
            val response = api.duplicateInvoice(id)
            if (response.isSuccess()) {
                val invoice = response.data!!
                cache.saveInvoice(invoice)
                Result.success(invoice)
            } else {
                Result.failure(Exception(response.message ?: "Failed to duplicate invoice"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get invoice summary - backend calculates all metrics
     */
    override suspend fun getInvoiceSummary(dateRange: DateRange?): Result<InvoiceSummary> {
        return try {
            // Check cache first
            val cachedSummary = cache.getInvoiceSummary(dateRange)
            if (cachedSummary != null) {
                return Result.success(cachedSummary)
            }

            // TODO: Implement RPC call - analytics should be handled by RPC service
            throw Exception("Invoice summary not implemented yet")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Generate PDF - backend creates with all current data
     */
    override suspend fun generatePdf(id: String): Result<String> {
        return try {
            val response = api.generatePdf(id, PdfGenerationOptions())
            if (response.isSuccess()) {
                Result.success(response.data!!.url)
            } else {
                Result.failure(Exception(response.message ?: "Failed to generate PDF"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Generate PDF with custom options
     */
    override suspend fun generatePdfWithOptions(id: String, options: PdfGenerationOptions): Result<PdfResponse> {
        return try {
            val response = api.generatePdf(id, options)
            if (response.isSuccess()) {
                Result.success(response.data!!)
            } else {
                Result.failure(Exception(response.message ?: "Failed to generate PDF"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // === TEMPLATE OPERATIONS ===

    override suspend fun getInvoiceTemplates(): Result<List<InvoiceTemplate>> {
        return try {
            // Check cache first
            val cachedTemplates = cache.getInvoiceTemplates()
            if (cachedTemplates.isNotEmpty()) {
                return Result.success(cachedTemplates)
            }

            // TODO: Implement template API endpoints
            Result.failure(Exception("Template operations not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getInvoiceTemplate(templateId: String): Result<InvoiceTemplate> {
        return try {
            // Check cache first
            val cachedTemplate = cache.getInvoiceTemplate(templateId)
            if (cachedTemplate != null) {
                return Result.success(cachedTemplate)
            }

            // TODO: Implement template API endpoints
            Result.failure(Exception("Template operations not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun convertToTemplate(invoiceId: String, templateName: String): Result<InvoiceTemplate> {
        return try {
            val response = api.convertToTemplate(invoiceId, templateName)
            if (response.isSuccess()) {
                val template = response.data!!
                cache.saveInvoiceTemplate(template)
                Result.success(template)
            } else {
                Result.failure(Exception(response.message ?: "Failed to convert to template"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createFromTemplate(templateId: String, request: CreateFromTemplateRequest): Result<Invoice> {
        return try {
            val response = api.createFromTemplate(templateId, request)
            if (response.isSuccess()) {
                val invoice = response.data!!
                cache.saveInvoice(invoice)
                Result.success(invoice)
            } else {
                Result.failure(Exception(response.message ?: "Failed to create from template"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTemplate(templateId: String, request: UpdateTemplateRequest): Result<InvoiceTemplate> {
        return try {
            // TODO: Implement template update API endpoint
            Result.failure(Exception("Template update not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTemplate(templateId: String): Result<Unit> {
        return try {
            // TODO: Implement template delete API endpoint
            cache.deleteInvoiceTemplate(templateId)
            Result.failure(Exception("Template delete not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // === RECURRING INVOICE OPERATIONS ===

    override suspend fun setupRecurringInvoice(invoiceId: String, recurringInfo: EnhancedRecurringInfo): Result<Unit> {
        return try {
            // TODO: Implement recurring setup API endpoint
            Result.failure(Exception("Recurring invoice setup not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecurringInvoices(activeOnly: Boolean): Result<List<Invoice>> {
        return try {
            // Check cache first
            val cachedRecurring = cache.getRecurringInvoices(activeOnly)
            if (cachedRecurring.isNotEmpty()) {
                return Result.success(cachedRecurring)
            }

            // TODO: Implement recurring invoices API endpoint
            Result.failure(Exception("Recurring invoices not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecurringSchedulePreview(
        invoiceId: String,
        recurringInfo: EnhancedRecurringInfo,
    ): Result<RecurringSchedulePreview> {
        return try {
            // Check cache first
            val cachedPreview = cache.getRecurringSchedulePreview(invoiceId, recurringInfo)
            if (cachedPreview != null) {
                return Result.success(cachedPreview)
            }

            // TODO: Implement recurring schedule preview API endpoint
            Result.failure(Exception("Recurring schedule preview not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateRecurringSettings(invoiceId: String, recurringInfo: EnhancedRecurringInfo): Result<Unit> {
        return try {
            // TODO: Implement recurring settings update API endpoint
            Result.failure(Exception("Recurring settings update not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelRecurringInvoice(invoiceId: String): Result<Unit> {
        return try {
            // TODO: Implement recurring invoice cancellation API endpoint
            Result.failure(Exception("Recurring invoice cancellation not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateRecurringInvoice(recurringInvoiceId: String): Result<Invoice> {
        return try {
            // TODO: Implement manual recurring invoice generation API endpoint
            Result.failure(Exception("Manual recurring invoice generation not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // === ANALYTICS AND REPORTING ===

    override suspend fun getInvoiceAnalytics(dateRange: DateRange?): Result<InvoiceAnalyticsData> {
        return try {
            // Check cache first
            val cachedAnalytics = cache.getInvoiceAnalytics(dateRange)
            if (cachedAnalytics != null) {
                return Result.success(cachedAnalytics)
            }

            // TODO: Implement analytics RPC endpoint
            Result.failure(Exception("Invoice analytics not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun exportInvoices(
        filter: InvoiceFilter?,
        format: ExportFormat,
        dateRange: DateRange?,
    ): Result<ExportResponse> {
        return try {
            // TODO: Implement export API endpoint
            Result.failure(Exception("Invoice export not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // === BATCH OPERATIONS ===

    override suspend fun sendInvoicesBatch(
        invoiceIds: List<String>,
        request: SendInvoiceRequest,
    ): Result<BatchOperationResult> {
        return try {
            // TODO: Implement batch send API endpoint
            Result.failure(Exception("Batch invoice operations not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteInvoicesBatch(invoiceIds: List<String>): Result<BatchOperationResult> {
        return try {
            // TODO: Implement batch delete API endpoint
            Result.failure(Exception("Batch invoice operations not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateInvoicesStatusBatch(
        invoiceIds: List<String>,
        status: InvoiceStatus,
    ): Result<BatchOperationResult> {
        return try {
            // TODO: Implement batch status update API endpoint
            Result.failure(Exception("Batch invoice operations not implemented yet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
