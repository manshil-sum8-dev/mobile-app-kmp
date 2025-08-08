package za.co.quantive.app.data.remote.api

import kotlinx.serialization.Serializable
import za.co.quantive.app.domain.entities.BusinessContact
import za.co.quantive.app.domain.entities.Invoice
import za.co.quantive.app.domain.entities.Money

/**
 * Analytics response models - all calculated by backend
 */

@Serializable
data class DashboardOverview(
    val totalRevenue: Money,
    val monthlyRevenue: Money,
    val totalInvoices: Int,
    val paidInvoices: Int,
    val pendingInvoices: Int,
    val overdueInvoices: Int,
    val totalCustomers: Int,
    val activeCustomers: Int,
    val averagePaymentDays: Double,
    val topCustomers: List<BusinessContact>,
    val recentInvoices: List<Invoice>,
    val revenueGrowth: Double,
    val paymentTrends: PaymentTrends,
)

@Serializable
data class BusinessMetrics(
    val period: String, // ISO date range string
    val granularity: MetricGranularity,
    val revenueMetrics: RevenueMetrics,
    val customerMetrics: CustomerMetrics,
    val paymentMetrics: PaymentMetrics,
    val taxMetrics: TaxMetrics,
    val trendsData: List<MetricDataPoint>,
)

@Serializable
data class RevenueAnalytics(
    val totalRevenue: Money,
    val periodRevenue: Money,
    val growthRate: Double,
    val forecastedRevenue: Money,
    val revenueByMonth: List<RevenueDataPoint>,
    val topRevenueCustomers: List<CustomerRevenueData>,
    val revenueByCategory: List<CategoryRevenueData>,
)

@Serializable
data class CustomerAnalytics(
    val totalCustomers: Int,
    val activeCustomers: Int,
    val newCustomers: Int,
    val churningCustomers: Int,
    val averageCustomerValue: Money,
    val customerRetentionRate: Double,
    val topCustomersByRevenue: List<CustomerRevenueData>,
    val customerSegmentation: List<CustomerSegment>,
    val customerGrowthTrend: List<CustomerGrowthData>,
)

@Serializable
data class PaymentAnalytics(
    val averagePaymentDays: Double,
    val onTimePaymentRate: Double,
    val latePaymentRate: Double,
    val totalOutstanding: Money,
    val overdueAmount: Money,
    val paymentMethodBreakdown: List<PaymentMethodData>,
    val paymentTrends: PaymentTrends,
    val customerPaymentProfiles: List<CustomerPaymentProfile>,
)

@Serializable
data class TaxAnalytics(
    val totalVatCollected: Money,
    val vatOwed: Money,
    val taxableRevenue: Money,
    val exemptRevenue: Money,
    val vatRate: Double,
    val complianceStatus: TaxComplianceStatus,
    val monthlyVatReports: List<MonthlyVatReport>,
    val upcomingTaxDeadlines: List<TaxDeadline>,
)

@Serializable
enum class MetricGranularity {
    DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY
}

// Supporting data classes
@Serializable
data class PaymentTrends(
    val averageDaysImprovement: Double,
    val onTimePaymentTrend: Double,
    val latePaymentTrend: Double,
)

@Serializable
data class RevenueMetrics(
    val total: Money,
    val growth: Double,
    val forecast: Money,
)

@Serializable
data class CustomerMetrics(
    val total: Int,
    val active: Int,
    val growth: Double,
    val retention: Double,
)

@Serializable
data class PaymentMetrics(
    val averageDays: Double,
    val onTimeRate: Double,
    val outstandingAmount: Money,
)

@Serializable
data class TaxMetrics(
    val vatCollected: Money,
    val vatOwed: Money,
    val complianceScore: Double,
)

@Serializable
data class MetricDataPoint(
    val date: String,
    val value: Double,
    val label: String,
)

@Serializable
data class RevenueDataPoint(
    val period: String,
    val amount: Money,
    val growth: Double,
)

@Serializable
data class CustomerRevenueData(
    val customer: BusinessContact,
    val totalRevenue: Money,
    val invoiceCount: Int,
    val averageInvoiceValue: Money,
)

@Serializable
data class CategoryRevenueData(
    val category: String,
    val amount: Money,
    val percentage: Double,
)

@Serializable
data class CustomerSegment(
    val name: String,
    val customerCount: Int,
    val totalRevenue: Money,
    val characteristics: List<String>,
)

@Serializable
data class CustomerGrowthData(
    val period: String,
    val newCustomers: Int,
    val churnedCustomers: Int,
    val netGrowth: Int,
)

@Serializable
data class PaymentMethodData(
    val method: String,
    val count: Int,
    val totalAmount: Money,
    val averageDays: Double,
)

@Serializable
data class CustomerPaymentProfile(
    val customer: BusinessContact,
    val averagePaymentDays: Double,
    val onTimePaymentRate: Double,
    val totalOutstanding: Money,
    val riskScore: Int,
)

@Serializable
data class TaxComplianceStatus(
    val isCompliant: Boolean,
    val score: Double,
    val issues: List<String>,
    val recommendations: List<String>,
)

@Serializable
data class MonthlyVatReport(
    val month: String,
    val vatCollected: Money,
    val vatOwed: Money,
    val filingStatus: String,
    val dueDate: String,
)

@Serializable
data class TaxDeadline(
    val type: String,
    val description: String,
    val dueDate: String,
    val amount: Money?,
    val status: String,
)

// Export models
@Serializable
enum class ReportType {
    REVENUE_REPORT,
    CUSTOMER_REPORT,
    TAX_REPORT,
    PAYMENT_REPORT,
    FULL_BUSINESS_REPORT,
}

@Serializable
enum class ExportFormat {
    PDF, EXCEL, CSV
}

@Serializable
enum class TrendDirection {
    UP, DOWN, STABLE
}

@Serializable
data class ExportResponse(
    val url: String,
    val filename: String,
    val expiresAt: String,
    val format: ExportFormat,
)
