package za.co.quantive.app.data.remote.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import za.co.quantive.app.data.remote.api.InvoiceApi
import za.co.quantive.app.data.remote.api.CreateInvoiceRequest
import za.co.quantive.app.data.remote.api.UpdateInvoiceRequest
import za.co.quantive.app.data.remote.api.SendInvoiceRequest
import za.co.quantive.app.data.remote.api.RecordPaymentRequest
import za.co.quantive.app.data.local.InvoiceCacheImpl
import za.co.quantive.app.domain.entities.*

// Add the interface definition here for now
interface InvoiceCache {
    suspend fun getInvoices(filter: InvoiceFilter? = null): List<Invoice>
    suspend fun getInvoice(id: String): Invoice?
    suspend fun saveInvoices(invoices: List<Invoice>)
    suspend fun saveInvoice(invoice: Invoice)
    suspend fun deleteInvoice(id: String)
    suspend fun clearCache()
}

/**
 * Backend-driven invoice repository
 * All business logic, calculations, and validations handled by backend
 */
class BackendInvoiceRepository(
    private val api: InvoiceApi,
    private val cache: InvoiceCache
) : InvoiceRepository {

    /**
     * Get invoices with backend-applied filtering and calculations
     */
    override suspend fun getInvoices(
        filter: InvoiceFilter?,
        forceRefresh: Boolean
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
                filter = filter
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
            val response = api.getInvoiceSummary(dateRange)
            if (response.isSuccess()) {
                Result.success(response.data!!)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch invoice summary"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Generate PDF - backend creates with all current data
     */
    override suspend fun generatePdf(id: String): Result<String> {
        return try {
            val response = api.generatePdf(id, za.co.quantive.app.data.remote.api.PdfGenerationOptions())
            if (response.isSuccess()) {
                Result.success(response.data!!.url)
            } else {
                Result.failure(Exception(response.message ?: "Failed to generate PDF"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Backend-driven invoice repository interface
 */
interface InvoiceRepository {
    suspend fun getInvoices(filter: InvoiceFilter? = null, forceRefresh: Boolean = false): Flow<Result<List<Invoice>>>
    suspend fun getInvoice(id: String): Result<Invoice>
    suspend fun createInvoice(request: CreateInvoiceRequest): Result<Invoice>
    suspend fun updateInvoice(id: String, request: UpdateInvoiceRequest): Result<Invoice>
    suspend fun deleteInvoice(id: String): Result<Unit>
    suspend fun sendInvoice(id: String, request: SendInvoiceRequest): Result<Invoice>
    suspend fun recordPayment(id: String, request: RecordPaymentRequest): Result<Invoice>
    suspend fun duplicateInvoice(id: String): Result<Invoice>
    suspend fun getInvoiceSummary(dateRange: DateRange? = null): Result<InvoiceSummary>
    suspend fun generatePdf(id: String): Result<String>
}

