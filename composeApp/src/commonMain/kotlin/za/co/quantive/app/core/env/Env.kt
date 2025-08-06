package za.co.quantive.app.core.env

// Expect/Actual environment provider for Supabase configuration
// Provide different values per platform without leaking secrets in source.
expect object Env {
    val supabaseUrl: String
    val supabaseAnonKey: String
}
