package za.co.quantive.app.presentation.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Quantive Material 3 Expressive Design Tokens
 * Enhanced tokens based on Material 3 Expressive research and principles
 */
object QuantiveExpressiveTokens {
    
    /**
     * Expressive Color Palette - Enhanced emotional connection
     */
    object Colors {
        // Expressive primary colors with enhanced vibrancy
        val ExpressivePrimary = Color(0xFF00695C)      // Deeper teal for more emotion
        val ExpressivePrimaryVariant = Color(0xFF004D40) // Darker variant
        val ExpressivePrimaryLight = Color(0xFF48A999)   // Lighter, more playful
        
        // Expressive secondary colors - more energetic
        val ExpressiveSecondary = Color(0xFF4CAF50)    // Vibrant green
        val ExpressiveSecondaryVariant = Color(0xFF81C784) // Playful light green
        
        // Emotion-based semantic colors
        val Joy = Color(0xFFFFD54F)          // Warm yellow
        val Energy = Color(0xFFFF7043)       // Energetic orange  
        val Trust = Color(0xFF42A5F5)        // Trustworthy blue
        val Growth = Color(0xFF66BB6A)       // Growth green
        val Creativity = Color(0xFFAB47BC)   // Creative purple
        val Warmth = Color(0xFFFF8A65)       // Warm coral
        
        // Enhanced business context colors
        val ExpressiveCurrency = Color(0xFF2E7D32)  // Richer currency green
        val ExpressiveSuccess = Color(0xFF388E3C)   // Deeper success
        val ExpressiveWarning = Color(0xFFF57C00)   // More vibrant warning
        val ExpressiveError = Color(0xFFD32F2F)     // Strong error red
    }
    
    /**
     * Expressive Motion System - Spring-based animations
     */
    object Motion {
        // Spring animation specs for expressive feel
        val SpringBouncy = spring<Float>(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
        
        val SpringSmooth = spring<Float>(
            dampingRatio = Spring.DampingRatioNoBouncy,  
            stiffness = Spring.StiffnessMedium
        )
        
        val SpringGentle = spring<Float>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
        
        // Enhanced duration tokens
        const val ExpressiveShort = 250
        const val ExpressiveMedium = 400  
        const val ExpressiveLong = 600
        const val ExpressiveXLong = 800
        
        // Stagger delays for sequential animations
        const val StaggerDelay = 50L
        const val StaggerDelayLong = 100L
        
        // Easing curves
        val ExpressiveEaseOut = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
        val ExpressiveEaseIn = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)
        val ExpressiveEaseInOut = CubicBezierEasing(0.45f, 0.0f, 0.55f, 1.0f)
    }
    
    /**
     * Dynamic Shape System - Morphing capabilities
     */
    object Shapes {
        // Base expressive shapes - more rounded for friendliness
        val ExpressiveSmall = RoundedCornerShape(10.dp)
        val ExpressiveMedium = RoundedCornerShape(16.dp)  
        val ExpressiveLarge = RoundedCornerShape(24.dp)
        val ExpressiveXLarge = RoundedCornerShape(32.dp)
        
        // Morphing shape functions
        fun morphingButton(pressed: Boolean, selected: Boolean = false) = when {
            pressed -> RoundedCornerShape(QuantiveDesignTokens.Radius.XLarge)
            selected -> RoundedCornerShape(QuantiveDesignTokens.Radius.Large)
            else -> RoundedCornerShape(QuantiveDesignTokens.Radius.Medium)
        }
        
        fun morphingCard(hovered: Boolean, elevated: Boolean = false) = when {
            hovered && elevated -> RoundedCornerShape(QuantiveDesignTokens.Radius.XLarge)
            hovered -> RoundedCornerShape(QuantiveDesignTokens.Radius.Large)
            elevated -> RoundedCornerShape(QuantiveDesignTokens.Radius.Large)
            else -> RoundedCornerShape(QuantiveDesignTokens.Radius.Medium)
        }
        
        // Connected shapes for button groups
        val ConnectedStart = RoundedCornerShape(
            topStart = QuantiveDesignTokens.Radius.Medium,
            bottomStart = QuantiveDesignTokens.Radius.Medium,
            topEnd = 4.dp,
            bottomEnd = 4.dp
        )
        
        val ConnectedMiddle = RoundedCornerShape(4.dp)
        
        val ConnectedEnd = RoundedCornerShape(
            topStart = 4.dp,
            bottomStart = 4.dp,
            topEnd = QuantiveDesignTokens.Radius.Medium,
            bottomEnd = QuantiveDesignTokens.Radius.Medium
        )
        
        // Playful shapes for special elements
        val PlayfulPill = RoundedCornerShape(50.dp)
        val PlayfulSquircle = RoundedCornerShape(18.dp)
        
        // Asymmetric shapes for visual interest  
        fun AsymmetricCard(topHeavy: Boolean = false) = if (topHeavy) {
            RoundedCornerShape(
                topStart = QuantiveDesignTokens.Radius.Large,
                topEnd = QuantiveDesignTokens.Radius.Large,
                bottomStart = QuantiveDesignTokens.Radius.Small,
                bottomEnd = QuantiveDesignTokens.Radius.Small
            )
        } else {
            RoundedCornerShape(
                topStart = QuantiveDesignTokens.Radius.Small,
                topEnd = QuantiveDesignTokens.Radius.Small,
                bottomStart = QuantiveDesignTokens.Radius.Large,
                bottomEnd = QuantiveDesignTokens.Radius.Large
            )
        }
    }
    
    /**
     * Expressive Elevation System - Enhanced depth perception
     */
    object Elevation {
        // Enhanced elevation tokens for more dramatic depth
        val ExpressiveNone = 0.dp
        val ExpressiveSubtle = 1.dp
        val ExpressiveLow = 3.dp        // Slightly higher base
        val ExpressiveMedium = 6.dp     // More pronounced
        val ExpressiveHigh = 12.dp      // Dramatic elevation
        val ExpressiveExtra = 20.dp     // Maximum drama
        
        // Floating action button elevations
        val FABResting = 6.dp
        val FABPressed = 12.dp
        val FABHovered = 8.dp
    }
    
    /**
     * Expressive Typography Enhancements
     */
    object Typography {
        // Expressive letter spacing for emotional impact
        const val ExpressiveTight = -0.5f
        const val ExpressiveNormal = 0.0f
        const val ExpressiveLoose = 0.8f
        const val ExpressiveVeryLoose = 1.2f
        
        // Line height multipliers for better readability
        const val ExpressiveCompact = 1.2f
        const val ExpressiveComfortable = 1.5f
        const val ExpressiveAiry = 1.8f
    }
    
    /**
     * Interactive States - Enhanced feedback
     */
    object InteractionStates {
        // Alpha values for different states
        const val PressedAlpha = 0.85f
        const val HoveredAlpha = 0.92f
        const val FocusedAlpha = 0.90f
        const val DisabledAlpha = 0.38f
        const val SelectedAlpha = 0.95f
        
        // Scale values for pressed states
        const val PressedScale = 0.96f
        const val HoveredScale = 1.02f
        const val SelectedScale = 1.05f
    }
    
    /**
     * Expressive Spacing - Rhythm and flow
     */
    object Spacing {
        // Enhanced spacing for better visual rhythm
        val ExpressiveTight = 2.dp
        val ExpressiveCozy = 6.dp
        val ExpressiveComfortable = 12.dp
        val ExpressiveSpacious = 20.dp
        val ExpressiveLoose = 32.dp
        val ExpressiveVeryLoose = 48.dp
        
        // Content-specific spacing
        val CardPadding = 20.dp         // More generous card padding
        val SectionSpacing = 28.dp      // Better section separation
        val ComponentSpacing = 16.dp    // Optimal component spacing
        val TextSpacing = 8.dp          // Text element spacing
    }
    
    /**
     * Business Context Enhancements
     */
    object Business {
        // Enhanced business colors with emotional context
        val ExpressiveProfit = Colors.Growth
        val ExpressiveLoss = Colors.ExpressiveError
        val ExpressiveNeutral = Color(0xFF757575)
        val ExpressivePending = Colors.ExpressiveWarning
        val ExpressiveProcessing = Colors.Trust
        
        // Status color mappings
        fun getStatusColor(status: String): Color = when (status.lowercase()) {
            "paid", "completed", "success" -> Colors.Growth
            "pending", "processing", "in_progress" -> Colors.ExpressiveWarning
            "failed", "error", "cancelled" -> Colors.ExpressiveError
            "draft", "new" -> Colors.Trust
            else -> ExpressiveNeutral
        }
    }
}