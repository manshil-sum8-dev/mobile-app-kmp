package za.co.quantive.app.presentation.onboarding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import za.co.quantive.app.data.remote.api.OnboardingApi
import za.co.quantive.app.domain.profile.BusinessAddress
import za.co.quantive.app.domain.profile.BusinessProfile
import za.co.quantive.app.domain.profile.BusinessType
import za.co.quantive.app.domain.profile.Country
import za.co.quantive.app.domain.profile.EmployeeCount
import za.co.quantive.app.domain.profile.Province

/**
 * Simplified ViewModel for the redesigned onboarding flow
 */
class OnboardingViewModel(
    private val onboardingApi: OnboardingApi?,
) {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    // Business Type Selection
    fun updateBusinessType(type: BusinessType) {
        _state.value = _state.value.copy(businessType = type)
    }

    // Business Info Methods
    fun updateBusinessName(name: String) {
        _state.value = _state.value.copy(businessName = name)
    }

    fun updateCompanyRegistrationNumber(regNumber: String) {
        _state.value = _state.value.copy(companyRegistrationNumber = regNumber)
    }

    fun updateWebsite(website: String) {
        _state.value = _state.value.copy(website = website)
    }

    fun updateIndustry(industry: String) {
        _state.value = _state.value.copy(industry = industry)
    }

    fun updateEmployeeCount(count: EmployeeCount) {
        _state.value = _state.value.copy(employeeCount = count)
    }

    fun updatePhone(phone: String) {
        _state.value = _state.value.copy(phone = phone)
    }

    fun updateDescription(description: String) {
        _state.value = _state.value.copy(description = description)
    }

    // Location Info Methods
    fun selectCountry(country: Country) {
        _state.value = _state.value.copy(
            selectedCountry = country,
            selectedProvince = null, // Reset province when country changes
        )
    }

    fun selectProvince(province: Province) {
        _state.value = _state.value.copy(selectedProvince = province)
    }

    fun updateBusinessAddress(address: BusinessAddress) {
        _state.value = _state.value.copy(businessAddress = address)
    }

    // Logo Upload
    fun uploadLogo(logoData: ByteArray, mimeType: String) {
        scope.launch {
            try {
                _state.value = _state.value.copy(isUploadingLogo = true)

                // For now, just simulate success - logo upload can be implemented later
                _state.value = _state.value.copy(
                    isUploadingLogo = false,
                    logoPath = "uploaded_logo_path.png",
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isUploadingLogo = false)
            }
        }
    }

    // Data Loading Methods
    fun loadCountries() {
        scope.launch {
            try {
                _state.value = _state.value.copy(isLoadingCountries = true)
                val countries = onboardingApi?.getCountries() ?: mockCountries()
                _state.value = _state.value.copy(
                    countries = countries,
                    isLoadingCountries = false,
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoadingCountries = false)
            }
        }
    }

    fun loadProvinces(countryId: String) {
        scope.launch {
            try {
                _state.value = _state.value.copy(isLoadingProvinces = true)
                val provinces = onboardingApi?.getProvinces(countryId) ?: mockProvinces(countryId)
                _state.value = _state.value.copy(
                    provinces = provinces,
                    isLoadingProvinces = false,
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoadingProvinces = false)
            }
        }
    }

    // Complete Onboarding
    fun completeOnboarding(onComplete: () -> Unit) {
        scope.launch {
            try {
                _state.value = _state.value.copy(isCompleting = true)

                if (onboardingApi != null) {
                    // Create business profile from current state
                    val profile = createBusinessProfileFromState()
                    val createdProfile = onboardingApi.createBusinessProfile(profile)

                    // Mark as complete
                    onboardingApi.completeOnboarding(createdProfile.id!!)
                }

                _state.value = _state.value.copy(isCompleting = false)
                onComplete()
            } catch (e: Exception) {
                _state.value = _state.value.copy(isCompleting = false)
            }
        }
    }

    private fun createBusinessProfileFromState(): BusinessProfile {
        val currentState = _state.value
        return BusinessProfile(
            name = currentState.businessName,
            business_type = currentState.businessType,
            company_registration_number = currentState.companyRegistrationNumber,
            website = currentState.website,
            industry = currentState.industry,
            employee_count = currentState.employeeCount,
            business_address = currentState.businessAddress,
            country_id = currentState.selectedCountry?.id,
            province_id = currentState.selectedProvince?.id,
            phone = currentState.phone,
            description = currentState.description,
            logo_path = currentState.logoPath,
        )
    }

    // Mock data for testing (when API is not available)
    private fun mockCountries(): List<Country> {
        return listOf(
            Country("za", "ZA", "ZAR", "South Africa", "ZAR", "+27", "Africa", "Southern Africa", "Southern Africa"),
            Country("us", "US", "USA", "United States", "USD", "+1", "North America", "North America", "North America"),
            Country("gb", "GB", "GBR", "United Kingdom", "GBP", "+44", "Europe", "Europe", "Western Europe"),
            Country("ca", "CA", "CAN", "Canada", "CAD", "+1", "North America", "North America", "North America"),
            Country("au", "AU", "AUS", "Australia", "AUD", "+61", "Oceania", "Oceania", "Australia and New Zealand"),
        )
    }

    private fun mockProvinces(countryId: String): List<Province> {
        return when (countryId) {
            "za" -> listOf(
                Province("za_wc", "za", "WC", "Western Cape", "Province"),
                Province("za_gp", "za", "GP", "Gauteng", "Province"),
                Province("za_kzn", "za", "KZN", "KwaZulu-Natal", "Province"),
            )
            "us" -> listOf(
                Province("us_ca", "us", "CA", "California", "State"),
                Province("us_ny", "us", "NY", "New York", "State"),
                Province("us_tx", "us", "TX", "Texas", "State"),
            )
            else -> emptyList()
        }
    }
}

/**
 * Onboarding UI State
 */
data class OnboardingState(
    val businessName: String = "",
    val businessType: BusinessType? = null,
    val companyRegistrationNumber: String? = null,
    val website: String? = null,
    val industry: String? = null,
    val employeeCount: EmployeeCount? = null,
    val phone: String? = null,
    val description: String? = null,
    val businessAddress: BusinessAddress? = null,
    val selectedCountry: Country? = null,
    val selectedProvince: Province? = null,
    val logoPath: String? = null,

    val countries: List<Country> = emptyList(),
    val provinces: List<Province> = emptyList(),

    val isLoadingCountries: Boolean = false,
    val isLoadingProvinces: Boolean = false,
    val isUploadingLogo: Boolean = false,
    val isCompleting: Boolean = false,
)
