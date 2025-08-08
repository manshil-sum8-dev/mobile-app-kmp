package za.co.quantive.app.data.remote.api

import za.co.quantive.app.domain.profile.BusinessProfile
import za.co.quantive.app.domain.profile.Country
import za.co.quantive.app.domain.profile.Province

/**
 * API interface for onboarding operations
 */
interface OnboardingApi {

    /**
     * Get list of countries for location selection
     */
    suspend fun getCountries(): List<Country>

    /**
     * Get list of provinces/states for a specific country
     */
    suspend fun getProvinces(countryId: String): List<Province>

    /**
     * Create a new business profile
     */
    suspend fun createBusinessProfile(profile: BusinessProfile): BusinessProfile

    /**
     * Update existing business profile
     */
    suspend fun updateBusinessProfile(profileId: String, profile: BusinessProfile): BusinessProfile

    /**
     * Upload business logo to storage
     */
    suspend fun uploadBusinessLogo(profileId: String, logoData: ByteArray, mimeType: String): String

    /**
     * Complete onboarding process
     */
    suspend fun completeOnboarding(profileId: String): Boolean
}
