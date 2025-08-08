package za.co.quantive.app.security

/**
 * Certificate pinning configuration for enterprise security.
 * Protects against man-in-the-middle attacks by validating server certificates.
 */
object CertificatePinning {

    /**
     * Certificate pins for production Supabase API.
     * These are SHA-256 hashes of the public key.
     *
     * NOTE: For local development (localhost), certificate pinning is disabled.
     * In production, these pins must be updated when certificates rotate.
     */
    val supabasePins = listOf(
        // Supabase production API certificate pins
        // These should be obtained from the actual production environment
        "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=", // Primary certificate
        "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=", // Backup certificate
    )

    /**
     * Determines if certificate pinning should be enabled based on the environment.
     *
     * @param url The API URL being accessed
     * @return true if certificate pinning should be enforced
     */
    fun shouldEnablePinning(url: String): Boolean {
        // Disable pinning for local development
        return !url.contains("localhost") &&
            !url.contains("127.0.0.1") &&
            !url.contains("10.0.2.2") &&
            !url.contains(".local")
    }

    /**
     * Gets the appropriate certificate pins for a given host.
     *
     * @param host The hostname being accessed
     * @return List of certificate pins or empty list if no pinning required
     */
    fun getPinsForHost(host: String): List<String> {
        return when {
            host.contains("supabase.co") || host.contains("supabase.io") -> supabasePins
            else -> emptyList() // No pinning for other hosts
        }
    }
}
