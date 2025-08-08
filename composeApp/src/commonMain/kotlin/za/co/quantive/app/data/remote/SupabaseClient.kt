package za.co.quantive.app.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import za.co.quantive.app.core.env.Env

expect fun createHttpEngine(): HttpClientEngine

class SupabaseClient(
    internal val accessTokenProvider: suspend () -> String?,
) {
    internal val json by lazy {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            prettyPrint = false
            coerceInputValues = true
            isLenient = true
        }
    }

    internal val client = HttpClient(createHttpEngine()) {
        install(ContentNegotiation) { json(json) }
        install(Logging) { level = LogLevel.INFO }
    }

    fun HttpRequestBuilder.supabaseHeaders(token: String?) {
        header("apikey", Env.supabaseAnonKey)
        if (!token.isNullOrBlank()) header("Authorization", "Bearer $token")
        accept(ContentType.Application.Json)
        contentType(ContentType.Application.Json)
    }

    internal suspend fun url(path: String) = buildString {
        append(Env.supabaseUrl.trimEnd('/'))
        append('/')
        append(path.trimStart('/'))
    }

    internal suspend inline fun <reified T> get(
        path: String,
        params: Map<String, String?> = emptyMap(),
    ): T {
        val token = accessTokenProvider()
        val resp = client.get(url(path)) {
            supabaseHeaders(token)
            params.forEach { (k, v) -> if (!v.isNullOrBlank()) parameter(k, v) }
        }
        return resp.body()
    }

    internal suspend inline fun <reified T, reified B> post(
        path: String,
        body: B,
        params: Map<String, String?> = emptyMap(),
    ): T {
        val token = accessTokenProvider()
        val resp = client.post(url(path)) {
            supabaseHeaders(token)
            params.forEach { (k, v) -> if (!v.isNullOrBlank()) parameter(k, v) }
            setBody(body)
        }
        return resp.body()
    }

    internal suspend inline fun <reified T, reified B> patch(
        path: String,
        body: B,
        params: Map<String, String?> = emptyMap(),
    ): T {
        val token = accessTokenProvider()
        val resp = client.patch(url(path)) {
            supabaseHeaders(token)
            params.forEach { (k, v) -> if (!v.isNullOrBlank()) parameter(k, v) }
            setBody(body)
        }
        return resp.body()
    }

    internal suspend inline fun <reified T> delete(
        path: String,
        params: Map<String, String?> = emptyMap(),
    ): T {
        val token = accessTokenProvider()
        val resp = client.delete(url(path)) {
            supabaseHeaders(token)
            params.forEach { (k, v) -> if (!v.isNullOrBlank()) parameter(k, v) }
        }
        return resp.body()
    }

    /**
     * Execute RPC function call
     */
    internal suspend inline fun <reified T> rpc(
        functionName: String,
        params: Map<String, Any> = emptyMap(),
    ): T {
        val token = accessTokenProvider()
        val resp = client.post(url("rest/v1/rpc/$functionName")) {
            supabaseHeaders(token)
            setBody(params)
        }
        return resp.body()
    }

    /**
     * Upload file to Supabase Storage
     */
    internal suspend fun uploadFile(
        bucket: String,
        path: String,
        data: ByteArray,
        mimeType: String,
    ): String {
        val token = accessTokenProvider()
        val storageUrl = url("storage/v1/object/$bucket/$path")

        client.post(storageUrl) {
            header("apikey", Env.supabaseAnonKey)
            if (!token.isNullOrBlank()) header("Authorization", "Bearer $token")
            contentType(ContentType.parse(mimeType))
            setBody(data)
        }

        return path
    }
}
