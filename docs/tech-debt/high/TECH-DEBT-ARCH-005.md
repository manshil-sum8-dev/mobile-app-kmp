# TECH-DEBT-ARCH-005: Missing Analytics Export Models

**Priority**: High  
**Category**: Architecture
**Impact**: Build compilation failures, incomplete Analytics export functionality
**Estimated Effort**: 1-2 days
**Assigned Agent**: kmp-mobile-expert

## Problem

The ktlint code style fixes revealed missing domain models for the Analytics export functionality, causing compilation errors in the BackendAnalyticsRepository and preventing business report generation.

### Missing Domain Models
1. **ReportType enum** - Classification of available business reports
2. **ExportFormat enum** - Supported export formats (PDF, Excel, CSV)
3. **ExportResponse data class** - Response model for generated reports
4. **DateRange data class** - Shared utility for date range operations

### Current Impact
- BackendAnalyticsRepository.kt: Unresolved references to ReportType, ExportFormat, ExportResponse
- Analytics export functionality: Completely non-functional  
- Interface implementation: Cannot override exportBusinessReport method
- Cross-feature impact: DateRange needed by Contact and Invoice features

## Implementation Plan

### Phase 1: Core Export Models (1 day)
```kotlin
// ReportType.kt
@Serializable
enum class ReportType {
    FINANCIAL_SUMMARY,
    INVOICE_ANALYSIS, 
    CUSTOMER_REPORT,
    SUPPLIER_REPORT,
    TAX_SUMMARY,
    PAYMENT_ANALYSIS,
    CASH_FLOW,
    PROFIT_LOSS,
    BALANCE_SHEET,
    CUSTOM_REPORT
}

// ExportFormat.kt
@Serializable  
enum class ExportFormat {
    PDF, EXCEL, CSV, JSON
}

// ExportResponse.kt
@Serializable
data class ExportResponse(
    val reportId: String,
    val fileName: String,
    val fileUrl: String,
    val format: ExportFormat,
    val generatedAt: String, // ISO timestamp
    val expiresAt: String?,  // URL expiration
    val fileSize: Long? = null,
    val metadata: Map<String, String> = emptyMap()
)
```

### Phase 2: Shared DateRange Utility (0.5 days)
```kotlin
// DateRange.kt - Shared across Contact, Invoice, Analytics
@Serializable
data class DateRange(
    val startDate: String, // ISO date string (YYYY-MM-DD)
    val endDate: String,   // ISO date string (YYYY-MM-DD)
    val preset: DateRangePreset? = null
) {
    companion object {
        fun today() = DateRange(
            startDate = getCurrentDate(),
            endDate = getCurrentDate(),
            preset = DateRangePreset.TODAY
        )
        
        fun thisMonth() = DateRange(
            startDate = getCurrentMonthStart(),
            endDate = getCurrentMonthEnd(), 
            preset = DateRangePreset.THIS_MONTH
        )
        
        fun lastMonth() = DateRange(
            startDate = getLastMonthStart(),
            endDate = getLastMonthEnd(),
            preset = DateRangePreset.LAST_MONTH
        )
        
        fun thisYear() = DateRange(
            startDate = getCurrentYearStart(),
            endDate = getCurrentYearEnd(),
            preset = DateRangePreset.THIS_YEAR
        )
        
        fun custom(start: String, end: String) = DateRange(
            startDate = start,
            endDate = end,
            preset = DateRangePreset.CUSTOM
        )
    }
}

// DateRangePreset.kt
@Serializable
enum class DateRangePreset {
    TODAY, YESTERDAY, THIS_WEEK, LAST_WEEK,
    THIS_MONTH, LAST_MONTH, THIS_QUARTER, LAST_QUARTER,
    THIS_YEAR, LAST_YEAR, CUSTOM
}
```

### Phase 3: Repository Integration (0.5 days)
- Update BackendAnalyticsRepository.kt to implement exportBusinessReport properly
- Ensure AnalyticsRepository interface includes proper export method signature
- Add RPC endpoint mapping for export functionality

## Success Criteria
- [ ] All BackendAnalyticsRepository.kt references resolve
- [ ] exportBusinessReport method properly implemented
- [ ] Analytics export functionality operational
- [ ] DateRange utility available across all features  
- [ ] All models properly serializable with kotlinx.serialization
- [ ] Export functionality builds successfully on Android and iOS

## Dependencies
- Should integrate with existing AnalyticsRpc implementation
- May require Supabase storage configuration for file hosting
- DateRange will be used by TECH-DEBT-ARCH-003 and TECH-DEBT-ARCH-004

## Testing Requirements
- Unit tests for all domain models and enums
- Serialization/deserialization tests  
- DateRange utility function tests
- Export functionality integration tests
- File generation and URL handling validation