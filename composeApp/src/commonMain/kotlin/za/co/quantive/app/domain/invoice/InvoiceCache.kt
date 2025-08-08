package za.co.quantive.app.domain.invoice

import za.co.quantive.app.data.remote.api.InvoiceTemplate
import za.co.quantive.app.domain.entities.Invoice
import za.co.quantive.app.domain.entities.InvoiceFilter
import za.co.quantive.app.domain.entities.InvoiceSummary
import za.co.quantive.app.domain.shared.DateRange

/**
 * Invoice cache interface for performance optimization
 * Implements TTL-based caching strategy following enterprise architecture patterns
 * Provides both invoice data and template caching capabilities
 */
interface InvoiceCache {

    // === INVOICE CACHING ===

    /**
     * Get cached invoices with optional filtering
     * Returns empty list if no cached data available
     */
    suspend fun getInvoices(filter: InvoiceFilter? = null): List<Invoice>

    /**
     * Get single cached invoice by ID
     * Returns null if not found in cache
     */
    suspend fun getInvoice(id: String): Invoice?

    /**
     * Save list of invoices to cache with TTL
     * Uses USER_DATA TTL (5 minutes) for performance
     */
    suspend fun saveInvoices(invoices: List<Invoice>)

    /**
     * Save single invoice to cache with TTL
     * Uses USER_DATA TTL (5 minutes) for performance
     */
    suspend fun saveInvoice(invoice: Invoice)

    /**
     * Remove specific invoice from cache
     * Also invalidates related cached data (summaries, lists)
     */
    suspend fun deleteInvoice(id: String)

    /**
     * Clear all invoice-related cache data
     * Used during logout or cache refresh
     */
    suspend fun clearCache()

    // === TEMPLATE CACHING ===

    /**
     * Get cached invoice templates
     * Returns empty list if no cached data available
     */
    suspend fun getInvoiceTemplates(): List<InvoiceTemplate>

    /**
     * Get single cached template by ID
     * Returns null if not found in cache
     */
    suspend fun getInvoiceTemplate(templateId: String): InvoiceTemplate?

    /**
     * Save invoice templates to cache with TTL
     * Uses CONFIGURATION TTL (30 minutes) as templates change infrequently
     */
    suspend fun saveInvoiceTemplates(templates: List<InvoiceTemplate>)

    /**
     * Save single template to cache with TTL
     */
    suspend fun saveInvoiceTemplate(template: InvoiceTemplate)

    /**
     * Remove specific template from cache
     */
    suspend fun deleteInvoiceTemplate(templateId: String)

    // === SUMMARY AND ANALYTICS CACHING ===

    /**
     * Get cached invoice summary for specific date range
     * Returns null if not found or expired
     */
    suspend fun getInvoiceSummary(dateRange: DateRange? = null): InvoiceSummary?

    /**
     * Save invoice summary to cache with TTL
     * Uses USER_DATA TTL (5 minutes) for current data accuracy
     */
    suspend fun saveInvoiceSummary(summary: InvoiceSummary, dateRange: DateRange? = null)

    /**
     * Get cached invoice analytics data
     * Returns null if not found or expired
     */
    suspend fun getInvoiceAnalytics(dateRange: DateRange? = null): InvoiceAnalyticsData?

    /**
     * Save invoice analytics to cache with TTL
     * Uses USER_DATA TTL (5 minutes) for current data accuracy
     */
    suspend fun saveInvoiceAnalytics(analytics: InvoiceAnalyticsData, dateRange: DateRange? = null)

    // === RECURRING INVOICE CACHING ===

    /**
     * Get cached recurring invoices
     * Returns empty list if no cached data available
     */
    suspend fun getRecurringInvoices(activeOnly: Boolean = true): List<Invoice>

    /**
     * Save recurring invoices to cache with TTL
     * Uses USER_DATA TTL (5 minutes) for performance
     */
    suspend fun saveRecurringInvoices(invoices: List<Invoice>, activeOnly: Boolean = true)

    /**
     * Get cached recurring schedule preview
     */
    suspend fun getRecurringSchedulePreview(
        invoiceId: String,
        recurringInfo: RecurringInfo,
    ): RecurringSchedulePreview?

    /**
     * Save recurring schedule preview to cache
     * Uses shorter TTL as this is preview data
     */
    suspend fun saveRecurringSchedulePreview(
        invoiceId: String,
        recurringInfo: RecurringInfo,
        preview: RecurringSchedulePreview,
    )

    // === CACHE MANAGEMENT ===

    /**
     * Invalidate all cached data for a specific invoice
     * Clears invoice data, related summaries, and list caches
     */
    suspend fun invalidateInvoice(id: String)

    /**
     * Invalidate cached data by pattern
     * Useful for clearing related data (e.g., all invoice lists)
     */
    suspend fun invalidateByPattern(pattern: String)

    /**
     * Check if cache has valid data for specific key
     * Useful for determining if API call is needed
     */
    suspend fun hasValidData(key: String): Boolean

    /**
     * Get cache statistics for monitoring
     */
    suspend fun getCacheStats(): InvoiceCacheStats
}

/**
 * Invoice cache statistics for monitoring and debugging
 */
data class InvoiceCacheStats(
    val totalEntries: Int,
    val invoiceCount: Int,
    val templateCount: Int,
    val summaryCount: Int,
    val analyticsCount: Int,
    val recurringCount: Int,
    val hitRate: Double,
    val memoryUsage: Long,
    val oldestEntry: String?,
    val newestEntry: String?,
)

/**
 * Cache key generator for consistent cache key creation
 */
object InvoiceCacheKeys {
    private const val PREFIX_INVOICE = "invoice_"
    private const val PREFIX_INVOICES = "invoices_"
    private const val PREFIX_TEMPLATE = "template_"
    private const val PREFIX_TEMPLATES = "templates_"
    private const val PREFIX_SUMMARY = "summary_"
    private const val PREFIX_ANALYTICS = "analytics_"
    private const val PREFIX_RECURRING = "recurring_"
    private const val PREFIX_SCHEDULE = "schedule_"

    fun invoice(id: String): String = "$PREFIX_INVOICE$id"

    fun invoices(filter: InvoiceFilter?): String =
        "$PREFIX_INVOICES${filter?.hashCode() ?: "all"}"

    fun template(id: String): String = "$PREFIX_TEMPLATE$id"

    fun templates(): String = "${PREFIX_TEMPLATES}all"

    fun summary(dateRange: DateRange?): String =
        "$PREFIX_SUMMARY${dateRange?.hashCode() ?: "all"}"

    fun analytics(dateRange: DateRange?): String =
        "$PREFIX_ANALYTICS${dateRange?.hashCode() ?: "all"}"

    fun recurringInvoices(activeOnly: Boolean): String =
        "$PREFIX_RECURRING${if (activeOnly) "active" else "all"}"

    fun schedulePreview(invoiceId: String, recurringInfo: RecurringInfo): String =
        "$PREFIX_SCHEDULE${invoiceId}_${recurringInfo.hashCode()}"
}
