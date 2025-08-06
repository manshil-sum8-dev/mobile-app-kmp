package za.co.quantive.app

import androidx.compose.runtime.*
import za.co.quantive.app.presentation.QuantiveAppContent
import za.co.quantive.app.presentation.theme.QuantiveTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Quantive Main Application Entry Point
 * Professional invoicing and business management platform
 */
@Composable
@Preview
fun App() {
    QuantiveTheme {
        QuantiveAppContent()
    }
}