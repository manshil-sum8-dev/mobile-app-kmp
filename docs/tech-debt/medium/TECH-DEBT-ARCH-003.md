# TECH-DEBT-ARCH-003: Implement Proper Dependency Injection Architecture

**Status**: Open  
**Priority**: Medium  
**Domain**: Architecture  
**Effort Estimate**: 1-2 weeks  
**Assigned Agent**: KMP Mobile Architect  
**Created**: 2025-01-08  

## Problem Description

The application currently uses a manual dependency injection approach with `AppServices` object that does not scale well and violates enterprise architecture principles. The current implementation lacks proper scoping, lifecycle management, and testability features required for enterprise applications.

**Current DI Issues**:
- Manual dependency injection with `AppServices` object
- No proper scoping (singleton, scoped, transient)
- Difficult to test due to hard dependencies
- No lifecycle-aware dependency management
- Missing dependency graph validation
- No compile-time dependency verification

## Blueprint Violation

**Blueprint Requirement**: "Dependency Injection: Koin or equivalent for clean architecture" and enterprise DI patterns:
- Proper DI framework: Koin, Dagger/Hilt, or similar
- Clean separation of concerns: Domain, data, presentation layers
- Testable architecture: Easy mocking and testing
- Scoped dependencies: Singleton, scoped, transient lifecycles
- Compile-time safety: Dependency graph validation

**Current State**: Manual DI with static `AppServices` object

## Affected Files

### Current Manual DI
- `/composeApp/src/commonMain/kotlin/za/co/quantive/app/app/AppServices.kt` - Current manual DI
- All classes using `AppServices` directly

### Files to Create/Update
- `src/commonMain/kotlin/za/co/quantive/app/di/AppModule.kt` - Main DI module
- `src/commonMain/kotlin/za/co/quantive/app/di/DataModule.kt` - Data layer dependencies
- `src/commonMain/kotlin/za/co/quantive/app/di/DomainModule.kt` - Domain layer dependencies
- `src/commonMain/kotlin/za/co/quantive/app/di/PresentationModule.kt` - Presentation dependencies
- `src/commonMain/kotlin/za/co/quantive/app/QuantiveApplication.kt` - DI initialization

### Build Configuration
- `/composeApp/build.gradle.kts` - Add Koin dependencies
- `/gradle/libs.versions.toml` - DI framework versions

## Risk Assessment

- **Testability Risk**: High - Current manual DI makes testing difficult
- **Maintainability Risk**: High - Hard dependencies make code rigid
- **Scalability Risk**: Medium - Manual DI doesn't scale with app growth
- **Architecture Risk**: Medium - Violates clean architecture principles

## Acceptance Criteria

### DI Framework Integration
- [ ] Integrate Koin for multiplatform dependency injection
- [ ] Replace manual `AppServices` with proper DI modules
- [ ] Implement proper dependency scoping (singleton, factory, scoped)
- [ ] Add lifecycle-aware dependency management
- [ ] Create modular DI structure by architectural layers

### Clean Architecture Implementation
- [ ] Separate DI modules by architectural layers (data, domain, presentation)
- [ ] Implement proper abstraction boundaries
- [ ] Remove direct `AppServices` usage across the codebase
- [ ] Add compile-time dependency validation
- [ ] Implement dependency interfaces for testability

### Testing Integration
- [ ] Create test-specific DI modules for unit testing
- [ ] Add mock implementations for all dependencies
- [ ] Implement easy dependency swapping for tests
- [ ] Add DI container validation in tests
- [ ] Create integration test DI configuration

### Platform-Specific Dependencies
- [ ] Handle platform-specific dependencies (Android/iOS)
- [ ] Implement expect/actual pattern for platform dependencies
- [ ] Add platform-specific DI modules
- [ ] Ensure proper cleanup of platform resources

## Implementation Strategy

### Phase 1: Koin Integration (Week 1)

#### Dependencies Setup
```kotlin
// gradle/libs.versions.toml
[versions]
koin = "3.5.3"

[libraries]
koin-core = { module = "io.insert-koin:koin-core", version.ref = "koin" }
koin-android = { module = "io.insert-koin:koin-android", version.ref = "koin" }
koin-compose = { module = "io.insert-koin:koin-compose", version.ref = "koin" }
koin-test = { module = "io.insert-koin:koin-test", version.ref = "koin" }
```

```kotlin
// composeApp/build.gradle.kts
sourceSets {
    commonMain.dependencies {
        implementation(libs.koin.core)
        implementation(libs.koin.compose)
    }
    androidMain.dependencies {
        implementation(libs.koin.android)
    }
    commonTest.dependencies {
        implementation(libs.koin.test)
    }
}
```

#### Core DI Modules
```kotlin
// di/AppModule.kt
val appModule = module {
    // Application-level singletons
    single<SupabaseClient> { 
        createSupabaseClient(
            supabaseUrl = get(),
            supabaseAnonKey = get()
        ) 
    }
    
    single<AuthService> { 
        AuthService(
            supabaseClient = get(),
            secureStore = get()
        ) 
    }
    
    // Configuration
    single<AppConfig> { AppConfig() }
    single<String>(qualifier = named("supabase_url")) { "https://your-supabase-url.com" }
    single<String>(qualifier = named("supabase_anon_key")) { "your-anon-key" }
    
    // Platform-specific dependencies
    single<SecureStore> { get<PlatformSecureStore>() }
    single<NetworkMonitor> { get<PlatformNetworkMonitor>() }
}

// di/DataModule.kt
val dataModule = module {
    // Remote data sources
    single<BusinessProfileRemote> { 
        BusinessProfileRemote(
            supabaseClient = get(),
            ownerIdProvider = get<AuthService>()::getCurrentUserId
        ) 
    }
    
    // Local data sources
    single<BusinessProfileLocal> { BusinessProfileLocal() }
    
    // Repositories
    single<BusinessProfileRepository> {
        BusinessProfileRepositoryImpl(
            remote = get(),
            local = get(),
            cache = get()
        )
    }
    
    // Cache implementations
    single<CacheManager> { 
        CacheManager(maxMemoryMB = 50) 
    }
    
    single<BusinessProfileCache> {
        BusinessProfileCache(cacheManager = get())
    }
    
    // API clients
    single<InvoiceApi> {
        InvoiceApiImpl(supabaseClient = get())
    }
    
    single<CustomerApi> {
        CustomerApiImpl(supabaseClient = get())
    }
}

// di/DomainModule.kt  
val domainModule = module {
    // Use cases
    factory<GetBusinessProfileUseCase> {
        GetBusinessProfileUseCase(repository = get())
    }
    
    factory<UpdateBusinessProfileUseCase> {
        UpdateBusinessProfileUseCase(repository = get())
    }
    
    factory<GetInvoicesUseCase> {
        GetInvoicesUseCase(repository = get())
    }
    
    factory<CreateInvoiceUseCase> {
        CreateInvoiceUseCase(repository = get())
    }
    
    // Domain services
    single<ValidationService> {
        ValidationService()
    }
    
    single<CalculationService> {
        CalculationService()
    }
}

// di/PresentationModule.kt
val presentationModule = module {
    // ViewModels - scoped to avoid memory leaks
    scope<ComponentActivity> {
        scoped<BusinessProfileViewModel> {
            BusinessProfileViewModel(
                getProfileUseCase = get(),
                updateProfileUseCase = get()
            )
        }
        
        scoped<InvoiceListViewModel> {
            InvoiceListViewModel(
                getInvoicesUseCase = get(),
                createInvoiceUseCase = get()
            )
        }
        
        scoped<AuthViewModel> {
            AuthViewModel(authService = get())
        }
    }
    
    // Navigation
    single<AppNavigator> { AppNavigator() }
    
    // UI helpers
    factory<CurrencyFormatter> { CurrencyFormatter() }
    factory<DateFormatter> { DateFormatter() }
}
```

#### Platform-Specific Modules
```kotlin
// di/PlatformModule.kt (Android)
val androidPlatformModule = module {
    single<PlatformSecureStore> { 
        AndroidSecureStore(context = get()) 
    }
    
    single<PlatformNetworkMonitor> { 
        AndroidNetworkMonitor(context = get()) 
    }
    
    single<Context> {
        androidContext()
    }
    
    // Android-specific services
    single<NotificationManager> {
        get<Context>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}

// di/PlatformModule.kt (iOS)
val iosPlatformModule = module {
    single<PlatformSecureStore> { 
        IOSSecureStore() 
    }
    
    single<PlatformNetworkMonitor> { 
        IOSNetworkMonitor() 
    }
    
    // iOS-specific services would go here
}
```

### Phase 2: Architecture Refactoring (Week 1-2)

#### Application Setup
```kotlin
// QuantiveApplication.kt
class QuantiveApplication {
    fun initializeDI() {
        startKoin {
            logger(if (BuildConfig.DEBUG) PrintLogger() else EmptyLogger())
            
            modules(
                appModule,
                dataModule,
                domainModule,
                presentationModule,
                platformModule // Platform-specific module
            )
        }
    }
    
    fun cleanup() {
        stopKoin()
    }
}

// Platform-specific initialization
// Android: MainActivity or Application class
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        QuantiveApplication().initializeDI()
        
        setContent {
            QuantiveTheme {
                QuantiveApp()
            }
        }
    }
}

// iOS: Main initialization
fun MainViewController() = ComposeUIViewController {
    QuantiveApplication().initializeDI()
    
    QuantiveTheme {
        QuantiveApp()
    }
}
```

#### Compose Integration
```kotlin
// QuantiveApp.kt - Updated with DI
@Composable
fun QuantiveApp() {
    val authViewModel: AuthViewModel = koinViewModel()
    val authState by authViewModel.authState.collectAsState()
    
    when (authState) {
        is AuthState.Loading -> {
            LoadingScreen()
        }
        is AuthState.Authenticated -> {
            MainApp()
        }
        is AuthState.Unauthenticated -> {
            AuthScreen()
        }
    }
}

@Composable
fun BusinessProfileScreen() {
    val viewModel: BusinessProfileViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    // Screen implementation using injected ViewModel
    BusinessProfileContent(
        uiState = uiState,
        onIntent = viewModel::handleIntent
    )
}

// Custom scope for ViewModels to prevent memory leaks
@Composable
inline fun <reified T : ViewModel> koinViewModel(): T {
    val scope = koinInject<ComponentActivity>()
    return getKoin().getScope(scope.toString()).get<T>()
}
```

#### Repository Refactoring
```kotlin
// BusinessProfileRepositoryImpl.kt - Updated with DI
class BusinessProfileRepositoryImpl(
    private val remote: BusinessProfileRemote,
    private val local: BusinessProfileLocal,
    private val cache: BusinessProfileCache
) : BusinessProfileRepository {
    
    override suspend fun getProfile(id: String): Flow<Result<BusinessProfile>> = flow {
        try {
            // Check cache first
            val cachedProfile = cache.getProfile(id)
            if (cachedProfile != null) {
                emit(Result.success(cachedProfile))
            }
            
            // Fetch from remote
            val remoteProfile = remote.getProfile(id)
            if (remoteProfile.isSuccess) {
                val profile = remoteProfile.getOrNull()!!
                
                // Update cache and local storage
                cache.saveProfile(profile)
                local.saveProfile(profile)
                
                emit(Result.success(profile))
            } else {
                // Fallback to local storage
                val localProfile = local.getProfile(id)
                if (localProfile != null) {
                    emit(Result.success(localProfile))
                } else {
                    emit(Result.failure(remoteProfile.exceptionOrNull()!!))
                }
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    override suspend fun updateProfile(profile: BusinessProfile): Result<BusinessProfile> {
        return try {
            val result = remote.updateProfile(profile)
            if (result.isSuccess) {
                val updatedProfile = result.getOrNull()!!
                
                // Update cache and local storage
                cache.saveProfile(updatedProfile)
                local.saveProfile(updatedProfile)
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### Phase 3: Testing Integration (Week 2)

#### Test DI Modules
```kotlin
// di/TestModule.kt
val testAppModule = module {
    // Mock implementations for testing
    single<SupabaseClient> { mockk<SupabaseClient>() }
    single<AuthService> { mockk<AuthService>() }
    single<SecureStore> { mockk<SecureStore>() }
}

val testDataModule = module {
    single<BusinessProfileRepository> { mockk<BusinessProfileRepository>() }
    single<BusinessProfileRemote> { mockk<BusinessProfileRemote>() }
    single<BusinessProfileLocal> { mockk<BusinessProfileLocal>() }
    single<CacheManager> { mockk<CacheManager>() }
}

val testDomainModule = module {
    factory<GetBusinessProfileUseCase> { mockk<GetBusinessProfileUseCase>() }
    factory<UpdateBusinessProfileUseCase> { mockk<UpdateBusinessProfileUseCase>() }
}
```

#### Test Base Classes
```kotlin
// BaseUnitTest.kt
abstract class BaseUnitTest {
    @BeforeTest
    fun setUp() {
        startKoin {
            modules(
                testAppModule,
                testDataModule, 
                testDomainModule
            )
        }
    }
    
    @AfterTest
    fun tearDown() {
        stopKoin()
    }
    
    inline fun <reified T> mockComponent(): T = get()
}

// Repository test example
class BusinessProfileRepositoryTest : BaseUnitTest() {
    private lateinit var repository: BusinessProfileRepository
    private lateinit var mockRemote: BusinessProfileRemote
    private lateinit var mockLocal: BusinessProfileLocal
    private lateinit var mockCache: BusinessProfileCache
    
    @BeforeTest
    override fun setUp() {
        super.setUp()
        
        mockRemote = mockComponent()
        mockLocal = mockComponent()
        mockCache = mockComponent()
        
        repository = BusinessProfileRepositoryImpl(mockRemote, mockLocal, mockCache)
    }
    
    @Test
    fun `getProfile returns cached data when available`() = runTest {
        // Given
        val profileId = "test-id"
        val cachedProfile = BusinessProfile(id = profileId, name = "Test Business")
        
        coEvery { mockCache.getProfile(profileId) } returns cachedProfile
        
        // When
        val result = repository.getProfile(profileId).first()
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(cachedProfile, result.getOrNull())
        coVerify { mockCache.getProfile(profileId) }
    }
}
```

#### ViewModel Testing
```kotlin
class BusinessProfileViewModelTest : BaseUnitTest() {
    private lateinit var viewModel: BusinessProfileViewModel
    private lateinit var mockGetProfileUseCase: GetBusinessProfileUseCase
    private lateinit var mockUpdateProfileUseCase: UpdateBusinessProfileUseCase
    
    @BeforeTest
    override fun setUp() {
        super.setUp()
        
        mockGetProfileUseCase = mockComponent()
        mockUpdateProfileUseCase = mockComponent()
        
        viewModel = BusinessProfileViewModel(mockGetProfileUseCase, mockUpdateProfileUseCase)
    }
    
    @Test
    fun `loadProfile updates UI state correctly`() = runTest {
        // Given
        val profile = BusinessProfile(id = "test", name = "Test Business")
        coEvery { mockGetProfileUseCase(any()) } returns flowOf(Result.success(profile))
        
        // When
        viewModel.handleIntent(BusinessProfileIntent.LoadProfile("test"))
        
        // Then
        val state = viewModel.uiState.value
        assertTrue(state is BusinessProfileUiState.Success)
        assertEquals(profile, (state as BusinessProfileUiState.Success).profile)
    }
}
```

## Migration Strategy

### Phase 1: Gradual Migration
1. **Add Koin alongside existing `AppServices`**
2. **Migrate repositories first** (least dependent)
3. **Migrate ViewModels and use cases**
4. **Finally remove `AppServices`**

### Phase 2: Validation
1. **Run comprehensive tests** after each migration step
2. **Validate dependency graph** with Koin's check feature
3. **Performance testing** to ensure no regression
4. **Memory usage validation**

## Dependencies

- **TECH-DEBT-ARCH-001**: ViewModels needed for proper DI scoping
- **TECH-DEBT-QA-001**: Testing infrastructure works with DI testing modules
- **Clean Architecture**: Proper layer separation needed for DI modules

## Related Issues

- TECH-DEBT-ARCH-001 (ViewModel architecture - proper scoping with DI)
- TECH-DEBT-QA-001 (Testing infrastructure - test DI modules)
- TECH-DEBT-ARCH-002 (Caching - DI for cache dependencies)

## Testing Strategy

### DI Container Testing
```kotlin
@Test
fun `DI container validates successfully`() {
    val koinApp = koinApplication {
        modules(appModule, dataModule, domainModule, presentationModule)
    }
    
    // Verify all dependencies can be resolved
    koinApp.checkModules()
}

@Test
fun `all repositories can be created`() {
    startKoin {
        modules(appModule, dataModule)
    }
    
    // Test repository creation
    val businessRepo: BusinessProfileRepository = get()
    assertNotNull(businessRepo)
    
    stopKoin()
}
```

### Memory Leak Testing with DI
```kotlin
@Test
fun `ViewModels are properly scoped and cleaned up`() = runTest {
    val activity = mockk<ComponentActivity>()
    
    startKoin {
        modules(appModule, presentationModule)
    }
    
    // Create scoped ViewModels
    val scope = getKoin().createScope<ComponentActivity>(activity.toString())
    val viewModel1 = scope.get<BusinessProfileViewModel>()
    val viewModel2 = scope.get<BusinessProfileViewModel>()
    
    // Should return same instance (scoped)
    assertSame(viewModel1, viewModel2)
    
    // Close scope
    scope.close()
    
    // Verify cleanup
    assertTrue(scope.closed)
    
    stopKoin()
}
```

## Success Metrics

- [ ] All manual DI replaced with Koin-based DI
- [ ] Proper dependency scoping implemented (singleton, scoped, factory)
- [ ] 100% of dependencies injectable and mockable for testing
- [ ] DI container validation passes without errors
- [ ] Test execution time not significantly impacted by DI
- [ ] Memory usage not increased due to DI overhead

## Definition of Done

- [ ] Koin framework integrated and configured
- [ ] All architectural layers have proper DI modules
- [ ] AppServices object completely removed
- [ ] Platform-specific dependencies properly handled
- [ ] Comprehensive test DI modules created
- [ ] All ViewModels properly scoped to prevent memory leaks
- [ ] DI container validation integrated into CI/CD
- [ ] Migration completed without functionality regression
- [ ] Code review completed by mobile architect
- [ ] DI architecture documentation updated