package za.co.quantive.app.domain.invoice

import kotlinx.serialization.Serializable

/**
 * Enhanced recurring frequency options for invoice scheduling
 * Includes DAILY frequency as required by TECH-DEBT-ARCH-004
 */
@Serializable
enum class RecurringFrequency(val displayName: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    ANNUALLY("Annually"),
    ;

    /**
     * Maps to the simplified recurring frequency used in backend entities
     */
    fun toSimpleRecurringFrequency(): za.co.quantive.app.domain.entities.RecurringFrequency {
        return when (this) {
            DAILY -> za.co.quantive.app.domain.entities.RecurringFrequency.WEEKLY // Fallback for backend compatibility
            WEEKLY -> za.co.quantive.app.domain.entities.RecurringFrequency.WEEKLY
            MONTHLY -> za.co.quantive.app.domain.entities.RecurringFrequency.MONTHLY
            QUARTERLY -> za.co.quantive.app.domain.entities.RecurringFrequency.QUARTERLY
            ANNUALLY -> za.co.quantive.app.domain.entities.RecurringFrequency.YEARLY
        }
    }

    companion object {
        /**
         * Creates enhanced recurring frequency from simple backend frequency
         */
        fun fromSimpleRecurringFrequency(
            simple: za.co.quantive.app.domain.entities.RecurringFrequency,
        ): RecurringFrequency {
            return when (simple) {
                za.co.quantive.app.domain.entities.RecurringFrequency.WEEKLY -> WEEKLY
                za.co.quantive.app.domain.entities.RecurringFrequency.MONTHLY -> MONTHLY
                za.co.quantive.app.domain.entities.RecurringFrequency.QUARTERLY -> QUARTERLY
                za.co.quantive.app.domain.entities.RecurringFrequency.YEARLY -> ANNUALLY
            }
        }
    }
}
