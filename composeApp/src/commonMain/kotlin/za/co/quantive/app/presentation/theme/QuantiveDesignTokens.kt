package za.co.quantive.app.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Quantive Design System Tokens
 * Based on Material 3 design principles with South African business context
 */
object QuantiveDesignTokens {

    /**
     * Custom Material 3 Color System - Premium Teal/Green Business Theme
     * Based on the user's custom theme with professional brand colors
     */
    object Colors {
        // Light Theme Primary Colors - Professional Teal/Green
        val Primary = Color(0xFF006B5F) // Custom brand teal
        val OnPrimary = Color(0xFFFFFFFF) // White
        val PrimaryContainer = Color(0xFF9EF2E2) // Light teal container
        val OnPrimaryContainer = Color(0xFF005047) // Dark teal

        // Secondary Colors - Harmonious teal variations
        val Secondary = Color(0xFF4A635E) // Muted teal-green
        val OnSecondary = Color(0xFFFFFFFF) // White
        val SecondaryContainer = Color(0xFFCDE8E2) // Very light teal
        val OnSecondaryContainer = Color(0xFF334B47) // Dark teal-green

        // Tertiary Colors - Complementary blue
        val Tertiary = Color(0xFF456179) // Blue-gray
        val OnTertiary = Color(0xFFFFFFFF) // White
        val TertiaryContainer = Color(0xFFCBE6FF) // Light blue
        val OnTertiaryContainer = Color(0xFF2D4A60) // Dark blue-gray

        // Error Colors
        val Error = Color(0xFFBA1A1A) // Material 3 error red
        val OnError = Color(0xFFFFFFFF) // White
        val ErrorContainer = Color(0xFFFFDAD6) // Light red container
        val OnErrorContainer = Color(0xFF93000A) // Dark red

        // Surface Colors - Clean, professional backgrounds
        val Background = Color(0xFFF4FBF8) // Very light green background
        val OnBackground = Color(0xFF171D1B) // Dark text
        val Surface = Color(0xFFF4FBF8) // Same as background
        val OnSurface = Color(0xFF171D1B) // Dark text
        val SurfaceVariant = Color(0xFFDAE5E1) // Light teal-gray
        val OnSurfaceVariant = Color(0xFF3F4946) // Medium dark text

        // Surface Container Hierarchy for proper elevation
        val SurfaceContainerLowest = Color(0xFFFFFFFF) // Pure white
        val SurfaceContainerLow = Color(0xFFEFF5F2) // Very light teal
        val SurfaceContainer = Color(0xFFE9EFEC) // Light teal
        val SurfaceContainerHigh = Color(0xFFE3EAE7) // Medium light teal
        val SurfaceContainerHighest = Color(0xFFDDE4E1) // Medium teal

        // Outline colors
        val Outline = Color(0xFF6F7976) // Medium teal-gray
        val OutlineVariant = Color(0xFFBEC9C5) // Light teal-gray
        val Scrim = Color(0xFF000000) // Black overlay

        // Inverse colors for dark elements on light theme
        val InverseSurface = Color(0xFF2B3230) // Dark surface
        val InverseOnSurface = Color(0xFFECF2EF) // Light text on dark
        val InversePrimary = Color(0xFF83D5C7) // Light teal for dark backgrounds

        // Business Context Colors
        val Currency = Color(0xFF1B5E20) // Dark green for money
        val Tax = Color(0xFF3E2723) // Brown for tax/government
        val VAT = Color(0xFF5D4037) // Brown for VAT
        val Invoice = Color(0xFF00695C) // Darker teal for invoices
        val Warning = Color(0xFFFF9800) // Orange for warnings
        val Success = Color(0xFF388E3C) // Green for success
        val Info = Color(0xFF1976D2) // Blue for information

        // Dark theme colors
        object Dark {
            val Primary = Color(0xFF83D5C7) // Light teal for dark theme
            val OnPrimary = Color(0xFF003731) // Very dark teal
            val PrimaryContainer = Color(0xFF005047) // Medium dark teal
            val OnPrimaryContainer = Color(0xFF9EF2E2) // Light teal

            val Secondary = Color(0xFFB1CCC6) // Light secondary for dark
            val OnSecondary = Color(0xFF1C3530) // Dark green
            val SecondaryContainer = Color(0xFF334B47) // Medium dark green
            val OnSecondaryContainer = Color(0xFFCDE8E2) // Light teal

            val Tertiary = Color(0xFFACCAE5) // Light blue for dark
            val OnTertiary = Color(0xFF143349) // Dark blue
            val TertiaryContainer = Color(0xFF2D4A60) // Medium dark blue
            val OnTertiaryContainer = Color(0xFFCBE6FF) // Light blue

            val Error = Color(0xFFFFB4AB) // Light red for dark theme
            val OnError = Color(0xFF690005) // Dark red
            val ErrorContainer = Color(0xFF93000A) // Medium dark red
            val OnErrorContainer = Color(0xFFFFDAD6) // Light red

            val Background = Color(0xFF0E1513) // Very dark green background
            val OnBackground = Color(0xFFDDE4E1) // Light text
            val Surface = Color(0xFF0E1513) // Same as background
            val OnSurface = Color(0xFFDDE4E1) // Light text

            // Surface containers for dark theme elevation
            val SurfaceContainerLowest = Color(0xFF090F0E) // Darkest
            val SurfaceContainerLow = Color(0xFF171D1B) // Very dark
            val SurfaceContainer = Color(0xFF1A211F) // Dark
            val SurfaceContainerHigh = Color(0xFF252B2A) // Medium dark
            val SurfaceContainerHighest = Color(0xFF303634) // Lightest dark
        }
    }

    /**
     * Typography Scale - Custom fonts with Raleway for display and Poppins for body
     * Following Material 3 Expressive typography scale
     */
    object Typography {
        val DisplayLarge = TextStyle(
            fontFamily = QuantiveDisplayFontFamily, // Raleway for headers
            fontWeight = FontWeight.W400,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp,
        )

        val DisplayMedium = TextStyle(
            fontFamily = QuantiveDisplayFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp,
        )

        val DisplaySmall = TextStyle(
            fontFamily = QuantiveDisplayFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp,
        )

        val HeadlineLarge = TextStyle(
            fontFamily = QuantiveDisplayFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp,
        )

        val HeadlineMedium = TextStyle(
            fontFamily = QuantiveDisplayFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp,
        )

        val HeadlineSmall = TextStyle(
            fontFamily = QuantiveDisplayFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp,
        )

        val TitleLarge = TextStyle(
            fontFamily = QuantiveDisplayFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
        )

        val TitleMedium = TextStyle(
            fontFamily = QuantiveDisplayFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp,
        )

        val TitleSmall = TextStyle(
            fontFamily = QuantiveDisplayFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
        )

        val BodyLarge = TextStyle(
            fontFamily = QuantiveBodyFontFamily, // Poppins for body text
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
        )

        val BodyMedium = TextStyle(
            fontFamily = QuantiveBodyFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp,
        )

        val BodySmall = TextStyle(
            fontFamily = QuantiveBodyFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp,
        )

        val LabelLarge = TextStyle(
            fontFamily = QuantiveBodyFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
        )

        val LabelMedium = TextStyle(
            fontFamily = QuantiveBodyFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
        )

        val LabelSmall = TextStyle(
            fontFamily = QuantiveBodyFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
        )
    }

    /**
     * Spacing System - 8pt Grid System
     */
    object Spacing {
        val None = 0.dp // 0
        val Tiny = 4.dp // 0.5 * 8
        val Small = 8.dp // 1 * 8
        val Medium = 16.dp // 2 * 8
        val Large = 24.dp // 3 * 8
        val XLarge = 32.dp // 4 * 8
        val XXLarge = 48.dp // 6 * 8
        val Huge = 64.dp // 8 * 8
        val Massive = 96.dp // 12 * 8
    }

    /**
     * Border Radius System
     */
    object Radius {
        val None = 0.dp
        val Small = 8.dp
        val Medium = 12.dp
        val Large = 16.dp
        val XLarge = 24.dp
        val Round = 50.dp // Circular
    }

    /**
     * Elevation System
     */
    object Elevation {
        val None = 0.dp
        val Small = 2.dp // Cards, buttons
        val Medium = 4.dp // FAB, snackbar
        val Large = 8.dp // Navigation drawer, modal
        val XLarge = 16.dp // Modal, dialog
    }

    /**
     * Component Dimensions
     */
    object Dimensions {
        // Button heights
        val ButtonHeightSmall = 32.dp
        val ButtonHeightMedium = 40.dp
        val ButtonHeightLarge = 48.dp

        // Icon sizes
        val IconSmall = 16.dp
        val IconMedium = 24.dp
        val IconLarge = 32.dp
        val IconXLarge = 48.dp

        // Touch targets (minimum 44dp for accessibility)
        val TouchTargetMinimum = 44.dp

        // Input field heights
        val InputFieldHeight = 56.dp
        val InputFieldHeightCompact = 44.dp

        // Bottom navigation
        val BottomNavigationHeight = 80.dp

        // App bars
        val AppBarHeight = 64.dp
        val AppBarHeightCompact = 48.dp
    }

    /**
     * Animation Durations (Material 3 motion tokens)
     */
    object Animation {
        const val Short = 200
        const val Medium = 300
        const val Long = 400
        const val ExtraLong = 500
    }

    /**
     * Business-specific UI constants for Quantive
     * ⚠️ No business logic calculations - only UI/UX constants
     */
    object Business {
        // UI Display constants only - backend provides actual currency formatting
        const val DEFAULT_DISPLAY_CURRENCY = "ZAR"
        const val DISPLAY_CURRENCY_SYMBOL = "R"

        // UI Input validation limits - backend enforces actual business rules
        const val MAX_INVOICE_ITEMS_UI = 50
        const val MAX_CONTACT_NAME_LENGTH_UI = 100
        const val MAX_COMPANY_NAME_LENGTH_UI = 150

        // Search UI behavior
        const val MIN_SEARCH_QUERY_LENGTH = 2
        const val SEARCH_DEBOUNCE_MS = 300L

        // UI Pagination
        const val DEFAULT_PAGE_SIZE = 20
        const val MAX_PAGE_SIZE = 100
    }
}
