package za.co.quantive.app.domain.shared

import kotlinx.serialization.Serializable

/**
 * Classification of available business reports for analytics export
 */
@Serializable
enum class ReportType(val displayName: String, val description: String) {
    FINANCIAL_SUMMARY(
        "Financial Summary",
        "Overview of business financial performance including revenue, expenses, and profit",
    ),
    INVOICE_ANALYSIS(
        "Invoice Analysis",
        "Detailed analysis of invoicing patterns, payment times, and invoice performance",
    ),
    CUSTOMER_REPORT(
        "Customer Report",
        "Customer analytics including acquisition, retention, and revenue per customer",
    ),
    SUPPLIER_REPORT(
        "Supplier Report",
        "Analysis of supplier relationships, payment patterns, and costs",
    ),
    TAX_SUMMARY(
        "Tax Summary",
        "Comprehensive tax reporting including VAT, withholding tax, and compliance data",
    ),
    PAYMENT_ANALYSIS(
        "Payment Analysis",
        "Payment trends, methods, and outstanding amounts analysis",
    ),
    CASH_FLOW(
        "Cash Flow",
        "Cash flow analysis showing inflows, outflows, and projections",
    ),
    PROFIT_LOSS(
        "Profit & Loss",
        "Detailed profit and loss statement with period comparisons",
    ),
    BALANCE_SHEET(
        "Balance Sheet",
        "Assets, liabilities, and equity statement",
    ),
    CUSTOM_REPORT(
        "Custom Report",
        "User-defined report with custom parameters and data selections",
    ),
    ;

    /**
     * Returns true if this report type requires a date range
     */
    fun requiresDateRange(): Boolean = when (this) {
        BALANCE_SHEET -> false // Balance sheet is point-in-time
        else -> true
    }

    /**
     * Returns true if this report type supports different granularities (daily, monthly, etc.)
     */
    fun supportsGranularity(): Boolean = when (this) {
        CASH_FLOW, PROFIT_LOSS, FINANCIAL_SUMMARY, PAYMENT_ANALYSIS -> true
        else -> false
    }

    /**
     * Returns true if this report type includes tax-related data
     */
    fun includesTaxData(): Boolean = when (this) {
        TAX_SUMMARY, PROFIT_LOSS, BALANCE_SHEET, FINANCIAL_SUMMARY -> true
        else -> false
    }

    /**
     * Returns the recommended export formats for this report type
     */
    fun getRecommendedFormats(): List<ExportFormat> = when (this) {
        FINANCIAL_SUMMARY, PROFIT_LOSS, BALANCE_SHEET -> listOf(ExportFormat.PDF, ExportFormat.EXCEL)
        INVOICE_ANALYSIS, CUSTOMER_REPORT, PAYMENT_ANALYSIS -> listOf(ExportFormat.EXCEL, ExportFormat.CSV)
        TAX_SUMMARY -> listOf(ExportFormat.PDF, ExportFormat.EXCEL)
        CASH_FLOW -> listOf(ExportFormat.PDF, ExportFormat.EXCEL)
        SUPPLIER_REPORT -> listOf(ExportFormat.EXCEL, ExportFormat.CSV)
        CUSTOM_REPORT -> ExportFormat.entries // All formats supported
    }
}
