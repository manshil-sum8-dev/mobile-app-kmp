# TECH-DEBT-SEC-003: Remove Debug Logging and Implement Structured Logging

**Status**: Open  
**Priority**: Critical  
**Domain**: Security  
**Effort Estimate**: 2-3 days  
**Assigned Agent**: Mobile Security Auditor  
**Created**: 2025-01-08  

## Problem Description

The application contains debug logging statements that expose sensitive authentication data and API responses in production builds. This creates a critical security vulnerability where credentials and user data can be accessed through device logs.

**Security Vulnerabilities**:
- Authentication tokens logged in plain text
- Full API responses logged including sensitive user data
- Debug println statements active in production builds
- No structured logging framework implemented

## Blueprint Violation

**Blueprint Requirement**: "Napier structured logging by feature/module" with no sensitive data in logs  
**Current State**: Debug println statements exposing sensitive authentication data

## Affected Files

### Files with Security Issues
- `/composeApp/src/commonMain/kotlin/za/co/quantive/app/auth/AuthService.kt:64`
  ```kotlin
  println("DEBUG: Full signup response: $respJson") // CRITICAL: Exposes auth tokens
  ```

### Files Requiring Logging Updates
- All service classes with potential debug logging
- Repository implementations
- Network client implementations  
- Any file using `println()` for debugging

## Risk Assessment

- **Security Risk**: Critical - Authentication tokens exposed in logs
- **Privacy Risk**: High - User data accessible through log files
- **Compliance Risk**: High - May violate data protection regulations
- **Production Impact**: CRITICAL - Active vulnerability in production

## Acceptance Criteria

### Remove Debug Logging
- [ ] Remove all `println()` statements containing sensitive data
- [ ] Remove all debug logging that exposes authentication tokens
- [ ] Remove API response logging in production code
- [ ] Audit all logging statements for sensitive data exposure

### Implement Structured Logging
- [ ] Add Napier logging framework dependency
- [ ] Implement feature-tagged logging system following blueprint template
- [ ] Create logging configuration for different build types
- [ ] Add proper log levels (DEBUG, INFO, WARN, ERROR)

### Security-Safe Logging
- [ ] Implement log sanitization for sensitive data
- [ ] Add logging policy that prohibits credential logging
- [ ] Create safe logging utilities for development debugging
- [ ] Add automated checks to prevent sensitive data logging

## Implementation Details

### Napier Integration (Blueprint Requirement)
```kotlin
// Add Napier dependency
implementation("io.github.aakira:napier:2.7.1")

// Feature-tagged logging implementation
object AuthLogger {
    private const val TAG = "Auth"
    
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
```

### Secure Logging Utilities
```kotlin
object SecureLogging {
    // Safe logging that masks sensitive data
    fun logApiCall(endpoint: String, success: Boolean, duration: Long) {
        if (BuildConfig.DEBUG) {
            Napier.d("API Call") { 
                "endpoint=${endpoint.maskSensitive()}, success=$success, duration=${duration}ms" 
            }
        }
    }
    
    // Extension to mask sensitive data
    private fun String.maskSensitive(): String {
        return when {
            contains("token", ignoreCase = true) -> "***MASKED***"
            contains("password", ignoreCase = true) -> "***MASKED***"
            contains("key", ignoreCase = true) -> "***MASKED***"
            else -> this
        }
    }
}
```

### Production Logging Configuration
```kotlin
// Application initialization
class QuantiveApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeLogging()
    }
    
    private fun initializeLogging() {
        if (BuildConfig.DEBUG) {
            // Verbose logging in debug builds
            Napier.base(DebugAntilog())
        } else {
            // Minimal logging in production
            Napier.base(CrashReportingAntilog())
        }
    }
}
```

## Critical Fixes Required

### AuthService.kt Line 64
```kotlin
// REMOVE THIS IMMEDIATELY:
println("DEBUG: Full signup response: $respJson")

// REPLACE WITH:
AuthLogger.d("User signup completed", mapOf(
    "success" to respJson.contains("access_token"),
    "user_id" to "***MASKED***"
))
```

### Safe API Response Logging
```kotlin
// Instead of logging full responses:
// println("API Response: $response") // NEVER DO THIS

// Use structured, safe logging:
ApiLogger.d("API call completed", mapOf(
    "endpoint" to endpoint.path,
    "status_code" to response.status.value,
    "duration_ms" to duration,
    "success" to response.status.isSuccess()
))
```

## Dependencies

- **Napier library**: Must be added to shared dependencies
- **Crash reporting**: Should integrate with TECH-DEBT-SEC-005 (GDPR compliance)
- **Build configuration**: Requires proper DEBUG/RELEASE build type handling

## Related Issues

- TECH-DEBT-SEC-001 (Secure storage - both critical for security)
- TECH-DEBT-SEC-002 (Certificate pinning - may generate logs)
- TECH-DEBT-QA-001 (Testing - proper logging needed for test debugging)

## Gradle Dependencies
```kotlin
commonMain {
    dependencies {
        implementation("io.github.aakira:napier:2.7.1")
    }
}

androidMain {
    dependencies {
        implementation("com.jakewharton.timber:timber:5.0.1") // Optional: Android-specific
    }
}
```

## Security Audit Checklist

### Code Review Requirements
- [ ] No `println()` statements in production code
- [ ] No authentication token logging
- [ ] No user PII in log statements  
- [ ] No API keys or secrets in logs
- [ ] All sensitive data properly masked

### Testing Requirements
- [ ] Verify no sensitive data in production logs
- [ ] Test log output in release builds
- [ ] Confirm structured logging works correctly
- [ ] Validate log level filtering works
- [ ] Test crash reporting integration

## Migration Strategy

### Phase 1: Critical Removal (Day 1)
- Remove all sensitive data logging immediately
- Add temporary safe logging for critical debugging

### Phase 2: Napier Implementation (Days 2-3)  
- Add Napier dependency and configuration
- Implement structured logging for all features
- Create logging utilities and policies

### Success Metrics

- [ ] Zero sensitive data found in production logs
- [ ] Structured logging working across all features
- [ ] Build-specific log level configuration working
- [ ] Security audit passes with no log-related vulnerabilities
- [ ] Development debugging capability maintained safely

## Definition of Done

- [ ] All sensitive debug logging removed
- [ ] Napier structured logging implemented
- [ ] Feature-tagged logging system working
- [ ] Production build logging sanitized
- [ ] Security review confirms no data exposure
- [ ] Development debugging workflows updated
- [ ] Code review completed by security specialist
- [ ] Documentation updated with logging guidelines