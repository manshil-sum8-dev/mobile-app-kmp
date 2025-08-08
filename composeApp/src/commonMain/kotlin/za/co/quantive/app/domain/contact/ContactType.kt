package za.co.quantive.app.domain.contact

import kotlinx.serialization.Serializable

/**
 * Contact type enumeration for comprehensive business relationship management
 * Supports all types of business contacts including hybrid and internal relationships
 */
@Serializable
enum class ContactType(
    val displayName: String,
    val pluralName: String,
    val description: String,
) {
    /**
     * Standard customer - entity that purchases goods/services
     */
    CUSTOMER(
        displayName = "Customer",
        pluralName = "Customers",
        description = "Entity that purchases goods or services from your business",
    ),

    /**
     * Standard supplier - entity that provides goods/services to the business
     */
    SUPPLIER(
        displayName = "Supplier",
        pluralName = "Suppliers",
        description = "Entity that provides goods or services to your business",
    ),

    /**
     * Hybrid relationship - both customer and supplier
     */
    BOTH(
        displayName = "Customer & Supplier",
        pluralName = "Customers & Suppliers",
        description = "Entity with both customer and supplier relationships",
    ),

    /**
     * Internal contact - employees, departments, subsidiaries
     */
    INTERNAL(
        displayName = "Internal",
        pluralName = "Internal Contacts",
        description = "Internal business entities like employees, departments, or subsidiaries",
    ),
    ;

    /**
     * Check if this contact type acts as a customer
     */
    fun isCustomer(): Boolean = this in listOf(CUSTOMER, BOTH)

    /**
     * Check if this contact type acts as a supplier
     */
    fun isSupplier(): Boolean = this in listOf(SUPPLIER, BOTH)

    /**
     * Check if this contact type is internal to the organization
     */
    fun isInternal(): Boolean = this == INTERNAL

    /**
     * Check if this contact type is external to the organization
     */
    fun isExternal(): Boolean = this != INTERNAL

    /**
     * Get appropriate invoice terminology based on contact type
     */
    fun getInvoiceTerminology(): String = when (this) {
        CUSTOMER, BOTH -> "Invoice"
        SUPPLIER -> "Bill/Purchase Order"
        INTERNAL -> "Internal Transfer"
    }

    /**
     * Get payment direction from business perspective
     */
    fun getPaymentDirection(): PaymentDirection = when (this) {
        CUSTOMER -> PaymentDirection.INCOMING
        SUPPLIER -> PaymentDirection.OUTGOING
        BOTH -> PaymentDirection.BIDIRECTIONAL
        INTERNAL -> PaymentDirection.INTERNAL
    }

    /**
     * Get contact type priority for sorting/filtering
     * Lower numbers indicate higher priority
     */
    fun getPriority(): Int = when (this) {
        CUSTOMER -> 1
        BOTH -> 2
        SUPPLIER -> 3
        INTERNAL -> 4
    }

    companion object {
        /**
         * Get all external contact types (excludes INTERNAL)
         */
        fun getExternalTypes(): List<ContactType> =
            listOf(CUSTOMER, SUPPLIER, BOTH)

        /**
         * Get all customer-related contact types
         */
        fun getCustomerTypes(): List<ContactType> =
            listOf(CUSTOMER, BOTH)

        /**
         * Get all supplier-related contact types
         */
        fun getSupplierTypes(): List<ContactType> =
            listOf(SUPPLIER, BOTH)

        /**
         * Parse contact type from string (case insensitive)
         */
        fun fromString(value: String): ContactType? {
            return entries.find {
                it.name.equals(value, ignoreCase = true) ||
                    it.displayName.equals(value, ignoreCase = true)
            }
        }
    }
}

/**
 * Payment direction enumeration for contact types
 */
@Serializable
enum class PaymentDirection(val displayName: String) {
    INCOMING("Money Incoming"),
    OUTGOING("Money Outgoing"),
    BIDIRECTIONAL("Money Both Directions"),
    INTERNAL("Internal Transfers"),
}
