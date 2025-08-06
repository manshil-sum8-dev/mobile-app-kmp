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

### Test Commands
- `./gradlew test` - Run tests (basic Kotlin test support available)

## Architecture

### Project Structure
- **composeApp/**: Main multiplatform module containing shared business logic and UI
  - **commonMain/**: Shared code across all platforms
  - **androidMain/**: Android-specific implementations
  - **iosMain/**: iOS-specific implementations
  - **commonTest/**: Shared test code

### Key Architecture Patterns
- **Clean Architecture**: Domain layer with repositories, use cases, and data sources
- **Repository Pattern**: Data layer abstraction with local/remote implementations
- **Manual Dependency Injection**: Using `AppServices` object for service management
- **Session Management**: Token-based auth with secure storage per platform

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

### Adding New Features
1. Define domain models in `domain/` package
2. Create repository interface in domain layer
3. Implement repository in `data/remote/` with Supabase integration
4. Add service to `AppServices` for DI
5. Create use cases if complex business logic is needed

### Database Changes
1. Create migration using `make supa-diff name="description"`
2. Test migration with `make supa-reset`
3. Update repository implementations as needed