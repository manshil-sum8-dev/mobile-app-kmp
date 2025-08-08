package za.co.quantive.app.data.remote.api

import za.co.quantive.app.data.remote.SupabaseClient
import za.co.quantive.app.domain.shared.DateRange
import za.co.quantive.app.domain.shared.ExportFormat
import za.co.quantive.app.domain.shared.ExportResponse
import za.co.quantive.app.domain.shared.ReportType

/**
 * Analytics API implementation using Supabase backend
 */
class AnalyticsRpcImpl(
    private val client: SupabaseClient,
) : AnalyticsRpc {

    override suspend fun getDashboardOverview(dateRange: DateRange?): ApiResponse<DashboardOverview> {
        return try {
            // Get dashboard overview via backend aggregation function
            val params = mutableMapOf<String, String?>()
            dateRange?.let {
                params["start_date"] = it.start
                params["end_date"] = it.end
            }

            // Call backend function that aggregates all dashboard data
            val overview: DashboardOverview = client.post(
                "rest/v1/rpc/get_dashboard_overview",
                params,
            )

            ApiResponse.success(overview)
        } catch (e: Exception) {
            ApiResponse.error("Failed to fetch dashboard overview: ${e.message}")
        }
    }

    override suspend fun getBusinessMetrics(
        dateRange: DateRange?,
        granularity: MetricGranularity,
    ): ApiResponse<BusinessMetrics> {
        return try {
            // Get business metrics via backend analytics function
            val metricsData = mutableMapOf<String, Any?>(
                "granularity" to granularity.name,
            )
            dateRange?.let {
                metricsData["start_date"] = it.start
                metricsData["end_date"] = it.end
            }

            // Call backend function that calculates time-series business metrics
            val metrics: BusinessMetrics = client.post(
                "rest/v1/rpc/get_business_metrics",
                metricsData,
            )

            ApiResponse.success(metrics)
        } catch (e: Exception) {
            ApiResponse.error("Failed to fetch business metrics: ${e.message}")
        }
    }

    override suspend fun getRevenueAnalytics(dateRange: DateRange?): ApiResponse<RevenueAnalytics> {
        return try {
            // Get revenue analytics via backend function
            val params = mutableMapOf<String, String?>()
            dateRange?.let {
                params["start_date"] = it.start
                params["end_date"] = it.end
            }

            // Call backend function that analyzes revenue patterns
            val analytics: RevenueAnalytics = client.post(
                "rest/v1/rpc/get_revenue_analytics",
                params,
            )

            ApiResponse.success(analytics)
        } catch (e: Exception) {
            ApiResponse.error("Failed to fetch revenue analytics: ${e.message}")
        }
    }

    override suspend fun getCustomerAnalytics(dateRange: DateRange?): ApiResponse<CustomerAnalytics> {
        return try {
            // Get customer analytics via backend function
            val params = mutableMapOf<String, String?>()
            dateRange?.let {
                params["start_date"] = it.start
                params["end_date"] = it.end
            }

            // Call backend function that analyzes customer behavior
            val analytics: CustomerAnalytics = client.post(
                "rest/v1/rpc/get_customer_analytics",
                params,
            )

            ApiResponse.success(analytics)
        } catch (e: Exception) {
            ApiResponse.error("Failed to fetch customer analytics: ${e.message}")
        }
    }

    override suspend fun getPaymentAnalytics(dateRange: DateRange?): ApiResponse<PaymentAnalytics> {
        return try {
            // Get payment analytics via backend function
            val params = mutableMapOf<String, String?>()
            dateRange?.let {
                params["start_date"] = it.start
                params["end_date"] = it.end
            }

            // Call backend function that analyzes payment patterns
            val analytics: PaymentAnalytics = client.post(
                "rest/v1/rpc/get_payment_analytics",
                params,
            )

            ApiResponse.success(analytics)
        } catch (e: Exception) {
            ApiResponse.error("Failed to fetch payment analytics: ${e.message}")
        }
    }

    override suspend fun getTaxAnalytics(dateRange: DateRange?): ApiResponse<TaxAnalytics> {
        return try {
            // Get tax analytics via backend function
            val params = mutableMapOf<String, String?>()
            dateRange?.let {
                params["start_date"] = it.start
                params["end_date"] = it.end
            }

            // Call backend function that calculates SARS-compliant tax reporting
            val analytics: TaxAnalytics = client.post(
                "rest/v1/rpc/get_tax_analytics",
                params,
            )

            ApiResponse.success(analytics)
        } catch (e: Exception) {
            ApiResponse.error("Failed to fetch tax analytics: ${e.message}")
        }
    }

    override suspend fun exportBusinessReport(
        reportType: ReportType,
        dateRange: DateRange,
        format: ExportFormat,
    ): ApiResponse<ExportResponse> {
        return try {
            // Export business report via backend function
            val exportData = mapOf(
                "report_type" to reportType.name,
                "format" to format.name,
                "start_date" to dateRange.start,
                "end_date" to dateRange.end,
            )

            // Call backend function that generates and exports report
            val exportResponse: ExportResponse = client.post(
                "rest/v1/rpc/export_business_report",
                exportData,
            )

            ApiResponse.success(exportResponse)
        } catch (e: Exception) {
            ApiResponse.error("Failed to export business report: ${e.message}")
        }
    }
}
