# TECH-DEBT-ARCH-003: Missing Contact Domain Models

**Priority**: High
**Category**: Architecture
**Impact**: Build compilation failures, incomplete Contact feature implementation  
**Estimated Effort**: 2-3 days
**Assigned Agent**: kmp-mobile-expert

## Problem

The ktlint code style fixes revealed missing domain models in the Contact feature, causing compilation errors across the Contact API and repository layers.

### Missing Domain Models
1. **ContactType enum** - Classification of contact types (customer, supplier, etc.)
2. **TaxDetails data class** - VAT registration, tax numbers, compliance information
3. **BankingDetails data class** - Bank account information for payments
4. **CreateContactRequest** - API request model for contact creation
5. **UpdateContactRequest** - API request model for contact updates  
6. **ContactSummary** - Analytics summary model for contact reporting

### Current Impact
- ContactApi.kt: Multiple unresolved references
- ContactApiImpl.kt: 80+ compilation errors
- BackendContactRepository.kt: Interface implementation failures
- Contact feature: Completely non-functional

## Implementation Plan

### Phase 1: Core Domain Models (1 day)
```kotlin
// ContactType.kt
enum class ContactType {
    CUSTOMER, SUPPLIER, BOTH, INTERNAL
}

// TaxDetails.kt  
data class TaxDetails(
    val vatNumber: String?,
    val companyRegistration: String?,
    val taxNumber: String?,
    val isVatRegistered: Boolean = false,
    val vatRate: Double? = null
)

// BankingDetails.kt
data class BankingDetails(
    val bankName: String?,
    val branchCode: String?,
    val accountNumber: String?,
    val accountType: String?,
    val accountHolderName: String?
)
```

### Phase 2: API Request Models (1 day)
```kotlin
// ContactRequests.kt
data class CreateContactRequest(
    val name: String,
    val email: String?,
    val phone: String?,
    val type: ContactType,
    val taxDetails: TaxDetails?,
    val bankingDetails: BankingDetails?
)

data class UpdateContactRequest(
    val name: String?,
    val email: String?,
    val phone: String?,
    val type: ContactType?,
    val taxDetails: TaxDetails?,
    val bankingDetails: BankingDetails?
)
```

### Phase 3: Analytics Models (0.5 days)
```kotlin
// ContactSummary.kt
data class ContactSummary(
    val totalContacts: Int,
    val customerCount: Int,
    val supplierCount: Int,
    val recentlyAdded: Int,
    val dateRange: DateRange
)
```

## Success Criteria
- [ ] All ContactApi.kt references resolve
- [ ] ContactApiImpl.kt compiles without errors
- [ ] BackendContactRepository.kt implements all interface methods
- [ ] Contact feature builds successfully on Android and iOS
- [ ] Domain models follow enterprise architecture patterns

## Dependencies
- Requires DateRange from TECH-DEBT-ARCH-004
- Should align with existing BusinessContact entity structure

## Testing Requirements
- Unit tests for all domain models
- Serialization/deserialization tests
- API request/response validation tests