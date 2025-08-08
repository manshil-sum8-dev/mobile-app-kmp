package za.co.quantive.app.core.cache

import kotlinx.coroutines.flow.SharedFlow

/**
 * Central cache management for coordinating all app caches
 * Simplified version focusing on core functionality
 */
class CacheManager(
    private val simpleCache: SimpleCache,
    private val invalidationManager: CacheInvalidationManager,
) {

    /**
     * Initialize cache system
     */
    suspend fun initialize() {
        cleanupExpiredEntries()
    }

    /**
     * Clear all caches (useful for logout, data corruption, etc.)
     */
    suspend fun clearAllCaches() {
        simpleCache.clearAll()
    }

    /**
     * Clear caches for specific feature
     */
    suspend fun clearFeatureCache(feature: String) {
        invalidationManager.invalidatePattern("${feature}_*")
    }

    /**
     * Force refresh specific feature (invalidate + clear cache)
     */
    suspend fun forceRefresh(feature: String) {
        invalidationManager.onManualRefresh(feature)
    }

    /**
     * Get basic cache statistics
     */
    suspend fun getCacheStats(): CacheStats {
        return CacheStats(
            totalEntries = simpleCache.size(),
            estimatedMemoryUsage = simpleCache.size() * 2048L, // 2KB per entry estimate
        )
    }

    /**
     * Perform periodic cache maintenance
     */
    suspend fun performMaintenance() {
        cleanupExpiredEntries()
    }

    /**
     * Clean up expired cache entries
     */
    suspend fun cleanupExpiredEntries() {
        invalidationManager.cleanupExpiredEntries()
    }

    /**
     * Handle user logout
     */
    suspend fun handleUserLogout() {
        invalidationManager.onUserLogout()
    }

    /**
     * Handle data updates
     */
    suspend fun handleDataUpdate(dataType: String, id: String) {
        invalidationManager.onDataUpdated(dataType, id)
    }

    /**
     * Get cache invalidation events
     */
    val invalidationEvents: SharedFlow<CacheInvalidationEvent> = invalidationManager.invalidationEvents

    /**
     * Access to underlying simple cache for direct operations
     */
    val cache: SimpleCache = simpleCache
}

/**
 * Basic cache statistics
 */
data class CacheStats(
    val totalEntries: Int,
    val estimatedMemoryUsage: Long,
)
