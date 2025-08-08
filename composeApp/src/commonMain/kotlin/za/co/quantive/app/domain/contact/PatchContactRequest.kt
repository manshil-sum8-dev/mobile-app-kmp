package za.co.quantive.app.domain.contact

import kotlinx.serialization.Serializable

/**
 * Request model for partial contact updates (PATCH operations)
 * Supports atomic updates to specific contact fields without full replacement
 */
@Serializable
data class PatchContactRequest(
    // === BASIC INFORMATION UPDATES ===
    /**
     * Update display name
     */
    val displayName: String? = null,

    /**
     * Update email address
     */
    val email: String? = null,

    /**
     * Update phone number
     */
    val phone: String? = null,

    /**
     * Update website URL
     */
    val website: String? = null,

    /**
     * Update contact notes
     */
    val notes: String? = null,

    /**
     * Update contact tags
     */
    val tags: List<String>? = null,

    // === STATUS UPDATES ===
    /**
     * Update active status
     */
    val isActive: Boolean? = null,

    /**
     * Mark contact as archived
     */
    val isArchived: Boolean? = null,

    // === BUSINESS SETTINGS UPDATES ===
    /**
     * Update default payment terms
     */
    val paymentTerms: String? = null,

    /**
     * Update credit limit
     */
    val creditLimit: Double? = null,

    /**
     * Update auto-send invoices setting
     */
    val autoSendInvoices: Boolean? = null,

    /**
     * Update payment reminders setting
     */
    val sendPaymentReminders: Boolean? = null,

    /**
     * Update preferred currency
     */
    val preferredCurrency: String? = null,

    // === COMMUNICATION PREFERENCES ===
    /**
     * Update preferred contact method
     */
    val preferredContactMethod: ContactMethod? = null,

    /**
     * Update language preference
     */
    val languagePreference: String? = null,

    /**
     * Update timezone
     */
    val timezone: String? = null,
) {

    /**
     * Check if the patch request contains any updates
     */
    fun hasUpdates(): Boolean {
        return displayName != null || email != null || phone != null || website != null ||
            notes != null || tags != null || isActive != null || isArchived != null ||
            paymentTerms != null || creditLimit != null || autoSendInvoices != null ||
            sendPaymentReminders != null || preferredCurrency != null ||
            preferredContactMethod != null || languagePreference != null || timezone != null
    }

    /**
     * Get list of fields being updated
     */
    fun getUpdatedFields(): List<String> {
        val fields = mutableListOf<String>()
        if (displayName != null) fields.add("displayName")
        if (email != null) fields.add("email")
        if (phone != null) fields.add("phone")
        if (website != null) fields.add("website")
        if (notes != null) fields.add("notes")
        if (tags != null) fields.add("tags")
        if (isActive != null) fields.add("isActive")
        if (isArchived != null) fields.add("isArchived")
        if (paymentTerms != null) fields.add("paymentTerms")
        if (creditLimit != null) fields.add("creditLimit")
        if (autoSendInvoices != null) fields.add("autoSendInvoices")
        if (sendPaymentReminders != null) fields.add("sendPaymentReminders")
        if (preferredCurrency != null) fields.add("preferredCurrency")
        if (preferredContactMethod != null) fields.add("preferredContactMethod")
        if (languagePreference != null) fields.add("languagePreference")
        if (timezone != null) fields.add("timezone")
        return fields
    }

    /**
     * Validate the patch request
     */
    fun validate(): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()

        if (!hasUpdates()) {
            errors.add(ValidationError("request", "At least one field must be updated"))
        }

        // Email validation if provided
        email?.let { emailValue ->
            if (emailValue.isBlank() || !isValidEmailFormat(emailValue)) {
                errors.add(ValidationError("email", "Invalid email format"))
            }
        }

        // Currency validation if provided
        preferredCurrency?.let { currency ->
            if (!isValidCurrencyCode(currency)) {
                errors.add(ValidationError("preferredCurrency", "Invalid currency code"))
            }
        }

        // Credit limit validation
        creditLimit?.let { limit ->
            if (limit < 0) {
                errors.add(ValidationError("creditLimit", "Credit limit cannot be negative"))
            }
        }

        return errors
    }

    companion object {
        /**
         * Create a patch request to update contact status
         */
        fun updateStatus(isActive: Boolean): PatchContactRequest {
            return PatchContactRequest(isActive = isActive)
        }

        /**
         * Create a patch request to update contact communication preferences
         */
        fun updateCommunicationPreferences(
            autoSendInvoices: Boolean? = null,
            sendPaymentReminders: Boolean? = null,
            preferredContactMethod: ContactMethod? = null,
        ): PatchContactRequest {
            return PatchContactRequest(
                autoSendInvoices = autoSendInvoices,
                sendPaymentReminders = sendPaymentReminders,
                preferredContactMethod = preferredContactMethod,
            )
        }

        /**
         * Create a patch request to update contact business settings
         */
        fun updateBusinessSettings(
            paymentTerms: String? = null,
            creditLimit: Double? = null,
            preferredCurrency: String? = null,
        ): PatchContactRequest {
            return PatchContactRequest(
                paymentTerms = paymentTerms,
                creditLimit = creditLimit,
                preferredCurrency = preferredCurrency,
            )
        }

        /**
         * Basic email format validation
         */
        private fun isValidEmailFormat(email: String): Boolean {
            return email.contains("@") && email.contains(".")
        }

        /**
         * Basic currency code validation
         */
        private fun isValidCurrencyCode(currency: String): Boolean {
            return currency.length == 3 && currency.all { it.isUpperCase() }
        }
    }
}
