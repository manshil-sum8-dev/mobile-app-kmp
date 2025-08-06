package za.co.quantive.app.app

import za.co.quantive.app.auth.AuthService
import za.co.quantive.app.auth.Session
import za.co.quantive.app.core.env.Env
import za.co.quantive.app.data.remote.SupabaseClient
import za.co.quantive.app.data.remote.BusinessProfileRemote
import za.co.quantive.app.data.remote.BusinessProfileRepositoryImpl
import za.co.quantive.app.data.local.BusinessProfileLocal
import za.co.quantive.app.domain.profile.BusinessProfileRepository
import za.co.quantive.app.data.remote.api.*
import za.co.quantive.app.data.remote.repository.*
import za.co.quantive.app.data.local.*
import za.co.quantive.app.security.SecureStore
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// Manual DI providers for early phases
object AppServices {
    private val mutex = Mutex()

    // In-memory cache of session for fast header access
    private var cachedSession: Session? = null

    // Provider: access token for SupabaseClient
    private val accessTokenProvider: suspend () -> String? = {
        mutex.withLock {
            if (cachedSession == null) cachedSession = SecureStore.getSession()
            cachedSession?.accessToken
        }
    }

    // Provider: owner ID for data operations
    private val ownerIdProvider: suspend () -> String? = {
        mutex.withLock {
            if (cachedSession == null) cachedSession = SecureStore.getSession()
            cachedSession?.userId
        }
    }

    // Provider: session for repositories
    private val sessionProvider: suspend () -> Session? = {
        mutex.withLock {
            if (cachedSession == null) cachedSession = SecureStore.getSession()
            cachedSession
        }
    }

    val supabase by lazy { SupabaseClient(accessTokenProvider) }
    val auth by lazy { AuthService(supabase) }

    // Backend-driven API services
    private val invoiceApi by lazy { InvoiceApiImpl(supabase) }
    private val analyticsApi by lazy { AnalyticsApiImpl(supabase) }
    private val contactApi by lazy { ContactApiImpl(supabase) }
    private val businessApi by lazy { BusinessApiImpl(supabase) }
    
    // Local cache implementations (placeholder)
    private val invoiceCache by lazy { InvoiceCacheImpl() }
    private val analyticsCache by lazy { AnalyticsCacheImpl() }
    private val contactCache by lazy { ContactCacheImpl() }
    
    // Backend-driven repositories
    val invoiceRepository: InvoiceRepository by lazy { 
        BackendInvoiceRepository(invoiceApi, invoiceCache)
    }
    
    val analyticsRepository: AnalyticsRepository by lazy {
        BackendAnalyticsRepository(analyticsApi, analyticsCache)
    }
    
    val contactRepository: ContactRepository by lazy {
        BackendContactRepository(contactApi, contactCache)
    }

    // Business Profile services (legacy - will be migrated)
    private val businessProfileLocal by lazy { BusinessProfileLocal() }
    private val businessProfileRemote by lazy { BusinessProfileRemote(supabase, ownerIdProvider) }
    val businessProfileRepository: BusinessProfileRepository by lazy { 
        BusinessProfileRepositoryImpl(businessProfileLocal, businessProfileRemote, sessionProvider) 
    }

    suspend fun setSession(session: Session?) {
        mutex.withLock {
            cachedSession = session
            SecureStore.saveSession(session)
        }
    }

    suspend fun clearSession() {
        mutex.withLock {
            cachedSession = null
            SecureStore.clearSession()
        }
    }
}
