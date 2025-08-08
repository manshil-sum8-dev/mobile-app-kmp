package za.co.quantive.app.domain.shared

import kotlinx.serialization.Serializable

/**
 * Predefined date range presets for common business reporting periods
 */
@Serializable
enum class DateRangePreset(val displayName: String) {
    TODAY("Today"),
    YESTERDAY("Yesterday"),
    THIS_WEEK("This Week"),
    LAST_WEEK("Last Week"),
    THIS_MONTH("This Month"),
    LAST_MONTH("Last Month"),
    THIS_QUARTER("This Quarter"),
    LAST_QUARTER("Last Quarter"),
    THIS_YEAR("This Year"),
    LAST_YEAR("Last Year"),
    LAST_7_DAYS("Last 7 Days"),
    LAST_30_DAYS("Last 30 Days"),
    LAST_90_DAYS("Last 90 Days"),
    CUSTOM("Custom Range"),
    ;

    /**
     * Returns true if this preset represents a fixed period that doesn't change daily
     */
    fun isFixedPeriod(): Boolean = when (this) {
        YESTERDAY, LAST_WEEK, LAST_MONTH, LAST_QUARTER, LAST_YEAR -> true
        else -> false
    }

    /**
     * Returns true if this preset represents a rolling period
     */
    fun isRollingPeriod(): Boolean = when (this) {
        LAST_7_DAYS, LAST_30_DAYS, LAST_90_DAYS -> true
        else -> false
    }

    /**
     * Returns true if this preset represents a current period that changes daily
     */
    fun isCurrentPeriod(): Boolean = when (this) {
        TODAY, THIS_WEEK, THIS_MONTH, THIS_QUARTER, THIS_YEAR -> true
        else -> false
    }
}
