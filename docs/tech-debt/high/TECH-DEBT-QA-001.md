# TECH-DEBT-QA-001: Implement Testing Infrastructure

**Status**: Open  
**Priority**: High  
**Domain**: Quality Assurance  
**Effort Estimate**: 2-3 weeks  
**Assigned Agent**: KMP QA Engineer  
**Created**: 2025-01-08  

## Problem Description

The application lacks comprehensive testing infrastructure required by the enterprise blueprint. Current test coverage is minimal (~15%) and does not meet the 80%+ requirement for shared business logic.

**Current Testing Issues**:
- Only 4 basic test files exist across the entire codebase
- No MockK integration for dependency mocking
- No screenshot testing implementation
- Missing repository layer integration tests
- No ViewModel testing (because ViewModels don't exist yet)
- No performance testing framework

## Blueprint Violation

**Blueprint Requirement**: "Unit Tests: 80%+ coverage for shared business logic" with comprehensive testing strategy:
- Unit Tests: 80%+ coverage for shared business logic
- Integration Tests: API contracts, repository layer
- UI Tests: Critical user journeys, screenshot regression
- Performance Tests: Memory leaks, startup time, UI responsiveness

**Current State**: ~15% test coverage with minimal testing infrastructure

## Affected Files

### Existing Test Files (Minimal)
- `/composeApp/src/commonTest/kotlin/za/co/quantive/app/ComposeAppCommonTest.kt`
- `/composeApp/src/commonTest/kotlin/za/co/quantive/app/data/remote/api/ApiResponseTest.kt`
- `/composeApp/src/commonTest/kotlin/za/co/quantive/app/data/remote/api/SimpleApiTest.kt`
- `/composeApp/src/commonTest/kotlin/za/co/quantive/app/domain/entities/SimpleEntityTest.kt`

### Critical Untested Business Logic
- `/composeApp/src/commonMain/kotlin/za/co/quantive/app/auth/AuthService.kt`
- `/composeApp/src/commonMain/kotlin/za/co/quantive/app/data/remote/BusinessProfileRepositoryImpl.kt`
- `/composeApp/src/commonMain/kotlin/za/co/quantive/app/app/AppServices.kt`
- `/composeApp/src/commonMain/kotlin/za/co/quantive/app/data/remote/SupabaseClient.kt`

### Files Requiring Updates
- `/composeApp/build.gradle.kts` - Add testing dependencies
- `/gradle/libs.versions.toml` - Add testing library versions

## Risk Assessment

- **Quality Risk**: High - No safety net for refactoring or changes
- **Regression Risk**: Critical - No protection against breaking changes
- **Maintainability Risk**: High - Cannot safely refactor code
- **Onboarding Risk**: Medium - New developers cannot verify their changes

## Acceptance Criteria

### Testing Framework Setup
- [ ] Add MockK for multiplatform mocking
- [ ] Configure kotlin-test for shared testing
- [ ] Add Turbine for Flow testing
- [ ] Setup Compose UI testing framework
- [ ] Add Shot library for screenshot testing

### Unit Testing Implementation
- [ ] AuthService comprehensive testing (authentication flows)
- [ ] Repository layer testing (all backend repositories)
- [ ] Use case testing (business logic validation)
- [ ] Cache implementation testing
- [ ] Utility and helper function testing

### Integration Testing Setup
- [ ] API contract testing with mock servers
- [ ] Repository integration tests with fake APIs
- [ ] End-to-end authentication flow testing
- [ ] Database integration testing

### UI Testing Infrastructure
- [ ] Compose UI testing setup
- [ ] Screenshot testing with Shot
- [ ] Critical user journey testing
- [ ] Navigation testing
- [ ] Theme and styling regression tests

### Performance Testing
- [ ] Memory leak detection tests
- [ ] Cold start performance tests
- [ ] UI responsiveness tests (60fps validation)
- [ ] Network performance tests

### Coverage Requirements
- [ ] Achieve 80%+ coverage for commonMain business logic
- [ ] 70%+ coverage for shared repository implementations
- [ ] 60%+ coverage for platform-specific implementations
- [ ] CI/CD coverage reporting and enforcement

## Implementation Plan

### Phase 1: Testing Framework Setup (Week 1)

#### Dependencies to Add
```kotlin
// commonTest
testImplementation("io.mockk:mockk:1.13.8")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
testImplementation("app.cash.turbine:turbine:1.0.0")

// Android-specific testing
androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version")
androidTestImplementation("com.karumi:shot:6.1.0")
androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

// iOS-specific testing - handled by kotlin-test
```

#### Test Configuration
```kotlin
// build.gradle.kts test configuration
kotlin {
    sourceSets {
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.mockk:mockk:1.13.8")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
                implementation("app.cash.turbine:turbine:1.0.0")
            }
        }
    }
}
```

### Phase 2: Core Business Logic Testing (Week 2)

#### AuthService Testing
```kotlin
class AuthServiceTest {
    @Mock
    private lateinit var supabaseClient: SupabaseClient
    @Mock  
    private lateinit var secureStore: SecureStore
    
    private lateinit var authService: AuthService
    
    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        authService = AuthService(supabaseClient, secureStore)
    }
    
    @Test
    fun `signUp creates new user successfully`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val mockResponse = """{"access_token":"mock_token","user":{"id":"123"}}"""
        
        every { supabaseClient.post(any(), any()) } returns mockResponse
        every { secureStore.saveSession(any()) } returns Unit
        
        // When
        val result = authService.signUp(email, password)
        
        // Then
        assertTrue(result.isSuccess)
        verify { secureStore.saveSession(any()) }
    }
}
```

#### Repository Testing Template
```kotlin
class BusinessProfileRepositoryTest {
    @Mock
    private lateinit var remote: BusinessProfileRemote
    @Mock
    private lateinit var local: BusinessProfileLocal
    
    private lateinit var repository: BusinessProfileRepositoryImpl
    
    @Test
    fun `getProfile returns cached data when available`() = runTest {
        // Implementation with proper Flow testing using Turbine
    }
}
```

### Phase 3: UI and Integration Testing (Week 3)

#### Screenshot Testing Setup
```kotlin
class AuthScreenScreenshotTest : AndroidInstrumentedTest() {
    
    @get:Rule
    val composeScreenshotRule = ComposeScreenShotTestRule()
    
    @Test
    fun authScreenStates() {
        composeScreenshotRule.setContent {
            QuantiveTheme {
                // Loading state
                AuthScreen(uiState = AuthUiState.Loading)
            }
        }
        composeScreenshotRule.compareScreenshot("auth_loading")
        
        // Error state
        composeScreenshotRule.setContent {
            AuthScreen(uiState = AuthUiState.Error("Invalid credentials"))
        }
        composeScreenshotRule.compareScreenshot("auth_error")
    }
}
```

## Dependencies

- **TECH-DEBT-ARCH-001**: ViewModels needed for proper ViewModel testing
- **TECH-DEBT-ARCH-002**: Cache system needed for cache testing
- **Feature Scaffolding**: Can generate test templates automatically

## Related Issues

- TECH-DEBT-ARCH-001 (ViewModel architecture - needed for ViewModel tests)
- TECH-DEBT-QA-002 (Quality gates - testing part of CI/CD pipeline)
- TECH-DEBT-ARCH-002 (Caching - cache testing required)

## Gradle Configuration Required

### libs.versions.toml Updates
```toml
[versions]
mockk = "1.13.8"
turbine = "1.0.0"
shot = "6.1.0"
espresso = "3.5.1"

[libraries]
mockk = { module = "io.mockk:mockk", version.ref = "mockk" }
turbine = { module = "app.cash.turbine:turbine", version.ref = "turbine" }
shot = { module = "com.karumi:shot", version.ref = "shot" }
espresso-core = { module = "androidx.test.espresso:espresso-core", version.ref = "espresso" }
```

### Shot Plugin Configuration
```kotlin
// Add to composeApp/build.gradle.kts
plugins {
    id("shot")
}

shot {
    applicationId = "za.co.quantive.app"
}
```

## Testing Strategy

### Test Distribution (Blueprint Requirement)
- **Unit Tests**: 80%+ coverage for shared business logic
- **Integration Tests**: API contracts, repository layer  
- **UI Tests**: Critical user journeys, screenshot regression
- **Performance Tests**: Memory leaks, startup time, UI responsiveness
- **Security Tests**: Authentication flows, data encryption

### Coverage Targets by Layer
- **Domain Layer**: 95%+ (business logic is critical)
- **Data Layer**: 80%+ (repository and API implementations)
- **Presentation Layer**: 60%+ (UI logic and state management)
- **Platform-Specific**: 70%+ (security-critical platform code)

### Continuous Integration
```yaml
# Add to CI/CD pipeline
- name: Run Unit Tests
  run: ./gradlew testDebugUnitTest
  
- name: Run Screenshot Tests  
  run: ./gradlew executeScreenshotTests
  
- name: Generate Coverage Report
  run: ./gradlew koverXmlReport
  
- name: Check Coverage Threshold
  run: ./gradlew koverVerify # Enforces 80% minimum
```

## Success Metrics

- [ ] 80%+ test coverage for commonMain business logic
- [ ] 70%+ test coverage for repository implementations
- [ ] 100% critical user journey coverage with UI tests
- [ ] Zero memory leaks detected in performance tests
- [ ] All screenshot tests passing in CI/CD
- [ ] Test execution time < 5 minutes for full suite

## Definition of Done

- [ ] MockK and testing framework integrated
- [ ] Comprehensive unit tests for AuthService and repositories
- [ ] Integration tests for API contracts
- [ ] Screenshot testing infrastructure working
- [ ] Performance testing framework implemented
- [ ] 80%+ coverage achieved for shared business logic
- [ ] CI/CD pipeline includes all testing phases
- [ ] Code review completed by QA specialist
- [ ] Documentation updated with testing guidelines
- [ ] Developer onboarding includes testing practices