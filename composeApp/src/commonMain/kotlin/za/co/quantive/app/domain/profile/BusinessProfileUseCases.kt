package za.co.quantive.app.domain.profile

class GetBusinessProfile(private val repo: BusinessProfileRepository) {
    suspend operator fun invoke(): BusinessProfile? = repo.get()
}

class SaveBusinessProfile(private val repo: BusinessProfileRepository) {
    suspend operator fun invoke(profile: BusinessProfile): BusinessProfile = repo.save(profile)
}
