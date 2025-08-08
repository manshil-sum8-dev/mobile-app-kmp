# TECH-DEBT-ARCH-004: Missing Invoice Domain Models  

**Priority**: High
**Category**: Architecture
**Impact**: Build compilation failures, incomplete Invoice feature implementation
**Estimated Effort**: 2-3 days
**Assigned Agent**: kmp-mobile-expert

## Problem

The ktlint code style fixes revealed missing domain models in the Invoice feature, causing compilation errors in the Invoice API layer and preventing proper invoice template and recurring functionality.

### Missing Domain Models
1. **TemplateInfo data class** - Invoice template configuration and metadata
2. **RecurringInfo data class** - Recurring invoice scheduling and patterns
3. **InvoiceRepository interface** - Repository contract for invoice operations
4. **InvoiceCache interface** - Caching contract for invoice data

### Current Impact  
- InvoiceApi.kt: Unresolved references to TemplateInfo, RecurringInfo
- AppServices.kt: Cannot instantiate InvoiceRepository
- Serialization errors: Missing @Serializable annotations
- Invoice feature: Template and recurring functionality broken

## Implementation Plan

### Phase 1: Core Invoice Domain Models (1.5 days)
```kotlin
// TemplateInfo.kt
@Serializable
data class TemplateInfo(
    val templateId: String,
    val templateName: String,
    val isDefault: Boolean = false,
    val customFields: Map<String, String> = emptyMap(),
    val logoUrl: String? = null,
    val brandingColors: Map<String, String> = emptyMap(),
    val termsAndConditions: String? = null,
    val footerText: String? = null
)

// RecurringInfo.kt  
@Serializable
data class RecurringInfo(
    val frequency: RecurringFrequency,
    val intervalCount: Int = 1,
    val startDate: String, // ISO date string
    val endDate: String? = null,
    val maxOccurrences: Int? = null,
    val isActive: Boolean = true,
    val nextInvoiceDate: String? = null,
    val lastGeneratedDate: String? = null
)

// RecurringFrequency.kt
@Serializable
enum class RecurringFrequency {
    DAILY, WEEKLY, MONTHLY, QUARTERLY, ANNUALLY
}
```

### Phase 2: Repository Interface (0.5 days)
```kotlin
// InvoiceRepository.kt
interface InvoiceRepository {
    suspend fun getInvoices(filter: InvoiceFilter? = null): Result<List<Invoice>>
    suspend fun getInvoice(id: String): Result<Invoice?>
    suspend fun createInvoice(invoice: Invoice): Result<Invoice>
    suspend fun updateInvoice(invoice: Invoice): Result<Invoice>
    suspend fun deleteInvoice(id: String): Result<Unit>
    suspend fun getInvoiceTemplates(): Result<List<TemplateInfo>>
    suspend fun createFromTemplate(templateId: String, data: Map<String, Any>): Result<Invoice>
    suspend fun setupRecurring(invoiceId: String, recurringInfo: RecurringInfo): Result<Unit>
    suspend fun getRecurringInvoices(): Result<List<Invoice>>
}
```

### Phase 3: Cache Interface (0.5 days)  
```kotlin
// InvoiceCache.kt - Already exists but needs proper interface
interface InvoiceCache {
    suspend fun getInvoices(filter: InvoiceFilter?): List<Invoice>
    suspend fun getInvoice(id: String): Invoice?
    suspend fun saveInvoices(invoices: List<Invoice>)
    suspend fun saveInvoice(invoice: Invoice)
    suspend fun deleteInvoice(id: String)
    suspend fun clearCache()
}
```

### Phase 4: Integration (0.5 days)
- Update AppServices.kt with proper InvoiceRepository instantiation
- Ensure BackendInvoiceRepository implements the interface
- Add proper cache integration

## Success Criteria
- [ ] All InvoiceApi.kt references resolve  
- [ ] InvoiceRepository interface properly defined
- [ ] AppServices.kt successfully instantiates invoice services
- [ ] Invoice feature builds successfully on Android and iOS
- [ ] Template and recurring functionality operational

## Dependencies
- May require DateRange utility from shared domain
- Should integrate with existing Invoice entity structure
- Needs proper Supabase RPC endpoint configuration

## Testing Requirements  
- Unit tests for all domain models
- Serialization tests with kotlinx.serialization
- Repository implementation tests with mocking
- Cache implementation validation tests