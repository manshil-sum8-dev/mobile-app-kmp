# TECH-DEBT-BE-001: Implement Real-time Synchronization

**Status**: Open  
**Priority**: High  
**Domain**: Backend Integration  
**Effort Estimate**: 2-4 weeks  
**Assigned Agent**: Backend Integration Specialist  
**Created**: 2025-01-08  

## Problem Description

The application lacks real-time synchronization capabilities required by the enterprise blueprint. All data updates require manual refresh, creating poor user experience and stale data issues in multi-user scenarios.

**Real-time Sync Issues**:
- No WebSocket connections for live updates
- No server-sent events (SSE) implementation  
- Missing real-time cache invalidation
- No collaborative editing capabilities
- Manual refresh required for data updates
- No live notifications for business events

## Blueprint Violation

**Blueprint Requirement**: "Real-time Sync: WebSocket/SSE for live updates" as part of backend-driven architecture:
- WebSocket/SSE for live updates
- Server-pushed cache invalidation  
- Real-time data synchronization
- Live collaborative features
- Automatic data refresh capabilities

**Current State**: No real-time capabilities, manual refresh only

## Affected Files

### Supabase Integration (Need Enhancement)
- `/composeApp/src/commonMain/kotlin/za/co/quantive/app/data/remote/SupabaseClient.kt`
- All repository implementations (need real-time subscriptions)

### Files to Create
- `src/commonMain/kotlin/za/co/quantive/app/core/realtime/RealtimeManager.kt`
- `src/commonMain/kotlin/za/co/quantive/app/core/realtime/SubscriptionManager.kt` 
- `src/commonMain/kotlin/za/co/quantive/app/core/realtime/RealtimeEvents.kt`
- `src/commonMain/kotlin/za/co/quantive/app/core/sync/DataSyncEngine.kt`

### Files to Update
- All `*Repository.kt` files for real-time data flows
- Cache implementations for real-time invalidation
- ViewModels for live data subscriptions

## Risk Assessment

- **User Experience Risk**: High - Stale data, manual refresh burden
- **Collaboration Risk**: High - Multiple users can conflict without real-time sync  
- **Business Logic Risk**: Medium - Important business events may be missed
- **Competitive Risk**: Medium - Modern apps expect real-time features

## Acceptance Criteria

### Real-time Infrastructure
- [ ] Implement Supabase Realtime WebSocket connection
- [ ] Add subscription management for database changes
- [ ] Create real-time event handling system
- [ ] Implement connection state management with reconnection logic
- [ ] Add real-time authentication and authorization

### Data Synchronization
- [ ] Real-time invoice updates (creation, modification, payments)
- [ ] Live contact/customer updates
- [ ] Real-time business profile changes
- [ ] Collaborative editing conflict resolution
- [ ] Real-time notification system

### Cache Integration
- [ ] Server-pushed cache invalidation
- [ ] Real-time cache updates
- [ ] Optimistic updates with rollback
- [ ] Cache consistency across real-time updates
- [ ] Conflict resolution for cached data

### Performance Optimization
- [ ] Selective subscriptions to reduce bandwidth
- [ ] Message batching for high-frequency updates
- [ ] Connection pooling and resource management
- [ ] Real-time performance monitoring

## Implementation Strategy

### Phase 1: Realtime Infrastructure (Weeks 1-2)

#### Supabase Realtime Integration
```kotlin
// RealtimeManager.kt
class RealtimeManager(private val supabase: SupabaseClient) {
    private val subscriptions = mutableMapOf<String, RealtimeSubscription>()
    private val _connectionState = MutableStateFlow(RealtimeConnectionState.Disconnected)
    val connectionState = _connectionState.asStateFlow()
    
    suspend fun initialize() {
        try {
            _connectionState.value = RealtimeConnectionState.Connecting
            
            // Initialize Supabase Realtime
            supabase.realtime.connect()
            
            _connectionState.value = RealtimeConnectionState.Connected
            RealtimeLogger.d("Realtime connection established")
            
        } catch (e: Exception) {
            _connectionState.value = RealtimeConnectionState.Error(e.message ?: "Connection failed")
            RealtimeLogger.e("Realtime connection failed", e)
            
            // Retry connection
            retryConnection()
        }
    }
    
    fun <T> subscribeToTable(
        table: String,
        filter: String? = null,
        eventHandler: suspend (RealtimeEvent<T>) -> Unit
    ): String {
        val subscriptionId = UUID.randomUUID().toString()
        
        val subscription = supabase.realtime
            .from(table)
            .apply {
                filter?.let { on(PostgresAction.ALL, it) }
            }
            .subscribe { payload ->
                CoroutineScope(Dispatchers.Default).launch {
                    try {
                        val event = parseRealtimeEvent<T>(payload)
                        eventHandler(event)
                    } catch (e: Exception) {
                        RealtimeLogger.e("Failed to handle realtime event", e)
                    }
                }
            }
            
        subscriptions[subscriptionId] = RealtimeSubscription(table, subscription)
        return subscriptionId
    }
    
    fun unsubscribe(subscriptionId: String) {
        subscriptions.remove(subscriptionId)?.let { sub ->
            supabase.realtime.removeSubscription(sub.subscription)
            RealtimeLogger.d("Unsubscribed from ${sub.table}")
        }
    }
    
    private suspend fun retryConnection() {
        var retryCount = 0
        val maxRetries = 5
        
        while (retryCount < maxRetries && _connectionState.value is RealtimeConnectionState.Error) {
            delay((retryCount + 1) * 2000L) // Exponential backoff
            
            try {
                initialize()
                break
            } catch (e: Exception) {
                retryCount++
                RealtimeLogger.w("Retry ${retryCount} failed", e)
            }
        }
    }
}

sealed class RealtimeConnectionState {
    object Disconnected : RealtimeConnectionState()
    object Connecting : RealtimeConnectionState()
    object Connected : RealtimeConnectionState()
    data class Error(val message: String) : RealtimeConnectionState()
}

data class RealtimeSubscription(
    val table: String,
    val subscription: RealtimeChannel
)
```

#### Real-time Event System
```kotlin
// RealtimeEvents.kt
sealed class RealtimeEvent<T> {
    data class Insert<T>(val new: T) : RealtimeEvent<T>()
    data class Update<T>(val old: T?, val new: T) : RealtimeEvent<T>()  
    data class Delete<T>(val old: T) : RealtimeEvent<T>()
    data class Error<T>(val message: String, val cause: Throwable?) : RealtimeEvent<T>()
}

class RealtimeEventHandler {
    suspend fun handleInvoiceEvent(event: RealtimeEvent<Invoice>) {
        when (event) {
            is RealtimeEvent.Insert -> {
                RealtimeLogger.d("New invoice created: ${event.new.id}")
                
                // Update local cache
                AppServices.cacheManager.invalidatePattern("invoices_*")
                
                // Notify UI
                EventBus.emit(InvoiceCreatedEvent(event.new))
                
                // Show notification
                NotificationService.showInvoiceNotification(
                    "New invoice created", 
                    "Invoice ${event.new.number} has been created"
                )
            }
            
            is RealtimeEvent.Update -> {
                RealtimeLogger.d("Invoice updated: ${event.new.id}")
                
                // Update specific cache entry
                AppServices.cacheManager.updateInvoice(event.new)
                
                // Check for important changes
                if (event.old?.status != event.new.status) {
                    NotificationService.showInvoiceStatusUpdate(event.new)
                }
                
                EventBus.emit(InvoiceUpdatedEvent(event.old, event.new))
            }
            
            is RealtimeEvent.Delete -> {
                RealtimeLogger.d("Invoice deleted: ${event.old.id}")
                
                AppServices.cacheManager.removeInvoice(event.old.id)
                EventBus.emit(InvoiceDeletedEvent(event.old))
            }
            
            is RealtimeEvent.Error -> {
                RealtimeLogger.e("Invoice realtime error", event.cause)
            }
        }
    }
}
```

### Phase 2: Repository Integration (Weeks 2-3)

#### Real-time Repository Pattern
```kotlin
// Enhanced BackendInvoiceRepository with real-time
class BackendInvoiceRepository(
    private val api: InvoiceApi,
    private val cache: InvoiceCache,
    private val realtimeManager: RealtimeManager
) : InvoiceRepository {
    
    private var realtimeSubscriptionId: String? = null
    
    override suspend fun getInvoices(
        filter: InvoiceFilter?,
        forceRefresh: Boolean
    ): Flow<Result<List<Invoice>>> = flow {
        // Setup real-time subscription if not already active
        setupRealtimeSubscription(filter)
        
        try {
            // Emit cached data first
            if (!forceRefresh) {
                val cachedInvoices = cache.getInvoices(filter)
                if (cachedInvoices.isNotEmpty()) {
                    emit(Result.success(cachedInvoices))
                }
            }
            
            // Fetch fresh data from backend
            val response = api.getInvoices(
                page = 0,
                limit = 100,
                filter = filter
            )
            
            if (response.isSuccess()) {
                val invoices = response.data!!.data
                cache.saveInvoices(invoices)
                emit(Result.success(invoices))
            } else {
                emit(Result.failure(Exception(response.message)))
            }
            
        } catch (e: Exception) {
            // Fallback to cache
            val cachedInvoices = cache.getInvoices(filter)
            if (cachedInvoices.isNotEmpty()) {
                emit(Result.success(cachedInvoices))
            } else {
                emit(Result.failure(e))
            }
        }
    }.shareIn(
        scope = CoroutineScope(Dispatchers.Default),
        started = SharingStarted.WhileSubscribed(5000),
        replay = 1
    )
    
    private suspend fun setupRealtimeSubscription(filter: InvoiceFilter?) {
        if (realtimeSubscriptionId != null) return
        
        val filterClause = buildFilterClause(filter)
        
        realtimeSubscriptionId = realtimeManager.subscribeToTable<Invoice>(
            table = "invoices",
            filter = filterClause
        ) { event ->
            handleRealtimeInvoiceEvent(event)
        }
    }
    
    private suspend fun handleRealtimeInvoiceEvent(event: RealtimeEvent<Invoice>) {
        when (event) {
            is RealtimeEvent.Insert -> {
                cache.saveInvoice(event.new)
                // Emit updated list to active subscribers
                _invoiceUpdates.emit(InvoiceUpdate.Created(event.new))
            }
            
            is RealtimeEvent.Update -> {
                cache.saveInvoice(event.new)
                _invoiceUpdates.emit(InvoiceUpdate.Modified(event.old, event.new))
            }
            
            is RealtimeEvent.Delete -> {
                cache.deleteInvoice(event.old.id)
                _invoiceUpdates.emit(InvoiceUpdate.Deleted(event.old))
            }
            
            is RealtimeEvent.Error -> {
                RealtimeLogger.e("Invoice realtime error", event.cause)
            }
        }
    }
    
    // Expose real-time updates as a Flow
    private val _invoiceUpdates = MutableSharedFlow<InvoiceUpdate>()
    val invoiceUpdates: SharedFlow<InvoiceUpdate> = _invoiceUpdates.asSharedFlow()
    
    override suspend fun cleanup() {
        realtimeSubscriptionId?.let {
            realtimeManager.unsubscribe(it)
            realtimeSubscriptionId = null
        }
    }
}

sealed class InvoiceUpdate {
    data class Created(val invoice: Invoice) : InvoiceUpdate()
    data class Modified(val old: Invoice?, val new: Invoice) : InvoiceUpdate()
    data class Deleted(val invoice: Invoice) : InvoiceUpdate()
}
```

### Phase 3: ViewModel Integration (Weeks 3-4)

#### Real-time ViewModel Pattern  
```kotlin
// Enhanced InvoiceListViewModel with real-time updates
class InvoiceListViewModel(
    private val repository: BackendInvoiceRepository
) : BaseViewModel() {
    
    override fun createInitialState() = InvoiceListUiState.Loading
    
    private var realtimeJob: Job? = null
    
    override fun handleIntent(intent: InvoiceListIntent) {
        when (intent) {
            is InvoiceListIntent.LoadData -> loadInvoices()
            is InvoiceListIntent.EnableRealtime -> enableRealtimeUpdates()
            is InvoiceListIntent.DisableRealtime -> disableRealtimeUpdates()
        }
    }
    
    private fun loadInvoices() {
        viewModelScope.launch {
            repository.getInvoices()
                .collect { result ->
                    updateState {
                        when {
                            result.isSuccess -> InvoiceListUiState.Success(
                                invoices = result.getOrNull() ?: emptyList(),
                                isRealtimeActive = realtimeJob?.isActive == true
                            )
                            else -> InvoiceListUiState.Error(
                                result.exceptionOrNull()?.message ?: "Unknown error"
                            )
                        }
                    }
                }
        }
    }
    
    private fun enableRealtimeUpdates() {
        realtimeJob?.cancel()
        realtimeJob = viewModelScope.launch {
            repository.invoiceUpdates.collect { update ->
                handleRealtimeUpdate(update)
            }
        }
        
        // Update UI state to show real-time is active
        val currentState = uiState.value
        if (currentState is InvoiceListUiState.Success) {
            updateState {
                currentState.copy(isRealtimeActive = true)
            }
        }
    }
    
    private fun handleRealtimeUpdate(update: InvoiceUpdate) {
        val currentState = uiState.value
        if (currentState !is InvoiceListUiState.Success) return
        
        val updatedInvoices = when (update) {
            is InvoiceUpdate.Created -> {
                currentState.invoices + update.invoice
            }
            is InvoiceUpdate.Modified -> {
                currentState.invoices.map { invoice ->
                    if (invoice.id == update.new.id) update.new else invoice
                }
            }
            is InvoiceUpdate.Deleted -> {
                currentState.invoices.filter { it.id != update.invoice.id }
            }
        }
        
        updateState {
            currentState.copy(invoices = updatedInvoices)
        }
        
        // Show subtle notification for updates
        when (update) {
            is InvoiceUpdate.Created -> {
                sendEvent(InvoiceListUiEvent.ShowSnackbar("New invoice created"))
            }
            is InvoiceUpdate.Modified -> {
                sendEvent(InvoiceListUiEvent.ShowSnackbar("Invoice ${update.new.number} updated"))
            }
            is InvoiceUpdate.Deleted -> {
                sendEvent(InvoiceListUiEvent.ShowSnackbar("Invoice deleted"))
            }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        disableRealtimeUpdates()
    }
    
    private fun disableRealtimeUpdates() {
        realtimeJob?.cancel()
        realtimeJob = null
        
        val currentState = uiState.value
        if (currentState is InvoiceListUiState.Success) {
            updateState {
                currentState.copy(isRealtimeActive = false)
            }
        }
    }
}

data class InvoiceListUiState(
    val invoices: List<Invoice> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRealtimeActive: Boolean = false
) : UiState

sealed class InvoiceListIntent : Intent {
    object LoadData : InvoiceListIntent()
    object EnableRealtime : InvoiceListIntent()
    object DisableRealtime : InvoiceListIntent()
    object Refresh : InvoiceListIntent()
}
```

### Phase 4: Performance Optimization

#### Connection Management
```kotlin
class RealtimeConnectionManager {
    private var activeSubscriptions = 0
    private val maxSubscriptions = 10
    private val subscriptionPriorities = mutableMapOf<String, Int>()
    
    fun requestSubscription(
        subscriptionId: String,
        priority: SubscriptionPriority
    ): Boolean {
        if (activeSubscriptions >= maxSubscriptions) {
            // Check if we can remove lower priority subscription
            val lowestPriority = subscriptionPriorities.minByOrNull { it.value }
            
            if (lowestPriority != null && lowestPriority.value < priority.value) {
                removeSubscription(lowestPriority.key)
            } else {
                return false // Cannot add subscription
            }
        }
        
        subscriptionPriorities[subscriptionId] = priority.value
        activeSubscriptions++
        return true
    }
    
    private fun removeSubscription(subscriptionId: String) {
        subscriptionPriorities.remove(subscriptionId)
        activeSubscriptions--
        
        RealtimeManager.unsubscribe(subscriptionId)
    }
}

enum class SubscriptionPriority(val value: Int) {
    LOW(1),
    MEDIUM(2), 
    HIGH(3),
    CRITICAL(4)
}
```

## Dependencies

- **TECH-DEBT-ARCH-002**: Smart caching needed for real-time cache invalidation
- **TECH-DEBT-PERF-002**: Network optimization needed for efficient real-time connections
- **TECH-DEBT-ARCH-001**: ViewModels needed for proper real-time state management

## Related Issues

- TECH-DEBT-ARCH-002 (Smart caching - cache invalidation via real-time)
- TECH-DEBT-PERF-002 (Network optimization - efficient WebSocket connections)
- TECH-DEBT-ARCH-001 (ViewModel architecture - real-time state management)

## Testing Strategy

### Real-time Testing
```kotlin
@Test
fun `real-time invoice updates reflected in UI`() = runTest {
    val viewModel = InvoiceListViewModel(mockRepository)
    val testFlow = viewModel.uiState.test()
    
    // Load initial data
    viewModel.handleIntent(InvoiceListIntent.LoadData)
    viewModel.handleIntent(InvoiceListIntent.EnableRealtime)
    
    // Simulate real-time update
    val newInvoice = Invoice(id = "new", number = "INV-001")
    mockRepository.simulateRealtimeUpdate(InvoiceUpdate.Created(newInvoice))
    
    // Verify UI state updated
    val finalState = testFlow.awaitItem() as InvoiceListUiState.Success
    assertTrue(finalState.invoices.contains(newInvoice))
}
```

### Connection Testing
```kotlin
@Test
fun `real-time connection recovers from failure`() = runTest {
    val realtimeManager = RealtimeManager(mockSupabase)
    
    // Simulate connection failure
    mockSupabase.simulateConnectionFailure()
    
    realtimeManager.initialize()
    
    // Wait for retry
    delay(5000)
    
    // Verify connection recovered
    assertEquals(
        RealtimeConnectionState.Connected,
        realtimeManager.connectionState.value
    )
}
```

## Success Metrics

- [ ] Real-time updates delivered within 2 seconds
- [ ] 99.9% real-time message delivery success rate
- [ ] Connection recovery within 10 seconds of failure
- [ ] Real-time subscriptions use <1MB additional bandwidth per hour
- [ ] UI updates smoothly without flickering during real-time changes
- [ ] Collaborative conflicts resolved automatically 90%+ of the time

## Definition of Done

- [ ] Supabase Realtime WebSocket integration operational
- [ ] Real-time subscriptions for all major data types
- [ ] Server-pushed cache invalidation working
- [ ] Real-time UI updates without manual refresh
- [ ] Connection management with reconnection logic
- [ ] Performance testing validates bandwidth usage
- [ ] Collaborative editing conflict resolution
- [ ] Real-time notification system functional
- [ ] Integration testing with live backend
- [ ] Code review completed by backend integration specialist
- [ ] Real-time monitoring and alerting configured