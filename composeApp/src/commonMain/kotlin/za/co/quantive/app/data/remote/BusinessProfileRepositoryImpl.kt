package za.co.quantive.app.data.remote

import za.co.quantive.app.auth.Session
import za.co.quantive.app.data.local.BusinessProfileLocal
import za.co.quantive.app.domain.profile.BusinessProfile
import za.co.quantive.app.domain.profile.BusinessProfileRepository

class BusinessProfileRepositoryImpl(
    private val local: BusinessProfileLocal,
    private val remote: BusinessProfileRemote,
    private val sessionProvider: suspend () -> Session?,
) : BusinessProfileRepository {
    override suspend fun get(): BusinessProfile? {
        // Try local first; if empty and session exists, fetch remote and cache
        val localVal = local.get()
        if (localVal != null) return localVal
        val session = sessionProvider() ?: return null
        val remoteVal = remote.get()
        if (remoteVal != null) local.save(remoteVal)
        return remoteVal
    }

    override suspend fun save(profile: BusinessProfile): BusinessProfile {
        local.save(profile)
        val session = sessionProvider()
        return if (session != null) {
            remote.upsert(profile) ?: profile
        } else {
            profile
        }
    }

    override suspend fun sync(): BusinessProfile? {
        val session = sessionProvider() ?: return local.get()
        val remoteVal = remote.get()
        if (remoteVal != null) local.save(remoteVal)
        return remoteVal
    }
}
