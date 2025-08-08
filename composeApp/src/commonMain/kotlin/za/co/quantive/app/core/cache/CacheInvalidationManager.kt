package za.co.quantive.app.core.cache

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Manages cache invalidation events and strategies
 * Coordinates TTL-based and manual cache invalidation
 */
class CacheInvalidationManager(
    private val simpleCache: SimpleCache,
) {
    private val _invalidationEvents = MutableSharedFlow<CacheInvalidationEvent>()
    val invalidationEvents: SharedFlow<CacheInvalidationEvent> = _invalidationEvents.asSharedFlow()

    /**
     * Manually invalidate specific cache key
     */
    suspend fun invalidate(key: String) {
        simpleCache.invalidate(key)
        _invalidationEvents.emit(CacheInvalidationEvent.KeyInvalidated(key))
    }

    /**
     * Invalidate cache keys matching pattern
     */
    suspend fun invalidatePattern(pattern: String) {
        simpleCache.invalidatePattern(pattern)
        _invalidationEvents.emit(CacheInvalidationEvent.PatternInvalidated(pattern))
    }

    /**
     * Handle user logout - clear all user-specific data
     */
    suspend fun onUserLogout() {
        simpleCache.clearAll()
        _invalidationEvents.emit(CacheInvalidationEvent.UserLogout)
    }

    /**
     * Handle manual refresh request
     */
    suspend fun onManualRefresh(feature: String) {
        val pattern = "${feature}_*"
        invalidatePattern(pattern)
        _invalidationEvents.emit(CacheInvalidationEvent.ManualRefresh(feature))
    }

    /**
     * Clean up expired entries
     */
    suspend fun cleanupExpiredEntries() {
        val beforeSize = simpleCache.size()
        simpleCache.cleanup()
        val afterSize = simpleCache.size()

        if (beforeSize > afterSize) {
            _invalidationEvents.emit(
                CacheInvalidationEvent.ExpiredEntriesCleanup(beforeSize - afterSize),
            )
        }
    }

    /**
     * Handle data update events from backend
     */
    suspend fun onDataUpdated(dataType: String, id: String) {
        // Invalidate specific item
        invalidate("${dataType}_$id")

        // Invalidate related list caches
        invalidatePattern("${dataType}_list_*")

        _invalidationEvents.emit(CacheInvalidationEvent.DataUpdated(dataType, id))
    }
}

/**
 * Cache invalidation events
 */
sealed class CacheInvalidationEvent {
    data class KeyInvalidated(val key: String) : CacheInvalidationEvent()
    data class PatternInvalidated(val pattern: String) : CacheInvalidationEvent()
    object UserLogout : CacheInvalidationEvent()
    data class ManualRefresh(val feature: String) : CacheInvalidationEvent()
    data class ExpiredEntriesCleanup(val entriesRemoved: Int) : CacheInvalidationEvent()
    data class DataUpdated(val dataType: String, val id: String) : CacheInvalidationEvent()
}
