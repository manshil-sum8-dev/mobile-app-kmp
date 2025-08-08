package za.co.quantive.app.domain.contact

import kotlinx.serialization.Serializable

/**
 * Comprehensive tax details for South African business compliance
 * All validation and formatting handled by backend for security and accuracy
 *
 * Note: This is a backend-driven model - client only displays backend-provided data
 */
@Serializable
data class TaxDetails(
    // === CORE TAX IDENTIFIERS ===
    /**
     * South African VAT registration number
     * Format: 10-digit number (e.g., 4123456789)
     * Backend validates format and SARS verification
     */
    val vatNumber: String? = null,

    /**
     * Company registration number (CK/CC number)
     * Format varies: CK2020/123456/23 or 2020/123456/07
     */
    val companyRegistration: String? = null,

    /**
     * Income tax reference number
     * Format: 10-digit number for entities
     */
    val taxNumber: String? = null,

    /**
     * PAYE (Pay As You Earn) reference number for employers
     * Format: 10-digit number
     */
    val payeNumber: String? = null,

    /**
     * UIF (Unemployment Insurance Fund) reference number
     * Format: Various formats depending on registration period
     */
    val uifNumber: String? = null,

    /**
     * SDL (Skills Development Levy) reference number
     * Format: 10-digit number (same as PAYE for most businesses)
     */
    val sdlNumber: String? = null,

    /**
     * WCA (Workers Compensation) reference number
     * Format: Varies by province and industry
     */
    val wcaNumber: String? = null,

    // === VAT CONFIGURATION ===
    /**
     * Whether the contact is VAT registered
     */
    val isVatRegistered: Boolean = false,

    /**
     * Applicable VAT rate for this contact
     * Default: 0.15 (15% - current SA VAT rate)
     * Can be 0.0 for zero-rated supplies or exemptions
     */
    val vatRate: Double = if (isVatRegistered) 0.15 else 0.0,

    /**
     * VAT vendor class (Standard, Exempt, Zero-rated, etc.)
     */
    val vatVendorClass: VatVendorClass = VatVendorClass.STANDARD,

    // === TAX COMPLIANCE SETTINGS ===
    /**
     * Whether this contact requires tax certificates (for PAYE compliance)
     */
    val requiresTaxCertificate: Boolean = false,

    /**
     * Whether this contact is a provisional taxpayer
     */
    val isProvisionalTaxpayer: Boolean = false,

    /**
     * BEE (Black Economic Empowerment) level
     * Valid values: 1-8, or null if not applicable/disclosed
     */
    val beeLevel: Int? = null,

    /**
     * BEE status (Exempted Micro Enterprise, Qualifying Small Enterprise, etc.)
     */
    val beeStatus: BeeStatus? = null,

    /**
     * Whether this contact qualifies for small business tax concessions
     */
    val qualifiesForSbc: Boolean = false,

    // === BACKEND VALIDATION STATUS ===
    /**
     * Backend-provided VAT number validation status
     * Only backend can set this after SARS verification
     */
    val vatValidationStatus: ValidationStatus = ValidationStatus.NOT_VALIDATED,

    /**
     * Backend-provided CIPC company registration validation status
     */
    val companyValidationStatus: ValidationStatus = ValidationStatus.NOT_VALIDATED,

    /**
     * Last validation date (ISO date string)
     * Backend sets this when tax details are verified
     */
    val lastValidationDate: String? = null,

    /**
     * Validation expiry date (ISO date string)
     * Backend sets this based on validation requirements
     */
    val validationExpiryDate: String? = null,

    // === DISPLAY FORMATTING (BACKEND PROVIDED) ===
    /**
     * Backend-formatted VAT number for display
     * Backend handles all formatting rules and presentation
     */
    val formattedVatNumber: String? = null,

    /**
     * Backend-formatted company registration for display
     */
    val formattedCompanyRegistration: String? = null,

    /**
     * Backend-calculated tax compliance score (0-100)
     * Based on completeness and validation status
     */
    val taxComplianceScore: Int? = null,

    // === AUDIT TRAIL ===
    /**
     * Last update timestamp (ISO datetime string)
     */
    val updatedAt: String? = null,

    /**
     * User/system that last updated these details
     */
    val updatedBy: String? = null,
) {

    /**
     * Check if basic VAT details are complete
     * Does not validate accuracy - only presence of required fields
     */
    fun hasBasicVatInfo(): Boolean {
        return isVatRegistered && !vatNumber.isNullOrBlank()
    }

    /**
     * Check if company registration details are present
     */
    fun hasCompanyRegistration(): Boolean {
        return !companyRegistration.isNullOrBlank()
    }

    /**
     * Get effective VAT rate for calculations
     * Returns 0.0 if not VAT registered, otherwise returns configured rate
     */
    fun getEffectiveVatRate(): Double {
        return if (isVatRegistered) vatRate else 0.0
    }

    /**
     * Check if tax details are complete enough for compliance
     * Basic check - backend provides comprehensive validation
     */
    fun isCompleteForCompliance(): Boolean {
        return when {
            !isVatRegistered -> true // No VAT registration required
            hasBasicVatInfo() -> vatValidationStatus == ValidationStatus.VALID
            else -> false
        }
    }

    /**
     * Get compliance warning level based on validation status
     */
    fun getComplianceLevel(): ComplianceLevel {
        return when {
            !isVatRegistered -> ComplianceLevel.COMPLIANT
            vatValidationStatus == ValidationStatus.VALID -> ComplianceLevel.COMPLIANT
            vatValidationStatus == ValidationStatus.INVALID -> ComplianceLevel.NON_COMPLIANT
            vatValidationStatus == ValidationStatus.EXPIRED -> ComplianceLevel.ATTENTION_REQUIRED
            else -> ComplianceLevel.PENDING_VALIDATION
        }
    }

    companion object {
        /**
         * Create basic tax details for non-VAT registered contact
         */
        fun nonVatRegistered(): TaxDetails {
            return TaxDetails(
                isVatRegistered = false,
                vatRate = 0.0,
                vatVendorClass = VatVendorClass.NON_VENDOR,
            )
        }

        /**
         * Create template for VAT registered contact
         * Validation and formatting will be handled by backend
         */
        fun vatRegisteredTemplate(vatNumber: String): TaxDetails {
            return TaxDetails(
                vatNumber = vatNumber,
                isVatRegistered = true,
                vatRate = 0.15, // Current SA VAT rate
                vatVendorClass = VatVendorClass.STANDARD,
                vatValidationStatus = ValidationStatus.NOT_VALIDATED,
            )
        }
    }
}

/**
 * VAT vendor classification for South African tax system
 */
@Serializable
enum class VatVendorClass(val displayName: String, val description: String) {
    STANDARD("Standard VAT Vendor", "Standard 15% VAT applies"),
    ZERO_RATED("Zero-Rated Supplier", "0% VAT on qualifying supplies"),
    EXEMPT("Exempt Supplier", "Exempt from VAT on qualifying supplies"),
    NON_VENDOR("Non-VAT Vendor", "Not registered for VAT"),
    INTERNATIONAL("International Supplier", "International supplier - special VAT rules"),
}

/**
 * BEE (Black Economic Empowerment) status classification
 */
@Serializable
enum class BeeStatus(val displayName: String, val description: String) {
    EME("Exempted Micro Enterprise", "Annual turnover ≤ R10 million"),
    QSE("Qualifying Small Enterprise", "Annual turnover > R10m but ≤ R50 million"),
    LARGE("Large Enterprise", "Annual turnover > R50 million"),
    NOT_APPLICABLE("Not Applicable", "BEE classification not applicable"),
    NOT_DISCLOSED("Not Disclosed", "BEE status not disclosed"),
}

/**
 * Validation status for tax details
 */
@Serializable
enum class ValidationStatus(val displayName: String) {
    NOT_VALIDATED("Not Validated"),
    VALID("Valid"),
    INVALID("Invalid"),
    EXPIRED("Expired"),
    PENDING("Pending Validation"),
    ERROR("Validation Error"),
}

/**
 * Tax compliance level assessment
 */
@Serializable
enum class ComplianceLevel(
    val displayName: String,
    val severity: String,
    val color: String,
) {
    COMPLIANT("Compliant", "success", "#10B981"),
    ATTENTION_REQUIRED("Attention Required", "warning", "#F59E0B"),
    NON_COMPLIANT("Non-Compliant", "error", "#EF4444"),
    PENDING_VALIDATION("Pending Validation", "info", "#3B82F6"),
}
