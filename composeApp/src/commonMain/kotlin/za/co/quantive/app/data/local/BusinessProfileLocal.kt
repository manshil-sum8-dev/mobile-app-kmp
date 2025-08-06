package za.co.quantive.app.data.local

import za.co.quantive.app.domain.profile.BusinessProfile

// Placeholder local store for BusinessProfile. Will be replaced with SQLDelight integration later.
class BusinessProfileLocal {
    private var cache: BusinessProfile? = null

    suspend fun get(): BusinessProfile? = cache
    suspend fun save(profile: BusinessProfile): BusinessProfile {
        cache = profile
        return profile
    }
}
