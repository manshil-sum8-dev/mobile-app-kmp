# TECH-DEBT-SEC-002: Implement Certificate Pinning

**Status**: Open  
**Priority**: Critical  
**Domain**: Security  
**Effort Estimate**: 3-5 days  
**Assigned Agent**: Mobile Security Auditor  
**Created**: 2025-01-08  

## Problem Description

The application lacks HTTPS certificate pinning implementation, making it vulnerable to man-in-the-middle (MITM) attacks and certificate substitution attacks. The current Ktor client uses basic HTTPS without SSL pinning validation.

**Security Vulnerabilities**:
- No certificate pinning for Supabase API connections
- Susceptible to MITM attacks with rogue certificates
- No backup certificate pins for certificate rotation
- Missing certificate validation for production API endpoints

## Blueprint Violation

**Blueprint Requirement**: "HTTPS with certificate pinning mandatory"  
**Current State**: Basic HTTPS without certificate validation or pinning

## Affected Files

- `/composeApp/src/commonMain/kotlin/za/co/quantive/app/data/remote/SupabaseClient.kt`
- `/composeApp/src/commonMain/kotlin/za/co/quantive/app/core/network/HttpClientFactory.kt` (if exists)
- Platform-specific HTTP engine configurations

## Risk Assessment

- **Security Risk**: Critical - Vulnerable to MITM attacks
- **Data Protection Risk**: High - API communications can be intercepted
- **Compliance Risk**: Medium - May violate security standards
- **Production Impact**: HIGH - Security vulnerability in production API calls

## Acceptance Criteria

### Certificate Pinning Implementation
- [ ] Implement certificate pinning for production Supabase API
- [ ] Add backup certificate pins for certificate rotation
- [ ] Configure certificate pinning for both primary and fallback certificates
- [ ] Add proper certificate validation error handling

### Production Configuration
- [ ] Pin certificates for `api.supabase.co` production endpoints
- [ ] Configure different pinning for development/staging/production
- [ ] Add certificate expiration monitoring and alerts
- [ ] Implement graceful fallback for certificate validation failures

### Error Handling and Recovery
- [ ] Add proper error handling for certificate validation failures
- [ ] Implement certificate pinning bypass for debugging (development only)
- [ ] Add logging for certificate validation events
- [ ] Create certificate update and rotation procedures

## Implementation Details

### Ktor Client Certificate Pinning
```kotlin
val httpClient = HttpClient(createHttpEngine()) {
    engine {
        https {
            serverName = "api.supabase.co"
            // Primary certificate pin
            addPinnedCertificate("sha256/k2v657xBsOVe1PQRwOsHsw3bsGT2VzIqz5K+59sNQws=")
            // Backup certificate pin for rotation
            addPinnedCertificate("sha256/WoiWRyIOVNa9ihaBciRSC7XHjliYS9VwUGOIud4PB18=")
        }
    }
    
    install(HttpRequestValidator) {
        validateResponse { response ->
            // Additional certificate validation if needed
        }
    }
}
```

### Configuration Management
```kotlin
object CertificatePinning {
    private val productionPins = mapOf(
        "api.supabase.co" to listOf(
            "sha256/k2v657xBsOVe1PQRwOsHsw3bsGT2VzIqz5K+59sNQws=", // Primary
            "sha256/WoiWRyIOVNa9ihaBciRSC7XHjliYS9VwUGOIud4PB18="  // Backup
        )
    )
    
    private val stagingPins = mapOf(
        "staging-api.supabase.co" to listOf(
            "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
        )
    )
    
    fun getPinsForEnvironment(environment: Environment): Map<String, List<String>> {
        return when (environment) {
            Environment.PRODUCTION -> productionPins
            Environment.STAGING -> stagingPins
            Environment.DEVELOPMENT -> emptyMap() // No pinning for dev
        }
    }
}
```

## Certificate Generation Process

### Obtaining Certificate Pins
1. **Extract certificate from production API**:
   ```bash
   # Get certificate
   openssl s_client -connect api.supabase.co:443 -servername api.supabase.co < /dev/null | openssl x509 -outform DER > supabase.crt
   
   # Generate SHA-256 pin
   openssl x509 -in supabase.crt -inform DER -pubkey -noout | openssl rsa -pubin -outform DER | openssl dgst -sha256 -binary | openssl enc -base64
   ```

2. **Verify certificate pins in staging environment**
3. **Test certificate validation with invalid certificates**

## Dependencies

- Requires production API certificate access
- May need coordination with backend team for certificate information
- Should be implemented after TECH-DEBT-SEC-003 (logging removal) to avoid exposing certificate details

## Related Issues

- TECH-DEBT-SEC-001 (Secure storage - both needed for complete security)
- TECH-DEBT-SEC-003 (Debug logging - may expose certificate info)

## Testing Requirements

### Certificate Validation Testing
- [ ] Test with valid certificates (should succeed)
- [ ] Test with invalid/expired certificates (should fail)
- [ ] Test certificate rotation scenarios
- [ ] Test network error handling during certificate validation
- [ ] Performance testing with certificate pinning enabled

### Security Testing
- [ ] Attempt MITM attack with pinning enabled (should block)
- [ ] Test certificate substitution attack (should block)
- [ ] Verify certificate validation error messages don't leak sensitive info
- [ ] Test certificate pinning bypass in development mode

## Environment Configuration

### Development Environment
```kotlin
// No certificate pinning in development
if (BuildConfig.DEBUG) {
    // Allow all certificates for local development
    engine {
        https {
            trustManager = TrustAllX509TrustManager
        }
    }
}
```

### Production Environment
```kotlin
// Strict certificate pinning in production
engine {
    https {
        serverName = BuildConfig.API_HOST
        BuildConfig.CERTIFICATE_PINS.forEach { pin ->
            addPinnedCertificate(pin)
        }
    }
}
```

## Success Metrics

- [ ] Certificate pinning validation passes for production API
- [ ] MITM attack testing fails (pinning blocks attack)
- [ ] Certificate rotation procedure tested successfully
- [ ] No performance degradation from certificate validation
- [ ] Security audit confirms proper certificate pinning implementation

## Definition of Done

- [ ] Certificate pinning implemented for all production API endpoints
- [ ] Backup certificate pins configured for rotation
- [ ] Certificate validation error handling implemented
- [ ] Security testing confirms MITM attack protection
- [ ] Development/production environment configuration working
- [ ] Certificate update procedures documented
- [ ] Code review completed by security specialist
- [ ] Performance testing shows no significant impact