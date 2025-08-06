package za.co.quantive.app.data.remote.api

import kotlinx.serialization.Serializable

/**
 * Standard API response wrapper for backend communication
 */
@Serializable
data class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val success: Boolean = false,
    val error: String? = null
) {
    fun isSuccess(): Boolean = success && data != null
    
    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(data = data, success = true)
        }
        
        fun <T> error(message: String): ApiResponse<T> {
            return ApiResponse(message = message, error = message, success = false)
        }
    }
}

/**
 * Paginated response for list endpoints
 */
@Serializable
data class PaginatedResponse<T>(
    val data: List<T>,
    val pagination: PaginationInfo
)

/**
 * Pagination information
 */
@Serializable
data class PaginationInfo(
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int = if (limit > 0) (total + limit - 1) / limit else 0,
    val hasNext: Boolean = page < totalPages - 1,
    val hasPrevious: Boolean = page > 0
)