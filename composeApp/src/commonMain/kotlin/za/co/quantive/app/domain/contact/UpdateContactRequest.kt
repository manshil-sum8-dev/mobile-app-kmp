package za.co.quantive.app.domain.contact

import kotlinx.serialization.Serializable
import za.co.quantive.app.domain.entities.BusinessAddress

/**
 * Request model for updating existing business contacts
 * Supports full contact updates via PUT operations
 * For partial updates, use PatchContactRequest instead
 */
@Serializable
data class UpdateContactRequest(
    // === BASIC CONTACT INFORMATION ===
    /**
     * Legal/registered name of the contact entity
     * Required - cannot be null in update operations
     */
    val name: String,

    /**
     * Display name for the contact
     * Falls back to 'name' if not provided
     */
    val displayName: String? = null,

    /**
     * Primary email address for the contact
     * Backend validates email format and deliverability
     */
    val email: String? = null,

    /**
     * Primary phone number for the contact
     * Backend validates format and regional compliance
     */
    val phone: String? = null,

    /**
     * Website URL for the contact
     * Backend validates URL format and accessibility
     */
    val website: String? = null,

    // === ADDRESS INFORMATION ===
    /**
     * Physical business address
     * Set to null to remove existing address
     */
    val address: BusinessAddress? = null,

    // === TAX AND COMPLIANCE ===
    /**
     * Tax registration and compliance details
     * Set to null to remove existing tax details
     */
    val taxDetails: TaxDetails? = null,

    // === BANKING INFORMATION ===
    /**
     * Banking details for payment processing
     * Set to null to remove existing banking details
     */
    val bankingDetails: BankingDetails? = null,

    // === METADATA AND PREFERENCES ===
    /**
     * Free-form notes about the contact
     * Set to null to clear existing notes
     */
    val notes: String? = null,

    /**
     * Tags for categorization and filtering
     * Replaces existing tags completely
     */
    val tags: List<String> = emptyList(),

    /**
     * Whether the contact is active
     * Inactive contacts are hidden from most operations
     */
    val isActive: Boolean = true,

    /**
     * Default currency for transactions with this contact
     */
    val preferredCurrency: String? = null,

    // === BUSINESS RELATIONSHIP SETTINGS ===
    /**
     * Default payment terms for this contact
     */
    val paymentTerms: String? = null,

    /**
     * Credit limit for customer contacts
     */
    val creditLimit: Double? = null,

    /**
     * Whether to automatically send invoices to this contact
     */
    val autoSendInvoices: Boolean? = null,

    /**
     * Whether to send payment reminders to this contact
     */
    val sendPaymentReminders: Boolean? = null,

    // === CONTACT PREFERENCES ===
    /**
     * Preferred communication method
     */
    val preferredContactMethod: ContactMethod? = null,

    /**
     * Language preference for communication
     */
    val languagePreference: String? = null,

    /**
     * Timezone for this contact
     */
    val timezone: String? = null,

    // === INTERNAL CONTACT SPECIFICS ===
    /**
     * Department/division for internal contacts
     */
    val department: String? = null,

    /**
     * Employee ID or internal reference
     */
    val employeeId: String? = null,

    /**
     * Job title or position
     */
    val jobTitle: String? = null,

    /**
     * Manager or supervisor contact ID
     */
    val managerId: String? = null,
) {

    /**
     * Validate the update contact request
     * Basic client-side validation before sending to backend
     */
    fun validate(): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()

        // Required field validation
        if (name.isBlank()) {
            errors.add(ValidationError("name", "Contact name is required"))
        }

        // Email validation if provided
        if (!email.isNullOrBlank() && !isValidEmailFormat(email)) {
            errors.add(ValidationError("email", "Invalid email format"))
        }

        // Credit limit validation
        if (creditLimit != null && creditLimit < 0) {
            errors.add(ValidationError("creditLimit", "Credit limit cannot be negative"))
        }

        // Currency validation if provided
        if (!preferredCurrency.isNullOrBlank() && !isValidCurrencyCode(preferredCurrency)) {
            errors.add(ValidationError("preferredCurrency", "Invalid currency code"))
        }

        return errors
    }

    /**
     * Create a summary of changes for confirmation
     */
    fun toUpdateSummary(): String {
        return buildString {
            appendLine("Updating Contact: $name")
            displayName?.let { appendLine("Display Name: $it") }
            email?.let { appendLine("Email: $it") }
            phone?.let { appendLine("Phone: $it") }
            address?.let { appendLine("Address: ${it.getFormattedAddress()}") }
            appendLine("Status: ${if (isActive) "Active" else "Inactive"}")
            if (tags.isNotEmpty()) {
                appendLine("Tags: ${tags.joinToString(", ")}")
            }
        }
    }

    /**
     * Check if any significant business information has changed
     * Used to determine if additional validation or approval is needed
     */
    fun hasSignificantChanges(): Boolean {
        return taxDetails != null ||
            bankingDetails != null ||
            creditLimit != null ||
            !isActive
    }

    companion object {
        /**
         * Create update request from existing contact with modifications
         */
        fun fromExisting(
            contact: za.co.quantive.app.domain.entities.BusinessContact,
            modifications: UpdateContactRequest.() -> UpdateContactRequest = { this },
        ): UpdateContactRequest {
            return UpdateContactRequest(
                name = contact.name,
                displayName = if (contact.displayName != contact.name) contact.displayName else null,
                email = contact.email,
                phone = contact.phone,
                website = contact.website,
                address = contact.address,
                taxDetails = contact.taxDetails,
                bankingDetails = contact.bankingDetails,
                notes = contact.notes,
                tags = contact.tags,
                isActive = contact.isActive,
                preferredCurrency = contact.preferredCurrency,
                paymentTerms = contact.paymentTerms,
                autoSendInvoices = contact.autoSendInvoices,
                sendPaymentReminders = contact.sendPaymentReminders,
            ).modifications()
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
