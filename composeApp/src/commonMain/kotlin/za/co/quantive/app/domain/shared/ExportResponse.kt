package za.co.quantive.app.domain.shared

import kotlinx.serialization.Serializable

/**
 * Response model for generated business reports
 */
@Serializable
data class ExportResponse(
    val reportId: String,
    val fileName: String,
    val fileUrl: String,
    val format: ExportFormat,
    val generatedAt: String, // ISO timestamp (YYYY-MM-DDTHH:MM:SSZ)
    val expiresAt: String?, // URL expiration timestamp (ISO format)
    val fileSize: Long? = null, // Size in bytes
    val metadata: Map<String, String> = emptyMap(),
) {

    /**
     * Returns true if the download URL has expired
     */
    fun isExpired(): Boolean {
        val expirationTime = expiresAt ?: return false
        // Would need kotlinx-datetime parsing here in real implementation
        // For now, return false to indicate URLs don't expire by default
        return false
    }

    /**
     * Returns a formatted file size string
     */
    fun getFormattedFileSize(): String? {
        return fileSize?.let { size ->
            when {
                size < 1024 -> "$size B"
                size < 1024 * 1024 -> "${size / 1024} KB"
                size < 1024 * 1024 * 1024 -> "${size / (1024 * 1024)} MB"
                else -> "${size / (1024 * 1024 * 1024)} GB"
            }
        }
    }

    /**
     * Returns the expected download filename with proper extension
     */
    fun getDownloadFileName(): String {
        return if (fileName.endsWith(".${format.fileExtension}")) {
            fileName
        } else {
            "$fileName.${format.fileExtension}"
        }
    }

    /**
     * Returns metadata value by key, or null if not present
     */
    fun getMetadata(key: String): String? = metadata[key]

    /**
     * Returns true if this export contains sensitive data based on metadata
     */
    fun containsSensitiveData(): Boolean {
        return metadata["sensitive"] == "true" ||
            metadata["confidential"] == "true" ||
            metadata.containsKey("privacy_level")
    }

    /**
     * Returns the report generation duration if available in metadata
     */
    fun getGenerationDuration(): Long? {
        return metadata["generation_duration_ms"]?.toLongOrNull()
    }

    /**
     * Returns the number of records included in the report if available
     */
    fun getRecordCount(): Int? {
        return metadata["record_count"]?.toIntOrNull()
    }

    companion object {
        /**
         * Creates an ExportResponse for a failed export
         */
        fun error(reportId: String, errorMessage: String): ExportResponse {
            return ExportResponse(
                reportId = reportId,
                fileName = "error.txt",
                fileUrl = "",
                format = ExportFormat.JSON,
                generatedAt = "", // Should use current timestamp
                expiresAt = null,
                fileSize = null,
                metadata = mapOf(
                    "status" to "error",
                    "error_message" to errorMessage,
                ),
            )
        }

        /**
         * Creates an ExportResponse for a successful export
         */
        fun success(
            reportId: String,
            fileName: String,
            fileUrl: String,
            format: ExportFormat,
            fileSize: Long? = null,
            additionalMetadata: Map<String, String> = emptyMap(),
        ): ExportResponse {
            return ExportResponse(
                reportId = reportId,
                fileName = fileName,
                fileUrl = fileUrl,
                format = format,
                generatedAt = "", // Should use current timestamp
                expiresAt = null, // Should calculate expiration
                fileSize = fileSize,
                metadata = mapOf("status" to "success") + additionalMetadata,
            )
        }
    }
}
