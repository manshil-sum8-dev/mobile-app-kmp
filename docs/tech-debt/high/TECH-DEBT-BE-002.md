# TECH-DEBT-BE-002: Implement RPC API Architecture

**Status**: Open  
**Priority**: High  
**Domain**: Backend Integration  
**Effort Estimate**: 2-3 weeks  
**Assigned Agent**: Backend Integration Specialist  
**Created**: 2025-01-08  

## Problem Description

The application lacks the RPC (Remote Procedure Call) API architecture required by the enterprise blueprint for heavy computational operations. All operations currently use REST APIs, which is not optimal for complex business calculations that should be backend-driven.

**Current RPC Issues**:
- No RPC endpoint definitions for computational operations
- Heavy business logic calculations handled client-side (blueprint violation)
- No batch processing capabilities for complex operations
- Missing async job handling for long-running computations
- No proper separation between CRUD (REST) and computation (RPC) concerns

## Blueprint Violation

**Blueprint Requirement**: "API Architecture: REST for CRUD, RPC for heavy computations" as part of backend-driven architecture:
- REST APIs: Simple CRUD operations (invoices, contacts, profiles)
- RPC APIs: Heavy computations (invoice calculations, tax processing, reporting)
- Backend-driven pattern: No business logic on client
- Async job processing: Long-running operations handled server-side
- Batch operations: Multiple computations in single RPC call

**Current State**: All operations use REST, heavy calculations done client-side

## Affected Files

### Files to Create
- `src/commonMain/kotlin/za/co/quantive/app/data/remote/rpc/RpcClient.kt`
- `src/commonMain/kotlin/za/co/quantive/app/data/remote/rpc/InvoiceRpcApi.kt`
- `src/commonMain/kotlin/za/co/quantive/app/data/remote/rpc/ReportingRpcApi.kt`
- `src/commonMain/kotlin/za/co/quantive/app/data/remote/rpc/dto/RpcRequest.kt`
- `src/commonMain/kotlin/za/co/quantive/app/data/remote/rpc/dto/RpcResponse.kt`

### Files to Update
- `/composeApp/src/commonMain/kotlin/za/co/quantive/app/data/remote/SupabaseClient.kt`
- All repository implementations (separate REST from RPC operations)
- Business logic classes (remove client-side calculations)

## Risk Assessment

- **Architecture Risk**: High - Violates backend-driven pattern
- **Performance Risk**: High - Heavy client-side calculations impact performance
- **Scalability Risk**: Medium - Complex operations don't scale on mobile
- **Maintainability Risk**: High - Business logic scattered between client/server

## Acceptance Criteria

### RPC Client Infrastructure
- [ ] Implement RPC client with proper error handling
- [ ] Add support for synchronous and asynchronous RPC calls
- [ ] Create request/response serialization handling
- [ ] Implement RPC method discovery and validation
- [ ] Add RPC call tracing and monitoring

### Invoice Computation RPC
- [ ] Move invoice total calculations to RPC endpoints
- [ ] Implement tax calculation RPC methods
- [ ] Add discount and promotion calculation RPC
- [ ] Create line item validation RPC
- [ ] Implement currency conversion RPC

### Reporting and Analytics RPC
- [ ] Create business report generation RPC
- [ ] Implement dashboard data aggregation RPC
- [ ] Add financial analytics computation RPC
- [ ] Create export processing RPC (PDF, Excel)
- [ ] Implement audit trail analysis RPC

### Batch Processing
- [ ] Support multiple calculations in single RPC call
- [ ] Add batch invoice processing
- [ ] Implement bulk reporting operations
- [ ] Create transaction batching for data consistency

## Implementation Strategy

### Phase 1: RPC Client Infrastructure (Week 1)

#### Core RPC Client Implementation
```kotlin
// RpcClient.kt
class RpcClient(
    private val httpClient: HttpClient,
    private val baseUrl: String
) {
    suspend inline fun <reified T> call(
        method: String,
        params: Map<String, Any>? = null,
        timeout: Duration = 30.seconds
    ): Result<T> {
        return try {
            val request = RpcRequest(
                method = method,
                params = params,
                id = generateRequestId()
            )
            
            val response = httpClient.post("$baseUrl/rpc") {
                contentType(ContentType.Application.Json)
                setBody(request)
                timeout {
                    requestTimeoutMillis = timeout.inWholeMilliseconds
                }
            }
            
            val rpcResponse = response.body<RpcResponse<T>>()
            
            if (rpcResponse.error != null) {
                Result.failure(RpcException(rpcResponse.error))
            } else {
                Result.success(rpcResponse.result!!)
            }
            
        } catch (e: Exception) {
            RpcLogger.e("RPC call failed for method: $method", e)
            Result.failure(e)
        }
    }
    
    suspend inline fun <reified T> callAsync(
        method: String,
        params: Map<String, Any>? = null
    ): Result<AsyncJobResult<T>> {
        return call<AsyncJobResult<T>>("${method}_async", params)
    }
    
    suspend inline fun <reified T> pollAsyncResult(jobId: String): Result<T> {
        return call<T>("get_async_result", mapOf("job_id" to jobId))
    }
    
    private fun generateRequestId(): String = UUID.randomUUID().toString()
}

data class RpcRequest(
    val method: String,
    val params: Map<String, Any>? = null,
    val id: String
)

data class RpcResponse<T>(
    val result: T? = null,
    val error: RpcError? = null,
    val id: String
)

data class RpcError(
    val code: Int,
    val message: String,
    val data: Any? = null
)

data class AsyncJobResult<T>(
    val jobId: String,
    val status: JobStatus,
    val result: T? = null,
    val error: String? = null,
    val progress: Float? = null
)

enum class JobStatus {
    PENDING, IN_PROGRESS, COMPLETED, FAILED
}

class RpcException(val error: RpcError) : Exception(error.message)
```

#### RPC Method Registry
```kotlin
// RpcMethodRegistry.kt
object RpcMethodRegistry {
    private val methods = mutableMapOf<String, RpcMethodInfo>()
    
    fun register(name: String, info: RpcMethodInfo) {
        methods[name] = info
    }
    
    fun validate(method: String, params: Map<String, Any>?): ValidationResult {
        val methodInfo = methods[method] 
            ?: return ValidationResult.Error("Unknown RPC method: $method")
        
        return methodInfo.validateParams(params)
    }
    
    fun getMethod(name: String): RpcMethodInfo? = methods[name]
}

data class RpcMethodInfo(
    val name: String,
    val parameters: List<RpcParameter>,
    val returnType: String,
    val isAsync: Boolean = false,
    val timeout: Duration = 30.seconds
) {
    fun validateParams(params: Map<String, Any>?): ValidationResult {
        // Parameter validation logic
        parameters.forEach { param ->
            if (param.required && params?.containsKey(param.name) != true) {
                return ValidationResult.Error("Missing required parameter: ${param.name}")
            }
        }
        return ValidationResult.Success
    }
}

data class RpcParameter(
    val name: String,
    val type: String,
    val required: Boolean = true,
    val description: String? = null
)

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}
```

### Phase 2: Invoice Computation RPC (Week 2)

#### Invoice RPC API Implementation
```kotlin
// InvoiceRpcApi.kt
class InvoiceRpcApi(private val rpcClient: RpcClient) {
    
    suspend fun calculateInvoiceTotal(
        invoiceId: String,
        lineItems: List<LineItem>,
        discounts: List<Discount> = emptyList(),
        taxes: List<TaxRule> = emptyList()
    ): Result<InvoiceCalculation> {
        return rpcClient.call(
            method = "invoice.calculate_total",
            params = mapOf(
                "invoice_id" to invoiceId,
                "line_items" to lineItems,
                "discounts" to discounts,
                "taxes" to taxes
            )
        )
    }
    
    suspend fun calculateTaxes(
        amount: BigDecimal,
        taxJurisdiction: String,
        itemCategories: List<String>
    ): Result<TaxCalculation> {
        return rpcClient.call(
            method = "invoice.calculate_taxes",
            params = mapOf(
                "amount" to amount.toString(),
                "jurisdiction" to taxJurisdiction,
                "categories" to itemCategories
            )
        )
    }
    
    suspend fun applyDiscounts(
        subtotal: BigDecimal,
        discountCodes: List<String>,
        customerTier: String? = null
    ): Result<DiscountCalculation> {
        return rpcClient.call(
            method = "invoice.apply_discounts",
            params = mapOf(
                "subtotal" to subtotal.toString(),
                "discount_codes" to discountCodes,
                "customer_tier" to customerTier
            )
        )
    }
    
    suspend fun convertCurrency(
        amount: BigDecimal,
        fromCurrency: String,
        toCurrency: String,
        effectiveDate: LocalDate? = null
    ): Result<CurrencyConversion> {
        return rpcClient.call(
            method = "invoice.convert_currency",
            params = mapOf(
                "amount" to amount.toString(),
                "from_currency" to fromCurrency,
                "to_currency" to toCurrency,
                "effective_date" to (effectiveDate?.toString() ?: LocalDate.now().toString())
            )
        )
    }
    
    suspend fun validateInvoice(invoiceData: InvoiceData): Result<ValidationResult> {
        return rpcClient.call(
            method = "invoice.validate",
            params = mapOf("invoice_data" to invoiceData)
        )
    }
    
    // Batch operations
    suspend fun batchCalculateInvoices(
        invoiceRequests: List<InvoiceCalculationRequest>
    ): Result<List<InvoiceCalculation>> {
        return rpcClient.call(
            method = "invoice.batch_calculate",
            params = mapOf("requests" to invoiceRequests),
            timeout = 60.seconds // Longer timeout for batch operations
        )
    }
}

data class InvoiceCalculation(
    val invoiceId: String,
    val subtotal: BigDecimal,
    val totalDiscounts: BigDecimal,
    val totalTaxes: BigDecimal,
    val grandTotal: BigDecimal,
    val currency: String,
    val calculations: List<CalculationStep>
)

data class CalculationStep(
    val type: CalculationType,
    val description: String,
    val amount: BigDecimal,
    val rate: Float? = null
)

enum class CalculationType {
    LINE_ITEM, DISCOUNT, TAX, CURRENCY_CONVERSION, ROUNDING
}

data class TaxCalculation(
    val jurisdiction: String,
    val taxableAmount: BigDecimal,
    val totalTax: BigDecimal,
    val taxBreakdown: List<TaxComponent>
)

data class TaxComponent(
    val name: String,
    val rate: Float,
    val taxableAmount: BigDecimal,
    val taxAmount: BigDecimal
)

data class DiscountCalculation(
    val originalAmount: BigDecimal,
    val totalDiscount: BigDecimal,
    val finalAmount: BigDecimal,
    val appliedDiscounts: List<AppliedDiscount>
)

data class AppliedDiscount(
    val code: String,
    val type: DiscountType,
    val value: Float,
    val discountAmount: BigDecimal
)

enum class DiscountType {
    PERCENTAGE, FIXED_AMOUNT, CUSTOMER_TIER
}
```

### Phase 3: Reporting RPC (Week 2-3)

#### Reporting RPC API Implementation
```kotlin
// ReportingRpcApi.kt
class ReportingRpcApi(private val rpcClient: RpcClient) {
    
    suspend fun generateBusinessReport(
        reportType: ReportType,
        dateRange: DateRange,
        filters: ReportFilters = ReportFilters()
    ): Result<AsyncJobResult<BusinessReport>> {
        return rpcClient.callAsync(
            method = "reporting.generate_business_report",
            params = mapOf(
                "report_type" to reportType.name,
                "date_range" to dateRange,
                "filters" to filters
            )
        )
    }
    
    suspend fun getDashboardData(
        userId: String,
        timeframe: Timeframe = Timeframe.LAST_30_DAYS
    ): Result<DashboardData> {
        return rpcClient.call(
            method = "reporting.get_dashboard_data",
            params = mapOf(
                "user_id" to userId,
                "timeframe" to timeframe.name
            )
        )
    }
    
    suspend fun calculateFinancialAnalytics(
        businessId: String,
        analysisType: AnalysisType,
        period: AnalysisPeriod
    ): Result<FinancialAnalytics> {
        return rpcClient.call(
            method = "reporting.calculate_financial_analytics",
            params = mapOf(
                "business_id" to businessId,
                "analysis_type" to analysisType.name,
                "period" to period
            )
        )
    }
    
    suspend fun exportData(
        exportRequest: ExportRequest
    ): Result<AsyncJobResult<ExportResult>> {
        return rpcClient.callAsync(
            method = "reporting.export_data",
            params = mapOf(
                "export_request" to exportRequest
            )
        )
    }
    
    suspend fun getAuditTrail(
        entityType: String,
        entityId: String,
        dateRange: DateRange? = null
    ): Result<List<AuditEntry>> {
        return rpcClient.call(
            method = "reporting.get_audit_trail",
            params = mapOf(
                "entity_type" to entityType,
                "entity_id" to entityId,
                "date_range" to dateRange
            )
        )
    }
}

enum class ReportType {
    SALES_SUMMARY, TAX_REPORT, PROFIT_LOSS, CASH_FLOW, CUSTOMER_ANALYSIS
}

data class DateRange(
    val startDate: LocalDate,
    val endDate: LocalDate
)

data class ReportFilters(
    val customerIds: List<String> = emptyList(),
    val invoiceStatuses: List<String> = emptyList(),
    val categories: List<String> = emptyList(),
    val minimumAmount: BigDecimal? = null,
    val maximumAmount: BigDecimal? = null
)

enum class Timeframe {
    TODAY, LAST_7_DAYS, LAST_30_DAYS, LAST_90_DAYS, LAST_YEAR, CUSTOM
}

data class DashboardData(
    val totalRevenue: BigDecimal,
    val totalInvoices: Int,
    val paidInvoices: Int,
    val overdueInvoices: Int,
    val recentActivity: List<Activity>,
    val performanceMetrics: PerformanceMetrics
)

enum class AnalysisType {
    REVENUE_TRENDS, CUSTOMER_SEGMENTATION, PROFIT_MARGINS, GROWTH_METRICS
}

data class AnalysisPeriod(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val granularity: Granularity = Granularity.MONTHLY
)

enum class Granularity {
    DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY
}
```

### Phase 4: Repository Integration (Week 3)

#### Updated Repository with RPC Integration
```kotlin
// Enhanced BusinessProfileRepositoryImpl with RPC separation
class BusinessProfileRepositoryImpl(
    private val restApi: BusinessProfileRestApi, // CRUD operations
    private val rpcApi: BusinessProfileRpcApi,   // Computations
    private val cache: BusinessProfileCache
) : BusinessProfileRepository {
    
    // REST operations (CRUD)
    override suspend fun getProfile(id: String): Flow<Result<BusinessProfile>> = flow {
        // Use REST API for simple data retrieval
        emit(cache.getProfile(id) ?: restApi.getProfile(id))
    }
    
    override suspend fun updateProfile(profile: BusinessProfile): Result<BusinessProfile> {
        // Use REST API for data updates
        return restApi.updateProfile(profile).also { result ->
            if (result.isSuccess) {
                cache.saveProfile(result.getOrNull()!!)
            }
        }
    }
    
    // RPC operations (computations)
    override suspend fun calculateBusinessMetrics(
        profileId: String,
        period: AnalysisPeriod
    ): Result<BusinessMetrics> {
        // Use RPC API for complex calculations
        return rpcApi.calculateBusinessMetrics(profileId, period)
    }
    
    override suspend fun generateComplianceReport(
        profileId: String,
        reportType: ComplianceReportType
    ): Result<AsyncJobResult<ComplianceReport>> {
        // Use RPC API for heavy report generation
        return rpcApi.generateComplianceReport(profileId, reportType)
    }
    
    override suspend fun validateBusinessData(
        profileData: BusinessProfileData
    ): Result<ValidationResult> {
        // Use RPC API for complex validation logic
        return rpcApi.validateBusinessData(profileData)
    }
}
```

## Dependencies

- **TECH-DEBT-PERF-002**: Network optimization needed for efficient RPC calls
- **TECH-DEBT-ARCH-001**: ViewModels needed to properly handle async RPC operations
- **TECH-DEBT-ARCH-002**: Smart caching needed for RPC result caching

## Related Issues

- TECH-DEBT-PERF-002 (Network optimization - efficient RPC transport)
- TECH-DEBT-BE-001 (Real-time sync - RPC for live computations)
- TECH-DEBT-ARCH-002 (Smart caching - cache RPC results)

## Testing Strategy

### RPC Client Testing
```kotlin
@Test
fun `RPC call handles success response correctly`() = runTest {
    val mockResponse = RpcResponse<String>(
        result = "test_result",
        id = "123"
    )
    
    mockHttpClient.prepareResponse(HttpStatusCode.OK, mockResponse)
    
    val result = rpcClient.call<String>("test_method", mapOf("param" to "value"))
    
    assertTrue(result.isSuccess)
    assertEquals("test_result", result.getOrNull())
}

@Test
fun `RPC call handles error response correctly`() = runTest {
    val mockError = RpcResponse<String>(
        error = RpcError(code = 400, message = "Invalid parameters"),
        id = "123"
    )
    
    mockHttpClient.prepareResponse(HttpStatusCode.OK, mockError)
    
    val result = rpcClient.call<String>("test_method")
    
    assertTrue(result.isFailure)
    assertIs<RpcException>(result.exceptionOrNull())
}
```

### Invoice Calculation Testing
```kotlin
@Test
fun `invoice calculation RPC returns correct totals`() = runTest {
    val lineItems = listOf(
        LineItem(description = "Item 1", quantity = 2, unitPrice = BigDecimal("10.00"))
    )
    
    val expectedCalculation = InvoiceCalculation(
        invoiceId = "INV-001",
        subtotal = BigDecimal("20.00"),
        totalDiscounts = BigDecimal.ZERO,
        totalTaxes = BigDecimal("2.00"),
        grandTotal = BigDecimal("22.00"),
        currency = "USD",
        calculations = emptyList()
    )
    
    mockRpcClient.prepareResponse(expectedCalculation)
    
    val result = invoiceRpcApi.calculateInvoiceTotal("INV-001", lineItems)
    
    assertTrue(result.isSuccess)
    assertEquals(expectedCalculation, result.getOrNull())
}
```

## Success Metrics

- [ ] All heavy computations moved to RPC endpoints
- [ ] Client-side business logic removed (100% backend-driven)
- [ ] RPC call success rate > 99%
- [ ] Average RPC response time < 2 seconds
- [ ] Async job processing operational for long-running tasks
- [ ] Batch processing reduces API calls by 60%

## Definition of Done

- [ ] RPC client infrastructure implemented and tested
- [ ] Invoice computation RPC endpoints integrated
- [ ] Reporting and analytics RPC operational
- [ ] REST vs RPC separation clearly implemented
- [ ] All client-side business logic removed
- [ ] Batch processing capabilities working
- [ ] Async job handling for long-running operations
- [ ] Performance testing validates RPC efficiency
- [ ] Integration testing with backend RPC services
- [ ] Code review completed by backend integration specialist
- [ ] RPC API documentation updated