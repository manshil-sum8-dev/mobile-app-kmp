package za.co.quantive.app.app

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import za.co.quantive.app.auth.AuthService
import za.co.quantive.app.auth.Session
import za.co.quantive.app.core.cache.CacheInvalidationManager
import za.co.quantive.app.core.cache.CacheManager
import za.co.quantive.app.core.cache.SimpleCache
import za.co.quantive.app.data.local.BusinessProfileLocal
import za.co.quantive.app.data.remote.BusinessProfileRemote
import za.co.quantive.app.data.remote.BusinessProfileRepositoryImpl
import za.co.quantive.app.data.remote.SupabaseClient
import za.co.quantive.app.data.remote.api.AnalyticsRpcImpl
import za.co.quantive.app.data.remote.api.BusinessApiImpl
import za.co.quantive.app.data.remote.api.ContactApiImpl
import za.co.quantive.app.data.remote.api.InvoiceApiImpl
import za.co.quantive.app.data.remote.repository.BackendAnalyticsRepository
import za.co.quantive.app.data.remote.repository.BackendContactRepository
import za.co.quantive.app.data.remote.repository.BackendInvoiceRepository
import za.co.quantive.app.data.remote.repository.ContactCache
import za.co.quantive.app.domain.contact.ContactRepository
import za.co.quantive.app.domain.invoice.InvoiceCache
import za.co.quantive.app.domain.invoice.InvoiceRepository
import za.co.quantive.app.domain.profile.BusinessProfileRepository
import za.co.quantive.app.security.SecureStore

// Manual DI providers for early phases
object AppServices {
    private val mutex = Mutex()

    // In-memory cache of session for fast header access
    private var cachedSession: Session? = null

    // Provider: access token for SupabaseClient
    private val accessTokenProvider: suspend () -> String? = {
        mutex.withLock {
            if (cachedSession == null) cachedSession = SecureStore.getSession()
            cachedSession?.accessToken
        }
    }

    // Provider: owner ID for data operations
    private val ownerIdProvider: suspend () -> String? = {
        mutex.withLock {
            if (cachedSession == null) cachedSession = SecureStore.getSession()
            cachedSession?.userId
        }
    }

    // Provider: session for repositories
    private val sessionProvider: suspend () -> Session? = {
        mutex.withLock {
            if (cachedSession == null) cachedSession = SecureStore.getSession()
            cachedSession
        }
    }

    // Core cache infrastructure
    private val simpleCache by lazy { SimpleCache() }
    private val cacheInvalidationManager by lazy { CacheInvalidationManager(simpleCache) }
    val cacheManager by lazy { CacheManager(simpleCache, cacheInvalidationManager) }

    val supabase by lazy { SupabaseClient(accessTokenProvider) }
    val auth by lazy { AuthService(supabase) }

    // Backend-driven API services
    private val invoiceApi by lazy { InvoiceApiImpl(supabase) }
    private val analyticsRpc by lazy { AnalyticsRpcImpl(supabase) }
    private val contactApi by lazy { ContactApiImpl(supabase) }
    private val businessApi by lazy { BusinessApiImpl(supabase) }

    // Smart caching implementations using SimpleCache
    private val invoiceCache by lazy {
        object : InvoiceCache {
            override suspend fun getInvoices(filter: za.co.quantive.app.domain.entities.InvoiceFilter?): List<za.co.quantive.app.domain.entities.Invoice> {
                val key = "invoices_${filter?.hashCode() ?: "all"}"
                return simpleCache.get<List<za.co.quantive.app.domain.entities.Invoice>>(key) ?: emptyList()
            }
            override suspend fun getInvoice(id: String): za.co.quantive.app.domain.entities.Invoice? {
                return simpleCache.get("invoice_$id")
            }
            override suspend fun saveInvoices(invoices: List<za.co.quantive.app.domain.entities.Invoice>) {
                val key = "invoices_all"
                simpleCache.put(key, invoices, za.co.quantive.app.core.cache.CacheTTL.USER_DATA)
            }
            override suspend fun saveInvoice(invoice: za.co.quantive.app.domain.entities.Invoice) {
                simpleCache.put("invoice_${invoice.id}", invoice, za.co.quantive.app.core.cache.CacheTTL.USER_DATA)
            }
            override suspend fun deleteInvoice(id: String) {
                simpleCache.invalidate("invoice_$id")
                simpleCache.invalidatePattern("invoices_*")
            }
            override suspend fun clearCache() {
                simpleCache.invalidatePattern("invoice*")
            }

            // Additional InvoiceCache methods - placeholder implementations
            override suspend fun getInvoiceTemplates() = emptyList<za.co.quantive.app.data.remote.api.InvoiceTemplate>()
            override suspend fun getInvoiceTemplate(templateId: String) = null
            override suspend fun saveInvoiceTemplates(templates: List<za.co.quantive.app.data.remote.api.InvoiceTemplate>) {}
            override suspend fun saveInvoiceTemplate(template: za.co.quantive.app.data.remote.api.InvoiceTemplate) {}
            override suspend fun deleteInvoiceTemplate(templateId: String) {}
            override suspend fun getInvoiceSummary(dateRange: za.co.quantive.app.domain.shared.DateRange?) = null
            override suspend fun saveInvoiceSummary(summary: za.co.quantive.app.domain.entities.InvoiceSummary, dateRange: za.co.quantive.app.domain.shared.DateRange?) {}
            override suspend fun getInvoiceAnalytics(dateRange: za.co.quantive.app.domain.shared.DateRange?) = null
            override suspend fun saveInvoiceAnalytics(analytics: za.co.quantive.app.domain.invoice.InvoiceAnalyticsData, dateRange: za.co.quantive.app.domain.shared.DateRange?) {}
            override suspend fun getRecurringInvoices(activeOnly: Boolean) = emptyList<za.co.quantive.app.domain.entities.Invoice>()
            override suspend fun saveRecurringInvoices(invoices: List<za.co.quantive.app.domain.entities.Invoice>, activeOnly: Boolean) {}
            override suspend fun getRecurringSchedulePreview(invoiceId: String, recurringInfo: za.co.quantive.app.domain.invoice.RecurringInfo) = null
            override suspend fun saveRecurringSchedulePreview(invoiceId: String, recurringInfo: za.co.quantive.app.domain.invoice.RecurringInfo, preview: za.co.quantive.app.domain.invoice.RecurringSchedulePreview) {}
            override suspend fun invalidateInvoice(id: String) {
                simpleCache.invalidate("invoice_$id")
                simpleCache.invalidatePattern("invoices_*")
            }
            override suspend fun invalidateByPattern(pattern: String) {
                simpleCache.invalidatePattern(pattern)
            }
            override suspend fun hasValidData(key: String) = simpleCache.get<Any>(key) != null
            override suspend fun getCacheStats() = za.co.quantive.app.domain.invoice.InvoiceCacheStats(
                totalEntries = 0, invoiceCount = 0, templateCount = 0, summaryCount = 0,
                analyticsCount = 0, recurringCount = 0, hitRate = 0.0, memoryUsage = 0L,
                oldestEntry = null, newestEntry = null,
            )
        }
    }

    private val contactCache by lazy {
        object : ContactCache {
            override suspend fun getContacts(filter: za.co.quantive.app.domain.entities.ContactFilter?): List<za.co.quantive.app.domain.entities.BusinessContact> {
                val key = "contacts_${filter?.hashCode() ?: "all"}"
                return simpleCache.get<List<za.co.quantive.app.domain.entities.BusinessContact>>(key) ?: emptyList()
            }
            override suspend fun getContact(id: String): za.co.quantive.app.domain.entities.BusinessContact? {
                return simpleCache.get("contact_$id")
            }
            override suspend fun saveContacts(contacts: List<za.co.quantive.app.domain.entities.BusinessContact>) {
                val key = "contacts_all"
                simpleCache.put(key, contacts, za.co.quantive.app.core.cache.CacheTTL.USER_DATA)
            }
            override suspend fun saveContact(contact: za.co.quantive.app.domain.entities.BusinessContact) {
                simpleCache.put("contact_${contact.id}", contact, za.co.quantive.app.core.cache.CacheTTL.USER_DATA)
            }
            override suspend fun deleteContact(id: String) {
                simpleCache.invalidate("contact_$id")
                simpleCache.invalidatePattern("contacts_*")
            }
            override suspend fun clearCache() {
                simpleCache.invalidatePattern("contact*")
            }
        }
    }

    // Analytics cache implementation
    private val analyticsCache by lazy {
        object : za.co.quantive.app.data.remote.repository.AnalyticsCache {
            override suspend fun getDashboardOverview(dateRange: za.co.quantive.app.domain.shared.DateRange?): za.co.quantive.app.data.remote.api.DashboardOverview? {
                val key = "dashboard_${dateRange?.hashCode() ?: "all"}"
                return simpleCache.get<za.co.quantive.app.data.remote.api.DashboardOverview>(key)
            }
            override suspend fun saveDashboardOverview(overview: za.co.quantive.app.data.remote.api.DashboardOverview, dateRange: za.co.quantive.app.domain.shared.DateRange?) {
                val key = "dashboard_${dateRange?.hashCode() ?: "all"}"
                simpleCache.put(key, overview, za.co.quantive.app.core.cache.CacheTTL.USER_DATA)
            }
            override suspend fun getBusinessMetrics(dateRange: za.co.quantive.app.domain.shared.DateRange?, granularity: za.co.quantive.app.data.remote.api.MetricGranularity): za.co.quantive.app.data.remote.api.BusinessMetrics? {
                val key = "metrics_${granularity}_${dateRange?.hashCode() ?: "all"}"
                return simpleCache.get<za.co.quantive.app.data.remote.api.BusinessMetrics>(key)
            }
            override suspend fun saveBusinessMetrics(metrics: za.co.quantive.app.data.remote.api.BusinessMetrics, dateRange: za.co.quantive.app.domain.shared.DateRange?, granularity: za.co.quantive.app.data.remote.api.MetricGranularity) {
                val key = "metrics_${granularity}_${dateRange?.hashCode() ?: "all"}"
                simpleCache.put(key, metrics, za.co.quantive.app.core.cache.CacheTTL.USER_DATA)
            }
            override suspend fun getRevenueAnalytics(dateRange: za.co.quantive.app.domain.shared.DateRange?): za.co.quantive.app.data.remote.api.RevenueAnalytics? {
                val key = "revenue_${dateRange?.hashCode() ?: "all"}"
                return simpleCache.get<za.co.quantive.app.data.remote.api.RevenueAnalytics>(key)
            }
            override suspend fun saveRevenueAnalytics(analytics: za.co.quantive.app.data.remote.api.RevenueAnalytics, dateRange: za.co.quantive.app.domain.shared.DateRange?) {
                val key = "revenue_${dateRange?.hashCode() ?: "all"}"
                simpleCache.put(key, analytics, za.co.quantive.app.core.cache.CacheTTL.USER_DATA)
            }
            override suspend fun getCustomerAnalytics(dateRange: za.co.quantive.app.domain.shared.DateRange?): za.co.quantive.app.data.remote.api.CustomerAnalytics? {
                val key = "customer_${dateRange?.hashCode() ?: "all"}"
                return simpleCache.get<za.co.quantive.app.data.remote.api.CustomerAnalytics>(key)
            }
            override suspend fun saveCustomerAnalytics(analytics: za.co.quantive.app.data.remote.api.CustomerAnalytics, dateRange: za.co.quantive.app.domain.shared.DateRange?) {
                val key = "customer_${dateRange?.hashCode() ?: "all"}"
                simpleCache.put(key, analytics, za.co.quantive.app.core.cache.CacheTTL.USER_DATA)
            }
            override suspend fun getPaymentAnalytics(dateRange: za.co.quantive.app.domain.shared.DateRange?): za.co.quantive.app.data.remote.api.PaymentAnalytics? {
                val key = "payment_${dateRange?.hashCode() ?: "all"}"
                return simpleCache.get<za.co.quantive.app.data.remote.api.PaymentAnalytics>(key)
            }
            override suspend fun savePaymentAnalytics(analytics: za.co.quantive.app.data.remote.api.PaymentAnalytics, dateRange: za.co.quantive.app.domain.shared.DateRange?) {
                val key = "payment_${dateRange?.hashCode() ?: "all"}"
                simpleCache.put(key, analytics, za.co.quantive.app.core.cache.CacheTTL.USER_DATA)
            }
            override suspend fun getTaxAnalytics(dateRange: za.co.quantive.app.domain.shared.DateRange?): za.co.quantive.app.data.remote.api.TaxAnalytics? {
                val key = "tax_${dateRange?.hashCode() ?: "all"}"
                return simpleCache.get<za.co.quantive.app.data.remote.api.TaxAnalytics>(key)
            }
            override suspend fun saveTaxAnalytics(analytics: za.co.quantive.app.data.remote.api.TaxAnalytics, dateRange: za.co.quantive.app.domain.shared.DateRange?) {
                val key = "tax_${dateRange?.hashCode() ?: "all"}"
                simpleCache.put(key, analytics, za.co.quantive.app.core.cache.CacheTTL.USER_DATA)
            }
            override suspend fun clearCache() {
                simpleCache.invalidatePattern("dashboard*")
                simpleCache.invalidatePattern("metrics*")
                simpleCache.invalidatePattern("revenue*")
                simpleCache.invalidatePattern("customer*")
                simpleCache.invalidatePattern("payment*")
                simpleCache.invalidatePattern("tax*")
            }
            override suspend fun clearExpiredCache() {
                // SimpleCache handles TTL automatically
            }
        }
    }

    // Backend-driven repositories
    val invoiceRepository: InvoiceRepository by lazy {
        BackendInvoiceRepository(invoiceApi, invoiceCache)
    }

    val analyticsRepository: za.co.quantive.app.data.remote.repository.AnalyticsRepository by lazy {
        za.co.quantive.app.data.remote.repository.BackendAnalyticsRepository(analyticsRpc, analyticsCache)
    }

    val contactRepository: ContactRepository by lazy {
        BackendContactRepository(contactApi, contactCache)
    }

    // Onboarding services (temporarily disabled)

    // Business Profile services (legacy - will be migrated)
    private val businessProfileLocal by lazy { BusinessProfileLocal() }
    private val businessProfileRemote by lazy { BusinessProfileRemote(supabase, ownerIdProvider) }
    val businessProfileRepository: BusinessProfileRepository by lazy {
        BusinessProfileRepositoryImpl(businessProfileLocal, businessProfileRemote, sessionProvider)
    }

    suspend fun setSession(session: Session?) {
        mutex.withLock {
            cachedSession = session
            SecureStore.saveSession(session)
        }
    }

    suspend fun clearSession() {
        mutex.withLock {
            cachedSession = null
            SecureStore.clearSession()
            // Clear all user-specific cache data
            cacheManager.handleUserLogout()
        }
    }

    suspend fun initializeServices() {
        cacheManager.initialize()
    }
}
