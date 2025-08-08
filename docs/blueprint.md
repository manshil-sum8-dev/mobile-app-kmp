# KMP Enterprise Development Standards & Architecture Guide
**Comprehensive standards for backend-driven KMP/Compose Multiplatform applications**

## 1. Core Development Principles

### SOLID, DRY, KISS, YAGNI Enforcement
```yaml
Strict Enforcement Via:
  Modularization:
    - Clear separation of shared, android, ios, desktop modules
    - Feature-based modularization within shared module
    - Domain-driven design boundaries
    
  Dependency Injection:
    - Koin for multiplatform DI
    - Manual DI where appropriate for performance
    - Interface-based abstractions
    
  Coroutines/Flows:
    - Structured concurrency only
    - No GlobalScope usage (enforced via Detekt rules)
    - viewModelScope and lifecycleScope preferred
```

### Industry Best Practices
```yaml
Security:
  - Credential/API key handling via SecureStorage expect/actual
  - HTTPS with certificate pinning mandatory
  - End-to-end encryption for sensitive data
  - Biometric authentication integration
  
Performance:
  - Cold-start optimizations < 2 seconds
  - Memory management with leak prevention
  - SQLDelight for strategic caching (not offline-first)
  - Background processing optimization
  
Compliance:
  - GDPR compliance with data classification
  - Accessibility via semantic properties in Compose
  - Audit logging for enterprise requirements
```

## 2. Architecture Foundation

### Core Architecture Pattern
```yaml
Clean Architecture + Backend-Driven:
  Domain Layer (Shared):
    - Use Cases: Business logic orchestration
    - Entities: Core business models
    - Repositories: Abstract data contracts
    - Event Bus: Cross-feature communication
    
  Data Layer (Shared):
    - Repository Implementations: API-first with smart caching
    - API Services: Ktor client with retry/circuit breaker
    - Cache Layer: Strategic caching for performance
    - Sync Engine: Background data refresh
    
  Presentation Layer (Platform-Specific):
    - ViewModels: State management + UI events
    - Compose UI: Reactive UI with backend state
    - Navigation: Deep linking + state preservation
    - Platform Adaptations: iOS/Android specific UI
```

### Backend-Driven Principles
```yaml
Server-First Architecture:
  - Single Source of Truth: Backend APIs define app state
  - Smart Caching: Performance optimization, not offline capability
  - Real-time Sync: WebSocket/SSE for live updates
  - Graceful Degradation: Limited functionality during connectivity issues
  - Cache Strategy: TTL-based, not persistence-based
```

## 3. Feature Scaffolding Standards (KMP-Centric)

### Template Includes

#### UI Layer Templates
```kotlin
// Generated: Material 3 Expressive Theme
@Composable
fun FeatureTheme(
    dynamicColor: Boolean = true,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkExpressiveColorScheme
        else -> LightExpressiveColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ExpressiveTypography,
        shapes = ExpressiveShapes,
        content = content
    )
}

// Generated: Base Compose Screen Template with ViewModel integration
@Composable
fun FeatureScreen(
    viewModel: FeatureViewModel = getViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val platformUI = remember { PlatformSpecificUI() }
    
    LaunchedEffect(Unit) {
        viewModel.handleIntent(FeatureIntent.LoadData)
    }
    
    LaunchedEffect(viewModel.uiEvents) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is FeatureUiEvent.NavigateBack -> onNavigateBack()
                is FeatureUiEvent.ShowError -> {
                    // Handle error with Material 3 SnackbarHost
                }
            }
        }
    }
    
    // Platform Adaptations with conditional logic
    platformUI.SafeAreaPadding {
        when (uiState) {
            is FeatureUiState.Loading -> LoadingScreen()
            is FeatureUiState.Success -> FeatureContent(
                data = uiState.data,
                onAction = { action ->
                    viewModel.handleIntent(FeatureIntent.HandleAction(action))
                }
            )
            is FeatureUiState.Error -> ErrorScreen(
                message = uiState.message,
                onRetry = { viewModel.handleIntent(FeatureIntent.Retry) }
            )
        }
    }
}

// Platform-Specific UI Tweaks (iOS SafeArea example)
expect class PlatformSpecificUI {
    @Composable
    fun SafeAreaPadding(content: @Composable () -> Unit)
    
    @Composable
    fun StatusBar(color: Color)
    
    @Composable 
    fun NavigationBar(content: @Composable () -> Unit)
}
```

#### State/Logic Layer Templates
```kotlin
// Generated: ViewModel with Coroutine-aware, sealed-class UiState
abstract class BaseViewModel : ViewModel() {
    protected val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    protected val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()
    
    abstract fun createInitialState(): UiState
    abstract fun handleIntent(intent: Intent)
    
    protected fun updateState(update: UiState.() -> UiState) {
        _uiState.value = _uiState.value.update()
    }
    
    protected fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvents.send(event)
        }
    }
}

// Generated: Feature-specific sealed classes
sealed class FeatureUiState : UiState {
    object Loading : FeatureUiState()
    data class Success(val data: FeatureData) : FeatureUiState()
    data class Error(val message: String) : FeatureUiState()
}

sealed class FeatureIntent : Intent {
    object LoadData : FeatureIntent()
    data class HandleAction(val action: FeatureAction) : FeatureIntent()
    object Retry : FeatureIntent()
}

sealed class FeatureUiEvent : UiEvent {
    object NavigateBack : FeatureUiEvent()
    data class ShowError(val message: String) : FeatureUiEvent()
}
```

#### API Integration Templates
```kotlin
// Generated: Pre-wired Ktor client with JsonFeature, error handling
val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        })
    }
    
    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 15_000
    }
    
    install(HttpRequestRetry) {
        retryOnServerErrors(maxRetries = 3)
        exponentialDelay()
    }
    
    install(Logging) {
        logger = NapierLogger() // Using Napier for structured logging
        level = LogLevel.INFO
    }
    
    install(Auth) {
        bearer {
            loadTokens { tokenProvider.getTokens() }
            refreshTokens { tokenProvider.refreshTokens() }
        }
    }
}

// Generated: Circuit breaker pattern with retry/timeout logic
class ApiService(private val httpClient: HttpClient) {
    private val circuitBreaker = CircuitBreaker(
        failureThreshold = 5,
        recoveryTimeout = 60.seconds
    )
    
    suspend fun <T> safeApiCall(call: suspend () -> T): Result<T> {
        return circuitBreaker.execute {
            try {
                Result.success(call())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
```

#### SQLDelight Caching Templates
```kotlin
// Generated: SQLDelight DAO stubs for backend-driven caching
CREATE TABLE FeatureCache (
    id TEXT NOT NULL PRIMARY KEY,
    data TEXT NOT NULL, -- JSON serialized data
    cached_at INTEGER NOT NULL,
    expires_at INTEGER NOT NULL
);

-- Insert or update cache entry
insertOrReplace:
INSERT OR REPLACE INTO FeatureCache(id, data, cached_at, expires_at)
VALUES (?, ?, ?, ?);

-- Get cached entry if not expired
getCachedData:
SELECT * FROM FeatureCache 
WHERE id = ? AND expires_at > ?;

-- Clear expired entries
clearExpired:
DELETE FROM FeatureCache WHERE expires_at <= ?;

// Generated: Cache Manager Implementation
class FeatureCacheManager(
    private val database: Database,
    private val ttlMinutes: Int = 5
) {
    private val queries = database.featureCacheQueries
    
    fun getCached(id: String): FeatureResponse? {
        val currentTime = System.currentTimeMillis()
        return queries.getCachedData(id, currentTime)
            .executeAsOneOrNull()
            ?.let { cached ->
                Json.decodeFromString<FeatureResponse>(cached.data)
            }
    }
    
    fun cache(id: String, response: FeatureResponse) {
        val currentTime = System.currentTimeMillis()
        val expiresAt = currentTime + (ttlMinutes * 60 * 1000)
        val jsonData = Json.encodeToString(response)
        
        queries.insertOrReplace(
            id = id,
            data = jsonData,
            cached_at = currentTime,
            expires_at = expiresAt
        )
    }
    
    fun isFresh(response: FeatureResponse): Boolean {
        return System.currentTimeMillis() < (response.cachedAt + (ttlMinutes * 60 * 1000))
    }
}
```

#### Navigation Templates
```kotlin
// Generated: Decompose Router setup with typed arguments
object FeatureDestination : Destination {
    const val ROUTE = "feature"
    const val FEATURE_ID_ARG = "featureId"
    const val ROUTE_WITH_ARGS = "$ROUTE/{$FEATURE_ID_ARG}"
}

// Generated: Decompose Component
class FeatureComponent(
    componentContext: ComponentContext,
    private val featureId: String,
    private val onNavigateBack: () -> Unit
) : ComponentContext by componentContext {
    
    val viewModel = FeatureViewModel(
        featureId = featureId,
        // Injected dependencies
    )
    
    fun navigateBack() = onNavigateBack()
}

// Alternative: Voyager setup
class FeatureScreenModel(
    private val featureId: String
) : ScreenModel {
    // Voyager-specific implementation
}
```

#### Testing Templates
```kotlin
// Generated: Shared Tests with kotlin-test and MockK
class FeatureRepositoryTest {
    private val mockApiService = mockk<FeatureApiService>()
    private val mockCacheManager = mockk<FeatureCacheManager>()
    private val mockConnectivityManager = mockk<ConnectivityManager>()
    
    private val repository = FeatureRepositoryImpl(
        apiService = mockApiService,
        cacheManager = mockCacheManager,
        connectivityManager = mockConnectivityManager
    )
    
    @Test
    fun `getFeatureData returns cached data when fresh`() = runTest {
        // Given
        val cachedData = FeatureData(id = "1", name = "Test")
        every { mockCacheManager.getCached("1") } returns cachedData.toResponse()
        every { mockCacheManager.isFresh(any()) } returns true
        
        // When
        val result = repository.getFeatureData("1").first()
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(cachedData, result.getOrNull())
        verify(exactly = 0) { mockApiService.getFeatureData(any()) }
    }
}

// Generated: Screenshot Testing with Shot
class FeatureScreenshotTest {
    
    @get:Rule
    val composeScreenshotRule = ComposeScreenShotTestRule()
    
    @Test
    fun featureScreenshots() {
        composeScreenshotRule.setContent {
            FeatureTheme {
                FeatureScreen(
                    viewModel = mockViewModel,
                    onNavigateBack = {}
                )
            }
        }
        
        // Loading state
        composeScreenshotRule.compareScreenshot("feature_loading")
        
        // Success state  
        updateViewModelState(FeatureUiState.Success(mockData))
        composeScreenshotRule.compareScreenshot("feature_success")
        
        // Error state
        updateViewModelState(FeatureUiState.Error("Network error"))
        composeScreenshotRule.compareScreenshot("feature_error")
    }
}
```

#### OpenAPI Integration Templates
```kotlin
// Generated: OpenAPI-derived models via kotlinx-serialization
// build.gradle.kts addition
plugins {
    id("org.openapi.generator") version "6.6.0"
}

openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("$rootDir/api-specs/feature-api.yaml")
    outputDir.set("$buildDir/generated/openapi")
    packageName.set("com.yourapp.features.feature.api.generated")
    configOptions.set(mapOf(
        "serializationLibrary" to "kotlinx_serialization"
    ))
}

// Generated API models extending base contracts
@Serializable
data class FeatureResponse(
    val id: String,
    val name: String,
    val data: String
) : ApiResponse {
    fun toDomain(): FeatureData = FeatureData(
        id = id,
        name = name,
        data = Json.decodeFromString(data)
    )
}

@Serializable
data class FeatureRequest(
    val name: String,
    val data: String
) : ApiRequest
```

## 4. Enterprise-Grade Additions

### Observability
```kotlin
// Structured logs with Napier (tagged by feature/module)
object FeatureLogger {
    private const val TAG = "Feature"
    
    fun d(message: String, extra: Map<String, Any> = emptyMap()) {
        Napier.d(tag = TAG) { 
            buildString {
                append(message)
                if (extra.isNotEmpty()) {
                    append(" | ")
                    append(extra.entries.joinToString(", ") { "${it.key}=${it.value}" })
                }
            }
        }
    }
    
    fun e(message: String, throwable: Throwable? = null, extra: Map<String, Any> = emptyMap()) {
        Napier.e(tag = TAG, throwable = throwable) {
            buildString {
                append(message)
                if (extra.isNotEmpty()) {
                    append(" | ")
                    append(extra.entries.joinToString(", ") { "${it.key}=${it.value}" })
                }
            }
        }
    }
}

// Firebase Crashlytics/Sentry integration
expect class CrashReporting {
    fun logException(throwable: Throwable)
    fun setCustomKey(key: String, value: String)
    fun setUserId(userId: String)
}
```

### CI/CD Integration
```kotlin
// gradle.properties-driven versioning (major.minor.patch)
VERSION_MAJOR=1
VERSION_MINOR=0  
VERSION_PATCH=0
VERSION_BUILD=0

// build.gradle.kts version configuration
val versionMajor = project.findProperty("VERSION_MAJOR")?.toString()?.toInt() ?: 1
val versionMinor = project.findProperty("VERSION_MINOR")?.toString()?.toInt() ?: 0
val versionPatch = project.findProperty("VERSION_PATCH")?.toString()?.toInt() ?: 0
val versionBuild = project.findProperty("VERSION_BUILD")?.toString()?.toInt() ?: 0

android {
    defaultConfig {
        versionCode = versionMajor * 10000 + versionMinor * 1000 + versionPatch * 100 + versionBuild
        versionName = "${versionMajor}.${versionMinor}.${versionPatch}"
    }
}
```

### Static Analysis - Detekt + KMP-Specific Rules
```yaml
# config/detekt/kmp-rules.yml
KmpSpecificRules:
  ExpectActualNaming:
    active: true
    expectClassSuffix: 'Expected'
    actualClassSuffix: 'Impl'
  
  SharedModuleDependencies:
    active: true
    allowedPlatformDependencies:
      - 'ktor-client-*'
      - 'kotlinx-*'
      - 'koin-*'
      - 'napier'
  
  CoroutineScopeUsage:
    active: true
    forbiddenScopes:
      - 'GlobalScope'
    allowedScopes:
      - 'viewModelScope'
      - 'lifecycleScope'
```

```kotlin
// Custom Detekt rule implementation
class NoGlobalCoroutineScopeRule : Rule() {
    override val issue = Issue(
        "NoGlobalCoroutineScope",
        Severity.Warning,
        "GlobalScope usage is forbidden, use structured concurrency",
        Debt.TEN_MINS
    )
    
    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)
        
        if (expression.text.contains("GlobalScope")) {
            report(CodeSmell(issue, Entity.from(expression), 
                "Use viewModelScope or structured concurrency instead of GlobalScope"))
        }
    }
}
```

### Documentation Standards
```markdown
# Generated: Feature Documentation Template
# Feature Name

## API Documentation
- **Swagger UI**: [http://localhost:8080/swagger-ui/feature](http://localhost:8080/swagger-ui/feature)
- **OpenAPI Spec**: [feature-api.yaml](../../../api-specs/feature-api.yaml)

## Architecture Decision Records
- [ADR-001: Backend-Driven Architecture](docs/adr/001-backend-driven.md)
- [ADR-002: Caching Strategy](docs/adr/002-caching-strategy.md)

## Generated API Models
All API models are generated from OpenAPI specifications using the `openApiGenerate` Gradle task.

To regenerate models after API changes:
```bash
./gradlew openApiGenerate
```

## Testing
- Unit Tests: `./gradlew shared:testDebugUnitTest`
- Screenshot Tests: `./gradlew androidApp:connectedAndroidTest`
- Integration Tests: `./gradlew integrationTest`
```

## 5. Data Architecture Standards

### Backend-Driven Data Flow
```kotlin
// Repository Pattern - API First with Smart Caching
interface FeatureRepository {
    suspend fun getFeatureData(id: String): Flow<Result<FeatureData>>
    suspend fun updateFeatureData(data: FeatureData): Result<FeatureData>
    suspend fun refreshFeatureData(id: String): Result<FeatureData>
}

class FeatureRepositoryImpl(
    private val apiService: FeatureApiService,
    private val cacheManager: FeatureCacheManager,
    private val connectivityManager: ConnectivityManager
) : FeatureRepository {
    
    override suspend fun getFeatureData(id: String): Flow<Result<FeatureData>> = flow {
        // Emit cached data first (if available and fresh)
        cacheManager.getCached(id)?.let { cached ->
            if (cacheManager.isFresh(cached)) {
                emit(Result.success(cached.toDomain()))
            }
        }
        
        // Fetch from backend
        try {
            val response = apiService.getFeatureData(id)
            val domain = response.toDomain()
            cacheManager.cache(id, response)
            emit(Result.success(domain))
        } catch (e: Exception) {
            if (!connectivityManager.isConnected()) {
                emit(Result.failure(NetworkException("No connectivity")))
            } else {
                emit(Result.failure(e))
            }
        }
    }
}
```

### Caching Strategy
```yaml
Cache Management:
  Purpose: Performance optimization, not offline support
  TTL Strategy:
    - User Data: 5 minutes
    - Configuration: 30 minutes
    - Static Content: 24 hours
    - Session Data: In-memory only
  
  Cache Layers:
    - Memory Cache: Hot data, short TTL
    - Disk Cache: Images, static resources
    - No Persistent Storage: For user-generated content
  
  Cache Invalidation:
    - TTL-based expiration
    - Manual invalidation on user actions
    - Server-pushed cache invalidation
```

## 6. Security Standards

### Authentication & Authorization
```kotlin
// Token Management with SecureStorage
class TokenManager(
    private val secureStorage: SecureStorage,
    private val apiService: AuthApiService
) {
    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val TOKEN_EXPIRY_KEY = "token_expiry"
    }
    
    suspend fun getAccessToken(): String? {
        val token = secureStorage.getString(ACCESS_TOKEN_KEY)
        val expiry = secureStorage.getLong(TOKEN_EXPIRY_KEY)
        
        return if (token != null && expiry > System.currentTimeMillis()) {
            token
        } else {
            refreshAccessToken()
        }
    }
}

// SecureStorage Expect/Actual Implementation
expect class SecureStorage {
    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String): String?
    suspend fun putLong(key: String, value: Long)
    suspend fun getLong(key: String): Long
    suspend fun remove(key: String)
    suspend fun clear()
}
```

### Network Security - HTTPS with Certificate Pinning
```kotlin
val httpClient = HttpClient(CIO) {
    engine {
        https {
            serverName = "api.yourapp.com"
            addPinnedCertificate("sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
        }
    }
}
```

## 7. Performance Standards

### Performance Requirements
```yaml
Performance Benchmarks:
  Cold Start: < 2 seconds on mid-range devices
  Memory Usage: < 100MB baseline, no memory leaks
  UI Responsiveness: 60fps minimum, < 100ms interaction response
  Network Efficiency: < 5MB/session typical usage
  Battery Impact: < 2% battery drain per hour active use
  
Monitoring Thresholds:
  Error Rate: < 0.1%
  API Response Time: < 500ms p95
  App Crashes: < 0.01%
  ANR Rate: 0%
```

### Compose Performance Optimization
```kotlin
@Composable
fun FeatureList(
    items: List<FeatureItem>,
    onItemClick: (FeatureItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(
            items = items,
            key = { item -> item.id } // Stable keys for recomposition optimization
        ) { item ->
            FeatureListItem(
                item = item,
                onClick = { onItemClick(item) }
            )
        }
    }
}

@Composable
private fun FeatureListItem(
    item: FeatureItem,
    onClick: () -> Unit
) {
    // Use remember for expensive calculations
    val formattedDate by remember(item.timestamp) {
        derivedStateOf { formatDate(item.timestamp) }
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        // Item content
    }
}
```

## 8. Testing Standards

### Testing Architecture
```yaml
Testing Strategy:
  Unit Tests: 80%+ coverage for shared business logic
  Integration Tests: API contracts, repository layer
  UI Tests: Critical user journeys, screenshot regression
  Performance Tests: Memory leaks, startup time, UI responsiveness
  Security Tests: Authentication flows, data encryption
  
Test Distribution:
  Shared Module Tests: 70% of total test coverage
  Platform-Specific Tests: 20% of total test coverage
  End-to-End Tests: 10% of total test coverage
```

## 9. Enforcement Mechanisms

### Pre-merge Hooks
```bash
#!/bin/sh
# .git/hooks/pre-commit

echo "Running pre-commit checks..."

# Kotlin linting
echo "Running ktlint..."
./gradlew ktlintCheck
if [ $? -ne 0 ]; then
    echo "ktlint failed. Please fix formatting issues."
    exit 1
fi

# Static analysis
echo "Running detekt..."
./gradlew detekt
if [ $? -ne 0 ]; then
    echo "Detekt failed. Please fix code quality issues."
    exit 1
fi

# Compose UI tests required for screens
echo "Running UI tests..."
./gradlew connectedAndroidTest
if [ $? -ne 0 ]; then
    echo "UI tests failed."
    exit 1
fi

echo "All pre-commit checks passed!"
```

### Review Checklist with YAGNI Enforcement
```markdown
# Pull Request Checklist

## YAGNI Compliance (Mandatory)
- [ ] **Does this violate YAGNI principles?**
  - [ ] No over-engineering or premature abstractions
  - [ ] No unused interfaces or excessive generalization  
  - [ ] Implementation solves current requirements only
  - [ ] No speculative features added

## Code Quality
- [ ] All code follows SOLID principles
- [ ] DRY principle applied appropriately
- [ ] KISS principle followed
- [ ] ktlint passes with zero warnings
- [ ] Detekt passes with zero issues (including KMP-specific rules)
- [ ] No hardcoded strings (use string resources)

## Architecture Compliance
- [ ] Follows Clean Architecture layers
- [ ] Repository pattern implemented correctly
- [ ] ViewModels handle state correctly with sealed classes
- [ ] Proper separation of concerns
- [ ] Backend-driven principles followed
- [ ] Material 3 Expressive theme usage

## Security & Performance
- [ ] No sensitive data in logs
- [ ] SecureStorage used for credentials
- [ ] Certificate pinning implemented
- [ ] No performance regressions
- [ ] Memory usage within limits
- [ ] Compose performance optimizations applied

## Testing & Documentation
- [ ] Unit tests for shared business logic (80%+ coverage)
- [ ] Screenshot tests for UI changes (using Shot)
- [ ] Integration tests for API changes
- [ ] Swagger UI documentation updated
- [ ] ADR created for significant architectural decisions

## Feature Scaffolding Compliance
- [ ] Generated templates used correctly
- [ ] OpenAPI models properly integrated
- [ ] Napier logging implemented
- [ ] SQLDelight caching strategy applied
- [ ] Navigation setup (Decompose/Voyager)
```

## 10. Feature Scaffolding Automation

### CLI Command for Feature Generation
```bash
# CLI Command for new feature scaffolding
./gradlew createFeature --name=UserProfile --type=CRUD

# Generated structure follows all standards above
shared/src/commonMain/kotlin/features/userprofile/
├── domain/
├── data/
├── presentation/
└── di/
```

### Gradle Plugin Integration
```kotlin
// buildSrc/src/main/kotlin/FeatureGeneratorPlugin.kt
class FeatureGeneratorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("createFeature", CreateFeatureTask::class.java)
        project.tasks.register("scaffoldFeature", InteractiveScaffoldTask::class.java)
    }
}

// Interactive scaffolding
tasks.register("scaffoldFeature") {
    group = "development"
    description = "Interactive feature scaffolding with all standards"
    
    doLast {
        val console = System.console()
        val featureName = console.readLine("Enter feature name (e.g., UserProfile): ")
        val featureType = console.readLine("Enter feature type (crud/readonly/custom): ") ?: "crud"
        val includeScreenshots = console.readLine("Include screenshot tests? (y/n): ")?.lowercase() == "y"
        val navigationFramework = console.readLine("Navigation framework (decompose/voyager): ") ?: "decompose"
        
        exec {
            commandLine("./gradlew", "createFeature", 
                "--name=$featureName", 
                "--type=$featureType",
                "--screenshots=$includeScreenshots",
                "--navigation=$navigationFramework"
            )
        }
    }
}
```

## 11. CI/CD Pipeline Integration

### GitHub Actions with All Quality Gates
```yaml
name: KMP Enterprise CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  code-quality:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup JDK
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Run ktlint
        run: ./gradlew ktlintCheck
      
      - name: Run Detekt (including KMP rules)
        run: ./gradlew detekt
      
      - name: YAGNI Compliance Check
        run: ./gradlew yagniCheck # Custom task to check for over-engineering
        
      - name: Run shared tests
        run: ./gradlew shared:testDebugUnitTest
      
      - name: Run screenshot tests
        run: ./gradlew executeScreenshotTests
        
      - name: Security scan
        run: ./gradlew dependencyCheck

  build-and-deploy:
    needs: code-quality
    runs-on: ubuntu-latest
    steps:
      - name: Build with version from gradle.properties
        run: ./gradlew assembleDebug
        
      - name: Deploy to staging
        if: github.ref == 'refs/heads/develop'
        run: ./gradlew deployStaging
```

## 12. Summary

This comprehensive guide enforces enterprise-grade standards for KMP/Compose Multiplatform development with:

### ✅ Core Principles Enforced
- **SOLID, DRY, KISS, YAGNI** via modularization, DI, and structured concurrency
- **Security** via SecureStorage, HTTPS with certificate pinning
- **Performance** via cold-start optimization and strategic caching
- **Compliance** via GDPR/accessibility standards

### ✅ Feature Scaffolding Automation
- **Material 3 Expressive** pre-configured themes
- **Compose Multiplatform** templates with platform adaptations
- **Sealed-class UiState** with coroutine-aware ViewModels
- **Ktor client** with pre-wired error handling
- **SQLDelight** caching for backend-driven architecture
- **Navigation** setup (Decompose/Voyager)
- **Testing** templates (kotlin-test, MockK, Shot)
- **OpenAPI** integration with kotlinx-serialization

### ✅ Enterprise-Grade Features
- **Napier** structured logging by feature/module
- **Firebase/Sentry** crash reporting integration
- **Gradle.properties** versioning (major.minor.patch)
- **Detekt** with KMP-specific custom rules
- **ADR** documentation standards
- **Swagger UI** integration

### ✅ Strict Enforcement
- **Pre-merge hooks** with ktlint + detekt (zero warnings)
- **Compose UI tests** required for all screens
- **YAGNI compliance** as mandatory PR question
- **Quality gates** integrated into build process
- **Screenshot regression** testing with Shot

All teams must follow this guide for consistent, enterprise-grade KMP/Compose Multiplatform development.