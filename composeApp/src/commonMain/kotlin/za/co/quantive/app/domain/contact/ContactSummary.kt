package za.co.quantive.app.domain.contact

import kotlinx.serialization.Serializable
import za.co.quantive.app.domain.entities.ContactInteraction
import za.co.quantive.app.domain.shared.DateRange

/**
 * Comprehensive contact analytics summary with DateRange integration
 * Provides business intelligence insights for contact management
 * All calculations and metrics are backend-driven for accuracy
 */
@Serializable
data class ContactSummary(
    // === SUMMARY METADATA ===
    /**
     * Date range for which this summary was generated
     */
    val dateRange: DateRange,

    /**
     * Business ID this summary belongs to
     */
    val businessId: String,

    /**
     * When this summary was generated (ISO datetime string)
     */
    val generatedAt: String,

    /**
     * Summary generation source (real-time, cached, scheduled)
     */
    val dataSource: SummaryDataSource = SummaryDataSource.REAL_TIME,

    // === CONTACT COUNT METRICS ===
    /**
     * Total number of contacts in the system
     */
    val totalContacts: Int,

    /**
     * Active contacts (isActive = true)
     */
    val activeContacts: Int,

    /**
     * Inactive contacts (isActive = false)
     */
    val inactiveContacts: Int,

    /**
     * Breakdown by contact type
     */
    val contactsByType: ContactTypeBreakdown,

    /**
     * New contacts added in the specified date range
     */
    val newContactsInPeriod: Int,

    /**
     * Contacts modified in the specified date range
     */
    val modifiedContactsInPeriod: Int,

    // === FINANCIAL METRICS ===
    /**
     * Revenue metrics from customer relationships
     */
    val revenueMetrics: ContactRevenueMetrics,

    /**
     * Expense metrics from supplier relationships
     */
    val expenseMetrics: ContactExpenseMetrics,

    /**
     * Contacts with outstanding balances
     */
    val contactsWithOutstanding: Int,

    /**
     * Total outstanding amount across all contacts
     */
    val totalOutstandingAmount: Double,

    /**
     * Average payment days across all contacts
     */
    val averagePaymentDays: Double,

    // === PERFORMANCE INSIGHTS ===
    /**
     * Top performing customers by revenue (limited list)
     */
    val topCustomersByRevenue: List<ContactPerformanceInsight>,

    /**
     * Top suppliers by transaction volume
     */
    val topSuppliersByVolume: List<ContactPerformanceInsight>,

    /**
     * Customers with payment delays
     */
    val customersWithDelays: List<ContactPerformanceInsight>,

    /**
     * High-value contacts (above defined threshold)
     */
    val highValueContacts: List<ContactPerformanceInsight>,

    // === INTERACTION METRICS ===
    /**
     * Recent interactions across all contacts
     */
    val recentInteractions: List<ContactInteraction>,

    /**
     * Total interactions in the specified date range
     */
    val totalInteractionsInPeriod: Int,

    /**
     * Breakdown of interactions by type
     */
    val interactionsByType: Map<String, Int>, // InteractionType name -> count

    /**
     * Contacts requiring follow-up
     */
    val contactsRequiringFollowUp: Int,

    // === COMPLIANCE AND RISK METRICS ===
    /**
     * Contacts with complete tax information
     */
    val contactsWithCompleteTaxInfo: Int,

    /**
     * Contacts with verified banking details
     */
    val contactsWithVerifiedBanking: Int,

    /**
     * Contacts with compliance issues
     */
    val contactsWithComplianceIssues: Int,

    /**
     * Overall contact data quality score (0-100)
     */
    val dataQualityScore: Int,

    // === GEOGRAPHICAL DISTRIBUTION ===
    /**
     * Contacts by South African province
     */
    val contactsByProvince: Map<String, Int>,

    /**
     * International contacts by country
     */
    val internationalContactsByCountry: Map<String, Int>,

    // === TRENDS AND FORECASTING ===
    /**
     * Growth trend compared to previous period
     */
    val growthTrend: GrowthTrend,

    /**
     * Predicted metrics for next period (if available)
     */
    val forecastMetrics: ForecastMetrics? = null,

    // === DATA FRESHNESS ===
    /**
     * Cache information for this summary
     */
    val cacheInfo: SummaryCacheInfo,
) {

    /**
     * Get contact distribution as percentages
     */
    fun getContactTypePercentages(): Map<ContactType, Double> {
        return if (totalContacts > 0) {
            mapOf(
                ContactType.CUSTOMER to (contactsByType.customers.toDouble() / totalContacts * 100),
                ContactType.SUPPLIER to (contactsByType.suppliers.toDouble() / totalContacts * 100),
                ContactType.BOTH to (contactsByType.both.toDouble() / totalContacts * 100),
                ContactType.INTERNAL to (contactsByType.internal.toDouble() / totalContacts * 100),
            )
        } else {
            mapOf()
        }
    }

    /**
     * Get overall financial health score (0-100)
     */
    fun getFinancialHealthScore(): Int {
        val outstandingRatio = if (revenueMetrics.totalRevenue > 0) {
            totalOutstandingAmount / revenueMetrics.totalRevenue
        } else {
            0.0
        }

        val paymentScore = when {
            averagePaymentDays <= 30 -> 100
            averagePaymentDays <= 45 -> 80
            averagePaymentDays <= 60 -> 60
            averagePaymentDays <= 90 -> 40
            else -> 20
        }

        val outstandingScore = when {
            outstandingRatio <= 0.1 -> 100
            outstandingRatio <= 0.2 -> 80
            outstandingRatio <= 0.3 -> 60
            outstandingRatio <= 0.5 -> 40
            else -> 20
        }

        return ((paymentScore + outstandingScore) / 2).toInt()
    }

    /**
     * Check if this summary needs refresh based on age and changes
     */
    fun needsRefresh(): Boolean {
        return cacheInfo.isStale() || dataSource == SummaryDataSource.CACHED_STALE
    }

    /**
     * Get summary statistics for display
     */
    fun getKeyStats(): ContactSummaryStats {
        return ContactSummaryStats(
            totalContacts = totalContacts,
            activePercentage = if (totalContacts > 0) (activeContacts.toDouble() / totalContacts * 100).toInt() else 0,
            topCustomerRevenue = topCustomersByRevenue.firstOrNull()?.metricValue ?: 0.0,
            averagePaymentDays = averagePaymentDays,
            outstandingAmount = totalOutstandingAmount,
            dataQualityScore = dataQualityScore,
        )
    }

    companion object {
        /**
         * Create an empty summary for initialization
         */
        fun empty(businessId: String, dateRange: DateRange): ContactSummary {
            return ContactSummary(
                dateRange = dateRange,
                businessId = businessId,
                generatedAt = kotlinx.datetime.Clock.System.now().toString(),
                totalContacts = 0,
                activeContacts = 0,
                inactiveContacts = 0,
                contactsByType = ContactTypeBreakdown.empty(),
                newContactsInPeriod = 0,
                modifiedContactsInPeriod = 0,
                revenueMetrics = ContactRevenueMetrics.empty(),
                expenseMetrics = ContactExpenseMetrics.empty(),
                contactsWithOutstanding = 0,
                totalOutstandingAmount = 0.0,
                averagePaymentDays = 0.0,
                topCustomersByRevenue = emptyList(),
                topSuppliersByVolume = emptyList(),
                customersWithDelays = emptyList(),
                highValueContacts = emptyList(),
                recentInteractions = emptyList(),
                totalInteractionsInPeriod = 0,
                interactionsByType = emptyMap(),
                contactsRequiringFollowUp = 0,
                contactsWithCompleteTaxInfo = 0,
                contactsWithVerifiedBanking = 0,
                contactsWithComplianceIssues = 0,
                dataQualityScore = 0,
                contactsByProvince = emptyMap(),
                internationalContactsByCountry = emptyMap(),
                growthTrend = GrowthTrend.empty(),
                cacheInfo = SummaryCacheInfo.fresh(),
            )
        }
    }
}

// === SUPPORTING DATA CLASSES ===

/**
 * Contact breakdown by type
 */
@Serializable
data class ContactTypeBreakdown(
    val customers: Int,
    val suppliers: Int,
    val both: Int,
    val internal: Int,
) {
    companion object {
        fun empty() = ContactTypeBreakdown(0, 0, 0, 0)
    }
}

/**
 * Revenue metrics from customer relationships
 */
@Serializable
data class ContactRevenueMetrics(
    val totalRevenue: Double,
    val averageRevenuePerCustomer: Double,
    val topCustomerRevenue: Double,
    val revenueGrowthRate: Double, // Percentage compared to previous period
    val paidInvoicesCount: Int,
    val paidInvoicesAmount: Double,
) {
    companion object {
        fun empty() = ContactRevenueMetrics(0.0, 0.0, 0.0, 0.0, 0, 0.0)
    }
}

/**
 * Expense metrics from supplier relationships
 */
@Serializable
data class ContactExpenseMetrics(
    val totalExpenses: Double,
    val averageExpensePerSupplier: Double,
    val topSupplierExpense: Double,
    val expenseGrowthRate: Double, // Percentage compared to previous period
    val paidBillsCount: Int,
    val paidBillsAmount: Double,
) {
    companion object {
        fun empty() = ContactExpenseMetrics(0.0, 0.0, 0.0, 0.0, 0, 0.0)
    }
}

/**
 * Individual contact performance insight
 */
@Serializable
data class ContactPerformanceInsight(
    val contactId: String,
    val contactName: String,
    val contactType: ContactType,
    val metricType: PerformanceMetricType,
    val metricValue: Double,
    val rankPosition: Int, // 1-based ranking
    val changeFromPreviousPeriod: Double? = null,
    val riskLevel: ContactRiskLevel = ContactRiskLevel.LOW,
)

/**
 * Growth trend analysis
 */
@Serializable
data class GrowthTrend(
    val contactGrowthRate: Double, // Percentage change in contact count
    val revenueGrowthRate: Double, // Percentage change in revenue
    val newCustomerRate: Double, // Rate of new customer acquisition
    val customerRetentionRate: Double, // Percentage of customers retained
    val trendDirection: TrendDirection,
    val confidenceLevel: Double, // 0.0 to 1.0
) {
    companion object {
        fun empty() = GrowthTrend(0.0, 0.0, 0.0, 0.0, TrendDirection.STABLE, 0.0)
    }
}

/**
 * Forecast metrics for next period
 */
@Serializable
data class ForecastMetrics(
    val predictedContactCount: Int,
    val predictedRevenue: Double,
    val predictedNewCustomers: Int,
    val confidenceInterval: ConfidenceInterval,
    val forecastPeriod: DateRange,
)

/**
 * Cache information for summary data
 */
@Serializable
data class SummaryCacheInfo(
    val cachedAt: String, // ISO datetime
    val expiresAt: String, // ISO datetime
    val ttlMinutes: Int,
    val hitCount: Int = 0,
) {
    fun isStale(): Boolean {
        val now = kotlinx.datetime.Clock.System.now()
        val expiry = kotlinx.datetime.Instant.parse(expiresAt)
        return now > expiry
    }

    companion object {
        fun fresh(ttlMinutes: Int = 30): SummaryCacheInfo {
            val now = kotlinx.datetime.Clock.System.now()
            val duration = kotlin.time.Duration.parse("${ttlMinutes}m")
            val expiry = now.plus(duration)

            return SummaryCacheInfo(
                cachedAt = now.toString(),
                expiresAt = expiry.toString(),
                ttlMinutes = ttlMinutes,
            )
        }
    }
}

/**
 * Key summary statistics for dashboard display
 */
@Serializable
data class ContactSummaryStats(
    val totalContacts: Int,
    val activePercentage: Int,
    val topCustomerRevenue: Double,
    val averagePaymentDays: Double,
    val outstandingAmount: Double,
    val dataQualityScore: Int,
)

/**
 * Confidence interval for forecasting
 */
@Serializable
data class ConfidenceInterval(
    val lower: Double,
    val upper: Double,
    val confidence: Double, // e.g., 0.95 for 95% confidence
)

// === ENUMERATIONS ===

@Serializable
enum class SummaryDataSource(val displayName: String) {
    REAL_TIME("Real-time"),
    CACHED_FRESH("Cached (Fresh)"),
    CACHED_STALE("Cached (Stale)"),
    SCHEDULED("Scheduled Report"),
}

@Serializable
enum class PerformanceMetricType(val displayName: String, val unit: String) {
    REVENUE("Total Revenue", "Currency"),
    TRANSACTION_COUNT("Transaction Count", "Count"),
    PAYMENT_SPEED("Payment Speed", "Days"),
    VOLUME("Transaction Volume", "Currency"),
    FREQUENCY("Interaction Frequency", "Count"),
}

@Serializable
enum class ContactRiskLevel(val displayName: String, val color: String) {
    LOW("Low Risk", "#10B981"),
    MEDIUM("Medium Risk", "#F59E0B"),
    HIGH("High Risk", "#EF4444"),
    CRITICAL("Critical Risk", "#DC2626"),
}

@Serializable
enum class TrendDirection(val displayName: String, val icon: String) {
    INCREASING("Increasing", "↗️"),
    DECREASING("Decreasing", "↘️"),
    STABLE("Stable", "→"),
    VOLATILE("Volatile", "↕️"),
}
