package za.co.quantive.app.data.remote.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import za.co.quantive.app.data.remote.api.AnalyticsApi
import za.co.quantive.app.data.remote.api.*
import za.co.quantive.app.data.local.AnalyticsCacheImpl
import za.co.quantive.app.domain.entities.DateRange

// Add the interface definition here for now
interface AnalyticsCache {
    suspend fun getDashboardOverview(dateRange: DateRange?): DashboardOverview?
    suspend fun saveDashboardOverview(overview: DashboardOverview, dateRange: DateRange?)
    
    suspend fun getBusinessMetrics(dateRange: DateRange?, granularity: MetricGranularity): BusinessMetrics?
    suspend fun saveBusinessMetrics(metrics: BusinessMetrics, dateRange: DateRange?, granularity: MetricGranularity)
    
    suspend fun getRevenueAnalytics(dateRange: DateRange?): RevenueAnalytics?
    suspend fun saveRevenueAnalytics(analytics: RevenueAnalytics, dateRange: DateRange?)
    
    suspend fun getCustomerAnalytics(dateRange: DateRange?): CustomerAnalytics?
    suspend fun saveCustomerAnalytics(analytics: CustomerAnalytics, dateRange: DateRange?)
    
    suspend fun getPaymentAnalytics(dateRange: DateRange?): PaymentAnalytics?
    suspend fun savePaymentAnalytics(analytics: PaymentAnalytics, dateRange: DateRange?)
    
    suspend fun getTaxAnalytics(dateRange: DateRange?): TaxAnalytics?
    suspend fun saveTaxAnalytics(analytics: TaxAnalytics, dateRange: DateRange?)
    
    suspend fun clearCache()
    suspend fun clearExpiredCache()
}

/**
 * Backend-driven analytics repository
 * All business metrics, calculations, and insights provided by backend
 */
class BackendAnalyticsRepository(
    private val api: AnalyticsApi,
    private val cache: AnalyticsCache
) : AnalyticsRepository {

    /**
     * Get dashboard overview - all metrics calculated by backend
     */
    override suspend fun getDashboardOverview(
        dateRange: DateRange?,
        forceRefresh: Boolean
    ): Flow<Result<DashboardOverview>> = flow {
        try {
            // Emit cached data first for better UX
            if (!forceRefresh) {
                val cachedOverview = cache.getDashboardOverview(dateRange)
                if (cachedOverview != null) {
                    emit(Result.success(cachedOverview))
                }
            }

            // Fetch fresh data from backend
            val response = api.getDashboardOverview(dateRange)
            if (response.isSuccess()) {
                val overview = response.data!!
                cache.saveDashboardOverview(overview, dateRange)
                emit(Result.success(overview))
            } else {
                if (forceRefresh) {
                    emit(Result.failure(Exception(response.message ?: "Failed to fetch dashboard overview")))
                }
            }
        } catch (e: Exception) {
            // Fallback to cached data on network error
            val cachedOverview = cache.getDashboardOverview(dateRange)
            if (cachedOverview != null) {
                emit(Result.success(cachedOverview))
            } else {
                emit(Result.failure(e))
            }
        }
    }

    /**
     * Get detailed business metrics - backend calculated
     */
    override suspend fun getBusinessMetrics(
        dateRange: DateRange?,
        granularity: MetricGranularity
    ): Result<BusinessMetrics> {
        return try {
            val response = api.getBusinessMetrics(dateRange, granularity)
            if (response.isSuccess()) {
                val metrics = response.data!!
                cache.saveBusinessMetrics(metrics, dateRange, granularity)
                Result.success(metrics)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch business metrics"))
            }
        } catch (e: Exception) {
            // Try cache fallback
            val cachedMetrics = cache.getBusinessMetrics(dateRange, granularity)
            if (cachedMetrics != null) {
                Result.success(cachedMetrics)
            } else {
                Result.failure(e)
            }
        }
    }

    /**
     * Get revenue analytics - backend calculated trends and forecasts
     */
    override suspend fun getRevenueAnalytics(dateRange: DateRange?): Result<RevenueAnalytics> {
        return try {
            val response = api.getRevenueAnalytics(dateRange)
            if (response.isSuccess()) {
                val analytics = response.data!!
                cache.saveRevenueAnalytics(analytics, dateRange)
                Result.success(analytics)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch revenue analytics"))
            }
        } catch (e: Exception) {
            val cachedAnalytics = cache.getRevenueAnalytics(dateRange)
            if (cachedAnalytics != null) {
                Result.success(cachedAnalytics)
            } else {
                Result.failure(e)
            }
        }
    }

    /**
     * Get customer analytics - backend insights and scoring
     */
    override suspend fun getCustomerAnalytics(dateRange: DateRange?): Result<CustomerAnalytics> {
        return try {
            val response = api.getCustomerAnalytics(dateRange)
            if (response.isSuccess()) {
                val analytics = response.data!!
                cache.saveCustomerAnalytics(analytics, dateRange)
                Result.success(analytics)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch customer analytics"))
            }
        } catch (e: Exception) {
            val cachedAnalytics = cache.getCustomerAnalytics(dateRange)
            if (cachedAnalytics != null) {
                Result.success(cachedAnalytics)
            } else {
                Result.failure(e)
            }
        }
    }

    /**
     * Get payment analytics - backend analysis of payment patterns
     */
    override suspend fun getPaymentAnalytics(dateRange: DateRange?): Result<PaymentAnalytics> {
        return try {
            val response = api.getPaymentAnalytics(dateRange)
            if (response.isSuccess()) {
                val analytics = response.data!!
                cache.savePaymentAnalytics(analytics, dateRange)
                Result.success(analytics)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch payment analytics"))
            }
        } catch (e: Exception) {
            val cachedAnalytics = cache.getPaymentAnalytics(dateRange)
            if (cachedAnalytics != null) {
                Result.success(cachedAnalytics)
            } else {
                Result.failure(e)
            }
        }
    }

    /**
     * Get tax analytics - backend compliance and tax calculations
     */
    override suspend fun getTaxAnalytics(dateRange: DateRange?): Result<TaxAnalytics> {
        return try {
            val response = api.getTaxAnalytics(dateRange)
            if (response.isSuccess()) {
                val analytics = response.data!!
                cache.saveTaxAnalytics(analytics, dateRange)
                Result.success(analytics)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch tax analytics"))
            }
        } catch (e: Exception) {
            val cachedAnalytics = cache.getTaxAnalytics(dateRange)
            if (cachedAnalytics != null) {
                Result.success(cachedAnalytics)
            } else {
                Result.failure(e)
            }
        }
    }

    /**
     * Export business report - backend generates comprehensive reports
     */
    override suspend fun exportBusinessReport(
        reportType: ReportType,
        dateRange: DateRange,
        format: ExportFormat
    ): Result<ExportResponse> {
        return try {
            val response = api.exportBusinessReport(reportType, dateRange, format)
            if (response.isSuccess()) {
                Result.success(response.data!!)
            } else {
                Result.failure(Exception(response.message ?: "Failed to export business report"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Backend-driven analytics repository interface
 */
interface AnalyticsRepository {
    suspend fun getDashboardOverview(dateRange: DateRange? = null, forceRefresh: Boolean = false): Flow<Result<DashboardOverview>>
    suspend fun getBusinessMetrics(dateRange: DateRange? = null, granularity: MetricGranularity = MetricGranularity.MONTHLY): Result<BusinessMetrics>
    suspend fun getRevenueAnalytics(dateRange: DateRange? = null): Result<RevenueAnalytics>
    suspend fun getCustomerAnalytics(dateRange: DateRange? = null): Result<CustomerAnalytics>
    suspend fun getPaymentAnalytics(dateRange: DateRange? = null): Result<PaymentAnalytics>
    suspend fun getTaxAnalytics(dateRange: DateRange? = null): Result<TaxAnalytics>
    suspend fun exportBusinessReport(reportType: ReportType, dateRange: DateRange, format: ExportFormat = ExportFormat.PDF): Result<ExportResponse>
}

