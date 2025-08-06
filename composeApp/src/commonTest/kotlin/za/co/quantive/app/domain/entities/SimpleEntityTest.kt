package za.co.quantive.app.domain.entities

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

/**
 * Simple entity tests that verify backend-driven data models
 * Tests focus on data integrity and business logic removal validation
 */
class SimpleEntityTest {

    @Test
    fun `Money object handles backend-formatted currency`() {
        val money = Money(
            amount = 1500.00,
            currency = "ZAR",
            formattedAmount = "R 1,500.00"
        )
        
        // Verify backend provides all formatting
        assertEquals(1500.00, money.amount)
        assertEquals("ZAR", money.currency) 
        assertEquals("R 1,500.00", money.formattedAmount)
    }

    @Test
    fun `InvoiceStatus enum provides all required states`() {
        // Verify all invoice statuses exist for backend-driven workflow
        assertTrue(InvoiceStatus.values().contains(InvoiceStatus.DRAFT))
        assertTrue(InvoiceStatus.values().contains(InvoiceStatus.SENT))
        assertTrue(InvoiceStatus.values().contains(InvoiceStatus.PAID))
        assertTrue(InvoiceStatus.values().contains(InvoiceStatus.OVERDUE))
        assertTrue(InvoiceStatus.values().contains(InvoiceStatus.CANCELLED))
        assertTrue(InvoiceStatus.values().contains(InvoiceStatus.PARTIALLY_PAID))
    }

    @Test
    fun `ContactType enum supports business relationships correctly`() {
        val customer = ContactType.CUSTOMER
        val supplier = ContactType.SUPPLIER  
        val both = ContactType.BOTH
        
        // Test business logic for contact types
        assertTrue(customer.isCustomer())
        assertFalse(customer.isSupplier())
        
        assertFalse(supplier.isCustomer())
        assertTrue(supplier.isSupplier())
        
        assertTrue(both.isCustomer())
        assertTrue(both.isSupplier())
        
        // Test display names for UI
        assertEquals("Customer", customer.displayName)
        assertEquals("Supplier", supplier.displayName)
        assertEquals("Customer & Supplier", both.displayName)
        
        assertEquals("Customers", customer.pluralName)
        assertEquals("Suppliers", supplier.pluralName)
    }

    @Test
    fun `DateRange represents backend-provided date ranges`() {
        val dateRange = DateRange(
            start = "2024-01-01",
            end = "2024-01-31"
        )
        
        assertEquals("2024-01-01", dateRange.start)
        assertEquals("2024-01-31", dateRange.end)
    }

    @Test
    fun `BusinessContact factory method creates proper instances`() {
        val contact = BusinessContact.create(
            businessId = "business_123",
            type = ContactType.CUSTOMER,
            name = "Test Customer",
            email = "test@example.com",
            phone = "+27123456789"
        )
        
        assertEquals("business_123", contact.businessId)
        assertEquals(ContactType.CUSTOMER, contact.type)
        assertEquals("Test Customer", contact.name)
        assertEquals("test@example.com", contact.email)
        assertEquals("+27123456789", contact.phone)
        assertTrue(contact.id.startsWith("contact_"))
        assertTrue(contact.createdAt.isNotEmpty())
        assertTrue(contact.updatedAt.isNotEmpty())
    }

    @Test
    fun `BusinessContact getPrimaryContact returns email over phone`() {
        // Test email preferred
        val contactWithBoth = BusinessContact(
            id = "contact_1",
            businessId = "biz_1",
            type = ContactType.CUSTOMER,
            name = "Test Contact",
            email = "test@example.com",
            phone = "+27123456789",
            createdAt = "2024-01-01T00:00:00Z",
            updatedAt = "2024-01-01T00:00:00Z"
        )
        assertEquals("test@example.com", contactWithBoth.getPrimaryContact())
        
        // Test phone fallback
        val contactPhoneOnly = BusinessContact(
            id = "contact_2", 
            businessId = "biz_1",
            type = ContactType.CUSTOMER,
            name = "Phone Contact",
            email = null,
            phone = "+27123456789",
            createdAt = "2024-01-01T00:00:00Z",
            updatedAt = "2024-01-01T00:00:00Z"
        )
        assertEquals("+27123456789", contactPhoneOnly.getPrimaryContact())
        
        // Test null case
        val contactNoContact = BusinessContact(
            id = "contact_3",
            businessId = "biz_1", 
            type = ContactType.CUSTOMER,
            name = "No Contact",
            email = null,
            phone = null,
            createdAt = "2024-01-01T00:00:00Z",
            updatedAt = "2024-01-01T00:00:00Z"
        )
        assertEquals(null, contactNoContact.getPrimaryContact())
    }

    @Test
    fun `BusinessAddress formats South African addresses correctly`() {
        val address = BusinessAddress(
            streetAddress = "123 Main Street",
            city = "Cape Town", 
            province = "Western Cape",
            postalCode = "8000",
            country = "South Africa"
        )
        
        val formatted = address.getFormattedAddress()
        assertEquals("123 Main Street, Cape Town, Western Cape, 8000", formatted)
        
        // Test South African postal code validation
        assertTrue(address.isValidSAPostalCode()) // 4 digits
        
        // Test invalid postal code
        val invalidAddress = BusinessAddress("1 Test St", "Joburg", "GP", "12345")
        assertFalse(invalidAddress.isValidSAPostalCode()) // 5 digits invalid
    }

    @Test
    fun `BankAccountType enum provides South African account types`() {
        val accountTypes = BankAccountType.values()
        
        assertTrue(accountTypes.contains(BankAccountType.CURRENT))
        assertTrue(accountTypes.contains(BankAccountType.SAVINGS))
        assertTrue(accountTypes.contains(BankAccountType.BUSINESS))
        assertTrue(accountTypes.contains(BankAccountType.TRANSMISSION))
        
        assertEquals("Current Account", BankAccountType.CURRENT.displayName)
        assertEquals("Business Account", BankAccountType.BUSINESS.displayName)
    }
}