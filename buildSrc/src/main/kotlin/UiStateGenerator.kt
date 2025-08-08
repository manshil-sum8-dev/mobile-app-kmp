/**
 * UI State generation utilities for feature scaffolding
 */

fun generateUiStateContent(packageName: String, className: String, featureType: FeatureType): String {
    return """
package $packageName

/**
 * $className UI state following MVI architecture pattern
 * Generated according to enterprise blueprint standards
 */
sealed class ${className}UiState {
    object Loading : ${className}UiState()
    data class Success(val data: List<$className>) : ${className}UiState()
    data class Error(val message: String) : ${className}UiState()
}

/**
 * $className user intents for MVI pattern
 */
sealed class ${className}Intent {
    object LoadData : ${className}Intent()
    object Refresh : ${className}Intent()
    object Retry : ${className}Intent()${when (featureType) {
        FeatureType.CRUD -> """
    data class Create${className}(val request: Create${className}Request) : ${className}Intent()
    data class Update${className}(val id: String, val request: Update${className}Request) : ${className}Intent()
    data class Delete${className}(val id: String) : ${className}Intent()"""
        FeatureType.READONLY -> ""
        FeatureType.CUSTOM -> """
    // Add custom intents as needed"""
    }}
}

/**
 * $className UI events for side effects
 */
sealed class ${className}UiEvent {
    object NavigateBack : ${className}UiEvent()
    data class ShowSnackbar(val message: String) : ${className}UiEvent()
    data class ShowError(val message: String) : ${className}UiEvent()${when (featureType) {
        FeatureType.CRUD -> """
    data class NavigateTo${className}Detail(val id: String) : ${className}UiEvent()
    object NavigateToCreate${className} : ${className}UiEvent()"""
        FeatureType.READONLY -> """
    data class NavigateTo${className}Detail(val id: String) : ${className}UiEvent()"""
        FeatureType.CUSTOM -> """
    // Add custom UI events as needed"""
    }}
}
""".trimIndent()
}