package za.co.quantive.app.presentation.theme

import androidx.compose.ui.text.font.FontFamily

/**
 * Quantive Font System for Kotlin Multiplatform
 *
 * Using expect/actual pattern to provide platform-specific font implementations
 * while maintaining a consistent API across all platforms.
 */

/**
 * Display font family for headers, titles, and emphasis text
 * Platform implementations should use Raleway or closest system equivalent
 */
expect val QuantiveDisplayFontFamily: FontFamily

/**
 * Body font family for readable content and UI text
 * Platform implementations should use Poppins or closest system equivalent
 */
expect val QuantiveBodyFontFamily: FontFamily

/**
 * Monospace font family for code, numbers, and technical content
 * Platform implementations should use system monospace fonts
 */
expect val QuantiveMonospaceFontFamily: FontFamily
