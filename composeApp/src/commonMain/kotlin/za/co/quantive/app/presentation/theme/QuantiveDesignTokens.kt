package za.co.quantive.app.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Quantive Design System Tokens
 * Based on Material 3 design principles with South African business context
 */
object QuantiveDesignTokens {
    
    /**
     * Color System - Professional business palette
     */
    object Colors {
        // Primary Colors - Teal (Professional, trustworthy)
        val Primary = Color(0xFF00796B)        // Teal 700
        val OnPrimary = Color(0xFFFFFFFF)      // White
        val PrimaryContainer = Color(0xFFB2DFDB) // Teal 100
        val OnPrimaryContainer = Color(0xFF004D40) // Teal 900
        
        // Secondary Colors - Green (Growth, success)
        val Secondary = Color(0xFF4CAF50)      // Green 500
        val OnSecondary = Color(0xFFFFFFFF)    // White
        val SecondaryContainer = Color(0xFFC8E6C9) // Green 100
        val OnSecondaryContainer = Color(0xFF1B5E20) // Green 900
        
        // Tertiary Colors - Blue (Trust, reliability)
        val Tertiary = Color(0xFF2196F3)       // Blue 500
        val OnTertiary = Color(0xFFFFFFFF)     // White
        val TertiaryContainer = Color(0xFFBBDEFB) // Blue 100
        val OnTertiaryContainer = Color(0xFF0D47A1) // Blue 900
        
        // Semantic Colors
        val Error = Color(0xFFD32F2F)          // Red 700
        val OnError = Color(0xFFFFFFFF)        // White
        val Warning = Color(0xFFFF9800)        // Orange 500
        val Success = Color(0xFF388E3C)        // Green 700
        val Info = Color(0xFF1976D2)           // Blue 700
        
        // South African Business Context Colors
        val Currency = Color(0xFF1B5E20)       // Dark Green (Rand)
        val Tax = Color(0xFF3E2723)            // Brown (SARS)
        val VAT = Color(0xFF5D4037)            // Brown 700
        val Invoice = Color(0xFF00695C)        // Teal 800
        
        // Surface Colors
        val Surface = Color(0xFFFFFBFE)        // Light surface
        val OnSurface = Color(0xFF1C1B1F)      // Dark text
        val SurfaceVariant = Color(0xFFF3F2F7) // Light variant
        val OnSurfaceVariant = Color(0xFF46464F) // Medium text
        
        val Background = Color(0xFFFFFBFE)     // Light background
        val OnBackground = Color(0xFF1C1B1F)   // Dark text
        
        // Outline colors
        val Outline = Color(0xFF79757F)        // Medium outline
        val OutlineVariant = Color(0xFFCAC4D0) // Light outline
        
        // Dark theme colors
        object Dark {
            val Primary = Color(0xFF4DB6AC)        // Teal 300
            val OnPrimary = Color(0xFF003128)      // Very dark teal
            val PrimaryContainer = Color(0xFF00564F) // Dark teal
            val OnPrimaryContainer = Color(0xFFB2DFDB) // Light teal
            
            val Surface = Color(0xFF141218)        // Dark surface
            val OnSurface = Color(0xFFE6E1E5)      // Light text
            val Background = Color(0xFF141218)     // Dark background
            val OnBackground = Color(0xFFE6E1E5)   // Light text
        }
    }
    
    /**
     * Typography Scale - Inter font family for professionalism
     */
    object Typography {
        val DisplayLarge = TextStyle(
            fontFamily = FontFamily.Default, // Will be replaced with Inter
            fontWeight = FontWeight.W400,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp
        )
        
        val DisplayMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W400,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp
        )
        
        val DisplaySmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W400,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp
        )
        
        val HeadlineLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W400,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp
        )
        
        val HeadlineMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W400,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp
        )
        
        val HeadlineSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W400,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        )
        
        val TitleLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W500,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        )
        
        val TitleMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W500,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        )
        
        val TitleSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        )
        
        val BodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
        
        val BodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        )
        
        val BodySmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        )
        
        val LabelLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        )
        
        val LabelMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
        
        val LabelSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W500,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    }
    
    /**
     * Spacing System - 8pt Grid System
     */
    object Spacing {
        val None = 0.dp        // 0
        val Tiny = 4.dp        // 0.5 * 8
        val Small = 8.dp       // 1 * 8
        val Medium = 16.dp     // 2 * 8
        val Large = 24.dp      // 3 * 8
        val XLarge = 32.dp     // 4 * 8
        val XXLarge = 48.dp    // 6 * 8
        val Huge = 64.dp       // 8 * 8
        val Massive = 96.dp    // 12 * 8
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
        val Round = 50.dp      // Circular
    }
    
    /**
     * Elevation System
     */
    object Elevation {
        val None = 0.dp
        val Small = 2.dp       // Cards, buttons
        val Medium = 4.dp      // FAB, snackbar
        val Large = 8.dp       // Navigation drawer, modal
        val XLarge = 16.dp     // Modal, dialog
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