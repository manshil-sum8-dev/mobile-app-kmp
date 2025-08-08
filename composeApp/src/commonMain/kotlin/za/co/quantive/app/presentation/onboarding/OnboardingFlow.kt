package za.co.quantive.app.presentation.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import za.co.quantive.app.presentation.onboarding.screens.BusinessInfoScreen
import za.co.quantive.app.presentation.onboarding.screens.LocationInfoScreen
import za.co.quantive.app.presentation.onboarding.screens.SplashScreen
import za.co.quantive.app.presentation.onboarding.screens.UserTypeSelectionScreen
import za.co.quantive.app.presentation.onboarding.screens.ValuePropositionCarousel

/**
 * New Redesigned Onboarding Flow
 * 5-screen streamlined experience per user requirements
 */
@Composable
fun QuantiveOnboardingFlow(
    onOnboardingComplete: () -> Unit,
) {
    var currentStep by remember { mutableStateOf(OnboardingStep.SPLASH) }

    // Initialize ViewModel (with mock API for now)
    val viewModel = remember { OnboardingViewModel(null) }
    val state by viewModel.state.collectAsState()

    // Load initial data
    LaunchedEffect(Unit) {
        viewModel.loadCountries()
    }

    AnimatedContent(
        targetState = currentStep,
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { if (targetState.ordinal > initialState.ordinal) it else -it },
                animationSpec = tween(300),
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { if (targetState.ordinal > initialState.ordinal) -it else it },
                animationSpec = tween(300),
            )
        },
        modifier = Modifier.fillMaxSize(),
        label = "onboarding_flow",
    ) { step ->
        when (step) {
            OnboardingStep.SPLASH -> {
                SplashScreen(
                    onSplashComplete = {
                        currentStep = OnboardingStep.VALUE_PROPOSITION
                    },
                )
            }

            OnboardingStep.VALUE_PROPOSITION -> {
                ValuePropositionCarousel(
                    onContinue = {
                        currentStep = OnboardingStep.USER_TYPE_SELECTION
                    },
                    onSkip = {
                        currentStep = OnboardingStep.USER_TYPE_SELECTION
                    },
                )
            }

            OnboardingStep.USER_TYPE_SELECTION -> {
                UserTypeSelectionScreen(
                    selectedBusinessType = state.businessType,
                    onBusinessTypeSelected = { businessType ->
                        viewModel.updateBusinessType(businessType)
                    },
                    onContinue = {
                        currentStep = OnboardingStep.BUSINESS_INFO
                    },
                )
            }

            OnboardingStep.BUSINESS_INFO -> {
                BusinessInfoScreen(
                    businessName = state.businessName,
                    onBusinessNameChange = { viewModel.updateBusinessName(it) },
                    businessType = state.businessType,
                    companyRegistrationNumber = state.companyRegistrationNumber,
                    onCompanyRegistrationNumberChange = { viewModel.updateCompanyRegistrationNumber(it) },
                    website = state.website,
                    onWebsiteChange = { viewModel.updateWebsite(it) },
                    industry = state.industry,
                    onIndustryChange = { viewModel.updateIndustry(it) },
                    employeeCount = state.employeeCount,
                    onEmployeeCountChange = { viewModel.updateEmployeeCount(it) },
                    logoPath = state.logoPath,
                    onLogoUpload = {
                        // TODO: Trigger image picker
                        viewModel.uploadLogo(byteArrayOf(), "image/png")
                    },
                    onContinue = {
                        currentStep = OnboardingStep.LOCATION_INFO
                    },
                )
            }

            OnboardingStep.LOCATION_INFO -> {
                LocationInfoScreen(
                    businessAddress = state.businessAddress,
                    onBusinessAddressChange = { viewModel.updateBusinessAddress(it) },
                    selectedCountry = state.selectedCountry,
                    onCountrySelected = { country ->
                        viewModel.selectCountry(country)
                        viewModel.loadProvinces(country.id)
                    },
                    selectedProvince = state.selectedProvince,
                    onProvinceSelected = { viewModel.selectProvince(it) },
                    phone = state.phone,
                    onPhoneChange = { viewModel.updatePhone(it) },
                    description = state.description,
                    onDescriptionChange = { viewModel.updateDescription(it) },
                    countries = state.countries,
                    provinces = state.provinces,
                    isLoadingProvinces = state.isLoadingProvinces,
                    onLoadCountries = { viewModel.loadCountries() },
                    onLoadProvinces = { countryId -> viewModel.loadProvinces(countryId) },
                    onComplete = {
                        viewModel.completeOnboarding {
                            onOnboardingComplete()
                        }
                    },
                )
            }
        }
    }
}

/**
 * Onboarding steps enum for navigation
 */
enum class OnboardingStep {
    SPLASH,
    VALUE_PROPOSITION,
    USER_TYPE_SELECTION,
    BUSINESS_INFO,
    LOCATION_INFO,
}
