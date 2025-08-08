package za.co.quantive.app.domain.invoice

import kotlinx.serialization.Serializable

/**
 * Enhanced template information for invoice templates
 * Provides comprehensive template configuration and branding customization
 * Used for both creating templates and applying template settings to invoices
 */
@Serializable
data class TemplateInfo(
    val templateId: String,
    val templateName: String,
    val isDefault: Boolean = false,
    val customFields: Map<String, String> = emptyMap(),
    val logoUrl: String? = null,
    val brandingColors: Map<String, String> = emptyMap(),
    val termsAndConditions: String? = null,
    val footerText: String? = null,
    val isActive: Boolean = true,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val description: String? = null,
) {
    /**
     * Backward compatibility for simple template info from backend invoice
     * Maps enhanced template info to simplified format when needed
     */
    fun toSimpleTemplateInfo(): za.co.quantive.app.domain.entities.TemplateInfo {
        return za.co.quantive.app.domain.entities.TemplateInfo(
            isTemplate = true,
            templateName = templateName,
            templateId = templateId,
        )
    }

    companion object {
        /**
         * Creates enhanced template info from simple backend template info
         */
        fun fromSimpleTemplateInfo(
            simpleInfo: za.co.quantive.app.domain.entities.TemplateInfo,
        ): TemplateInfo? {
            return if (simpleInfo.isTemplate && simpleInfo.templateId != null) {
                TemplateInfo(
                    templateId = simpleInfo.templateId,
                    templateName = simpleInfo.templateName ?: "Unnamed Template",
                )
            } else {
                null
            }
        }

        /**
         * Creates a default template info for new templates
         */
        fun createDefault(
            templateId: String,
            templateName: String,
        ): TemplateInfo {
            return TemplateInfo(
                templateId = templateId,
                templateName = templateName,
                brandingColors = mapOf(
                    "primary" to "#1976D2",
                    "secondary" to "#424242",
                    "accent" to "#FF5722",
                ),
            )
        }
    }
}

/**
 * Template creation request for converting invoices to templates
 */
@Serializable
data class CreateTemplateRequest(
    val templateName: String,
    val description: String? = null,
    val isDefault: Boolean = false,
    val customFields: Map<String, String> = emptyMap(),
    val brandingColors: Map<String, String> = emptyMap(),
    val termsAndConditions: String? = null,
    val footerText: String? = null,
)

/**
 * Template customization options for creating invoices from templates
 */
@Serializable
data class TemplateCustomization(
    val logoUrl: String? = null,
    val brandingColors: Map<String, String> = emptyMap(),
    val customFields: Map<String, String> = emptyMap(),
    val termsAndConditions: String? = null,
    val footerText: String? = null,
    val overrideDefaults: Boolean = false,
)
