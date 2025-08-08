package za.co.quantive.app.domain.shared

import kotlinx.serialization.Serializable

/**
 * Supported export formats for business reports
 */
@Serializable
enum class ExportFormat(
    val displayName: String,
    val fileExtension: String,
    val mimeType: String,
    val description: String,
) {
    PDF(
        displayName = "PDF Document",
        fileExtension = "pdf",
        mimeType = "application/pdf",
        description = "Portable Document Format - ideal for sharing and printing",
    ),
    EXCEL(
        displayName = "Excel Spreadsheet",
        fileExtension = "xlsx",
        mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        description = "Microsoft Excel format - ideal for data analysis and calculations",
    ),
    CSV(
        displayName = "CSV File",
        fileExtension = "csv",
        mimeType = "text/csv",
        description = "Comma-separated values - universal data format",
    ),
    JSON(
        displayName = "JSON Data",
        fileExtension = "json",
        mimeType = "application/json",
        description = "JavaScript Object Notation - for API integration and data exchange",
    ),
    ;

    /**
     * Returns true if this format supports complex formatting (charts, images, etc.)
     */
    fun supportsComplexFormatting(): Boolean = when (this) {
        PDF, EXCEL -> true
        CSV, JSON -> false
    }

    /**
     * Returns true if this format is human-readable without special tools
     */
    fun isHumanReadable(): Boolean = when (this) {
        CSV, JSON -> true
        PDF, EXCEL -> false // Requires specific viewers/editors
    }

    /**
     * Returns true if this format supports multiple sheets/pages
     */
    fun supportsMultipleSheets(): Boolean = when (this) {
        EXCEL -> true
        PDF, CSV, JSON -> false
    }

    /**
     * Returns the typical file size category for this format
     */
    fun getFileSizeCategory(): FileSizeCategory = when (this) {
        JSON -> FileSizeCategory.SMALL
        CSV -> FileSizeCategory.MEDIUM
        PDF -> FileSizeCategory.MEDIUM
        EXCEL -> FileSizeCategory.LARGE
    }

    /**
     * Returns true if this format is suitable for automated processing
     */
    fun isMachineReadable(): Boolean = when (this) {
        CSV, JSON -> true
        EXCEL -> true // With appropriate libraries
        PDF -> false // Requires OCR for data extraction
    }

    enum class FileSizeCategory {
        SMALL, MEDIUM, LARGE
    }
}
