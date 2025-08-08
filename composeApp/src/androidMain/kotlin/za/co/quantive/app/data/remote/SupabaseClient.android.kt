package za.co.quantive.app.data.remote

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import za.co.quantive.app.core.env.Env
import za.co.quantive.app.security.CertificatePinning
import java.util.concurrent.TimeUnit

/**
 * Creates Android HTTP client engine with certificate pinning for enhanced security.
 */
actual fun createHttpEngine(): HttpClientEngine {
    val okHttpClient = OkHttpClient.Builder().apply {
        // Configure timeouts
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)

        // Configure certificate pinning if enabled
        if (CertificatePinning.shouldEnablePinning(Env.supabaseUrl)) {
            val certificatePinner = CertificatePinner.Builder().apply {
                // Extract host from URL
                val host = try {
                    java.net.URL(Env.supabaseUrl).host
                } catch (e: Exception) {
                    println("SECURITY: Failed to parse URL for certificate pinning: ${e.message}")
                    null
                }

                if (host != null) {
                    val pins = CertificatePinning.getPinsForHost(host)
                    pins.forEach { pin ->
                        add(host, pin)
                        println("SECURITY: Certificate pin added for host: $host")
                    }
                }
            }.build()

            certificatePinner(certificatePinner)
        } else {
            println("SECURITY: Certificate pinning disabled for development environment")
        }
    }.build()

    return OkHttp.create {
        preconfigured = okHttpClient
    }
}
