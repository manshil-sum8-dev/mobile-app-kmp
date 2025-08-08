package za.co.quantive.app.data.remote.api

import za.co.quantive.app.domain.shared.DateRange
import za.co.quantive.app.domain.shared.ExportFormat
import za.co.quantive.app.domain.shared.ExportResponse
import za.co.quantive.app.domain.shared.ReportType

/**
 * RPC interface for business analytics and dashboard data
 * All metrics, calculations, and insights are heavy computations handled by backend
 */
interface AnalyticsRpc {

    /**
     * RPC: Get dashboard overview - all metrics calculated by backend
     */
    suspend fun getDashboardOverview(
        dateRange: DateRange? = null,
    ): ApiResponse<DashboardOverview>

    /**
     * RPC: Get detailed business metrics
     */
    suspend fun getBusinessMetrics(
        dateRange: DateRange? = null,
        granularity: MetricGranularity = MetricGranularity.MONTHLY,
    ): ApiResponse<BusinessMetrics>

    /**
     * RPC: Get revenue analytics
     */
    suspend fun getRevenueAnalytics(
        dateRange: DateRange? = null,
    ): ApiResponse<RevenueAnalytics>

    /**
     * RPC: Get customer analytics
     */
    suspend fun getCustomerAnalytics(
        dateRange: DateRange? = null,
    ): ApiResponse<CustomerAnalytics>

    /**
     * RPC: Get payment analytics
     */
    suspend fun getPaymentAnalytics(
        dateRange: DateRange? = null,
    ): ApiResponse<PaymentAnalytics>

    /**
     * RPC: Get tax/compliance analytics
     */
    suspend fun getTaxAnalytics(
        dateRange: DateRange? = null,
    ): ApiResponse<TaxAnalytics>

    /**
     * RPC: Export business report - backend generates comprehensive report
     */
    suspend fun exportBusinessReport(
        reportType: ReportType,
        dateRange: DateRange,
        format: ExportFormat = ExportFormat.PDF,
    ): ApiResponse<ExportResponse>
}

// All analytics models are defined in AnalyticsModels.kt
