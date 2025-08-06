# Quantive KMP Development Standards & Guidelines - Material 3 Expressive Edition

## Table of Contents
1. [Architecture Principles](#architecture-principles)
2. [Material 3 Expressive Design Standards](#material-3-expressive-design-standards)
3. [Design System Standards](#design-system-standards)
4. [Backend Integration Patterns](#backend-integration-patterns)
5. [Code Organization & Structure](#code-organization--structure)
6. [Development Workflows](#development-workflows)
7. [Performance Standards](#performance-standards)
8. [Testing Requirements](#testing-requirements)
9. [Accessibility Guidelines](#accessibility-guidelines)

## Architecture Principles

### 1. Clean Architecture + MVI Pattern
```
┌─────────────────────────────────────┐
│              Presentation           │
│  ┌─────────────────────────────────┐│
│  │         Compose UI              ││
│  │  ┌─────────────────────────────┐││
│  │  │        ViewModel            │││
│  │  │  (MVI State Management)     │││
│  │  └─────────────────────────────┘││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
┌─────────────────────────────────────┐
│               Domain                │
│  ┌─────────────────────────────────┐│
│  │        Use Cases                ││
│  │  ┌─────────────────────────────┐││
│  │  │      Entities               │││
│  │  │   Repository Interfaces     │││
│  │  └─────────────────────────────┘││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
┌─────────────────────────────────────┐
│                Data                 │
│  ┌─────────────────────────────────┐│
│  │    Repository Implementations   ││
│  │  ┌─────────────────────────────┐││
│  │  │   Remote    │   Local       │││
│  │  │ Data Source │ Data Source   │││
│  │  └─────────────────────────────┘││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
```

### 2. Backend-Logic First Approach
- **Domain Logic Priority**: Business rules implemented in domain layer first
- **API as Implementation Detail**: Backend serves domain requirements, not UI
- **Offline-First**: Local state is source of truth, sync with backend
- **Event-Driven**: Use domain events for cross-boundary communication

## Material 3 Expressive Design Standards

### 1. Expressive Design Principles

**Research-Backed Design**:
- Material 3 Expressive is Google's most rigorously researched design refresh
- Based on 46 global studies, hundreds of design variations, and insights from 18,000+ participants
- Proven to improve usability with users identifying key UI elements up to 4x faster

**Emotional Engagement**:
- Designs should be playful, creative, energetic, and friendly
- Focus on delight and emotional connection with users
- Use expressive elements without compromising functionality

### 2. New Component Requirements

**Button Groups**:
```kotlin
@Composable
fun QuantiveExpressiveButtonGroup(
    buttons: List<ButtonGroupItem>,
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ButtonGroup(
        modifier = modifier,
        // Shape morphing on touch
        morphingEnabled = true,
        // Connected button shapes
        shapes = ButtonGroupDefaults.connectedShapes(),
        spacing = ButtonGroupDefaults.spacing()
    ) {
        buttons.forEachIndexed { index, item ->
            QuantiveExpressiveButton(
                text = item.text,
                onClick = { onSelectionChange(index) },
                selected = selectedIndex == index,
                morphOnTouch = true
            )
        }
    }
}
```

**Split Button Implementation**:
```kotlin
@Composable
fun QuantiveSplitButton(
    primaryText: String,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    SplitButtonLayout(
        modifier = modifier,
        leadingButton = {
            SplitButtonDefaults.LeadingButton(
                onClick = onPrimaryClick,
                enabled = enabled
            ) {
                Text(primaryText)
            }
        },
        trailingButton = {
            SplitButtonDefaults.TrailingButton(
                onClick = onSecondaryClick,
                enabled = enabled
            ) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }
    )
}
```

### 3. Enhanced Motion System

**Spring-Based Animations**:
```kotlin
object QuantiveMotionTokens {
    // Expressive spring animations
    val SpringBouncy = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )
    
    val SpringSmooth = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )
    
    // Enhanced duration tokens
    const val ExpressiveShort = 300
    const val ExpressiveMedium = 500
    const val ExpressiveLong = 700
}

@Composable
fun QuantiveExpressiveTransition(
    targetState: Boolean,
    content: @Composable (Boolean) -> Unit
) {
    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            slideInHorizontally(
                animationSpec = QuantiveMotionTokens.SpringBouncy,
                initialOffsetX = { it / 3 }
            ) with slideOutHorizontally(
                animationSpec = QuantiveMotionTokens.SpringSmooth,
                targetOffsetX = { -it / 3 }
            )
        }
    ) { state ->
        content(state)
    }
}
```

### 4. Shape Morphing System

**Dynamic Shape Tokens**:
```kotlin
object QuantiveExpressiveShapes {
    // Shape morphing for interactive elements
    fun morphingButton(pressed: Boolean) = if (pressed) {
        RoundedCornerShape(QuantiveDesignTokens.Radius.Large)
    } else {
        RoundedCornerShape(QuantiveDesignTokens.Radius.Medium)
    }
    
    fun morphingCard(hovered: Boolean) = if (hovered) {
        RoundedCornerShape(QuantiveDesignTokens.Radius.XLarge)
    } else {
        RoundedCornerShape(QuantiveDesignTokens.Radius.Medium)
    }
    
    // Connected shapes for button groups
    val ConnectedStart = RoundedCornerShape(
        topStart = QuantiveDesignTokens.Radius.Medium,
        bottomStart = QuantiveDesignTokens.Radius.Medium,
        topEnd = 0.dp,
        bottomEnd = 0.dp
    )
    
    val ConnectedMiddle = RoundedCornerShape(0.dp)
    
    val ConnectedEnd = RoundedCornerShape(
        topStart = 0.dp,
        bottomStart = 0.dp,
        topEnd = QuantiveDesignTokens.Radius.Medium,
        bottomEnd = QuantiveDesignTokens.Radius.Medium
    )
}
```

### 5. Loading and Progress Indicators

**Expressive Loading Components**:
```kotlin
@Composable
fun QuantiveExpressiveLoadingIndicator(
    progress: Float? = null, // null for indeterminate
    modifier: Modifier = Modifier,
    message: String? = null
) {
    if (progress != null) {
        // Determinate progress with spring animation
        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = QuantiveMotionTokens.SpringBouncy
        )
        
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = modifier,
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    } else {
        // Indeterminate with bounce effect
        CircularProgressIndicator(
            modifier = modifier.scale(
                animateFloatAsState(
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Reverse
                    )
                ).value
            ),
            color = MaterialTheme.colorScheme.primary
        )
    }
    
    if (message != null) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = QuantiveDesignTokens.Spacing.Small)
        )
    }
}
```

## Design System Standards

### 1. Material 3 Design Tokens
```kotlin
object QuantiveDesignTokens {
    // Color System
    object Colors {
        val Primary = Color(0xFF00796B)      // Teal 700
        val Secondary = Color(0xFF4CAF50)    // Green 500
        val Tertiary = Color(0xFF2196F3)     // Blue 500
        val Error = Color(0xFFD32F2F)        // Red 700
        val Warning = Color(0xFFFF9800)      // Orange 500
        val Success = Color(0xFF388E3C)      // Green 700
        
        // South African Business Context
        val Currency = Color(0xFF1B5E20)     // Dark Green (Rand)
        val Tax = Color(0xFF3E2723)          // Brown (SARS)
    }
    
    // Typography Scale
    object Typography {
        val DisplayLarge = TextStyle(
            fontFamily = FontFamily.Inter,
            fontWeight = FontWeight.W400,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp
        )
        
        val HeadlineMedium = TextStyle(
            fontFamily = FontFamily.Inter,
            fontWeight = FontWeight.W400,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp
        )
        
        val BodyLarge = TextStyle(
            fontFamily = FontFamily.Inter,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
        
        val LabelMedium = TextStyle(
            fontFamily = FontFamily.Inter,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    }
    
    // Spacing System (8pt Grid)
    object Spacing {
        val Tiny = 4.dp      // 0.5 * 8
        val Small = 8.dp     // 1 * 8
        val Medium = 16.dp   // 2 * 8
        val Large = 24.dp    // 3 * 8
        val XLarge = 32.dp   // 4 * 8
        val XXLarge = 48.dp  // 6 * 8
        val Huge = 64.dp     // 8 * 8
    }
    
    // Border Radius
    object Radius {
        val Small = 8.dp
        val Medium = 12.dp
        val Large = 16.dp
        val XLarge = 24.dp
        val Round = 50.dp
    }
    
    // Elevation
    object Elevation {
        val None = 0.dp
        val Small = 2.dp
        val Medium = 4.dp
        val Large = 8.dp
        val XLarge = 16.dp
    }
}
```

### 2. Component Standards

**Card Components**:
```kotlin
@Composable
fun QuantiveCard(
    modifier: Modifier = Modifier,
    elevation: Dp = QuantiveDesignTokens.Elevation.Medium,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.then(
            if (onClick != null) Modifier.clickable { onClick() } else Modifier
        ),
        shape = RoundedCornerShape(QuantiveDesignTokens.Radius.Medium),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(QuantiveDesignTokens.Spacing.Medium),
            content = content
        )
    }
}
```

**Button Standards**:
```kotlin
@Composable
fun QuantivePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled && !loading,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(QuantiveDesignTokens.Radius.Medium)
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                style = QuantiveDesignTokens.Typography.LabelMedium
            )
        }
    }
}
```

## Backend Integration Patterns

### 1. Hybrid RPC/REST Architecture

**REST for CRUD Operations**:
```kotlin
interface InvoiceRestApi {
    @GET("/api/v1/invoices")
    suspend fun getInvoices(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("status") status: String?
    ): ApiResponse<PaginatedResponse<Invoice>>
    
    @POST("/api/v1/invoices")
    suspend fun createInvoice(@Body invoice: CreateInvoiceRequest): ApiResponse<Invoice>
    
    @PUT("/api/v1/invoices/{id}")
    suspend fun updateInvoice(
        @Path("id") id: String,
        @Body invoice: UpdateInvoiceRequest
    ): ApiResponse<Invoice>
}
```

**RPC for Complex Business Operations**:
```kotlin
interface BusinessLogicRpcApi {
    @POST("/rpc/calculate-invoice-totals")
    suspend fun calculateInvoiceTotals(@Body request: CalculateInvoiceTotalsRequest): ApiResponse<InvoiceTotals>
    
    @POST("/rpc/generate-invoice-pdf")
    suspend fun generateInvoicePdf(@Body request: GenerateInvoicePdfRequest): ApiResponse<PdfResponse>
    
    @POST("/rpc/process-bulk-payment")
    suspend fun processBulkPayment(@Body request: BulkPaymentRequest): ApiResponse<PaymentResult>
    
    @POST("/rpc/sync-business-metrics")
    suspend fun syncBusinessMetrics(@Body request: MetricsSyncRequest): ApiResponse<BusinessMetrics>
}
```

### 2. Repository Pattern with Caching

```kotlin
class InvoiceRepositoryImpl(
    private val restApi: InvoiceRestApi,
    private val rpcApi: BusinessLogicRpcApi,
    private val localDataSource: InvoiceLocalDataSource,
    private val cacheManager: CacheManager
) : InvoiceRepository {

    override suspend fun getInvoices(
        filter: InvoiceFilter,
        forceRefresh: Boolean
    ): Flow<PaginatedData<Invoice>> = flow {
        // Emit cached data first
        emit(localDataSource.getInvoices(filter))
        
        // Fetch fresh data if needed
        if (forceRefresh || cacheManager.isExpired("invoices_${filter.key}")) {
            try {
                val response = restApi.getInvoices(
                    page = filter.page,
                    limit = filter.limit,
                    status = filter.status
                )
                
                if (response.isSuccess()) {
                    localDataSource.saveInvoices(response.data.items)
                    cacheManager.markFresh("invoices_${filter.key}")
                    emit(response.data)
                }
            } catch (e: Exception) {
                // Emit error while keeping cached data
                emit(PaginatedData.error(e, localDataSource.getInvoices(filter).data))
            }
        }
    }

    override suspend fun createInvoice(invoice: Invoice): Result<Invoice> {
        return try {
            // Save locally first (optimistic update)
            localDataSource.saveInvoice(invoice.copy(status = InvoiceStatus.SYNCING))
            
            // Sync with backend
            val response = restApi.createInvoice(invoice.toCreateRequest())
            
            if (response.isSuccess()) {
                val createdInvoice = response.data
                localDataSource.saveInvoice(createdInvoice)
                Result.success(createdInvoice)
            } else {
                // Rollback local changes
                localDataSource.deleteInvoice(invoice.id)
                Result.failure(response.error)
            }
        } catch (e: Exception) {
            // Mark as failed sync for later retry
            localDataSource.saveInvoice(invoice.copy(status = InvoiceStatus.SYNC_FAILED))
            Result.failure(e)
        }
    }
}
```

### 3. Event-Driven Architecture

```kotlin
// Domain Events
sealed class InvoiceEvent {
    data class InvoiceCreated(val invoice: Invoice) : InvoiceEvent()
    data class InvoiceStatusChanged(val invoiceId: String, val newStatus: InvoiceStatus) : InvoiceEvent()
    data class PaymentReceived(val invoiceId: String, val payment: Payment) : InvoiceEvent()
}

// Event Bus Implementation
class EventBus {
    private val _events = MutableSharedFlow<DomainEvent>()
    val events: SharedFlow<DomainEvent> = _events.asSharedFlow()
    
    suspend fun emit(event: DomainEvent) {
        _events.emit(event)
    }
    
    inline fun <reified T : DomainEvent> subscribe(): Flow<T> {
        return events.filterIsInstance<T>()
    }
}

// Event Handlers
class BusinessMetricsEventHandler(
    private val metricsRepository: MetricsRepository
) {
    suspend fun handleInvoiceEvents() {
        eventBus.subscribe<InvoiceEvent>().collect { event ->
            when (event) {
                is InvoiceEvent.InvoiceCreated -> {
                    metricsRepository.updateTotalRevenue(event.invoice.amount)
                }
                is InvoiceEvent.PaymentReceived -> {
                    metricsRepository.updateOutstandingAmount(-event.payment.amount)
                }
            }
        }
    }
}
```

## Code Organization & Structure

### 1. Package Structure
```
io.sum8.mobile/
├── commonMain/
│   ├── domain/
│   │   ├── entities/           # Core business entities
│   │   ├── repositories/       # Repository interfaces
│   │   ├── usecases/          # Business logic use cases
│   │   └── events/            # Domain events
│   ├── data/
│   │   ├── remote/            # API clients and DTOs
│   │   ├── local/             # Local database and cache
│   │   ├── repositories/      # Repository implementations
│   │   └── mappers/           # Data transformation
│   ├── presentation/
│   │   ├── ui/
│   │   │   ├── screens/       # Screen composables
│   │   │   ├── components/    # Reusable UI components
│   │   │   └── theme/         # Design system
│   │   ├── viewmodels/        # ViewModels with MVI
│   │   └── navigation/        # Navigation logic
│   ├── di/                    # Dependency injection
│   └── core/
│       ├── utils/             # Utility functions
│       ├── extensions/        # Extension functions
│       └── constants/         # App constants
├── androidMain/
│   ├── platform/              # Android-specific implementations
│   └── MainActivity.kt
└── iosMain/
    └── platform/              # iOS-specific implementations
```

### 2. Naming Conventions

**Files & Classes**:
- PascalCase for classes: `InvoiceRepository`, `BusinessMetrics`
- camelCase for functions: `createInvoice`, `calculateTotals`
- SCREAMING_SNAKE_CASE for constants: `MAX_INVOICE_ITEMS`, `DEFAULT_CURRENCY`

**Compose Components**:
- Prefix with `Quantive`: `QuantiveCard`, `QuantiveButton`, `QuantiveTextField`
- Screen components end with `Screen`: `DashboardScreen`, `InvoiceListScreen`
- Use descriptive names: `InvoiceAmountSelectionGrid`, `BusinessMetricsCard`

### 3. MVI State Management Pattern

```kotlin
// State
data class InvoiceListUiState(
    val invoices: List<Invoice> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedFilter: InvoiceFilter = InvoiceFilter.All,
    val error: String? = null
)

// Actions (User Interactions)
sealed class InvoiceListAction {
    object LoadInvoices : InvoiceListAction()
    object RefreshInvoices : InvoiceListAction()
    data class SearchInvoices(val query: String) : InvoiceListAction()
    data class FilterInvoices(val filter: InvoiceFilter) : InvoiceListAction()
    data class SelectInvoice(val invoiceId: String) : InvoiceListAction()
    object ClearError : InvoiceListAction()
}

// Effects (One-time events)
sealed class InvoiceListEffect {
    data class NavigateToInvoiceDetail(val invoiceId: String) : InvoiceListEffect()
    data class ShowError(val message: String) : InvoiceListEffect()
    object ShowNoInternetMessage : InvoiceListEffect()
}

// ViewModel
class InvoiceListViewModel(
    private val invoiceRepository: InvoiceRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(InvoiceListUiState())
    val uiState: StateFlow<InvoiceListUiState> = _uiState.asStateFlow()
    
    private val _effects = MutableSharedFlow<InvoiceListEffect>()
    val effects: SharedFlow<InvoiceListEffect> = _effects.asSharedFlow()
    
    fun handleAction(action: InvoiceListAction) {
        when (action) {
            is InvoiceListAction.LoadInvoices -> loadInvoices()
            is InvoiceListAction.SearchInvoices -> searchInvoices(action.query)
            is InvoiceListAction.SelectInvoice -> selectInvoice(action.invoiceId)
            // ... handle other actions
        }
    }
    
    private fun loadInvoices() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            invoiceRepository.getInvoices(InvoiceFilter.All)
                .catch { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
                .collect { invoices ->
                    _uiState.value = _uiState.value.copy(
                        invoices = invoices,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }
}
```

## Development Workflows

### 1. Feature Development Process

1. **Domain First**: Define entities, use cases, and repository interfaces
2. **Data Layer**: Implement repositories with local/remote data sources
3. **Presentation**: Create ViewModel with MVI pattern
4. **UI**: Build Compose screens using design system components
5. **Integration**: Wire everything together with dependency injection
6. **Testing**: Unit tests for domain, integration tests for data, UI tests for presentation

### 2. Git Workflow

**Branch Naming**:
- `feature/invoice-creation-flow`
- `bugfix/dashboard-metrics-calculation`
- `refactor/repository-caching-layer`
- `chore/update-dependencies`

**Commit Messages**:
```
feat(invoices): add progressive invoice creation flow

- Implement amount selection grid component
- Add invoice item management with drag-to-reorder
- Integrate with backend invoice calculation RPC
- Add form validation and error handling

Resolves: SUM8-123
```

### 3. Code Review Standards

**Required Checks**:
- [ ] Follows Clean Architecture principles
- [ ] Uses established design system components
- [ ] Implements proper error handling
- [ ] Includes unit tests for business logic
- [ ] Follows MVI pattern for UI state management
- [ ] Uses proper dependency injection
- [ ] Follows South African business compliance requirements
- [ ] Implements proper accessibility attributes

## Performance Standards

### 1. App Performance Targets
- **Cold Start Time**: <2 seconds
- **Screen Transition**: <300ms
- **API Response Processing**: <500ms
- **Database Query Time**: <100ms
- **Memory Usage**: <200MB baseline
- **APK Size**: <50MB

### 2. Caching Strategy

```kotlin
class CacheManager(
    private val preferences: Preferences,
    private val database: Database
) {
    companion object {
        private val CACHE_DURATIONS = mapOf(
            "business_metrics" to 5.minutes,
            "invoices" to 10.minutes,
            "contacts" to 30.minutes,
            "user_profile" to 1.hours
        )
    }
    
    suspend fun <T> getCachedOrFetch(
        key: String,
        fetcher: suspend () -> T
    ): T {
        val cachedData = getFromCache<T>(key)
        val isExpired = isExpired(key)
        
        return when {
            cachedData != null && !isExpired -> cachedData
            else -> {
                val freshData = fetcher()
                saveToCache(key, freshData)
                markFresh(key)
                freshData
            }
        }
    }
}
```

### 3. Image Loading & Caching

```kotlin
@Composable
fun QuantiveAsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    error: Painter? = null,
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .build(),
        placeholder = placeholder,
        error = error ?: painterResource(R.drawable.ic_error),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    )
}
```

## Testing Requirements

### 1. Testing Strategy

**Test Pyramid**:
- 70% Unit Tests (Domain logic, ViewModels)
- 20% Integration Tests (Repository, API clients)
- 10% UI Tests (Critical user flows)

### 2. Unit Test Standards

```kotlin
class InvoiceCalculationUseCaseTest {
    
    private val mockTaxService = mockk<TaxCalculationService>()
    private val useCase = CalculateInvoiceTotalsUseCase(mockTaxService)
    
    @Test
    fun `calculate totals with SA VAT rate returns correct amounts`() = runTest {
        // Given
        val invoiceItems = listOf(
            InvoiceItem(description = "Service 1", amount = 1000.0),
            InvoiceItem(description = "Service 2", amount = 500.0)
        )
        every { mockTaxService.getSAVatRate() } returns 0.15
        
        // When
        val result = useCase.execute(
            CalculateInvoiceTotalsUseCase.Params(
                items = invoiceItems,
                vatIncluded = false
            )
        )
        
        // Then
        result.isSuccess shouldBe true
        result.getOrNull() shouldBe InvoiceTotals(
            subtotal = 1500.0,
            vatAmount = 225.0,
            total = 1725.0
        )
    }
}
```

### 3. UI Test Standards

```kotlin
class InvoiceCreationScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun invoice_creation_flow_completes_successfully() {
        // Given
        composeTestRule.setContent {
            QuantiveTheme {
                InvoiceCreationScreen(
                    state = InvoiceCreationUiState(),
                    onAction = { /* mock actions */ }
                )
            }
        }
        
        // When
        composeTestRule.onNodeWithText("Add Client").performClick()
        composeTestRule.onNodeWithTag("client_selection").performClick()
        composeTestRule.onNodeWithText("Acme Corp").performClick()
        
        composeTestRule.onNodeWithTag("amount_1000").performClick()
        composeTestRule.onNodeWithText("Create Invoice").performClick()
        
        // Then
        composeTestRule.onNodeWithText("Invoice Created Successfully").assertIsDisplayed()
    }
}
```

## Accessibility Guidelines

### 1. Accessibility Standards

**WCAG 2.1 AA Compliance**:
- Minimum contrast ratio of 4.5:1 for normal text
- Minimum contrast ratio of 3:1 for large text
- Touch targets minimum 44dp
- Support for screen readers and TalkBack
- Keyboard navigation support

### 2. Implementation

```kotlin
@Composable
fun QuantiveAccessibleCard(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(
                onClick = onClick,
                role = Role.Button,
                onClickLabel = "Open $title details"
            )
            .semantics(mergeDescendants = true) {
                contentDescription = buildString {
                    append(title)
                    subtitle?.let { append(", $it") }
                }
            }
            .padding(QuantiveDesignTokens.Spacing.Medium)
    ) {
        Column {
            Text(
                text = title,
                style = QuantiveDesignTokens.Typography.HeadlineMedium,
                modifier = Modifier.semantics {
                    heading()
                }
            )
            
            subtitle?.let {
                Text(
                    text = it,
                    style = QuantiveDesignTokens.Typography.BodyLarge
                )
            }
        }
    }
}
```

## South African Business Requirements

### 1. Compliance Standards

**POPIA (Protection of Personal Information Act)**:
- Explicit user consent for data processing
- Right to data portability
- Data breach notification requirements
- Secure data storage and transmission

**SARS Integration**:
- VAT registration number validation
- Tax invoice requirements
- Electronic submission capabilities

### 2. Implementation

```kotlin
object SouthAfricanCompliance {
    
    fun validateVatNumber(vatNumber: String): ValidationResult {
        // Implement SA VAT number validation
        val cleanNumber = vatNumber.replace(Regex("[^0-9]"), "")
        
        return when {
            cleanNumber.length != 10 -> ValidationResult.Invalid("VAT number must be 10 digits")
            !cleanNumber.startsWith("4") -> ValidationResult.Invalid("VAT number must start with 4")
            !isValidChecksum(cleanNumber) -> ValidationResult.Invalid("Invalid VAT number checksum")
            else -> ValidationResult.Valid
        }
    }
    
    fun formatCurrency(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("en", "ZA"))
            .format(amount)
    }
    
    fun generateTaxInvoice(invoice: Invoice): TaxInvoice {
        return TaxInvoice(
            invoiceNumber = invoice.number,
            supplierVatNumber = invoice.supplier.vatNumber,
            customerVatNumber = invoice.customer.vatNumber,
            taxPointDate = invoice.createdAt,
            vatAmount = invoice.calculateVat(),
            totalAmount = invoice.total
        )
    }
}
```

---

## Implementation Rules Summary

1. **Backend-Logic First**: Always implement domain logic before UI
2. **Hybrid API**: Use REST for CRUD, RPC for complex operations
3. **Offline-First**: Local state is source of truth
4. **MVI Pattern**: Unidirectional data flow for all screens
5. **Design System**: Use Quantive design tokens consistently
6. **Clean Architecture**: Strict separation of concerns
7. **Testing Required**: Minimum 70% test coverage
8. **Accessibility First**: WCAG 2.1 AA compliance mandatory
9. **SA Compliance**: Follow POPIA and SARS requirements
10. **Performance**: Meet all defined performance targets

These standards ensure consistent, maintainable, and scalable development while meeting South African business requirements and international accessibility standards.