package za.co.quantive.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Quantive Material 3 Theme Implementation
 * Professional business theme with South African context
 */

private val LightColorScheme = lightColorScheme(
    primary = QuantiveDesignTokens.Colors.Primary,
    onPrimary = QuantiveDesignTokens.Colors.OnPrimary,
    primaryContainer = QuantiveDesignTokens.Colors.PrimaryContainer,
    onPrimaryContainer = QuantiveDesignTokens.Colors.OnPrimaryContainer,

    secondary = QuantiveDesignTokens.Colors.Secondary,
    onSecondary = QuantiveDesignTokens.Colors.OnSecondary,
    secondaryContainer = QuantiveDesignTokens.Colors.SecondaryContainer,
    onSecondaryContainer = QuantiveDesignTokens.Colors.OnSecondaryContainer,

    tertiary = QuantiveDesignTokens.Colors.Tertiary,
    onTertiary = QuantiveDesignTokens.Colors.OnTertiary,
    tertiaryContainer = QuantiveDesignTokens.Colors.TertiaryContainer,
    onTertiaryContainer = QuantiveDesignTokens.Colors.OnTertiaryContainer,

    error = QuantiveDesignTokens.Colors.Error,
    onError = QuantiveDesignTokens.Colors.OnError,

    surface = QuantiveDesignTokens.Colors.Surface,
    onSurface = QuantiveDesignTokens.Colors.OnSurface,
    surfaceVariant = QuantiveDesignTokens.Colors.SurfaceVariant,
    onSurfaceVariant = QuantiveDesignTokens.Colors.OnSurfaceVariant,

    background = QuantiveDesignTokens.Colors.Background,
    onBackground = QuantiveDesignTokens.Colors.OnBackground,

    outline = QuantiveDesignTokens.Colors.Outline,
    outlineVariant = QuantiveDesignTokens.Colors.OutlineVariant,
)

private val DarkColorScheme = darkColorScheme(
    primary = QuantiveDesignTokens.Colors.Dark.Primary,
    onPrimary = QuantiveDesignTokens.Colors.Dark.OnPrimary,
    primaryContainer = QuantiveDesignTokens.Colors.Dark.PrimaryContainer,
    onPrimaryContainer = QuantiveDesignTokens.Colors.Dark.OnPrimaryContainer,

    secondary = QuantiveDesignTokens.Colors.Secondary,
    onSecondary = QuantiveDesignTokens.Colors.OnSecondary,
    secondaryContainer = QuantiveDesignTokens.Colors.SecondaryContainer,
    onSecondaryContainer = QuantiveDesignTokens.Colors.OnSecondaryContainer,

    tertiary = QuantiveDesignTokens.Colors.Tertiary,
    onTertiary = QuantiveDesignTokens.Colors.OnTertiary,
    tertiaryContainer = QuantiveDesignTokens.Colors.TertiaryContainer,
    onTertiaryContainer = QuantiveDesignTokens.Colors.OnTertiaryContainer,

    error = QuantiveDesignTokens.Colors.Error,
    onError = QuantiveDesignTokens.Colors.OnError,

    surface = QuantiveDesignTokens.Colors.Dark.Surface,
    onSurface = QuantiveDesignTokens.Colors.Dark.OnSurface,
    surfaceVariant = QuantiveDesignTokens.Colors.SurfaceVariant,
    onSurfaceVariant = QuantiveDesignTokens.Colors.OnSurfaceVariant,

    background = QuantiveDesignTokens.Colors.Dark.Background,
    onBackground = QuantiveDesignTokens.Colors.Dark.OnBackground,

    outline = QuantiveDesignTokens.Colors.Outline,
    outlineVariant = QuantiveDesignTokens.Colors.OutlineVariant,
)

private val QuantiveTypography = Typography(
    displayLarge = QuantiveDesignTokens.Typography.DisplayLarge,
    displayMedium = QuantiveDesignTokens.Typography.DisplayMedium,
    displaySmall = QuantiveDesignTokens.Typography.DisplaySmall,

    headlineLarge = QuantiveDesignTokens.Typography.HeadlineLarge,
    headlineMedium = QuantiveDesignTokens.Typography.HeadlineMedium,
    headlineSmall = QuantiveDesignTokens.Typography.HeadlineSmall,

    titleLarge = QuantiveDesignTokens.Typography.TitleLarge,
    titleMedium = QuantiveDesignTokens.Typography.TitleMedium,
    titleSmall = QuantiveDesignTokens.Typography.TitleSmall,

    bodyLarge = QuantiveDesignTokens.Typography.BodyLarge,
    bodyMedium = QuantiveDesignTokens.Typography.BodyMedium,
    bodySmall = QuantiveDesignTokens.Typography.BodySmall,

    labelLarge = QuantiveDesignTokens.Typography.LabelLarge,
    labelMedium = QuantiveDesignTokens.Typography.LabelMedium,
    labelSmall = QuantiveDesignTokens.Typography.LabelSmall,
)

private val QuantiveShapes = Shapes(
    small = RoundedCornerShape(QuantiveDesignTokens.Radius.Small),
    medium = RoundedCornerShape(QuantiveDesignTokens.Radius.Medium),
    large = RoundedCornerShape(QuantiveDesignTokens.Radius.Large),
)

/**
 * Extended color scheme for business-specific colors
 */
data class QuantiveExtendedColors(
    val currency: androidx.compose.ui.graphics.Color,
    val tax: androidx.compose.ui.graphics.Color,
    val vat: androidx.compose.ui.graphics.Color,
    val invoice: androidx.compose.ui.graphics.Color,
    val warning: androidx.compose.ui.graphics.Color,
    val success: androidx.compose.ui.graphics.Color,
    val info: androidx.compose.ui.graphics.Color,
)

private val LocalQuantiveExtendedColors = staticCompositionLocalOf {
    QuantiveExtendedColors(
        currency = QuantiveDesignTokens.Colors.Currency,
        tax = QuantiveDesignTokens.Colors.Tax,
        vat = QuantiveDesignTokens.Colors.VAT,
        invoice = QuantiveDesignTokens.Colors.Invoice,
        warning = QuantiveDesignTokens.Colors.Warning,
        success = QuantiveDesignTokens.Colors.Success,
        info = QuantiveDesignTokens.Colors.Info,
    )
}

/**
 * Quantive Theme composable
 *
 * @param darkTheme Whether to use dark theme colors
 * @param dynamicColor Whether to use dynamic colors (Android 12+)
 * @param content The content to be themed
 */
@Composable
fun QuantiveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled for brand consistency
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColors = QuantiveExtendedColors(
        currency = QuantiveDesignTokens.Colors.Currency,
        tax = QuantiveDesignTokens.Colors.Tax,
        vat = QuantiveDesignTokens.Colors.VAT,
        invoice = QuantiveDesignTokens.Colors.Invoice,
        warning = QuantiveDesignTokens.Colors.Warning,
        success = QuantiveDesignTokens.Colors.Success,
        info = QuantiveDesignTokens.Colors.Info,
    )

    CompositionLocalProvider(
        LocalQuantiveExtendedColors provides extendedColors,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = QuantiveTypography,
            shapes = QuantiveShapes,
            content = content,
        )
    }
}

/**
 * Access to Quantive-specific colors
 */
object QuantiveTheme {
    val extendedColors: QuantiveExtendedColors
        @Composable
        get() = LocalQuantiveExtendedColors.current
}
