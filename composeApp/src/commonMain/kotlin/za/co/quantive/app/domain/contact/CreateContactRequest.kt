package za.co.quantive.app.domain.contact

import kotlinx.serialization.Serializable
import za.co.quantive.app.domain.entities.BusinessAddress

/**
 * Request model for creating new business contacts
 * Follows REST API conventions with comprehensive validation support
 */
@Serializable
data class CreateContactRequest(
    // === REQUIRED FIELDS ===
    /**
     * Business ID that owns this contact
     * Must be a valid, active business ID
     */
    val businessId: String,

    /**
     * Type of contact relationship
     */
    val type: ContactType,

    /**
     * Legal/registered name of the contact entity
     * Required for all contact types
     */
    val name: String,

    // === BASIC CONTACT INFORMATION ===
    /**
     * Display name for the contact (optional)
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
     * Required for suppliers and recommended for customers
     */
    val address: BusinessAddress? = null,

    // === TAX AND COMPLIANCE ===
    /**
     * Tax registration and compliance details
     * Required for VAT-registered suppliers
     */
    val taxDetails: TaxDetails? = null,

    // === BANKING INFORMATION ===
    /**
     * Banking details for payment processing
     * Required for suppliers, optional for customers
     */
    val bankingDetails: BankingDetails? = null,

    // === METADATA AND PREFERENCES ===
    /**
     * Free-form notes about the contact
     * Supports markdown formatting
     */
    val notes: String? = null,

    /**
     * Tags for categorization and filtering
     * Backend validates against business tag policies
     */
    val tags: List<String> = emptyList(),

    /**
     * Default currency for transactions with this contact
     * Defaults to business base currency if not specified
     */
    val preferredCurrency: String? = null,

    // === BUSINESS RELATIONSHIP SETTINGS ===
    /**
     * Default payment terms for this contact (e.g., "Net 30", "Due on receipt")
     */
    val paymentTerms: String? = null,

    /**
     * Credit limit for customer contacts
     * Used for credit control and risk management
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
     * ISO 639-1 language codes (e.g., "en", "af", "zu")
     */
    val languagePreference: String? = null,

    /**
     * Timezone for this contact (IANA timezone identifier)
     * Used for scheduling and communication timing
     */
    val timezone: String? = null,

    // === INTERNAL CONTACT SPECIFICS ===
    /**
     * Department/division for internal contacts
     * Only applicable when type = INTERNAL
     */
    val department: String? = null,

    /**
     * Employee ID or internal reference
     * Only applicable when type = INTERNAL
     */
    val employeeId: String? = null,

    /**
     * Job title or position
     * Applicable for internal contacts and key business contacts
     */
    val jobTitle: String? = null,

    /**
     * Manager or supervisor contact ID
     * Only applicable when type = INTERNAL
     */
    val managerId: String? = null,
) {

    /**
     * Validate the create contact request
     * Basic client-side validation before sending to backend
     * Backend performs comprehensive validation
     */
    fun validate(): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()

        // Required field validation
        if (businessId.isBlank()) {
            errors.add(ValidationError("businessId", "Business ID is required"))
        }

        if (name.isBlank()) {
            errors.add(ValidationError("name", "Contact name is required"))
        }

        // Email validation if provided
        if (!email.isNullOrBlank() && !isValidEmailFormat(email)) {
            errors.add(ValidationError("email", "Invalid email format"))
        }

        // Contact type specific validation
        when (type) {
            ContactType.SUPPLIER -> {
                if (address == null) {
                    errors.add(ValidationError("address", "Address is required for suppliers"))
                }
            }
            ContactType.INTERNAL -> {
                if (department.isNullOrBlank()) {
                    errors.add(ValidationError("department", "Department is required for internal contacts"))
                }
            }
            else -> {} // No additional validation for CUSTOMER and BOTH
        }

        // Currency validation if provided
        if (!preferredCurrency.isNullOrBlank() && !isValidCurrencyCode(preferredCurrency)) {
            errors.add(ValidationError("preferredCurrency", "Invalid currency code"))
        }

        return errors
    }

    /**
     * Convert to display summary for confirmation
     */
    fun toDisplaySummary(): String {
        return buildString {
            appendLine("New ${type.displayName}: $name")
            displayName?.let { appendLine("Display Name: $it") }
            email?.let { appendLine("Email: $it") }
            phone?.let { appendLine("Phone: $it") }
            address?.let { appendLine("Address: ${it.getFormattedAddress()}") }
            if (tags.isNotEmpty()) {
                appendLine("Tags: ${tags.joinToString(", ")}")
            }
        }
    }

    companion object {
        /**
         * Create a basic customer contact request
         */
        fun createCustomer(
            businessId: String,
            name: String,
            email: String? = null,
            phone: String? = null,
        ): CreateContactRequest {
            return CreateContactRequest(
                businessId = businessId,
                type = ContactType.CUSTOMER,
                name = name,
                email = email,
                phone = phone,
                autoSendInvoices = true,
                sendPaymentReminders = true,
            )
        }

        /**
         * Create a basic supplier contact request
         */
        fun createSupplier(
            businessId: String,
            name: String,
            email: String? = null,
            address: BusinessAddress? = null,
            taxDetails: TaxDetails? = null,
        ): CreateContactRequest {
            return CreateContactRequest(
                businessId = businessId,
                type = ContactType.SUPPLIER,
                name = name,
                email = email,
                address = address,
                taxDetails = taxDetails,
                autoSendInvoices = false,
                sendPaymentReminders = false,
            )
        }

        /**
         * Create an internal contact request
         */
        fun createInternal(
            businessId: String,
            name: String,
            email: String? = null,
            department: String,
            employeeId: String? = null,
            jobTitle: String? = null,
        ): CreateContactRequest {
            return CreateContactRequest(
                businessId = businessId,
                type = ContactType.INTERNAL,
                name = name,
                email = email,
                department = department,
                employeeId = employeeId,
                jobTitle = jobTitle,
                autoSendInvoices = false,
                sendPaymentReminders = false,
            )
        }

        /**
         * Basic email format validation
         * Backend performs comprehensive validation
         */
        private fun isValidEmailFormat(email: String): Boolean {
            return email.contains("@") && email.contains(".")
        }

        /**
         * Basic currency code validation
         * Backend validates against ISO 4217 standards
         */
        private fun isValidCurrencyCode(currency: String): Boolean {
            return currency.length == 3 && currency.all { it.isUpperCase() }
        }
    }
}

/**
 * Validation error for request validation
 */
@Serializable
data class ValidationError(
    val field: String,
    val message: String,
)

/**
 * Preferred contact method enumeration
 */
@Serializable
enum class ContactMethod(val displayName: String) {
    EMAIL("Email"),
    PHONE("Phone Call"),
    SMS("SMS/Text"),
    WHATSAPP("WhatsApp"),
    POST("Postal Mail"),
    IN_PERSON("In Person"),
}
