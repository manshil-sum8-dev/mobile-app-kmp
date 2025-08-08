package za.co.quantive.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.karumi.shot.ScreenshotTest
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Base class for screenshot tests using Shot library
 * Provides consistent screenshot testing patterns for Compose UI components
 */
@RunWith(AndroidJUnit4::class)
abstract class ScreenshotTestBase : ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Capture screenshot of a Compose UI component
     */
    protected fun captureScreenshot(
        name: String,
        content: @Composable () -> Unit,
    ) {
        composeTestRule.setContent {
            content()
        }

        // Wait for composition to complete
        composeTestRule.waitForIdle()

        // Capture screenshot
        compareScreenshot(composeTestRule, name)
    }

    /**
     * Capture screenshot with theme variants
     */
    protected fun captureThemeVariants(
        baseName: String,
        content: @Composable (isDarkTheme: Boolean) -> Unit,
    ) {
        // Light theme screenshot
        captureScreenshot("${baseName}_light") {
            content(false)
        }

        // Dark theme screenshot
        captureScreenshot("${baseName}_dark") {
            content(true)
        }
    }

    /**
     * Capture screenshot in different screen sizes
     */
    protected fun captureScreenSizes(
        baseName: String,
        content: @Composable () -> Unit,
    ) {
        // Regular screenshot
        captureScreenshot(baseName, content)

        // Could be extended for tablet/landscape variants
        // This is a placeholder for future responsive testing
    }
}
