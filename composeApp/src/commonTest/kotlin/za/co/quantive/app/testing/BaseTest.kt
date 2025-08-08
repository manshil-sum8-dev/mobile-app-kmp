package za.co.quantive.app.testing

import kotlin.test.AfterTest
import kotlin.test.BeforeTest

/**
 * Base test class providing common testing patterns and setup/teardown
 * Follows Given-When-Then structure for consistent test organization
 */
abstract class BaseTest {

    @BeforeTest
    open fun setUp() {
        // Override in subclasses for test-specific setup
    }

    @AfterTest
    open fun tearDown() {
        // Override in subclasses for test-specific cleanup
    }

    /**
     * Test execution helper that enforces Given-When-Then pattern
     */
    protected fun givenWhenThen(
        given: () -> Unit = {},
        `when`: () -> Unit = {},
        then: () -> Unit = {},
    ) {
        // Given - Setup test conditions
        given()

        // When - Execute the action being tested
        `when`()

        // Then - Verify the results
        then()
    }

    /**
     * Async test execution helper for suspend functions
     */
    protected suspend fun givenWhenThenAsync(
        given: suspend () -> Unit = {},
        `when`: suspend () -> Unit = {},
        then: suspend () -> Unit = {},
    ) {
        // Given - Setup test conditions
        given()

        // When - Execute the action being tested
        `when`()

        // Then - Verify the results
        then()
    }
}
