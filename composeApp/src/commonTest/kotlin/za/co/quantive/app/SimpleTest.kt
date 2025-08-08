package za.co.quantive.app

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Simple test to verify basic testing infrastructure works
 */
class SimpleTest {

    @Test
    fun `basic kotlin test assertion works`() {
        // Given
        val expected = "Hello"
        val actual = "Hello"

        // Then
        assertEquals(expected, actual)
        assertTrue(actual.isNotEmpty())
    }

    @Test
    fun `runTest coroutine function works`() = runTest {
        // Given
        suspend fun suspendingFunction(): String {
            return "test result"
        }

        // When
        val result = suspendingFunction()

        // Then
        assertEquals("test result", result)
    }

    @Test
    fun `basic collection operations work`() {
        // Given
        val list = listOf(1, 2, 3, 4, 5)

        // When
        val sum = list.sum()
        val filtered = list.filter { it > 3 }

        // Then
        assertEquals(15, sum)
        assertEquals(listOf(4, 5), filtered)
    }
}
