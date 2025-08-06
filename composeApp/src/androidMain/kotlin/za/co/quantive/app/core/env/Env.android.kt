package za.co.quantive.app.core.env

import za.co.quantive.app.BuildConfig

actual object Env {
    actual val supabaseUrl: String = BuildConfig.SUPABASE_URL
    actual val supabaseAnonKey: String = BuildConfig.SUPABASE_ANON_KEY
}
