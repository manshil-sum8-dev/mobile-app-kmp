package za.co.quantive.app.domain.profile

interface BusinessProfileRepository {
    suspend fun get(): BusinessProfile?
    suspend fun save(profile: BusinessProfile): BusinessProfile
    suspend fun sync(): BusinessProfile?
}
