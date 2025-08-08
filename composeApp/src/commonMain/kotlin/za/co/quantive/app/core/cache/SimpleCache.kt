package za.co.quantive.app.core.cache

import kotlinx.datetime.Clock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.time.Duration

/**
 * Simple in-memory cache for backend-first architecture
 * Focuses on TTL-based caching without complex persistence
 * Fixed serialization issues for KMP compatibility
 */
class SimpleCache(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    },
) {
    private val cache = mutableMapOf<String, SimpleCacheEntry>()

    /**
     * Store data in cache with TTL using explicit serializer
     */
    fun <T> put(key: String, value: T, serializer: KSerializer<T>, ttl: Duration) {
        val entry = SimpleCacheEntry(
            data = json.encodeToString(serializer, value),
            timestamp = Clock.System.now().toEpochMilliseconds(),
            ttl = ttl.inWholeMilliseconds,
        )
        cache[key] = entry
    }

    /**
     * Store data in cache with TTL using reified generics (convenience method)
     */
    inline fun <reified T> put(key: String, value: T, ttl: Duration) {
        put(key, value, serializer<T>(), ttl)
    }

    /**
     * Retrieve data from cache using explicit serializer
     */
    fun <T> get(key: String, serializer: KSerializer<T>): T? {
        val entry = cache[key] ?: return null

        return if (isExpired(entry)) {
            cache.remove(key)
            null
        } else {
            try {
                json.decodeFromString(serializer, entry.data)
            } catch (e: Exception) {
                cache.remove(key) // Remove corrupted entry
                null
            }
        }
    }

    /**
     * Retrieve data from cache using reified generics (convenience method)
     */
    inline fun <reified T> get(key: String): T? {
        return get(key, serializer<T>())
    }

    /**
     * Check if cache entry exists and is not expired
     */
    fun isValid(key: String): Boolean {
        val entry = cache[key] ?: return false
        return if (isExpired(entry)) {
            cache.remove(key)
            false
        } else {
            true
        }
    }

    /**
     * Remove specific cache entry
     */
    fun invalidate(key: String) {
        cache.remove(key)
    }

    /**
     * Remove all cache entries matching pattern
     */
    fun invalidatePattern(pattern: String) {
        val regex = pattern.replace("*", ".*").toRegex()
        val keysToRemove = cache.keys.filter { regex.matches(it) }
        keysToRemove.forEach { cache.remove(it) }
    }

    /**
     * Clear all cache entries
     */
    fun clearAll() {
        cache.clear()
    }

    /**
     * Get cache size for monitoring
     */
    fun size(): Int = cache.size

    /**
     * Clean up expired entries
     */
    fun cleanup() {
        val now = Clock.System.now().toEpochMilliseconds()
        val expiredKeys = cache.filter { (_, entry) ->
            (now - entry.timestamp) > entry.ttl
        }.keys
        expiredKeys.forEach { cache.remove(it) }
    }

    private fun isExpired(entry: SimpleCacheEntry): Boolean {
        val now = Clock.System.now().toEpochMilliseconds()
        return (now - entry.timestamp) > entry.ttl
    }
}

/**
 * Simple cache entry
 */
@Serializable
data class SimpleCacheEntry(
    val data: String, // JSON serialized data
    val timestamp: Long,
    val ttl: Long, // TTL in milliseconds
)

/**
 * TTL constants as per enterprise blueprint
 */
object CacheTTL {
    val USER_DATA = Duration.parse("5m") // User Data: 5 minutes
    val CONFIG_DATA = Duration.parse("30m") // Configuration: 30 minutes
    val STATIC_CONTENT = Duration.parse("24h") // Static Content: 24 hours
    // Session Data: In-memory only (no TTL needed)
}
