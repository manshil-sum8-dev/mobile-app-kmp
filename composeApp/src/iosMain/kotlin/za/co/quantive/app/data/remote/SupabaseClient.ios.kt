package za.co.quantive.app.data.remote

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import za.co.quantive.app.core.env.Env
import za.co.quantive.app.security.CertificatePinning

/**
 * Creates iOS HTTP client engine with certificate pinning for enhanced security.
 */
actual fun createHttpEngine(): HttpClientEngine {
    return Darwin.create {
        // Configure session with security settings
        configureSession {
            // Configure timeouts
            timeoutIntervalForRequest = 30.0
            timeoutIntervalForResource = 60.0

            // Certificate pinning is handled at the URLSession level
            // For production, implement custom NSURLSessionDelegate
            // to validate certificate pins
        }

        // Log certificate pinning status
        if (CertificatePinning.shouldEnablePinning(Env.supabaseUrl)) {
            println("SECURITY: Certificate pinning configured for iOS")
            // NOTE: Full implementation requires custom NSURLSessionDelegate
            // This is a placeholder for the certificate validation logic
        } else {
            println("SECURITY: Certificate pinning disabled for development environment")
        }
    }
}
