# TECH-DEBT-PERF-002: Optimize Network Performance

**Status**: Open  
**Priority**: High  
**Domain**: Performance  
**Effort Estimate**: 1-2 weeks  
**Assigned Agent**: Backend Integration Specialist  
**Created**: 2025-01-08  

## Problem Description

The application's network performance significantly exceeds enterprise blueprint requirements. Current network usage is 15-20MB per session compared to the required <5MB, with poor API response times and missing resilience patterns.

**Network Performance Issues**:
- No connection pooling for HTTP requests
- Missing request batching capabilities
- No retry mechanism with exponential backoff  
- No circuit breaker pattern for resilience
- Excessive data transfer due to lack of efficient APIs
- No request/response compression

## Blueprint Violation

**Blueprint Requirement**: "Network Efficiency: < 5MB/session typical usage" and enterprise-grade HTTP client with resilience patterns:
- API Response Time: < 500ms p95
- Network usage: < 5MB per typical session
- Circuit breaker pattern with retry/timeout logic
- HttpRequestRetry with exponential backoff
- Connection pooling and timeout configuration

**Current State**: 15-20MB per session (4x over limit), basic Ktor client without enterprise features

## Affected Files

### Primary Network Components
- `/composeApp/src/commonMain/kotlin/za/co/quantive/app/data/remote/SupabaseClient.kt`
- All API implementation files (`*ApiImpl.kt`)
- Repository implementations with network calls
- `/composeApp/build.gradle.kts` - HTTP client dependencies

### Configuration Files (Need Creation)
- `src/commonMain/kotlin/za/co/quantive/app/core/network/HttpClientFactory.kt`
- `src/commonMain/kotlin/za/co/quantive/app/core/network/NetworkMonitor.kt`
- `src/commonMain/kotlin/za/co/quantive/app/core/network/CircuitBreaker.kt`

## Risk Assessment

- **Performance Risk**: High - 4x excessive network usage
- **User Experience Risk**: High - Slow API responses, poor offline experience
- **Cost Risk**: Medium - Higher bandwidth costs
- **Reliability Risk**: High - No resilience for network failures

## Acceptance Criteria

### Network Efficiency
- [ ] Achieve <5MB per typical user session
- [ ] Reduce API response times to <500ms p95
- [ ] Implement request/response compression
- [ ] Add efficient pagination for large datasets
- [ ] Optimize payload sizes with field selection

### Resilience Patterns
- [ ] Implement circuit breaker pattern (5 failures, 60s recovery)
- [ ] Add retry mechanism with exponential backoff (max 3 retries)
- [ ] Configure proper timeouts (connect: 15s, request: 30s)
- [ ] Add connection pooling for efficient resource usage
- [ ] Implement graceful degradation for network failures

### Monitoring and Analytics
- [ ] Track network usage per session
- [ ] Monitor API response times and success rates
- [ ] Add network failure analytics
- [ ] Implement bandwidth usage reporting
- [ ] Create network performance dashboard

## Implementation Strategy

### Phase 1: Enterprise HTTP Client (Week 1)

#### Enhanced Ktor Client Configuration
```kotlin
// HttpClientFactory.kt
object HttpClientFactory {
    fun createClient(): HttpClient {
        return HttpClient(createHttpEngine()) {
            // Content negotiation with compression
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                    explicitNulls = false
                })
            }
            
            // Request/Response compression
            install(ContentEncoding) {
                gzip(0.9F)
                deflate(1.0F)
            }
            
            // Timeout configuration
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000  // 30 seconds
                connectTimeoutMillis = 15_000  // 15 seconds
                socketTimeoutMillis = 30_000   // 30 seconds
            }
            
            // Retry with exponential backoff
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                retryOnException(maxRetries = 3, retryOnTimeout = true)
                exponentialDelay() // 1s, 2s, 4s delays
                
                modifyRequest { request ->
                    request.headers.append("X-Retry-Count", retryCount.toString())
                }
            }
            
            // Structured logging
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        NetworkLogger.d("HTTP", message)
                    }
                }
                level = if (BuildConfig.DEBUG) LogLevel.INFO else LogLevel.NONE
                
                // Log only essential info in production
                sanitizeHeader { name -> name.lowercase() !in secureHeaders }
            }
            
            // Authentication with token refresh
            install(Auth) {
                bearer {
                    loadTokens {
                        val session = AppServices.authService.getCurrentSession()
                        session?.let {
                            BearerTokens(it.accessToken, it.refreshToken)
                        }
                    }
                    
                    refreshTokens {
                        val refreshed = AppServices.authService.refreshTokens()
                        refreshed?.let {
                            BearerTokens(it.accessToken, it.refreshToken)
                        }
                    }
                }
            }
            
            // Network monitoring
            install(NetworkMonitorPlugin)
            
            // Circuit breaker  
            install(CircuitBreakerPlugin) {
                failureThreshold = 5
                recoveryTimeoutMillis = 60_000
                halfOpenRequestCount = 3
            }
        }
    }
    
    private val secureHeaders = setOf(
        "authorization", "cookie", "set-cookie", 
        "x-api-key", "x-auth-token"
    )
}
```

#### Circuit Breaker Implementation
```kotlin
// CircuitBreaker.kt
class CircuitBreaker(
    private val failureThreshold: Int = 5,
    private val recoveryTimeout: Duration = 60.seconds,
    private val halfOpenRequestCount: Int = 3
) {
    private var state: CircuitBreakerState = CircuitBreakerState.Closed
    private var failureCount = 0
    private var lastFailureTime = 0L
    private var halfOpenRequests = 0
    
    suspend fun <T> execute(operation: suspend () -> T): T {
        when (state) {
            CircuitBreakerState.Open -> {
                if (shouldAttemptReset()) {
                    state = CircuitBreakerState.HalfOpen
                    halfOpenRequests = 0
                } else {
                    throw CircuitBreakerException("Circuit breaker is OPEN")
                }
            }
            CircuitBreakerState.HalfOpen -> {
                if (halfOpenRequests >= halfOpenRequestCount) {
                    throw CircuitBreakerException("Circuit breaker is HALF_OPEN with max requests")
                }
                halfOpenRequests++
            }
            CircuitBreakerState.Closed -> {
                // Allow request
            }
        }
        
        return try {
            val result = operation()
            onSuccess()
            result
        } catch (e: Exception) {
            onFailure()
            throw e
        }
    }
    
    private fun shouldAttemptReset(): Boolean {
        return (Clock.System.now().toEpochMilliseconds() - lastFailureTime) >= recoveryTimeout.inWholeMilliseconds
    }
    
    private fun onSuccess() {
        when (state) {
            CircuitBreakerState.HalfOpen -> {
                if (halfOpenRequests >= halfOpenRequestCount) {
                    state = CircuitBreakerState.Closed
                    failureCount = 0
                }
            }
            CircuitBreakerState.Closed -> {
                failureCount = 0
            }
            CircuitBreakerState.Open -> { /* Should not happen */ }
        }
    }
    
    private fun onFailure() {
        failureCount++
        lastFailureTime = Clock.System.now().toEpochMilliseconds()
        
        when (state) {
            CircuitBreakerState.HalfOpen -> {
                state = CircuitBreakerState.Open
            }
            CircuitBreakerState.Closed -> {
                if (failureCount >= failureThreshold) {
                    state = CircuitBreakerState.Open
                }
            }
            CircuitBreakerState.Open -> { /* Already open */ }
        }
    }
}

enum class CircuitBreakerState {
    Closed, Open, HalfOpen
}

class CircuitBreakerException(message: String) : Exception(message)
```

### Phase 2: API Optimization (Week 1-2)

#### Request Batching
```kotlin
class BatchRequestManager {
    private val pendingRequests = mutableMapOf<String, MutableList<BatchRequest>>()
    private val batchSize = 10
    private val batchTimeout = 100.milliseconds
    
    suspend fun <T> batchRequest(
        endpoint: String,
        request: Any,
        responseType: KClass<T>
    ): T {
        val batchRequest = BatchRequest(request, CompletableDeferred<T>())
        
        synchronized(pendingRequests) {
            pendingRequests.getOrPut(endpoint) { mutableListOf() }.add(batchRequest)
            
            if (pendingRequests[endpoint]!!.size >= batchSize) {
                processBatch(endpoint)
            } else {
                // Schedule batch processing
                CoroutineScope(Dispatchers.Default).launch {
                    delay(batchTimeout)
                    synchronized(pendingRequests) {
                        if (pendingRequests[endpoint]?.isNotEmpty() == true) {
                            processBatch(endpoint)
                        }
                    }
                }
            }
        }
        
        return batchRequest.response.await()
    }
    
    private suspend fun processBatch(endpoint: String) {
        val requests = pendingRequests.remove(endpoint) ?: return
        
        try {
            val batchResponse = httpClient.post("$endpoint/batch") {
                setBody(requests.map { it.request })
            }
            
            // Process batch response and complete individual requests
            val responses: List<Any> = batchResponse.body()
            requests.forEachIndexed { index, request ->
                request.response.complete(responses[index] as T)
            }
        } catch (e: Exception) {
            requests.forEach { it.response.completeExceptionally(e) }
        }
    }
}

data class BatchRequest(
    val request: Any,
    val response: CompletableDeferred<Any>
)
```

#### Efficient Pagination
```kotlin
class PaginationOptimizer {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
        const val MAX_PAGE_SIZE = 100
        const val PREFETCH_THRESHOLD = 0.8 // Prefetch when 80% through current page
    }
    
    suspend fun <T> getPaginatedData(
        api: suspend (page: Int, size: Int) -> PaginatedResponse<T>,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        prefetchNext: Boolean = true
    ): Flow<List<T>> = flow {
        var currentPage = 0
        var hasMoreData = true
        val allData = mutableListOf<T>()
        
        while (hasMoreData) {
            val response = api(currentPage, pageSize)
            
            allData.addAll(response.data)
            emit(allData.toList())
            
            hasMoreData = response.hasMore
            
            if (prefetchNext && hasMoreData && shouldPrefetch(allData.size, pageSize)) {
                // Prefetch next page in background
                launch {
                    try {
                        api(currentPage + 1, pageSize)
                        // Cache the prefetched data
                    } catch (e: Exception) {
                        NetworkLogger.w("Prefetch failed", e)
                    }
                }
            }
            
            currentPage++
        }
    }
    
    private fun shouldPrefetch(currentItems: Int, pageSize: Int): Boolean {
        val currentPageItems = currentItems % pageSize
        return currentPageItems >= (pageSize * PREFETCH_THRESHOLD)
    }
}
```

### Phase 3: Payload Optimization

#### Field Selection for APIs
```kotlin
// Optimize API responses with field selection
interface OptimizedInvoiceApi {
    suspend fun getInvoices(
        fields: Set<InvoiceField> = InvoiceField.SUMMARY_FIELDS,
        page: Int = 0,
        limit: Int = 20
    ): ApiResponse<PaginatedResponse<Invoice>>
}

enum class InvoiceField(val apiName: String) {
    ID("id"),
    NUMBER("number"), 
    AMOUNT("amount"),
    STATUS("status"),
    DUE_DATE("due_date"),
    CUSTOMER_NAME("customer.name"),
    // Full details only when needed
    LINE_ITEMS("line_items"),
    CALCULATIONS("calculations"),
    AUDIT_LOG("audit_log");
    
    companion object {
        val SUMMARY_FIELDS = setOf(ID, NUMBER, AMOUNT, STATUS, DUE_DATE, CUSTOMER_NAME)
        val DETAIL_FIELDS = values().toSet()
    }
}
```

#### Response Compression  
```kotlin
class PayloadOptimizer {
    fun optimizeInvoiceList(invoices: List<Invoice>): List<InvoiceSummary> {
        return invoices.map { invoice ->
            InvoiceSummary(
                id = invoice.id,
                number = invoice.number,
                amount = invoice.total.amount,
                status = invoice.status,
                customerName = invoice.customer.name,
                dueDate = invoice.dueDate
            )
        }
    }
    
    fun calculatePayloadSize(data: Any): Long {
        val json = Json.encodeToString(data)
        return json.toByteArray(Charsets.UTF_8).size.toLong()
    }
    
    fun shouldUseFullPayload(summarySize: Long, detailSize: Long): Boolean {
        // Use full payload if difference is < 30%
        return (detailSize - summarySize) / summarySize.toDouble() < 0.3
    }
}
```

### Phase 4: Network Monitoring

#### Network Usage Tracking
```kotlin
class NetworkMonitor {
    private var sessionStartTime = Clock.System.now()
    private var totalBytesTransferred = 0L
    private val apiCallMetrics = mutableListOf<ApiCallMetric>()
    
    fun trackRequest(url: String, requestSize: Long, responseSize: Long, duration: Long) {
        totalBytesTransferred += requestSize + responseSize
        
        apiCallMetrics.add(
            ApiCallMetric(
                url = url.removeAuthParams(),
                requestSize = requestSize,
                responseSize = responseSize,  
                duration = duration,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
        )
        
        // Check if exceeding session limit
        if (totalBytesTransferred > 5 * 1024 * 1024) { // 5MB threshold
            NetworkLogger.w("Network usage exceeded: ${totalBytesTransferred / 1024 / 1024}MB")
            
            // Report to analytics
            AnalyticsService.track("network_usage_exceeded", mapOf(
                "total_mb" to totalBytesTransferred / 1024 / 1024,
                "session_duration_min" to getSessionDurationMinutes()
            ))
        }
    }
    
    fun getNetworkStats(): NetworkStats {
        return NetworkStats(
            sessionDurationMs = Clock.System.now() - sessionStartTime,
            totalBytesTransferred = totalBytesTransferred,
            apiCallCount = apiCallMetrics.size,
            averageResponseTime = apiCallMetrics.map { it.duration }.average(),
            largestResponse = apiCallMetrics.maxByOrNull { it.responseSize }?.responseSize ?: 0L
        )
    }
    
    fun reset() {
        sessionStartTime = Clock.System.now()
        totalBytesTransferred = 0L
        apiCallMetrics.clear()
    }
}

data class ApiCallMetric(
    val url: String,
    val requestSize: Long,
    val responseSize: Long,
    val duration: Long,
    val timestamp: Long
)

data class NetworkStats(
    val sessionDurationMs: Long,
    val totalBytesTransferred: Long,
    val apiCallCount: Int,
    val averageResponseTime: Double,
    val largestResponse: Long
)
```

## Dependencies

- **TECH-DEBT-ARCH-002**: Smart caching reduces network calls
- **TECH-DEBT-SEC-002**: Certificate pinning works with enhanced HTTP client
- **TECH-DEBT-SEC-003**: Structured logging needed for network monitoring

## Related Issues

- TECH-DEBT-ARCH-002 (Smart caching - reduces network load)
- TECH-DEBT-BE-001 (Real-time sync - needs efficient network layer)
- TECH-DEBT-PERF-001 (Cold start - depends on faster network initialization)

## Testing Strategy

### Network Performance Testing
```kotlin
@Test
fun `network usage stays under 5MB per session`() = runTest {
    val networkMonitor = NetworkMonitor()
    
    // Simulate typical user session
    performTypicalUserWorkflow()
    
    val stats = networkMonitor.getNetworkStats()
    val mbUsed = stats.totalBytesTransferred / 1024.0 / 1024.0
    
    assertTrue(
        "Network usage ${mbUsed}MB exceeds 5MB limit",
        mbUsed < 5.0
    )
}

@Test
fun `API response times under 500ms p95`() = runTest {
    val responseTimes = mutableListOf<Long>()
    
    // Make 100 API calls
    repeat(100) {
        val startTime = System.currentTimeMillis()
        makeApiCall()
        val responseTime = System.currentTimeMillis() - startTime
        responseTimes.add(responseTime)
    }
    
    val p95 = responseTimes.sorted()[95]
    assertTrue(
        "P95 response time ${p95}ms exceeds 500ms",
        p95 < 500
    )
}
```

### Circuit Breaker Testing
```kotlin
@Test
fun `circuit breaker opens after threshold failures`() = runTest {
    val circuitBreaker = CircuitBreaker(failureThreshold = 3)
    
    // Trigger failures
    repeat(3) {
        assertFailsWith<Exception> {
            circuitBreaker.execute { throw Exception("Simulated failure") }
        }
    }
    
    // Circuit should be open
    assertFailsWith<CircuitBreakerException> {
        circuitBreaker.execute { "success" }
    }
}
```

## Success Metrics

- [ ] Network usage < 5MB per typical user session
- [ ] API response times < 500ms p95
- [ ] Circuit breaker prevents cascading failures
- [ ] Request retry success rate > 90%
- [ ] Connection pooling reduces connection overhead by 30%
- [ ] Batch requests reduce API calls by 40%

## Definition of Done

- [ ] Network usage meets <5MB per session requirement
- [ ] Enterprise HTTP client with all resilience patterns
- [ ] Circuit breaker and retry mechanisms operational
- [ ] Request batching implemented for high-frequency calls
- [ ] Payload optimization reducing data transfer by 50%
- [ ] Network monitoring and alerting functional
- [ ] Performance testing validates all metrics
- [ ] Integration testing with backend systems
- [ ] Code review completed by backend integration specialist
- [ ] Network performance documentation updated