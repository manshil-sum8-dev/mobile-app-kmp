package za.co.quantive.app.presentation.onboarding.design

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import za.co.quantive.app.presentation.theme.QuantiveDesignTokens

/**
 * Quantive Onboarding Design System
 * Provides consistent design tokens for the onboarding flow
 * Now integrated with Material 3 theme tokens for consistency
 */
object OnboardingDesignSystem {

    /**
     * Spacing System - Based on Material 3 8dp grid system
     */
    object Spacing {
        // Use theme spacing tokens for consistency
        val xs: Dp = QuantiveDesignTokens.Spacing.Tiny
        val sm: Dp = QuantiveDesignTokens.Spacing.Small
        val md: Dp = QuantiveDesignTokens.Spacing.Medium
        val lg: Dp = QuantiveDesignTokens.Spacing.Large
        val xl: Dp = QuantiveDesignTokens.Spacing.XLarge
        val xxl: Dp = QuantiveDesignTokens.Spacing.XXLarge
        val xxxl: Dp = QuantiveDesignTokens.Spacing.Huge

        // Component specific spacing aligned with theme
        val cardPadding: Dp = QuantiveDesignTokens.Spacing.Large // 24dp
        val screenHorizontalPadding: Dp = QuantiveDesignTokens.Spacing.Large // 24dp
        val sectionVerticalSpacing: Dp = QuantiveDesignTokens.Spacing.XLarge // 32dp
        val inputVerticalSpacing: Dp = QuantiveDesignTokens.Spacing.Medium // 16dp
        val buttonHeight: Dp = QuantiveDesignTokens.Dimensions.ButtonHeightLarge // 48dp
    }

    /**
     * Typography System - Material 3 Expressive with custom fonts
     * References the main theme typography for consistency
     */
    object Typography {
        // Use theme typography directly for consistency
        val displayLarge = QuantiveDesignTokens.Typography.DisplayLarge
        val displayMedium = QuantiveDesignTokens.Typography.DisplayMedium
        val displaySmall = QuantiveDesignTokens.Typography.DisplaySmall

        val headlineLarge = QuantiveDesignTokens.Typography.HeadlineLarge
        val headlineMedium = QuantiveDesignTokens.Typography.HeadlineMedium
        val headlineSmall = QuantiveDesignTokens.Typography.HeadlineSmall

        val titleLarge = QuantiveDesignTokens.Typography.TitleLarge
        val titleMedium = QuantiveDesignTokens.Typography.TitleMedium
        val titleSmall = QuantiveDesignTokens.Typography.TitleSmall

        val bodyLarge = QuantiveDesignTokens.Typography.BodyLarge
        val bodyMedium = QuantiveDesignTokens.Typography.BodyMedium
        val bodySmall = QuantiveDesignTokens.Typography.BodySmall

        val labelLarge = QuantiveDesignTokens.Typography.LabelLarge
        val labelMedium = QuantiveDesignTokens.Typography.LabelMedium
        val labelSmall = QuantiveDesignTokens.Typography.LabelSmall
    }

    /**
     * Shape System - Material 3 rounded corner system
     */
    object Shapes {
        // Use theme radius tokens for consistency
        val extraSmall: Shape = RoundedCornerShape(QuantiveDesignTokens.Radius.None)
        val small: Shape = RoundedCornerShape(QuantiveDesignTokens.Radius.Small)
        val medium: Shape = RoundedCornerShape(QuantiveDesignTokens.Radius.Medium)
        val large: Shape = RoundedCornerShape(QuantiveDesignTokens.Radius.Large)
        val extraLarge: Shape = RoundedCornerShape(QuantiveDesignTokens.Radius.XLarge)

        // Component specific shapes
        val cardShape: Shape = RoundedCornerShape(QuantiveDesignTokens.Radius.Large) // 16dp
        val buttonShape: Shape = RoundedCornerShape(QuantiveDesignTokens.Radius.XLarge) // 24dp
        val inputShape: Shape = RoundedCornerShape(QuantiveDesignTokens.Radius.Medium) // 12dp
    }

    /**
     * Animation System - Consistent motion design
     */
    object Animations {
        // Duration constants
        const val FAST = 150
        const val MEDIUM = 300
        const val SLOW = 500
        const val EXTRA_SLOW = 800

        // Standard animations
        val fastTransition: AnimationSpec<Float> = tween(FAST)
        val mediumTransition: AnimationSpec<Float> = tween(MEDIUM)
        val slowTransition: AnimationSpec<Float> = tween(SLOW)

        // Spring animations for delightful interactions
        val bouncySpring: AnimationSpec<Float> = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        )

        val gentleSpring: AnimationSpec<Float> = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        )

        // Page transitions
        val pageEnterTransition: AnimationSpec<Float> = tween(MEDIUM)
        val pageExitTransition: AnimationSpec<Float> = tween(FAST)

        // Microinteractions
        val hoverScale = 1.02f
        val pressScale = 0.98f
        val cardSelectScale = 1.02f
    }

    /**
     * Component Sizes - Aligned with Material 3 dimensions
     */
    object ComponentSizes {
        val logoSizeSplash: Dp = 160.dp // Custom size for splash
        val logoSizeCard: Dp = QuantiveDesignTokens.Dimensions.IconXLarge // 48dp
        val iconSizeLarge: Dp = QuantiveDesignTokens.Dimensions.IconLarge // 32dp
        val iconSizeMedium: Dp = QuantiveDesignTokens.Dimensions.IconMedium // 24dp
        val iconSizeSmall: Dp = QuantiveDesignTokens.Dimensions.IconSmall // 16dp

        val progressIndicatorHeight: Dp = 4.dp
        val progressIndicatorWidth: Dp = QuantiveDesignTokens.Spacing.XLarge // 32dp
        val progressIndicatorInactiveWidth: Dp = QuantiveDesignTokens.Spacing.Medium // 16dp

        val cardMinHeight: Dp = 120.dp // Custom card minimum
        val inputMinHeight: Dp = QuantiveDesignTokens.Dimensions.InputFieldHeight // 56dp
    }

    /**
     * Elevation System - Material 3 elevation tokens
     */
    object Elevation {
        val none: Dp = QuantiveDesignTokens.Elevation.None
        val low: Dp = QuantiveDesignTokens.Elevation.Small // 2dp
        val medium: Dp = QuantiveDesignTokens.Elevation.Medium // 4dp
        val high: Dp = QuantiveDesignTokens.Elevation.Large // 8dp
        val extraHigh: Dp = QuantiveDesignTokens.Elevation.XLarge // 16dp
    }
}
