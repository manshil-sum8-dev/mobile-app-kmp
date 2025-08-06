package za.co.quantive.app.core.time

actual object Clock {
    actual fun currentTimeMillis(): Long = System.currentTimeMillis()
}