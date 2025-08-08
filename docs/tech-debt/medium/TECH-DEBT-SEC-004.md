# TECH-DEBT-SEC-004: Implement Security Monitoring and Audit Logging

**Status**: Open  
**Priority**: Medium  
**Domain**: Security  
**Effort Estimate**: 2-3 weeks  
**Assigned Agent**: Mobile Security Auditor  
**Created**: 2025-01-08  

## Problem Description

The application lacks comprehensive security monitoring and audit logging capabilities required for enterprise security compliance. No security event tracking, suspicious activity detection, or audit trail functionality is implemented.

**Current Security Monitoring Issues**:
- No audit logging for sensitive operations
- No security event monitoring (failed logins, permission changes)
- No suspicious activity detection
- No compliance audit trail
- Missing security metrics and reporting
- No real-time security alerting

## Blueprint Violation

**Blueprint Requirement**: Enterprise security standards require comprehensive security monitoring and audit capabilities:
- Audit Logging: All security-sensitive operations logged
- Security Monitoring: Real-time detection of suspicious activities  
- Compliance: Audit trails for regulatory compliance
- Incident Response: Security event alerting and response
- Forensics: Detailed security event analysis capabilities

**Current State**: No security monitoring, audit logging, or incident detection

## Affected Files

### Files to Create
- `src/commonMain/kotlin/za/co/quantive/app/core/security/AuditLogger.kt`
- `src/commonMain/kotlin/za/co/quantive/app/core/security/SecurityMonitor.kt`
- `src/commonMain/kotlin/za/co/quantive/app/core/security/ThreatDetector.kt`
- `src/commonMain/kotlin/za/co/quantive/app/core/security/SecurityEventCollector.kt`
- `src/commonMain/kotlin/za/co/quantive/app/core/security/SecurityMetrics.kt`

### Files to Update
- `/composeApp/src/commonMain/kotlin/za/co/quantive/app/auth/AuthService.kt` - Add audit logging
- `/composeApp/src/commonMain/kotlin/za/co/quantive/app/data/remote/SupabaseClient.kt` - Security monitoring
- All repository implementations - Audit data access operations
- All security-sensitive operations across the app

## Risk Assessment

- **Compliance Risk**: High - Missing audit trails for regulatory compliance
- **Security Risk**: High - No detection of security incidents
- **Forensics Risk**: High - Cannot investigate security events
- **Operational Risk**: Medium - No visibility into security posture

## Acceptance Criteria

### Audit Logging System
- [ ] Implement comprehensive audit logging for all sensitive operations
- [ ] Log authentication events (login, logout, failed attempts)
- [ ] Track data access and modification events
- [ ] Log permission and role changes
- [ ] Store audit logs securely with integrity protection

### Security Event Monitoring
- [ ] Real-time monitoring of security-sensitive events
- [ ] Detection of suspicious login patterns
- [ ] Monitoring of data access patterns
- [ ] API abuse and rate limiting detection
- [ ] Anomaly detection for user behavior

### Threat Detection
- [ ] Failed authentication attempt tracking
- [ ] Brute force attack detection
- [ ] Session hijacking detection
- [ ] Data exfiltration pattern detection
- [ ] Privilege escalation detection

### Compliance and Reporting
- [ ] Generate compliance audit reports
- [ ] Security metrics dashboard
- [ ] Incident response workflow integration
- [ ] Regulatory compliance audit trails
- [ ] Security KPI tracking and reporting

## Implementation Strategy

### Phase 1: Audit Logging Infrastructure (Week 1)

#### Core Audit Logging System
```kotlin
// AuditLogger.kt
class AuditLogger(
    private val storage: AuditStorage,
    private val encryption: AuditEncryption
) {
    suspend fun logSecurityEvent(event: SecurityEvent) {
        try {
            val auditEntry = AuditEntry(
                id = UUID.randomUUID().toString(),
                timestamp = Clock.System.now().toEpochMilliseconds(),
                eventType = event.type,
                userId = event.userId,
                sessionId = event.sessionId,
                action = event.action,
                resource = event.resource,
                details = event.details,
                ipAddress = event.ipAddress,
                userAgent = event.userAgent,
                success = event.success,
                riskLevel = calculateRiskLevel(event),
                checksum = calculateChecksum(event)
            )
            
            // Encrypt sensitive audit data
            val encryptedEntry = encryption.encrypt(auditEntry)
            
            // Store audit entry
            storage.store(encryptedEntry)
            
            // Real-time security monitoring
            SecurityMonitor.instance.processEvent(auditEntry)
            
            AuditLogger.d("Security event logged: ${event.type} for user ${event.userId}")
            
        } catch (e: Exception) {
            // Audit logging failure is critical
            SecurityLogger.e("Failed to log security event", e)
            // Consider fallback logging mechanism
        }
    }
    
    suspend fun logAuthEvent(
        userId: String?,
        action: AuthAction,
        success: Boolean,
        details: Map<String, Any> = emptyMap(),
        ipAddress: String? = null
    ) {
        val event = SecurityEvent(
            type = SecurityEventType.AUTHENTICATION,
            userId = userId,
            sessionId = getCurrentSessionId(),
            action = action.name,
            resource = "auth",
            details = details,
            ipAddress = ipAddress,
            userAgent = getUserAgent(),
            success = success
        )
        
        logSecurityEvent(event)
    }
    
    suspend fun logDataAccess(
        userId: String,
        resource: String,
        action: DataAction,
        success: Boolean,
        recordIds: List<String> = emptyList(),
        sensitiveFields: List<String> = emptyList()
    ) {
        val event = SecurityEvent(
            type = SecurityEventType.DATA_ACCESS,
            userId = userId,
            sessionId = getCurrentSessionId(),
            action = action.name,
            resource = resource,
            details = mapOf(
                "record_ids" to recordIds,
                "sensitive_fields" to sensitiveFields,
                "record_count" to recordIds.size
            ),
            ipAddress = getCurrentIpAddress(),
            userAgent = getUserAgent(),
            success = success
        )
        
        logSecurityEvent(event)
    }
    
    suspend fun logPermissionChange(
        adminUserId: String,
        targetUserId: String,
        oldPermissions: List<String>,
        newPermissions: List<String>
    ) {
        val event = SecurityEvent(
            type = SecurityEventType.PERMISSION_CHANGE,
            userId = adminUserId,
            sessionId = getCurrentSessionId(),
            action = "PERMISSION_UPDATE",
            resource = "user_permissions",
            details = mapOf(
                "target_user_id" to targetUserId,
                "old_permissions" to oldPermissions,
                "new_permissions" to newPermissions,
                "permissions_added" to (newPermissions - oldPermissions),
                "permissions_removed" to (oldPermissions - newPermissions)
            ),
            ipAddress = getCurrentIpAddress(),
            userAgent = getUserAgent(),
            success = true
        )
        
        logSecurityEvent(event)
    }
    
    private fun calculateRiskLevel(event: SecurityEvent): RiskLevel {
        return when {
            !event.success && event.type == SecurityEventType.AUTHENTICATION -> RiskLevel.HIGH
            event.type == SecurityEventType.PERMISSION_CHANGE -> RiskLevel.HIGH
            event.type == SecurityEventType.DATA_ACCESS && 
                event.details["sensitive_fields"]?.let { (it as List<*>).isNotEmpty() } == true -> RiskLevel.MEDIUM
            !event.success -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }
    }
    
    private fun calculateChecksum(event: SecurityEvent): String {
        val data = "${event.type}_${event.userId}_${event.action}_${event.resource}_${event.timestamp}"
        return data.sha256()
    }
}

data class SecurityEvent(
    val type: SecurityEventType,
    val userId: String?,
    val sessionId: String?,
    val action: String,
    val resource: String,
    val details: Map<String, Any>,
    val ipAddress: String?,
    val userAgent: String?,
    val success: Boolean
)

data class AuditEntry(
    val id: String,
    val timestamp: Long,
    val eventType: SecurityEventType,
    val userId: String?,
    val sessionId: String?,
    val action: String,
    val resource: String,
    val details: Map<String, Any>,
    val ipAddress: String?,
    val userAgent: String?,
    val success: Boolean,
    val riskLevel: RiskLevel,
    val checksum: String
)

enum class SecurityEventType {
    AUTHENTICATION, DATA_ACCESS, PERMISSION_CHANGE, 
    SESSION_MANAGEMENT, SECURITY_VIOLATION, SYSTEM_ACCESS
}

enum class AuthAction {
    LOGIN, LOGOUT, REFRESH_TOKEN, PASSWORD_CHANGE, 
    ACCOUNT_LOCK, ACCOUNT_UNLOCK, FAILED_LOGIN
}

enum class DataAction {
    READ, CREATE, UPDATE, DELETE, EXPORT, BULK_OPERATION
}

enum class RiskLevel {
    LOW, MEDIUM, HIGH, CRITICAL
}
```

#### Secure Audit Storage
```kotlin
// AuditStorage.kt
interface AuditStorage {
    suspend fun store(entry: EncryptedAuditEntry)
    suspend fun query(filter: AuditFilter): List<EncryptedAuditEntry>
    suspend fun verifyIntegrity(): IntegrityResult
}

class SecureAuditStorage(
    private val localStorage: LocalAuditStorage,
    private val remoteStorage: RemoteAuditStorage,
    private val encryptionKey: ByteArray
) : AuditStorage {
    
    override suspend fun store(entry: EncryptedAuditEntry) {
        // Store locally first for reliability
        localStorage.store(entry)
        
        try {
            // Replicate to secure remote storage
            remoteStorage.store(entry)
        } catch (e: Exception) {
            AuditLogger.w("Failed to replicate audit entry to remote storage", e)
            // Queue for retry
            localStorage.queueForRetry(entry)
        }
    }
    
    override suspend fun query(filter: AuditFilter): List<EncryptedAuditEntry> {
        return try {
            // Try remote first for most up-to-date data
            remoteStorage.query(filter)
        } catch (e: Exception) {
            // Fallback to local storage
            AuditLogger.w("Failed to query remote audit storage, using local", e)
            localStorage.query(filter)
        }
    }
    
    override suspend fun verifyIntegrity(): IntegrityResult {
        val localEntries = localStorage.getAllEntries()
        val remoteEntries = remoteStorage.getAllEntries()
        
        val localHashes = localEntries.map { it.checksum }
        val remoteHashes = remoteEntries.map { it.checksum }
        
        val missing = remoteHashes - localHashes
        val extra = localHashes - remoteHashes
        val corrupted = mutableListOf<String>()
        
        // Verify checksums
        localEntries.forEach { entry ->
            if (!verifyChecksum(entry)) {
                corrupted.add(entry.id)
            }
        }
        
        return IntegrityResult(
            isIntact = missing.isEmpty() && extra.isEmpty() && corrupted.isEmpty(),
            missingEntries = missing.size,
            extraEntries = extra.size,
            corruptedEntries = corrupted.size,
            details = mapOf(
                "missing" to missing,
                "extra" to extra,
                "corrupted" to corrupted
            )
        )
    }
    
    private fun verifyChecksum(entry: EncryptedAuditEntry): Boolean {
        // Decrypt and verify checksum
        val decrypted = decrypt(entry)
        val expectedChecksum = calculateChecksum(decrypted)
        return expectedChecksum == decrypted.checksum
    }
}

data class EncryptedAuditEntry(
    val id: String,
    val encryptedData: ByteArray,
    val checksum: String,
    val timestamp: Long
)

data class AuditFilter(
    val userId: String? = null,
    val eventType: SecurityEventType? = null,
    val startTime: Long? = null,
    val endTime: Long? = null,
    val riskLevel: RiskLevel? = null,
    val success: Boolean? = null
)

data class IntegrityResult(
    val isIntact: Boolean,
    val missingEntries: Int,
    val extraEntries: Int,
    val corruptedEntries: Int,
    val details: Map<String, Any>
)
```

### Phase 2: Security Monitoring and Threat Detection (Week 2)

#### Real-time Security Monitor
```kotlin
// SecurityMonitor.kt
class SecurityMonitor(
    private val threatDetector: ThreatDetector,
    private val alertManager: SecurityAlertManager
) {
    companion object {
        lateinit var instance: SecurityMonitor
    }
    
    private val securityMetrics = SecurityMetrics()
    private val activeThreats = mutableSetOf<ActiveThreat>()
    
    suspend fun processEvent(auditEntry: AuditEntry) {
        // Update security metrics
        securityMetrics.recordEvent(auditEntry)
        
        // Analyze for threats
        val threats = threatDetector.analyze(auditEntry)
        
        threats.forEach { threat ->
            handleThreat(threat, auditEntry)
        }
        
        // Check for patterns across multiple events
        val patternThreats = threatDetector.analyzePatterns(
            recentEvents = getRecentEvents(auditEntry.userId, 1.hours)
        )
        
        patternThreats.forEach { threat ->
            handleThreat(threat, auditEntry)
        }
    }
    
    private suspend fun handleThreat(threat: DetectedThreat, triggeringEvent: AuditEntry) {
        val activeThreat = ActiveThreat(
            id = UUID.randomUUID().toString(),
            type = threat.type,
            severity = threat.severity,
            userId = triggeringEvent.userId,
            sessionId = triggeringEvent.sessionId,
            firstDetected = Clock.System.now().toEpochMilliseconds(),
            lastSeen = Clock.System.now().toEpochMilliseconds(),
            eventCount = 1,
            details = threat.details
        )
        
        activeThreats.add(activeThreat)
        
        // Log threat detection
        SecurityLogger.w("Security threat detected: ${threat.type} for user ${triggeringEvent.userId}")
        
        // Send alerts based on severity
        when (threat.severity) {
            ThreatSeverity.CRITICAL -> {
                alertManager.sendImediateAlert(activeThreat)
                // Consider automatic response (e.g., session termination)
                considerAutomaticResponse(activeThreat)
            }
            ThreatSeverity.HIGH -> {
                alertManager.sendHighPriorityAlert(activeThreat)
            }
            ThreatSeverity.MEDIUM -> {
                alertManager.queueAlert(activeThreat)
            }
            ThreatSeverity.LOW -> {
                // Log only, no immediate action
            }
        }
        
        // Update security metrics
        securityMetrics.recordThreat(threat)
    }
    
    private suspend fun considerAutomaticResponse(threat: ActiveThreat) {
        when (threat.type) {
            ThreatType.BRUTE_FORCE_ATTACK -> {
                // Temporarily lock user account
                threat.userId?.let { userId ->
                    SecurityResponseService.temporaryAccountLock(userId, 15.minutes)
                }
            }
            ThreatType.SESSION_HIJACKING -> {
                // Terminate all sessions for user
                threat.userId?.let { userId ->
                    SessionManager.terminateAllUserSessions(userId)
                }
            }
            ThreatType.DATA_EXFILTRATION -> {
                // Block data access for user temporarily
                threat.userId?.let { userId ->
                    SecurityResponseService.restrictDataAccess(userId, 30.minutes)
                }
            }
            else -> {
                // No automatic response, require manual intervention
            }
        }
    }
    
    fun getSecurityDashboard(): SecurityDashboard {
        return SecurityDashboard(
            activeThreats = activeThreats.toList(),
            metrics = securityMetrics.getCurrentMetrics(),
            recentAlerts = alertManager.getRecentAlerts(),
            systemStatus = getSystemSecurityStatus()
        )
    }
    
    private fun getSystemSecurityStatus(): SystemSecurityStatus {
        val criticalThreats = activeThreats.count { it.severity == ThreatSeverity.CRITICAL }
        val highThreats = activeThreats.count { it.severity == ThreatSeverity.HIGH }
        
        return when {
            criticalThreats > 0 -> SystemSecurityStatus.CRITICAL
            highThreats > 5 -> SystemSecurityStatus.HIGH_RISK
            highThreats > 0 -> SystemSecurityStatus.ELEVATED
            else -> SystemSecurityStatus.NORMAL
        }
    }
}

data class DetectedThreat(
    val type: ThreatType,
    val severity: ThreatSeverity,
    val confidence: Float,
    val details: Map<String, Any>
)

data class ActiveThreat(
    val id: String,
    val type: ThreatType,
    val severity: ThreatSeverity,
    val userId: String?,
    val sessionId: String?,
    val firstDetected: Long,
    val lastSeen: Long,
    val eventCount: Int,
    val details: Map<String, Any>
)

enum class ThreatType {
    BRUTE_FORCE_ATTACK, SESSION_HIJACKING, DATA_EXFILTRATION,
    PRIVILEGE_ESCALATION, SUSPICIOUS_API_USAGE, ACCOUNT_TAKEOVER
}

enum class ThreatSeverity {
    LOW, MEDIUM, HIGH, CRITICAL
}

enum class SystemSecurityStatus {
    NORMAL, ELEVATED, HIGH_RISK, CRITICAL
}
```

#### Threat Detection Engine
```kotlin
// ThreatDetector.kt
class ThreatDetector {
    private val failedLoginTracker = mutableMapOf<String, MutableList<Long>>()
    private val apiUsageTracker = mutableMapOf<String, MutableList<Long>>()
    private val dataAccessTracker = mutableMapOf<String, MutableList<DataAccessEvent>>()
    
    fun analyze(event: AuditEntry): List<DetectedThreat> {
        val threats = mutableListOf<DetectedThreat>()
        
        when (event.eventType) {
            SecurityEventType.AUTHENTICATION -> {
                threats.addAll(analyzeAuthEvent(event))
            }
            SecurityEventType.DATA_ACCESS -> {
                threats.addAll(analyzeDataAccess(event))
            }
            SecurityEventType.PERMISSION_CHANGE -> {
                threats.addAll(analyzePermissionChange(event))
            }
            else -> {
                // Other event types
            }
        }
        
        return threats
    }
    
    private fun analyzeAuthEvent(event: AuditEntry): List<DetectedThreat> {
        val threats = mutableListOf<DetectedThreat>()
        
        if (!event.success && event.action == AuthAction.LOGIN.name) {
            val userId = event.userId ?: event.ipAddress ?: "unknown"
            val failedAttempts = failedLoginTracker.getOrPut(userId) { mutableListOf() }
            
            failedAttempts.add(event.timestamp)
            
            // Remove old attempts (older than 1 hour)
            val oneHourAgo = event.timestamp - 3600000
            failedAttempts.removeAll { it < oneHourAgo }
            
            // Check for brute force attack
            if (failedAttempts.size >= 5) { // 5 failed attempts in 1 hour
                threats.add(
                    DetectedThreat(
                        type = ThreatType.BRUTE_FORCE_ATTACK,
                        severity = ThreatSeverity.HIGH,
                        confidence = 0.9f,
                        details = mapOf(
                            "failed_attempts" to failedAttempts.size,
                            "time_window" to "1 hour",
                            "user_id" to userId
                        )
                    )
                )
            }
        }
        
        // Check for suspicious login patterns
        if (event.success && event.action == AuthAction.LOGIN.name) {
            threats.addAll(analyzeSuspiciousLogin(event))
        }
        
        return threats
    }
    
    private fun analyzeSuspiciousLogin(event: AuditEntry): List<DetectedThreat> {
        val threats = mutableListOf<DetectedThreat>()
        
        // Check for impossible travel (login from different locations too quickly)
        // Check for unusual time patterns
        // Check for new device/user agent
        
        val userAgent = event.userAgent
        val ipAddress = event.ipAddress
        
        if (userAgent != null && isNewDevice(event.userId, userAgent)) {
            threats.add(
                DetectedThreat(
                    type = ThreatType.ACCOUNT_TAKEOVER,
                    severity = ThreatSeverity.MEDIUM,
                    confidence = 0.6f,
                    details = mapOf(
                        "reason" to "new_device",
                        "user_agent" to userAgent
                    )
                )
            )
        }
        
        return threats
    }
    
    private fun analyzeDataAccess(event: AuditEntry): List<DetectedThreat> {
        val threats = mutableListOf<DetectedThreat>()
        
        val userId = event.userId ?: return threats
        val dataAccessEvents = dataAccessTracker.getOrPut(userId) { mutableListOf() }
        
        val accessEvent = DataAccessEvent(
            timestamp = event.timestamp,
            resource = event.resource,
            action = event.action,
            recordCount = (event.details["record_count"] as? Int) ?: 1,
            sensitiveFields = (event.details["sensitive_fields"] as? List<String>) ?: emptyList()
        )
        
        dataAccessEvents.add(accessEvent)
        
        // Remove old events (older than 24 hours)
        val dayAgo = event.timestamp - 86400000
        dataAccessEvents.removeAll { it.timestamp < dayAgo }
        
        // Check for data exfiltration patterns
        val recentAccess = dataAccessEvents.filter { it.timestamp > event.timestamp - 3600000 } // Last hour
        val totalRecords = recentAccess.sumOf { it.recordCount }
        
        if (totalRecords > 1000) { // More than 1000 records accessed in 1 hour
            threats.add(
                DetectedThreat(
                    type = ThreatType.DATA_EXFILTRATION,
                    severity = ThreatSeverity.HIGH,
                    confidence = 0.8f,
                    details = mapOf(
                        "records_accessed" to totalRecords,
                        "time_window" to "1 hour",
                        "access_pattern" to "bulk_access"
                    )
                )
            )
        }
        
        return threats
    }
    
    fun analyzePatterns(recentEvents: List<AuditEntry>): List<DetectedThreat> {
        val threats = mutableListOf<DetectedThreat>()
        
        // Analyze cross-event patterns
        // Look for privilege escalation attempts
        // Detect session hijacking patterns
        // Identify coordinated attacks
        
        return threats
    }
    
    private fun isNewDevice(userId: String?, userAgent: String): Boolean {
        // Check if this user agent has been seen before for this user
        // This would typically query a database of known devices
        return false // Placeholder implementation
    }
}

data class DataAccessEvent(
    val timestamp: Long,
    val resource: String,
    val action: String,
    val recordCount: Int,
    val sensitiveFields: List<String>
)
```

### Phase 3: Integration and Reporting (Week 3)

#### Enhanced AuthService with Audit Logging
```kotlin
// AuthService.kt - Enhanced with security monitoring
class AuthService(
    private val supabaseClient: SupabaseClient,
    private val secureStore: SecureStore,
    private val auditLogger: AuditLogger
) {
    suspend fun signIn(email: String, password: String): Result<Session> {
        val ipAddress = getCurrentIpAddress()
        
        return try {
            val response = supabaseClient.post(
                endpoint = "/auth/v1/token?grant_type=password",
                body = mapOf(
                    "email" to email,
                    "password" to password
                )
            )
            
            if (response.isSuccessful()) {
                val session = parseSessionResponse(response.body)
                secureStore.saveSession(session)
                
                // Log successful authentication
                auditLogger.logAuthEvent(
                    userId = session.user.id,
                    action = AuthAction.LOGIN,
                    success = true,
                    details = mapOf(
                        "email" to email,
                        "login_method" to "email_password"
                    ),
                    ipAddress = ipAddress
                )
                
                Result.success(session)
            } else {
                // Log failed authentication
                auditLogger.logAuthEvent(
                    userId = null,
                    action = AuthAction.FAILED_LOGIN,
                    success = false,
                    details = mapOf(
                        "email" to email,
                        "error" to response.errorMessage,
                        "attempt_method" to "email_password"
                    ),
                    ipAddress = ipAddress
                )
                
                Result.failure(AuthException(response.errorMessage))
            }
        } catch (e: Exception) {
            // Log authentication error
            auditLogger.logAuthEvent(
                userId = null,
                action = AuthAction.FAILED_LOGIN,
                success = false,
                details = mapOf(
                    "email" to email,
                    "error" to e.message,
                    "error_type" to e::class.simpleName
                ),
                ipAddress = ipAddress
            )
            
            Result.failure(e)
        }
    }
    
    suspend fun signOut(): Result<Unit> {
        val currentSession = getCurrentSession()
        
        return try {
            // Call backend logout
            supabaseClient.post("/auth/v1/logout", emptyMap())
            
            // Clear local session
            secureStore.clearSession()
            
            // Log logout
            auditLogger.logAuthEvent(
                userId = currentSession?.user?.id,
                action = AuthAction.LOGOUT,
                success = true,
                details = mapOf("logout_method" to "user_initiated")
            )
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

#### Security Metrics and Reporting
```kotlin
// SecurityMetrics.kt
class SecurityMetrics {
    private val eventCounts = mutableMapOf<SecurityEventType, Long>()
    private val threatCounts = mutableMapOf<ThreatType, Long>()
    private val riskLevelCounts = mutableMapOf<RiskLevel, Long>()
    private var totalEvents = 0L
    private var totalThreats = 0L
    
    fun recordEvent(event: AuditEntry) {
        eventCounts[event.eventType] = eventCounts.getOrDefault(event.eventType, 0) + 1
        riskLevelCounts[event.riskLevel] = riskLevelCounts.getOrDefault(event.riskLevel, 0) + 1
        totalEvents++
    }
    
    fun recordThreat(threat: DetectedThreat) {
        threatCounts[threat.type] = threatCounts.getOrDefault(threat.type, 0) + 1
        totalThreats++
    }
    
    fun getCurrentMetrics(): SecurityMetricsSnapshot {
        return SecurityMetricsSnapshot(
            totalEvents = totalEvents,
            totalThreats = totalThreats,
            eventsByType = eventCounts.toMap(),
            threatsByType = threatCounts.toMap(),
            riskDistribution = riskLevelCounts.toMap(),
            securityScore = calculateSecurityScore(),
            timestamp = Clock.System.now().toEpochMilliseconds()
        )
    }
    
    private fun calculateSecurityScore(): Double {
        // Calculate a security score based on threat frequency and severity
        val baseScore = 100.0
        val threatPenalty = totalThreats * 5.0
        val highRiskPenalty = riskLevelCounts.getOrDefault(RiskLevel.HIGH, 0) * 2.0
        val criticalRiskPenalty = riskLevelCounts.getOrDefault(RiskLevel.CRITICAL, 0) * 10.0
        
        return maxOf(0.0, baseScore - threatPenalty - highRiskPenalty - criticalRiskPenalty)
    }
    
    fun generateComplianceReport(period: ReportPeriod): ComplianceReport {
        return ComplianceReport(
            period = period,
            totalAuditEntries = totalEvents,
            authenticationEvents = eventCounts.getOrDefault(SecurityEventType.AUTHENTICATION, 0),
            dataAccessEvents = eventCounts.getOrDefault(SecurityEventType.DATA_ACCESS, 0),
            permissionChanges = eventCounts.getOrDefault(SecurityEventType.PERMISSION_CHANGE, 0),
            securityIncidents = totalThreats,
            complianceScore = calculateComplianceScore(),
            recommendations = generateRecommendations()
        )
    }
    
    private fun calculateComplianceScore(): Double {
        // Compliance score based on audit coverage and incident rate
        val auditCoverage = if (totalEvents > 0) 100.0 else 0.0
        val incidentRate = if (totalEvents > 0) (totalThreats.toDouble() / totalEvents) * 100 else 0.0
        
        return maxOf(0.0, auditCoverage - incidentRate * 10)
    }
    
    private fun generateRecommendations(): List<String> {
        val recommendations = mutableListOf<String>()
        
        if (totalThreats > 10) {
            recommendations.add("Consider implementing additional security controls due to high threat count")
        }
        
        if (riskLevelCounts.getOrDefault(RiskLevel.CRITICAL, 0) > 0) {
            recommendations.add("Immediate review required for critical security events")
        }
        
        val failedAuthRate = threatCounts.getOrDefault(ThreatType.BRUTE_FORCE_ATTACK, 0)
        if (failedAuthRate > 5) {
            recommendations.add("Consider implementing account lockout policies")
        }
        
        return recommendations
    }
}

data class SecurityMetricsSnapshot(
    val totalEvents: Long,
    val totalThreats: Long,
    val eventsByType: Map<SecurityEventType, Long>,
    val threatsByType: Map<ThreatType, Long>,
    val riskDistribution: Map<RiskLevel, Long>,
    val securityScore: Double,
    val timestamp: Long
)

data class ComplianceReport(
    val period: ReportPeriod,
    val totalAuditEntries: Long,
    val authenticationEvents: Long,
    val dataAccessEvents: Long,
    val permissionChanges: Long,
    val securityIncidents: Long,
    val complianceScore: Double,
    val recommendations: List<String>
)

data class ReportPeriod(
    val startDate: LocalDate,
    val endDate: LocalDate
)
```

## Dependencies

- **TECH-DEBT-SEC-001**: Secure storage needed for audit log encryption
- **TECH-DEBT-SEC-002**: Network security needed for secure audit transmission
- **TECH-DEBT-ARCH-001**: ViewModels for security monitoring UI
- **TECH-DEBT-QA-001**: Testing infrastructure for security testing

## Related Issues

- TECH-DEBT-SEC-001 (Secure storage - encrypted audit storage)
- TECH-DEBT-SEC-002 (Certificate pinning - secure audit transmission)
- TECH-DEBT-SEC-003 (Debug logging - proper production logging)

## Testing Strategy

### Security Event Testing
```kotlin
@Test
fun `audit logger captures authentication events`() = runTest {
    val mockStorage = mockk<AuditStorage>()
    val mockEncryption = mockk<AuditEncryption>()
    val auditLogger = AuditLogger(mockStorage, mockEncryption)
    
    // When
    auditLogger.logAuthEvent(
        userId = "user123",
        action = AuthAction.LOGIN,
        success = true,
        ipAddress = "192.168.1.1"
    )
    
    // Then
    coVerify {
        mockEncryption.encrypt(any<AuditEntry>())
        mockStorage.store(any())
    }
}

@Test
fun `threat detector identifies brute force attacks`() = runTest {
    val detector = ThreatDetector()
    val userId = "attacker123"
    
    // Simulate 5 failed login attempts
    repeat(5) {
        val event = AuditEntry(
            id = UUID.randomUUID().toString(),
            timestamp = Clock.System.now().toEpochMilliseconds(),
            eventType = SecurityEventType.AUTHENTICATION,
            userId = userId,
            sessionId = null,
            action = AuthAction.FAILED_LOGIN.name,
            resource = "auth",
            details = emptyMap(),
            ipAddress = "192.168.1.100",
            userAgent = "Mozilla/5.0",
            success = false,
            riskLevel = RiskLevel.HIGH,
            checksum = "test-checksum"
        )
        
        val threats = detector.analyze(event)
        
        if (it == 4) { // 5th attempt (0-indexed)
            assertTrue(threats.any { threat -> 
                threat.type == ThreatType.BRUTE_FORCE_ATTACK 
            })
        }
    }
}
```

## Success Metrics

- [ ] 100% of security-sensitive operations logged to audit trail
- [ ] Security threat detection accuracy >90%
- [ ] Audit log integrity maintained (zero corruption)
- [ ] Security incident response time <15 minutes
- [ ] Compliance report generation functional
- [ ] Security metrics dashboard operational with real-time data

## Definition of Done

- [ ] Comprehensive audit logging system implemented
- [ ] Real-time security monitoring operational
- [ ] Threat detection engine functional with pattern analysis
- [ ] Secure audit storage with encryption and integrity protection
- [ ] Security metrics and compliance reporting working
- [ ] Integration with all security-sensitive operations
- [ ] Security incident response workflow implemented
- [ ] Performance impact <5% on application response times
- [ ] Security testing validates threat detection accuracy
- [ ] Code review completed by security specialist