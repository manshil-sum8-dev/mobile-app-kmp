package za.co.quantive.app.core.env

import platform.Foundation.NSBundle

actual object Env {
    private fun getPlistValue(key: String): String =
        NSBundle.mainBundle.objectForInfoDictionaryKey(key)?.toString() ?: ""

    actual val supabaseUrl: String = getPlistValue("SUPABASE_URL")
    actual val supabaseAnonKey: String = getPlistValue("SUPABASE_ANON_KEY")
}
