package za.co.quantive.app.domain.invoice

import kotlinx.serialization.Serializable

/**
 * Enhanced recurring invoice information for comprehensive scheduling
 * Provides advanced recurring invoice scheduling and management
 */
@Serializable
data class RecurringInfo(
    val frequency: RecurringFrequency,
    val intervalCount: Int = 1,
    val startDate: String, // ISO date string (YYYY-MM-DD)
    val endDate: String? = null, // ISO date string (YYYY-MM-DD)
    val maxOccurrences: Int? = null,
    val isActive: Boolean = true,
    val nextInvoiceDate: String? = null, // ISO date string (YYYY-MM-DD)
    val lastGeneratedDate: String? = null, // ISO date string (YYYY-MM-DD)
    val currentOccurrence: Int = 0,
    val createdAt: String? = null,
    val updatedAt: String? = null,
) {

    init {
        // Validation for interval count
        require(intervalCount > 0) { "intervalCount must be positive: $intervalCount" }

        // Validation for date order if both dates are provided
        if (endDate != null) {
            // Basic validation - proper date parsing would be done by DateRange utility
            require(endDate >= startDate) { "endDate ($endDate) must be after startDate ($startDate)" }
        }

        // Validation for max occurrences
        if (maxOccurrences != null) {
            require(maxOccurrences > 0) { "maxOccurrences must be positive: $maxOccurrences" }
        }
    }

    /**
     * Checks if the recurring invoice has reached its end condition
     */
    fun hasReachedEnd(): Boolean {
        // Check if reached max occurrences
        if (maxOccurrences != null && currentOccurrence >= maxOccurrences) {
            return true
        }

        // Check if past end date
        if (endDate != null && nextInvoiceDate != null && nextInvoiceDate > endDate) {
            return true
        }

        return false
    }

    /**
     * Gets the display string for the recurring pattern
     */
    fun getRecurringDisplayString(): String {
        val baseFrequency = frequency.displayName.lowercase()
        return when {
            intervalCount == 1 -> frequency.displayName
            frequency == RecurringFrequency.DAILY -> "Every $intervalCount days"
            frequency == RecurringFrequency.WEEKLY -> "Every $intervalCount weeks"
            frequency == RecurringFrequency.MONTHLY -> "Every $intervalCount months"
            frequency == RecurringFrequency.QUARTERLY -> "Every ${intervalCount * 3} months"
            frequency == RecurringFrequency.ANNUALLY -> "Every $intervalCount years"
            else -> "Every $intervalCount ${baseFrequency.removeSuffix("ly")}s"
        }
    }

    /**
     * Backward compatibility for simple recurring info from backend invoice
     */
    fun toSimpleRecurringInfo(): za.co.quantive.app.domain.entities.RecurringInfo {
        return za.co.quantive.app.domain.entities.RecurringInfo(
            isRecurring = true,
            frequency = frequency.toSimpleRecurringFrequency(),
            interval = intervalCount,
            nextInvoiceDate = nextInvoiceDate,
            endDate = endDate,
            maxOccurrences = maxOccurrences,
            currentOccurrence = currentOccurrence,
            isActive = isActive,
        )
    }

    companion object {
        /**
         * Creates enhanced recurring info from simple backend recurring info
         */
        fun fromSimpleRecurringInfo(
            simpleInfo: za.co.quantive.app.domain.entities.RecurringInfo,
            startDate: String,
        ): RecurringInfo? {
            return if (simpleInfo.isRecurring && simpleInfo.frequency != null) {
                RecurringInfo(
                    frequency = RecurringFrequency.fromSimpleRecurringFrequency(simpleInfo.frequency),
                    intervalCount = simpleInfo.interval ?: 1,
                    startDate = startDate,
                    endDate = simpleInfo.endDate,
                    maxOccurrences = simpleInfo.maxOccurrences,
                    isActive = simpleInfo.isActive,
                    nextInvoiceDate = simpleInfo.nextInvoiceDate,
                    currentOccurrence = simpleInfo.currentOccurrence ?: 0,
                )
            } else {
                null
            }
        }

        /**
         * Creates a default daily recurring pattern
         */
        fun createDaily(startDate: String, maxOccurrences: Int? = null): RecurringInfo {
            return RecurringInfo(
                frequency = RecurringFrequency.DAILY,
                intervalCount = 1,
                startDate = startDate,
                maxOccurrences = maxOccurrences,
            )
        }

        /**
         * Creates a default weekly recurring pattern
         */
        fun createWeekly(startDate: String, maxOccurrences: Int? = null): RecurringInfo {
            return RecurringInfo(
                frequency = RecurringFrequency.WEEKLY,
                intervalCount = 1,
                startDate = startDate,
                maxOccurrences = maxOccurrences,
            )
        }

        /**
         * Creates a default monthly recurring pattern
         */
        fun createMonthly(startDate: String, maxOccurrences: Int? = null): RecurringInfo {
            return RecurringInfo(
                frequency = RecurringFrequency.MONTHLY,
                intervalCount = 1,
                startDate = startDate,
                maxOccurrences = maxOccurrences,
            )
        }

        /**
         * Creates a default quarterly recurring pattern
         */
        fun createQuarterly(startDate: String, maxOccurrences: Int? = null): RecurringInfo {
            return RecurringInfo(
                frequency = RecurringFrequency.QUARTERLY,
                intervalCount = 1,
                startDate = startDate,
                maxOccurrences = maxOccurrences,
            )
        }

        /**
         * Creates a default annually recurring pattern
         */
        fun createAnnually(startDate: String, maxOccurrences: Int? = null): RecurringInfo {
            return RecurringInfo(
                frequency = RecurringFrequency.ANNUALLY,
                intervalCount = 1,
                startDate = startDate,
                maxOccurrences = maxOccurrences,
            )
        }
    }
}

/**
 * Request for setting up recurring invoices
 */
@Serializable
data class SetupRecurringRequest(
    val frequency: RecurringFrequency,
    val intervalCount: Int = 1,
    val startDate: String, // ISO date string
    val endDate: String? = null,
    val maxOccurrences: Int? = null,
    val customizations: Map<String, String> = emptyMap(),
)

/**
 * Response containing recurring invoice schedule preview
 */
@Serializable
data class RecurringSchedulePreview(
    val recurringInfo: RecurringInfo,
    val nextInvoiceDates: List<String>, // Next few invoice dates for preview
    val estimatedTotal: Double? = null,
    val totalOccurrences: Int? = null,
)
