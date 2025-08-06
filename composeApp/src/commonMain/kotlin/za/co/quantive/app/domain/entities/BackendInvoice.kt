package za.co.quantive.app.domain.entities

import kotlinx.serialization.Serializable

/**
 * Backend-driven Invoice entity for Quantive
 * All calculations, validations, and business logic are handled by the backend
 */
@Serializable
data class Invoice(
    val id: String,
    val invoiceNumber: String,
    val businessId: String,
    val customerId: String,
    val customer: Customer?,
    val status: InvoiceStatus,
    val items: List<InvoiceItem>,
    
    // Backend-calculated financial data
    val calculations: InvoiceCalculations,
    
    // Backend-provided metadata
    val metadata: InvoiceMetadata,
    
    // Backend-validated compliance info
    val compliance: ComplianceInfo,
    
    // Backend-managed template/recurring info
    val templateInfo: TemplateInfo? = null,
    val recurringInfo: RecurringInfo? = null
) {
    /**
     * Convenience properties for backward compatibility
     */
    val total: Double get() = calculations.total.amount
    val subtotal: Double get() = calculations.subtotal.amount
    val vatAmount: Double get() = calculations.tax.amount
    val outstandingAmount: Double get() = calculations.amountDue.amount
    val currency: String get() = calculations.total.currency
}

/**
 * Backend-calculated financial information
 * All amounts are calculated and validated by the backend
 */
@Serializable
data class InvoiceCalculations(
    val subtotal: Money,
    val tax: Money,
    val total: Money,
    val amountDue: Money,
    val amountPaid: Money,
    val taxBreakdown: List<TaxLine>,
    val discounts: List<DiscountLine>
)

/**
 * Money representation with currency
 */
@Serializable
data class Money(
    val amount: Double,
    val currency: String,
    val formattedAmount: String // Backend-formatted for display
)

/**
 * Tax line item - calculated by backend
 */
@Serializable
data class TaxLine(
    val type: String, // "VAT", "WithholdingTax", etc.
    val rate: Double, // Backend-provided rate
    val baseAmount: Money,
    val taxAmount: Money,
    val description: String
)

/**
 * Discount line item
 */
@Serializable
data class DiscountLine(
    val type: String, // "PERCENTAGE", "FIXED"
    val description: String,
    val discountAmount: Money
)

/**
 * Invoice metadata from backend
 */
@Serializable
data class InvoiceMetadata(
    val dueDate: String, // ISO date string
    val createdAt: String,
    val updatedAt: String,
    val sentAt: String? = null,
    val paidAt: String? = null,
    val notes: String? = null,
    val termsAndConditions: String? = null,
    val paymentTerms: String? = null,
    val dueDays: Int? = null, // Backend calculated
    val isOverdue: Boolean, // Backend determined
    val daysSinceDue: Int? = null // Backend calculated (null if not overdue)
)

/**
 * Compliance information validated by backend
 */
@Serializable
data class ComplianceInfo(
    val taxJurisdiction: String, // "ZA", "US", etc.
    val vatRegistrationRequired: Boolean,
    val businessVatNumber: String? = null,
    val customerVatNumber: String? = null,
    val complianceStatus: ComplianceStatus,
    val validationErrors: List<String> = emptyList(),
    val requiredFields: List<String> = emptyList()
)

@Serializable
enum class ComplianceStatus {
    COMPLIANT,
    NON_COMPLIANT,
    PENDING_VALIDATION,
    VALIDATION_FAILED
}

/**
 * Template information
 */
@Serializable
data class TemplateInfo(
    val isTemplate: Boolean,
    val templateName: String? = null,
    val templateId: String? = null
)

/**
 * Recurring invoice information
 */
@Serializable
data class RecurringInfo(
    val isRecurring: Boolean,
    val frequency: RecurringFrequency? = null,
    val interval: Int? = null,
    val nextInvoiceDate: String? = null,
    val endDate: String? = null,
    val maxOccurrences: Int? = null,
    val currentOccurrence: Int? = null,
    val isActive: Boolean = false
)

/**
 * Invoice item - backend provides calculated totals
 */
@Serializable
data class InvoiceItem(
    val id: String,
    val description: String,
    val quantity: Double,
    val unitPrice: Money,
    val lineTotal: Money, // Backend calculated
    val taxAmount: Money, // Backend calculated  
    val notes: String? = null,
    val productId: String? = null,
    val productCode: String? = null
)

/**
 * Simplified Customer reference
 */
@Serializable
data class Customer(
    val id: String,
    val name: String,
    val email: String? = null,
    val vatNumber: String? = null,
    val address: Address? = null
)

/**
 * Address information
 */
@Serializable
data class Address(
    val street: String,
    val city: String,
    val state: String? = null,
    val postalCode: String,
    val country: String
)

/**
 * Invoice status enumeration
 */
@Serializable
enum class InvoiceStatus(val displayName: String) {
    DRAFT("Draft"),
    SENT("Sent"),
    VIEWED("Viewed"),
    PAID("Paid"),
    PARTIALLY_PAID("Partially Paid"),
    OVERDUE("Overdue"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded");
    
    fun isActive(): Boolean {
        return this in listOf(SENT, VIEWED, PARTIALLY_PAID, OVERDUE)
    }
    
    fun isPaid(): Boolean {
        return this == PAID
    }
}

/**
 * Recurring frequency options
 */
@Serializable
enum class RecurringFrequency(val displayName: String) {
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    YEARLY("Yearly")
}

/**
 * Invoice filter for API requests
 */
data class InvoiceFilter(
    val status: InvoiceStatus? = null,
    val customerId: String? = null,
    val dateRange: DateRange? = null,
    val searchQuery: String? = null,
    val isOverdue: Boolean? = null,
    val amountRange: AmountRange? = null
)

/**
 * Date range filter
 */
data class DateRange(
    val start: String, // ISO date string
    val end: String    // ISO date string
)

/**
 * Amount range filter
 */
data class AmountRange(
    val min: Double,
    val max: Double,
    val currency: String = "ZAR"
)

/**
 * Backend-provided invoice summary for dashboard
 */
@Serializable
data class InvoiceSummary(
    val totalInvoices: Int,
    val totalAmount: Money,
    val paidAmount: Money,
    val outstandingAmount: Money,
    val overdueAmount: Money,
    val statusBreakdown: Map<InvoiceStatus, Int>,
    val averageInvoiceValue: Money,
    val trends: InvoiceTrends
)

/**
 * Invoice trends calculated by backend
 */
@Serializable
data class InvoiceTrends(
    val monthOverMonth: TrendData,
    val yearOverYear: TrendData,
    val averagePaymentTime: Int // days
)

@Serializable
data class TrendData(
    val percentageChange: Double,
    val direction: TrendDirection,
    val previousValue: Money
)

@Serializable
enum class TrendDirection {
    UP, DOWN, NEUTRAL
}