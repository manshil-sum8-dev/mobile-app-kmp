package za.co.quantive.app

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import za.co.quantive.app.presentation.QuantiveApp
import za.co.quantive.app.presentation.theme.QuantiveTheme

/**
 * Quantive Main Application Entry Point
 * Professional invoicing and business management platform
 */
@Composable
@Preview
fun App() {
    QuantiveTheme {
        QuantiveApp()
    }
}
