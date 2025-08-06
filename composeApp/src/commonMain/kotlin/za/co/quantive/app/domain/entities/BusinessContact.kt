package za.co.quantive.app.domain.entities

import kotlinx.serialization.Serializable

/**
 * ⚠️ Business Contact entity - Backend Driven
 * All business calculations, validations, and relationship metrics handled by backend
 * Client only displays backend-provided data
 */
@Serializable
data class BusinessContact(
    val id: String,
    val businessId: String, // Owner business ID
    val type: ContactType,
    val name: String,
    val displayName: String = name,
    val email: String? = null,
    val phone: String? = null,
    val website: String? = null,
    val address: BusinessAddress? = null,
    val taxDetails: TaxDetails? = null,
    val bankingDetails: BankingDetails? = null,
    val notes: String? = null,
    val tags: List<String> = emptyList(),
    val isActive: Boolean = true,
    val createdAt: String,
    val updatedAt: String,
    val lastInteractionDate: String? = null,
    
    // Business relationship data
    val totalInvoicesSent: Int = 0,
    val totalAmountInvoiced: Double = 0.0,
    val totalAmountPaid: Double = 0.0,
    val outstandingAmount: Double = 0.0,
    val averagePaymentDays: Int? = null,
    val creditLimit: Double? = null,
    val paymentTerms: String? = null, // e.g., "Net 30", "Due on receipt"
    
    // Preferences
    val preferredCurrency: String = "ZAR",
    val autoSendInvoices: Boolean = false,
    val sendPaymentReminders: Boolean = true
) {
    companion object {
        fun create(
            businessId: String,
            type: ContactType,
            name: String,
            email: String? = null,
            phone: String? = null
        ): BusinessContact {
            val now = "2024-01-01T00:00:00Z" // Simplified for now
            
            return BusinessContact(
                id = generateContactId(),
                businessId = businessId,
                type = type,
                name = name,
                email = email,
                phone = phone,
                createdAt = now,
                updatedAt = now
            )
        }
        
        private fun generateContactId(): String {
            return "contact_${kotlin.random.Random.nextLong()}_${(1000..9999).random()}"
        }
    }
    
    /**
     * Get primary contact method (email preferred, then phone)
     */
    fun getPrimaryContact(): String? {
        return email ?: phone
    }
    
    /**
     * ⚠️ REMOVED - Client-side business logic
     * Backend now provides:
     * - taxComplianceStatus: Boolean (backend validated)
     * - paymentReliabilityScore: Int (backend calculated)
     * - All business relationship metrics calculated server-side
     */
}

/**
 * Contact type enumeration
 */
@Serializable
enum class ContactType(val displayName: String, val pluralName: String) {
    CUSTOMER("Customer", "Customers"),
    SUPPLIER("Supplier", "Suppliers"),
    BOTH("Customer & Supplier", "Customers & Suppliers");
    
    fun isCustomer(): Boolean = this in listOf(CUSTOMER, BOTH)
    fun isSupplier(): Boolean = this in listOf(SUPPLIER, BOTH)
}

/**
 * Business address with South African postal code format
 */
@Serializable
data class BusinessAddress(
    val streetAddress: String,
    val city: String,
    val province: String? = null, // South African provinces
    val postalCode: String,
    val country: String = "South Africa"
) {
    /**
     * Format address for display
     */
    fun getFormattedAddress(): String {
        return buildString {
            append(streetAddress)
            append(", $city")
            if (province != null) {
                append(", $province")
            }
            append(", $postalCode")
            if (country != "South Africa") {
                append(", $country")
            }
        }
    }
    
    /**
     * Validate South African postal code format
     */
    fun isValidSAPostalCode(): Boolean {
        return postalCode.matches(Regex("\\d{4}"))
    }
}

/**
 * Tax details for South African business compliance
 */
@Serializable
data class TaxDetails(
    val vatNumber: String? = null, // SA VAT number format: 4123456789
    val companyRegistration: String? = null, // SA company registration format
    val taxNumber: String? = null, // Income tax number
    val isVatRegistered: Boolean = false,
    val vatRate: Double = 0.15 // Default SA VAT rate
) {
    /**
     * ⚠️ REMOVED - Client-side validation and formatting
     * Backend now provides:
     * - formattedVatNumber: String? (backend formatted)
     * - vatValidationStatus: ValidationStatus (backend validated)
     * - All formatting and validation handled server-side
     */
}

/**
 * Banking details for payment processing
 */
@Serializable
data class BankingDetails(
    val bankName: String,
    val accountHolderName: String,
    val accountNumber: String,
    val branchCode: String? = null, // SA branch code format
    val accountType: BankAccountType = BankAccountType.CURRENT,
    val swiftCode: String? = null, // For international transfers
    val reference: String? = null // Preferred payment reference
) {
    /**
     * ⚠️ REMOVED - Client-side banking validation
     * Backend now provides:
     * - bankingValidationStatus: ValidationStatus (backend validated)
     * - formattedAccountNumber: String (backend formatted)
     * - All banking details validated server-side for security
     */
}

/**
 * Bank account type enumeration
 */
@Serializable
enum class BankAccountType(val displayName: String) {
    CURRENT("Current Account"),
    SAVINGS("Savings Account"),
    BUSINESS("Business Account"),
    TRANSMISSION("Transmission Account")
}

/**
 * Contact interaction record for relationship tracking
 */
@Serializable
data class ContactInteraction(
    val id: String,
    val contactId: String,
    val type: InteractionType,
    val subject: String,
    val description: String? = null,
    val date: String, // ISO date string
    val outcome: InteractionOutcome? = null,
    val followUpDate: String? = null,
    val relatedInvoiceId: String? = null,
    val createdBy: String? = null
)

/**
 * Interaction type enumeration
 */
@Serializable
enum class InteractionType(val displayName: String, val icon: String) {
    EMAIL("Email Sent", "📧"),
    PHONE_CALL("Phone Call", "📞"),
    MEETING("Meeting", "🤝"),
    INVOICE_SENT("Invoice Sent", "📄"),
    PAYMENT_RECEIVED("Payment Received", "💰"),
    QUOTE_SENT("Quote Sent", "📋"),
    FOLLOW_UP("Follow Up", "📅"),
    COMPLAINT("Complaint", "⚠️"),
    COMPLIMENT("Compliment", "⭐"),
    OTHER("Other", "📝")
}

/**
 * Interaction outcome enumeration
 */
@Serializable
enum class InteractionOutcome(val displayName: String) {
    SUCCESSFUL("Successful"),
    PARTIAL("Partially Successful"),
    UNSUCCESSFUL("Unsuccessful"),
    FOLLOW_UP_REQUIRED("Follow-up Required"),
    ESCALATION_REQUIRED("Escalation Required")
}

/**
 * Contact search and filter options
 */
data class ContactFilter(
    val type: ContactType? = null,
    val searchQuery: String? = null,
    val isActive: Boolean? = null,
    val hasOutstandingInvoices: Boolean? = null,
    val tags: List<String> = emptyList(),
    val paymentReliabilityThreshold: Int? = null,
    val province: String? = null
)

/**
 * Contact summary for analytics
 */
@Serializable
data class ContactSummary(
    val totalContacts: Int,
    val customerCount: Int,
    val supplierCount: Int,
    val activeContacts: Int,
    val contactsWithOutstanding: Int,
    val averagePaymentDays: Double,
    val topCustomersByRevenue: List<BusinessContact>,
    val recentInteractions: List<ContactInteraction>
)