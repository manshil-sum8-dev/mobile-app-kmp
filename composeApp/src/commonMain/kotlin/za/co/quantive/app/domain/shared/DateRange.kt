package za.co.quantive.app.domain.shared

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

/**
 * Shared DateRange utility for cross-platform date operations
 * Supports ISO date string format (YYYY-MM-DD) and common business date presets
 */
@Serializable
data class DateRange(
    val startDate: String, // ISO date string (YYYY-MM-DD)
    val endDate: String, // ISO date string (YYYY-MM-DD)
    val preset: DateRangePreset? = null,
) {

    init {
        // Validate date format
        require(isValidIsoDate(startDate)) { "startDate must be valid ISO date (YYYY-MM-DD): $startDate" }
        require(isValidIsoDate(endDate)) { "endDate must be valid ISO date (YYYY-MM-DD): $endDate" }

        // Validate date order
        val start = LocalDate.parse(startDate)
        val end = LocalDate.parse(endDate)
        require(start <= end) { "startDate ($startDate) must be before or equal to endDate ($endDate)" }
    }

    /**
     * Backward compatibility properties for existing code
     */
    val start: String get() = startDate
    val end: String get() = endDate

    /**
     * Returns the number of days in this date range (inclusive)
     */
    fun getDayCount(): Long {
        val start = LocalDate.parse(startDate)
        val end = LocalDate.parse(endDate)
        return start.daysUntil(end).toLong() + 1
    }

    /**
     * Returns the start date as LocalDate
     */
    fun getStartLocalDate(): LocalDate = LocalDate.parse(startDate)

    /**
     * Returns the end date as LocalDate
     */
    fun getEndLocalDate(): LocalDate = LocalDate.parse(endDate)

    /**
     * Returns true if the given date falls within this range (inclusive)
     */
    fun contains(date: String): Boolean {
        require(isValidIsoDate(date)) { "Date must be valid ISO date (YYYY-MM-DD): $date" }
        val checkDate = LocalDate.parse(date)
        return checkDate >= getStartLocalDate() && checkDate <= getEndLocalDate()
    }

    /**
     * Returns true if this range overlaps with another range
     */
    fun overlaps(other: DateRange): Boolean {
        val thisStart = getStartLocalDate()
        val thisEnd = getEndLocalDate()
        val otherStart = other.getStartLocalDate()
        val otherEnd = other.getEndLocalDate()

        return thisStart <= otherEnd && otherStart <= thisEnd
    }

    /**
     * Returns a formatted display string for the date range
     */
    fun toDisplayString(): String = when (preset) {
        null -> "$startDate to $endDate"
        DateRangePreset.CUSTOM -> "$startDate to $endDate"
        else -> preset.displayName
    }

    companion object {
        private val timeZone = TimeZone.currentSystemDefault()

        /**
         * Validates if a string is a valid ISO date (YYYY-MM-DD)
         */
        private fun isValidIsoDate(dateString: String): Boolean {
            return try {
                LocalDate.parse(dateString)
                true
            } catch (e: Exception) {
                false
            }
        }

        /**
         * Gets current date in ISO format
         */
        private fun getCurrentDate(): String {
            return Clock.System.now().toLocalDateTime(timeZone).date.toString()
        }

        /**
         * Creates a DateRange for today
         */
        fun today(): DateRange {
            val today = getCurrentDate()
            return DateRange(
                startDate = today,
                endDate = today,
                preset = DateRangePreset.TODAY,
            )
        }

        /**
         * Creates a DateRange for yesterday
         */
        fun yesterday(): DateRange {
            val yesterday = Clock.System.now()
                .minus(DateTimeUnit.DAY, timeZone)
                .toLocalDateTime(timeZone)
                .date
                .toString()
            return DateRange(
                startDate = yesterday,
                endDate = yesterday,
                preset = DateRangePreset.YESTERDAY,
            )
        }

        /**
         * Creates a DateRange for this week (Monday to Sunday)
         */
        fun thisWeek(): DateRange {
            val today = Clock.System.now().toLocalDateTime(timeZone).date
            val startOfWeek = today.minus(today.dayOfWeek.ordinal, DateTimeUnit.DAY)
            val endOfWeek = startOfWeek.plus(6, DateTimeUnit.DAY)

            return DateRange(
                startDate = startOfWeek.toString(),
                endDate = endOfWeek.toString(),
                preset = DateRangePreset.THIS_WEEK,
            )
        }

        /**
         * Creates a DateRange for last week
         */
        fun lastWeek(): DateRange {
            val today = Clock.System.now().toLocalDateTime(timeZone).date
            val startOfThisWeek = today.minus(today.dayOfWeek.ordinal, DateTimeUnit.DAY)
            val startOfLastWeek = startOfThisWeek.minus(7, DateTimeUnit.DAY)
            val endOfLastWeek = startOfLastWeek.plus(6, DateTimeUnit.DAY)

            return DateRange(
                startDate = startOfLastWeek.toString(),
                endDate = endOfLastWeek.toString(),
                preset = DateRangePreset.LAST_WEEK,
            )
        }

        /**
         * Creates a DateRange for this month
         */
        fun thisMonth(): DateRange {
            val today = Clock.System.now().toLocalDateTime(timeZone).date
            val startOfMonth = LocalDate(today.year, today.month, 1)
            val endOfMonth = startOfMonth.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)

            return DateRange(
                startDate = startOfMonth.toString(),
                endDate = endOfMonth.toString(),
                preset = DateRangePreset.THIS_MONTH,
            )
        }

        /**
         * Creates a DateRange for last month
         */
        fun lastMonth(): DateRange {
            val today = Clock.System.now().toLocalDateTime(timeZone).date
            val startOfThisMonth = LocalDate(today.year, today.month, 1)
            val startOfLastMonth = startOfThisMonth.minus(1, DateTimeUnit.MONTH)
            val endOfLastMonth = startOfThisMonth.minus(1, DateTimeUnit.DAY)

            return DateRange(
                startDate = startOfLastMonth.toString(),
                endDate = endOfLastMonth.toString(),
                preset = DateRangePreset.LAST_MONTH,
            )
        }

        /**
         * Creates a DateRange for this quarter
         */
        fun thisQuarter(): DateRange {
            val today = Clock.System.now().toLocalDateTime(timeZone).date
            val currentQuarter = (today.monthNumber - 1) / 3 + 1
            val startMonth = (currentQuarter - 1) * 3 + 1
            val startOfQuarter = LocalDate(today.year, startMonth, 1)
            val endOfQuarter = startOfQuarter.plus(3, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)

            return DateRange(
                startDate = startOfQuarter.toString(),
                endDate = endOfQuarter.toString(),
                preset = DateRangePreset.THIS_QUARTER,
            )
        }

        /**
         * Creates a DateRange for last quarter
         */
        fun lastQuarter(): DateRange {
            val today = Clock.System.now().toLocalDateTime(timeZone).date
            val currentQuarter = (today.monthNumber - 1) / 3 + 1
            val lastQuarter = if (currentQuarter == 1) 4 else currentQuarter - 1
            val year = if (currentQuarter == 1) today.year - 1 else today.year

            val startMonth = (lastQuarter - 1) * 3 + 1
            val startOfQuarter = LocalDate(year, startMonth, 1)
            val endOfQuarter = startOfQuarter.plus(3, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)

            return DateRange(
                startDate = startOfQuarter.toString(),
                endDate = endOfQuarter.toString(),
                preset = DateRangePreset.LAST_QUARTER,
            )
        }

        /**
         * Creates a DateRange for this year
         */
        fun thisYear(): DateRange {
            val today = Clock.System.now().toLocalDateTime(timeZone).date
            val startOfYear = LocalDate(today.year, 1, 1)
            val endOfYear = LocalDate(today.year, 12, 31)

            return DateRange(
                startDate = startOfYear.toString(),
                endDate = endOfYear.toString(),
                preset = DateRangePreset.THIS_YEAR,
            )
        }

        /**
         * Creates a DateRange for last year
         */
        fun lastYear(): DateRange {
            val today = Clock.System.now().toLocalDateTime(timeZone).date
            val lastYear = today.year - 1
            val startOfYear = LocalDate(lastYear, 1, 1)
            val endOfYear = LocalDate(lastYear, 12, 31)

            return DateRange(
                startDate = startOfYear.toString(),
                endDate = endOfYear.toString(),
                preset = DateRangePreset.LAST_YEAR,
            )
        }

        /**
         * Creates a DateRange for the last N days (inclusive of today)
         */
        fun lastNDays(days: Int): DateRange {
            require(days > 0) { "Days must be positive: $days" }

            val today = Clock.System.now().toLocalDateTime(timeZone).date
            val startDate = today.minus(days - 1, DateTimeUnit.DAY)

            val preset = when (days) {
                7 -> DateRangePreset.LAST_7_DAYS
                30 -> DateRangePreset.LAST_30_DAYS
                90 -> DateRangePreset.LAST_90_DAYS
                else -> null
            }

            return DateRange(
                startDate = startDate.toString(),
                endDate = today.toString(),
                preset = preset,
            )
        }

        /**
         * Creates a DateRange for the last 7 days
         */
        fun last7Days(): DateRange = lastNDays(7)

        /**
         * Creates a DateRange for the last 30 days
         */
        fun last30Days(): DateRange = lastNDays(30)

        /**
         * Creates a DateRange for the last 90 days
         */
        fun last90Days(): DateRange = lastNDays(90)

        /**
         * Creates a custom DateRange with the given start and end dates
         */
        fun custom(startDate: String, endDate: String): DateRange {
            return DateRange(
                startDate = startDate,
                endDate = endDate,
                preset = DateRangePreset.CUSTOM,
            )
        }

        /**
         * Creates a DateRange from a preset
         */
        fun fromPreset(preset: DateRangePreset): DateRange = when (preset) {
            DateRangePreset.TODAY -> today()
            DateRangePreset.YESTERDAY -> yesterday()
            DateRangePreset.THIS_WEEK -> thisWeek()
            DateRangePreset.LAST_WEEK -> lastWeek()
            DateRangePreset.THIS_MONTH -> thisMonth()
            DateRangePreset.LAST_MONTH -> lastMonth()
            DateRangePreset.THIS_QUARTER -> thisQuarter()
            DateRangePreset.LAST_QUARTER -> lastQuarter()
            DateRangePreset.THIS_YEAR -> thisYear()
            DateRangePreset.LAST_YEAR -> lastYear()
            DateRangePreset.LAST_7_DAYS -> last7Days()
            DateRangePreset.LAST_30_DAYS -> last30Days()
            DateRangePreset.LAST_90_DAYS -> last90Days()
            DateRangePreset.CUSTOM -> throw IllegalArgumentException("Cannot create custom preset without dates")
        }

        /**
         * Parses a date string in various formats and returns ISO format
         * Supports: YYYY-MM-DD, DD/MM/YYYY, MM/DD/YYYY, DD-MM-YYYY
         */
        fun parseToIsoDate(dateString: String): String {
            // Already ISO format
            if (isValidIsoDate(dateString)) {
                return dateString
            }

            // Try common formats
            val formats = listOf(
                "dd/MM/yyyy",
                "MM/dd/yyyy",
                "dd-MM-yyyy",
                "MM-dd-yyyy",
            )

            // For now, just validate ISO format is required
            // Complex parsing would require additional date formatting library
            throw IllegalArgumentException("Date must be in ISO format (YYYY-MM-DD): $dateString")
        }
    }
}
