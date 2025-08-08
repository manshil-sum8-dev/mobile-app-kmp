# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Kotlin Multiplatform mobile application (Quantive) targeting Android and iOS platforms using Compose Multiplatform. The app integrates with a local Supabase backend for data persistence and authentication.

**Package Structure**: `za.co.quantive.app`
**Main Components**: Uses Quantive design system with Material 3 Expressive features

## Build & Development Commands

### Gradle Commands
- `./gradlew :composeApp:assembleDebug` - Build Android debug APK
- `./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64` - Build iOS simulator framework
- `./gradlew clean` - Clean build artifacts

### Makefile Commands (preferred)
- `make android-build` - Build Android debug APK
- `make ios-framework` - Build iOS simulator framework
- `make clean` - Clean build artifacts

### Supabase Backend Commands
- `make supa-start` - Start local Supabase instance (isolated ports)
- `make supa-stop` - Stop local Supabase
- `make supa-status` - Show running containers
- `make supa-reset` - Reset database (drops, migrates, seeds)
- `make supa-migrate` - Run migrations
- `make supa-diff name=MSG` - Create migration diff

### Feature Scaffolding Commands (PRIMARY DEVELOPMENT TOOL)
- `make scaffold-feature` - Interactive feature creation wizard (RECOMMENDED)
- `./gradlew scaffoldFeature --no-daemon` - Interactive wizard (direct Gradle)
- `./gradlew createFeature -PfeatureName=FeatureName -PfeatureType=crud` - Direct feature creation
- Configuration cache warnings can be ignored - functionality works correctly

### Test Commands
- `./gradlew test` - Run tests (basic Kotlin test support available)

## Architecture

### **⚠️ CRITICAL: Enterprise Development Standards**
**ALL development MUST follow the comprehensive blueprint at `docs/blueprint.md`**

This project follows **Enterprise-Grade KMP Development Standards** with strict enforcement of:
- **SOLID, DRY, KISS, YAGNI** principles with zero tolerance for over-engineering
- **Backend-Driven Architecture** with smart TTL-based caching (NOT offline-first)
- **Material 3 Expressive** design system with platform adaptations
- **Security Standards** with SecureStorage expect/actual and certificate pinning
- **Performance Requirements** < 2s cold start, 60fps UI, < 100MB memory baseline
- **Testing Standards** 80%+ shared test coverage with screenshot regression
- **Quality Gates** ktlint + detekt with KMP-specific rules (zero warnings)

### Project Structure
- **composeApp/**: Main multiplatform module containing shared business logic and UI
  - **commonMain/**: Shared code across all platforms (70% of test coverage here)
  - **androidMain/**: Android-specific implementations
  - **iosMain/**: iOS-specific implementations
  - **commonTest/**: Shared test code

### Key Architecture Patterns
- **Clean Architecture + Backend-Driven**: Domain layer with backend-first repositories
- **Repository Pattern**: API-first with strategic TTL-based caching for performance
- **Smart Caching Strategy**: Performance optimization (NOT offline capability)
  - User Data: 5min TTL
  - Configuration: 30min TTL  
  - Static Content: 24hr TTL
  - Session Data: In-memory only
- **Manual Dependency Injection**: Using `AppServices` object (transitioning to Koin)
- **Session Management**: Token-based auth with platform-specific secure storage
- **REST vs RPC Pattern**: CRUD operations use REST APIs, heavy computations use RPC

### Core Components

#### Data Layer
- `SupabaseClient` - HTTP client wrapper for Supabase API calls
- `BusinessProfileRepository` - Domain interface for profile data operations
- `BusinessProfileRepositoryImpl` - Remote implementation using Supabase
- `BusinessProfileLocal` - Local data source (placeholder)

#### Authentication
- `AuthService` - Handles signup, signin, and token refresh via Supabase Auth API
- `Session` - Data class representing user session with tokens
- `SecureStore` - Platform-specific secure storage for sensitive data

#### Security
- Row-Level Security (RLS) enabled on all database tables
- Platform-specific secure storage implementations:
  - Android: Uses Android Keystore system
  - iOS: Uses iOS Keychain services

### Key Files
- `App.kt` - Main Compose UI entry point
- `AppServices.kt` - Manual DI container and session management
- `Platform.kt` - Platform-specific implementations interface

## Supabase Backend

### Configuration
- **Isolated Ports**: API (54322), DB (54329), Studio (54328)
- **Config**: `supabase/quantive-backend/config.toml`
- **Migrations**: Located in `supabase/quantive-backend/migrations/`

### Important Notes
- Never use service role keys in mobile app - only anon key
- All app tables require authentication (`owner_id = auth.uid()`)
- For device testing, use host machine IP instead of localhost

### Build Configuration
- **Android**: API 24+, targets API 35, Java 11
- **iOS**: Supports x64, ARM64, and Simulator ARM64
- **Dependencies**: Ktor for networking, Koin for DI, Compose Multiplatform for UI

## Development Notes

### Networking
- Uses Ktor client with content negotiation and JSON serialization
- Bearer token authentication via access token provider pattern
- Automatic token refresh handling in `AppServices`

### Settings Management
- Uses `multiplatform-settings` library for cross-platform preferences
- Secure credentials stored via platform-specific secure storage

### Testing
- Basic Kotlin test framework setup
- Test files located in `commonTest/` for shared test logic

## Common Tasks

### Adding New Features (**USE SCAFFOLDING FIRST**)
**ALWAYS use feature scaffolding system before manual implementation:**
1. **Run scaffolding**: `./gradlew createFeature` - Interactive wizard will generate complete feature structure
2. **Manual steps** (only if scaffolding doesn't cover specific needs):
   - Define domain models in `domain/` package  
   - Create repository interface in domain layer
   - Implement repository in `data/remote/` with Supabase integration
   - Add service to `AppServices` for DI
   - Create use cases if complex business logic is needed

**Feature scaffolding generates**:
- Complete Clean Architecture structure (domain/data/presentation layers)
- Repository pattern with REST/RPC API separation  
- ViewModel with MVI pattern
- Compose UI screens with Material 3 components
- Unit test templates with MockK setup
- Integration with dependency injection

### Database Changes
1. Create migration using `make supa-diff name="description"`
2. Test migration with `make supa-reset`
3. Update repository implementations as needed

## ⚠️ CRITICAL VIOLATIONS - MUST BE FIXED

### Current Architecture Violations
**The current state violates several enterprise requirements:**

#### 🚨 **Smart Caching DISABLED** - Major Violation
- `SimpleCache.kt.disabled` due to compilation issues
- Using no-op placeholder cache implementations
- **Impact**: Cannot achieve 50ms response time requirement
- **Fix Required**: Resolve kotlinx.serialization issues and re-enable TTL caching

#### 🚨 **Missing Analytics RPC Implementation**
- `BackendAnalyticsRepository.kt.disabled`
- Heavy computation pattern not implemented
- **Violates**: REST vs RPC separation requirement
- **Fix Required**: Complete analytics RPC implementation

#### 🚨 **Incomplete API Implementations**
- Multiple endpoints return `ApiResponse.error("Not implemented")`
- **Violates**: Backend-driven architecture pattern
- **Fix Required**: Implement all API endpoints with Supabase integration

#### ⚠️ **Missing Quality Gates**
- No ktlint configuration
- No detekt with KMP-specific rules
- No pre-commit hooks
- **Fix Required**: Add all quality enforcement mechanisms per blueprint

#### ⚠️ **Missing Testing Infrastructure**  
- No screenshot testing setup (Shot library)
- Missing structured testing templates
- No 80%+ coverage enforcement
- **Fix Required**: Implement comprehensive testing strategy

### Next Steps to Achieve Compliance
1. **Fix caching infrastructure** - Top priority for performance requirements
2. **Implement analytics RPC** - Complete heavy computation pattern  
3. **Add quality gates** - ktlint, detekt, pre-commit hooks
4. **Setup testing infrastructure** - Shot, structured templates, coverage
5. **Complete API implementations** - All endpoints must work with Supabase backend

**All development must pause until these critical violations are resolved.**