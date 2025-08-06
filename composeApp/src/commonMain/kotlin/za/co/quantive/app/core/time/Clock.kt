package za.co.quantive.app.core.time

expect object Clock {
    fun currentTimeMillis(): Long
}