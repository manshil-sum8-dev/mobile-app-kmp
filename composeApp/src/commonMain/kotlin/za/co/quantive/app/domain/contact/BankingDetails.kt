package za.co.quantive.app.domain.contact

import kotlinx.serialization.Serializable

/**
 * Comprehensive banking details for payment processing and financial operations
 * All validation and formatting handled by backend for security compliance
 *
 * Note: This is a backend-driven model - client only displays backend-provided data
 * Sensitive banking information requires special handling and encryption
 */
@Serializable
data class BankingDetails(
    // === CORE BANKING INFORMATION ===
    /**
     * Bank name (e.g., "Standard Bank", "FNB", "Nedbank", "ABSA")
     * Backend validates against official bank registry
     */
    val bankName: String,

    /**
     * Account holder name as it appears on bank statements
     * Must match legal entity name for business accounts
     */
    val accountHolderName: String,

    /**
     * Bank account number
     * Backend handles validation and secure storage
     */
    val accountNumber: String,

    /**
     * South African bank branch code (6-digit format)
     * Some banks use universal branch codes
     */
    val branchCode: String? = null,

    /**
     * Account type classification
     */
    val accountType: BankAccountType = BankAccountType.CURRENT,

    /**
     * Bank's SWIFT/BIC code for international transfers
     * Required for international payments
     */
    val swiftCode: String? = null,

    /**
     * IBAN (International Bank Account Number) if applicable
     * Used for international transfers in some regions
     */
    val iban: String? = null,

    // === PAYMENT PREFERENCES ===
    /**
     * Preferred payment reference for this account
     * Used as default reference for payments
     */
    val paymentReference: String? = null,

    /**
     * Whether this is the primary/default banking account
     */
    val isPrimary: Boolean = false,

    /**
     * Supported payment methods for this account
     */
    val supportedPaymentMethods: List<PaymentMethod> = listOf(PaymentMethod.EFT),

    /**
     * Currency code (ISO 4217) - defaults to ZAR
     */
    val currency: String = "ZAR",

    /**
     * Daily transaction limit (if known/applicable)
     */
    val dailyLimit: Double? = null,

    /**
     * Monthly transaction limit (if known/applicable)
     */
    val monthlyLimit: Double? = null,

    // === VERIFICATION AND COMPLIANCE ===
    /**
     * Backend-provided account verification status
     * Only backend can verify account ownership and validity
     */
    val verificationStatus: BankAccountVerificationStatus = BankAccountVerificationStatus.NOT_VERIFIED,

    /**
     * Date of last successful verification (ISO date string)
     */
    val lastVerificationDate: String? = null,

    /**
     * Whether account is active and can receive payments
     */
    val isActive: Boolean = true,

    /**
     * Whether this account supports real-time payments
     */
    val supportsRealTimePayments: Boolean = false,

    /**
     * Whether this account is approved for automated payments
     */
    val approvedForAutomation: Boolean = false,

    // === BACKEND VALIDATION AND FORMATTING ===
    /**
     * Backend-formatted account number for display
     * Masks sensitive digits while showing account identification
     */
    val formattedAccountNumber: String? = null,

    /**
     * Backend-formatted branch code for display
     */
    val formattedBranchCode: String? = null,

    /**
     * Backend-provided account validation status
     */
    val accountValidationStatus: BankingValidationStatus = BankingValidationStatus.NOT_VALIDATED,

    /**
     * Banking details compliance score (0-100)
     * Backend calculates based on completeness and verification
     */
    val complianceScore: Int? = null,

    // === RISK AND SECURITY ===
    /**
     * Risk level assessment for this banking relationship
     * Backend determines based on verification and transaction history
     */
    val riskLevel: BankingRiskLevel = BankingRiskLevel.MEDIUM,

    /**
     * Whether additional verification is required for large transactions
     */
    val requiresAdditionalVerification: Boolean = false,

    /**
     * Maximum single transaction amount without additional approval
     */
    val maxSingleTransactionAmount: Double? = null,

    // === AUDIT TRAIL ===
    /**
     * When these banking details were last updated (ISO datetime string)
     */
    val updatedAt: String? = null,

    /**
     * User/system that last updated these details
     */
    val updatedBy: String? = null,

    /**
     * When these details were first added (ISO datetime string)
     */
    val createdAt: String? = null,

    /**
     * Notes about this banking relationship
     */
    val notes: String? = null,
) {

    /**
     * Check if banking details have minimum required information
     */
    fun hasMinimumInfo(): Boolean {
        return bankName.isNotBlank() &&
            accountHolderName.isNotBlank() &&
            accountNumber.isNotBlank()
    }

    /**
     * Check if banking details are complete for domestic payments
     */
    fun isCompleteForDomesticPayments(): Boolean {
        return hasMinimumInfo() &&
            (branchCode != null || accountType == BankAccountType.UNIVERSAL_BRANCH)
    }

    /**
     * Check if banking details support international payments
     */
    fun supportsInternationalPayments(): Boolean {
        return hasMinimumInfo() &&
            (!swiftCode.isNullOrBlank() || !iban.isNullOrBlank())
    }

    /**
     * Get the most appropriate payment method for this account
     */
    fun getPreferredPaymentMethod(): PaymentMethod {
        return when {
            supportsRealTimePayments && supportedPaymentMethods.contains(PaymentMethod.REAL_TIME) ->
                PaymentMethod.REAL_TIME
            supportedPaymentMethods.contains(PaymentMethod.EFT) -> PaymentMethod.EFT
            supportedPaymentMethods.isNotEmpty() -> supportedPaymentMethods.first()
            else -> PaymentMethod.EFT // Default fallback
        }
    }

    /**
     * Check if account can be used for the specified amount
     */
    fun canProcessAmount(amount: Double): Boolean {
        return when {
            !isActive -> false
            verificationStatus == BankAccountVerificationStatus.BLOCKED -> false
            dailyLimit != null && amount > dailyLimit -> false
            maxSingleTransactionAmount != null && amount > maxSingleTransactionAmount -> false
            else -> true
        }
    }

    /**
     * Get display name for the banking details
     */
    fun getDisplayName(): String {
        val maskedAccount = formattedAccountNumber
            ?: if (accountNumber.length > 4) "****${accountNumber.takeLast(4)}" else "****"
        return "$bankName ($maskedAccount)"
    }

    /**
     * Get payment processing time estimate
     */
    fun getProcessingTimeEstimate(): String {
        return when (getPreferredPaymentMethod()) {
            PaymentMethod.REAL_TIME -> "Immediate"
            PaymentMethod.EFT -> "1-3 business days"
            PaymentMethod.WIRE_TRANSFER -> "Same day - 1 business day"
            PaymentMethod.INTERNATIONAL_WIRE -> "3-5 business days"
            PaymentMethod.CHECK -> "3-7 business days"
            PaymentMethod.CASH -> "Immediate"
        }
    }

    companion object {
        /**
         * Create basic banking details template
         */
        fun createBasic(
            bankName: String,
            accountHolderName: String,
            accountNumber: String,
            branchCode: String? = null,
        ): BankingDetails {
            return BankingDetails(
                bankName = bankName,
                accountHolderName = accountHolderName,
                accountNumber = accountNumber,
                branchCode = branchCode,
                verificationStatus = BankAccountVerificationStatus.NOT_VERIFIED,
                accountValidationStatus = BankingValidationStatus.NOT_VALIDATED,
            )
        }
    }
}

/**
 * South African bank account type enumeration
 */
@Serializable
enum class BankAccountType(val displayName: String, val description: String) {
    CURRENT("Current Account", "Standard business current account"),
    SAVINGS("Savings Account", "Personal or business savings account"),
    BUSINESS("Business Account", "Dedicated business banking account"),
    TRANSMISSION("Transmission Account", "High-volume payment processing account"),
    CALL("Call Account", "High-interest call deposit account"),
    UNIVERSAL_BRANCH("Universal Branch", "Account using universal branch code"),
    INTERNATIONAL("International Account", "Foreign currency or international account"),
}

/**
 * Supported payment methods for banking accounts
 */
@Serializable
enum class PaymentMethod(
    val displayName: String,
    val description: String,
    val processingTime: String,
) {
    EFT("Electronic Funds Transfer", "Standard bank-to-bank transfer", "1-3 business days"),
    REAL_TIME("Real-Time Payment", "Immediate electronic payment", "Immediate"),
    WIRE_TRANSFER("Wire Transfer", "Same-day guaranteed transfer", "Same day"),
    INTERNATIONAL_WIRE("International Wire", "Cross-border wire transfer", "3-5 business days"),
    CHECK("Cheque", "Traditional paper cheque", "3-7 business days"),
    CASH("Cash Payment", "Physical cash transaction", "Immediate"),
}

/**
 * Bank account verification status
 */
@Serializable
enum class BankAccountVerificationStatus(val displayName: String, val description: String) {
    NOT_VERIFIED("Not Verified", "Account ownership not verified"),
    PENDING_VERIFICATION("Pending Verification", "Verification process in progress"),
    VERIFIED("Verified", "Account ownership confirmed"),
    VERIFICATION_FAILED("Verification Failed", "Could not verify account ownership"),
    BLOCKED("Blocked", "Account blocked due to security concerns"),
    CLOSED("Closed", "Account is closed or inactive"),
}

/**
 * Banking details validation status
 */
@Serializable
enum class BankingValidationStatus(val displayName: String) {
    NOT_VALIDATED("Not Validated"),
    VALID("Valid"),
    INVALID("Invalid"),
    PARTIALLY_VALID("Partially Valid"),
    VALIDATION_ERROR("Validation Error"),
    EXPIRED("Expired Validation"),
}

/**
 * Banking relationship risk assessment
 */
@Serializable
enum class BankingRiskLevel(
    val displayName: String,
    val color: String,
    val description: String,
) {
    LOW("Low Risk", "#10B981", "Verified account with good transaction history"),
    MEDIUM("Medium Risk", "#F59E0B", "Standard risk level requiring normal precautions"),
    HIGH("High Risk", "#EF4444", "Requires additional verification and monitoring"),
    CRITICAL("Critical Risk", "#DC2626", "Account flagged for security review"),
}
