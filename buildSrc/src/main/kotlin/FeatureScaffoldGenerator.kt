import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import org.gradle.api.Project
import java.io.File
import java.io.StringWriter

/**
 * Core feature scaffolding generator
 * Implements all blueprint templates and patterns
 */
class FeatureScaffoldGenerator(private val project: Project) {
    
    private val freemarkerConfig = Configuration(Configuration.VERSION_2_3_32).apply {
        setClassForTemplateLoading(this::class.java, "/")
        defaultEncoding = "UTF-8"
        templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
        logTemplateExceptions = false
        wrapUncheckedExceptions = true
        fallbackOnNullLoopVariable = false
    }
    
    private val basePackage = "za.co.quantive.app.features"
    
    fun generateFeature(config: FeatureConfig) {
        val featureDir = createFeatureDirectory(config)
        
        // Generate all architectural layers
        generateDomainLayer(featureDir, config)
        generateDataLayer(featureDir, config) 
        generatePresentationLayer(featureDir, config)
        generateDILayer(featureDir, config)
        
        // Generate tests if requested
        if (config.includeScreenshots) {
            generateTestLayer(featureDir, config)
        }
        
        println("📦 Generated complete feature structure for ${config.name}")
    }
    
    private fun createFeatureDirectory(config: FeatureConfig): File {
        val featureDir = File(
            project.projectDir,
            "composeApp/src/commonMain/kotlin/za/co/quantive/app/features/${config.packageName}"
        )
        
        // Create directory structure
        listOf("domain", "data", "presentation", "di", "test").forEach { layer ->
            File(featureDir, layer).mkdirs()
        }
        
        return featureDir
    }
    
    private fun generateDomainLayer(featureDir: File, config: FeatureConfig) {
        val domainDir = File(featureDir, "domain")
        
        // Generate entity
        generateFromTemplate(
            "templates/domain/Entity.kt.ftl",
            File(domainDir, "${config.className}.kt"),
            mapOf(
                "packageName" to "$basePackage.${config.packageName}.domain",
                "className" to config.className,
                "featureName" to config.name
            )
        )
        
        // Generate repository interface
        generateFromTemplate(
            "templates/domain/Repository.kt.ftl",
            File(domainDir, "${config.className}Repository.kt"),
            mapOf(
                "packageName" to "$basePackage.${config.packageName}.domain",
                "className" to config.className,
                "featureName" to config.name,
                "featureType" to config.type
            )
        )
        
        // Generate use cases
        generateFromTemplate(
            "templates/domain/UseCases.kt.ftl",
            File(domainDir, "${config.className}UseCases.kt"),
            mapOf(
                "packageName" to "$basePackage.${config.packageName}.domain", 
                "className" to config.className,
                "featureName" to config.name,
                "featureType" to config.type
            )
        )
    }
    
    private fun generateDataLayer(featureDir: File, config: FeatureConfig) {
        val dataDir = File(featureDir, "data")
        
        // Generate API interface
        generateFromTemplate(
            "templates/data/Api.kt.ftl",
            File(dataDir, "${config.className}Api.kt"),
            mapOf(
                "packageName" to "$basePackage.${config.packageName}.data",
                "className" to config.className,
                "featureName" to config.name,
                "featureType" to config.type
            )
        )
        
        // Generate API implementation
        generateFromTemplate(
            "templates/data/ApiImpl.kt.ftl",
            File(dataDir, "${config.className}ApiImpl.kt"),
            mapOf(
                "packageName" to "$basePackage.${config.packageName}.data",
                "className" to config.className,
                "featureName" to config.name,
                "featureType" to config.type
            )
        )
        
        // Generate repository implementation
        generateFromTemplate(
            "templates/data/RepositoryImpl.kt.ftl", 
            File(dataDir, "Backend${config.className}Repository.kt"),
            mapOf(
                "packageName" to "$basePackage.${config.packageName}.data",
                "className" to config.className,
                "featureName" to config.name,
                "featureType" to config.type,
                "includeCaching" to config.includeCaching
            )
        )
        
        // Generate cache implementation if requested
        if (config.includeCaching) {
            generateFromTemplate(
                "templates/data/CacheImpl.kt.ftl",
                File(dataDir, "${config.className}CacheImpl.kt"),
                mapOf(
                    "packageName" to "$basePackage.${config.packageName}.data",
                    "className" to config.className,
                    "featureName" to config.name
                )
            )
        }
    }
    
    private fun generatePresentationLayer(featureDir: File, config: FeatureConfig) {
        val presentationDir = File(featureDir, "presentation")
        
        // Generate ViewModel
        generateFromTemplate(
            "templates/presentation/ViewModel.kt.ftl",
            File(presentationDir, "${config.className}ViewModel.kt"),
            mapOf(
                "packageName" to "$basePackage.${config.packageName}.presentation",
                "className" to config.className,
                "featureName" to config.name,
                "featureType" to config.type
            )
        )
        
        // Generate UI states
        generateFromTemplate(
            "templates/presentation/UiState.kt.ftl",
            File(presentationDir, "${config.className}UiState.kt"),
            mapOf(
                "packageName" to "$basePackage.${config.packageName}.presentation",
                "className" to config.className,
                "featureName" to config.name,
                "featureType" to config.type
            )
        )
        
        // Generate screen composable
        generateFromTemplate(
            "templates/presentation/Screen.kt.ftl",
            File(presentationDir, "${config.className}Screen.kt"),
            mapOf(
                "packageName" to "$basePackage.${config.packageName}.presentation",
                "className" to config.className,
                "featureName" to config.name,
                "featureType" to config.type,
                "navigationFramework" to config.navigationFramework
            )
        )
    }
    
    private fun generateDILayer(featureDir: File, config: FeatureConfig) {
        val diDir = File(featureDir, "di")
        
        generateFromTemplate(
            "templates/di/Module.kt.ftl",
            File(diDir, "${config.className}Module.kt"),
            mapOf(
                "packageName" to "$basePackage.${config.packageName}.di",
                "className" to config.className,
                "featureName" to config.name,
                "includeCaching" to config.includeCaching
            )
        )
    }
    
    private fun generateTestLayer(featureDir: File, config: FeatureConfig) {
        val testDir = File(featureDir, "test")
        
        // Generate ViewModel tests
        generateFromTemplate(
            "templates/test/ViewModelTest.kt.ftl",
            File(testDir, "${config.className}ViewModelTest.kt"),
            mapOf(
                "packageName" to "$basePackage.${config.packageName}.test",
                "className" to config.className,
                "featureName" to config.name
            )
        )
        
        // Generate repository tests
        generateFromTemplate(
            "templates/test/RepositoryTest.kt.ftl",
            File(testDir, "${config.className}RepositoryTest.kt"),
            mapOf(
                "packageName" to "$basePackage.${config.packageName}.test",
                "className" to config.className,
                "featureName" to config.name
            )
        )
        
        // Generate screenshot tests
        if (config.includeScreenshots) {
            generateFromTemplate(
                "templates/test/ScreenshotTest.kt.ftl",
                File(testDir, "${config.className}ScreenshotTest.kt"),
                mapOf(
                    "packageName" to "$basePackage.${config.packageName}.test",
                    "className" to config.className,
                    "featureName" to config.name
                )
            )
        }
    }
    
    private fun generateFromTemplate(templatePath: String, outputFile: File, dataModel: Map<String, Any>) {
        try {
            // For now, generate basic content without FreeMarker templates
            // This will be enhanced with actual templates
            val content = generateBasicContent(templatePath, dataModel)
            outputFile.parentFile.mkdirs()
            outputFile.writeText(content)
        } catch (e: Exception) {
            println("⚠️  Warning: Could not generate ${outputFile.name}: ${e.message}")
            // Generate basic placeholder instead
            outputFile.parentFile.mkdirs()
            outputFile.writeText(generatePlaceholder(outputFile.name, dataModel))
        }
    }
    
    private fun generateBasicContent(templatePath: String, dataModel: Map<String, Any>): String {
        val className = dataModel["className"] as String
        val packageName = dataModel["packageName"] as String
        val featureType = dataModel.getOrDefault("featureType", FeatureType.CRUD) as FeatureType
        
        return when {
            templatePath.contains("Entity.kt") -> generateEntityContent(packageName, className)
            templatePath.contains("Repository.kt") -> generateRepositoryContent(packageName, className, featureType)
            templatePath.contains("ViewModel.kt") -> generateViewModelContent(packageName, className)
            templatePath.contains("UiState.kt") -> generateUiStateContent(packageName, className)
            templatePath.contains("Screen.kt") -> generateScreenContent(packageName, className)
            templatePath.contains("Api.kt") && !templatePath.contains("ApiImpl") -> generateApiContent(packageName, className, featureType)
            templatePath.contains("ApiImpl.kt") -> generateApiImplContent(packageName, className, featureType)
            templatePath.contains("RepositoryImpl.kt") -> generateRepositoryImplContent(packageName, className, featureType)
            templatePath.contains("Module.kt") -> generateModuleContent(packageName, className)
            templatePath.contains("CacheImpl.kt") -> generateCacheImplContent(packageName, className)
            templatePath.contains("UseCases.kt") -> generateUseCasesContent(packageName, className)
            templatePath.contains("ViewModelTest.kt") -> generateViewModelTestContent(packageName, className)
            templatePath.contains("RepositoryTest.kt") -> generateRepositoryTestContent(packageName, className)
            templatePath.contains("ScreenshotTest.kt") -> generateScreenshotTestContent(packageName, className)
            else -> generatePlaceholder(templatePath, dataModel)
        }
    }
    
    private fun generatePlaceholder(fileName: String, dataModel: Map<String, Any>): String {
        val className = dataModel["className"] as String
        val packageName = dataModel["packageName"] as String
        
        return """
package $packageName

/**
 * Generated $fileName for $className feature
 * TODO: Implement according to blueprint standards
 */
class ${fileName.removeSuffix(".kt")} {
    // TODO: Implement feature functionality
}
""".trimIndent()
    }
    
    // Content generation methods will be added for each template type
    private fun generateEntityContent(packageName: String, className: String): String {
        return """
package $packageName

import kotlinx.serialization.Serializable

/**
 * $className entity following domain-driven design
 * Generated according to enterprise blueprint standards
 */
@Serializable
data class $className(
    val id: String,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Serializable
data class ${className}Filter(
    val searchQuery: String? = null,
    val sortBy: ${className}SortBy = ${className}SortBy.CREATED_AT,
    val sortOrder: SortOrder = SortOrder.DESC
)

enum class ${className}SortBy {
    NAME, CREATED_AT, UPDATED_AT
}

enum class SortOrder {
    ASC, DESC
}
""".trimIndent()
    }
    
    private fun generateRepositoryContent(packageName: String, className: String, featureType: FeatureType): String {
        val crudMethods = when (featureType) {
            FeatureType.CRUD -> """
    suspend fun create$className(request: Create${className}Request): Result<$className>
    suspend fun update$className(id: String, request: Update${className}Request): Result<$className>
    suspend fun delete$className(id: String): Result<Unit>"""
            FeatureType.READONLY -> ""
            FeatureType.CUSTOM -> "// Add custom methods as needed"
        }
        
        return """
package $packageName

import kotlinx.coroutines.flow.Flow
import za.co.quantive.app.data.remote.api.*

/**
 * $className repository interface following Clean Architecture principles
 * Backend-driven with smart caching for performance
 */
interface ${className}Repository {
    suspend fun get${className}s(
        filter: ${className}Filter? = null,
        forceRefresh: Boolean = false
    ): Flow<Result<List<$className>>>
    
    suspend fun get$className(id: String): Result<$className>$crudMethods
}

// API Request/Response models
@kotlinx.serialization.Serializable
data class Create${className}Request(
    val name: String
)

@kotlinx.serialization.Serializable  
data class Update${className}Request(
    val name: String? = null
)
""".trimIndent()
    }
    
    private fun generateViewModelContent(packageName: String, className: String): String {
        return """
package $packageName

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * $className ViewModel following MVI architecture pattern
 * Generated according to enterprise blueprint standards
 */
class ${className}ViewModel(
    private val repository: ${className}Repository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(${className}UiState.Loading)
    val uiState: StateFlow<${className}UiState> = _uiState.asStateFlow()
    
    private val _uiEvents = Channel<${className}UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()
    
    fun handleIntent(intent: ${className}Intent) {
        when (intent) {
            is ${className}Intent.LoadData -> loadData()
            is ${className}Intent.Refresh -> loadData(forceRefresh = true)
            is ${className}Intent.Retry -> loadData()
        }
    }
    
    private fun loadData(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = ${className}UiState.Loading
            
            repository.get${className}s(forceRefresh = forceRefresh)
                .collect { result ->
                    _uiState.value = when {
                        result.isSuccess -> ${className}UiState.Success(result.getOrNull() ?: emptyList())
                        else -> ${className}UiState.Error(
                            result.exceptionOrNull()?.message ?: "Unknown error occurred"
                        )
                    }
                }
        }
    }
    
    private fun sendEvent(event: ${className}UiEvent) {
        viewModelScope.launch {
            _uiEvents.send(event)
        }
    }
}
""".trimIndent()
    }
    
    private fun generateScreenContent(packageName: String, className: String): String {
        return """
package $packageName

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import za.co.quantive.app.presentation.theme.QuantiveTheme

/**
 * $className screen following Material 3 Expressive design
 * Generated according to enterprise blueprint standards
 */
@Composable
fun ${className}Screen(
    uiState: ${className}UiState,
    onIntent: (${className}Intent) -> Unit,
    onNavigateBack: () -> Unit
) {
    
    LaunchedEffect(Unit) {
        onIntent(${className}Intent.LoadData)
    }
    
    QuantiveTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (uiState) {
                is ${className}UiState.Loading -> LoadingContent()
                is ${className}UiState.Success -> ${className}Content(
                    items = uiState.data,
                    onRetry = { onIntent(${className}Intent.Retry) }
                )
                is ${className}UiState.Error -> ErrorContent(
                    message = uiState.message,
                    onRetry = { onIntent(${className}Intent.Retry) }
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ${className}Content(
    items: List<$className>,
    onRetry: () -> Unit
) {
    LazyColumn {
        items(items) { item ->
            ${className}Item(
                item = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun ${className}Item(
    item: $className,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "ID: ${'$'}{item.id}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
""".trimIndent()
    }
    
    private fun generateApiContent(packageName: String, className: String, featureType: FeatureType): String {
        val crudMethods = when (featureType) {
            FeatureType.CRUD -> """
    /**
     * POST /${className.lowercase()}s - Create new $className
     */
    suspend fun create$className(request: Create${className}Request): ApiResponse<$className>
    
    /**
     * PUT /${className.lowercase()}s/{id} - Update existing $className
     */
    suspend fun update$className(id: String, request: Update${className}Request): ApiResponse<$className>
    
    /**
     * DELETE /${className.lowercase()}s/{id} - Delete $className
     */
    suspend fun delete$className(id: String): ApiResponse<Unit>"""
            FeatureType.READONLY -> ""
            FeatureType.CUSTOM -> "// Add custom API methods as needed"
        }
        
        return """
package $packageName

import za.co.quantive.app.data.remote.api.*

/**
 * $className API interface following REST-first principles
 * CRUD operations use REST endpoints, complex operations use RPC
 */
interface ${className}Api {
    
    /**
     * GET /${className.lowercase()}s - Get paginated list with backend filtering
     */
    suspend fun get${className}s(
        page: Int = 0,
        limit: Int = 20,
        filter: ${className}Filter? = null
    ): ApiResponse<PaginatedResponse<$className>>
    
    /**
     * GET /${className.lowercase()}s/{id} - Get single $className by ID
     */
    suspend fun get$className(id: String): ApiResponse<$className>$crudMethods
}
""".trimIndent()
    }
    
    private fun generateApiImplContent(packageName: String, className: String, featureType: FeatureType): String {
        val crudImplementations = when (featureType) {
            FeatureType.CRUD -> """
    override suspend fun create$className(request: Create${className}Request): ApiResponse<$className> {
        return try {
            val created${className}: $className = client.post("rest/v1/${className.lowercase()}s", request)
            ApiResponse.success(created$className)
        } catch (e: Exception) {
            ApiResponse.error("Failed to create ${className.lowercase()}: ${'$'}{e.message}")
        }
    }
    
    override suspend fun update$className(id: String, request: Update${className}Request): ApiResponse<$className> {
        return try {
            val updated${className}: $className = client.put("rest/v1/${className.lowercase()}s/${'$'}id", request)
            ApiResponse.success(updated$className)
        } catch (e: Exception) {
            ApiResponse.error("Failed to update ${className.lowercase()}: ${'$'}{e.message}")
        }
    }
    
    override suspend fun delete$className(id: String): ApiResponse<Unit> {
        return try {
            client.delete("rest/v1/${className.lowercase()}s/${'$'}id")
            ApiResponse.success(Unit)
        } catch (e: Exception) {
            ApiResponse.error("Failed to delete ${className.lowercase()}: ${'$'}{e.message}")
        }
    }"""
            FeatureType.READONLY -> ""
            FeatureType.CUSTOM -> "// Implement custom API methods"
        }
        
        return """
package $packageName

import za.co.quantive.app.data.remote.SupabaseClient
import za.co.quantive.app.data.remote.api.*

/**
 * $className API implementation using Supabase backend
 * Follows enterprise patterns with proper error handling
 */
class ${className}ApiImpl(
    private val client: SupabaseClient
) : ${className}Api {
    
    override suspend fun get${className}s(
        page: Int,
        limit: Int, 
        filter: ${className}Filter?
    ): ApiResponse<PaginatedResponse<$className>> {
        return try {
            val response: PaginatedResponse<$className> = client.get(
                "rest/v1/${className.lowercase()}s",
                mapOf(
                    "offset" to (page * limit).toString(),
                    "limit" to limit.toString()
                ).plus(filter?.toQueryParams() ?: emptyMap())
            )
            ApiResponse.success(response)
        } catch (e: Exception) {
            ApiResponse.error("Failed to fetch ${className.lowercase()}s: ${'$'}{e.message}")
        }
    }
    
    override suspend fun get$className(id: String): ApiResponse<$className> {
        return try {
            val ${className.lowercase()}: $className = client.get("rest/v1/${className.lowercase()}s/${'$'}id")
            ApiResponse.success(${className.lowercase()})
        } catch (e: Exception) {
            ApiResponse.error("Failed to fetch ${className.lowercase()}: ${'$'}{e.message}")
        }
    }$crudImplementations
}

private fun ${className}Filter.toQueryParams(): Map<String, String> {
    return buildMap {
        searchQuery?.let { put("search", it) }
        put("order", "${'$'}{sortBy.name.lowercase()}.${'$'}{sortOrder.name.lowercase()}")
    }
}
""".trimIndent()
    }
    
    private fun generateRepositoryImplContent(packageName: String, className: String, featureType: FeatureType): String {
        val crudMethods = when (featureType) {
            FeatureType.CRUD -> """
    override suspend fun create$className(request: Create${className}Request): Result<$className> {
        return try {
            val response = api.create$className(request)
            if (response.isSuccess()) {
                val created = response.data!!
                cache.save$className(created)
                Result.success(created)
            } else {
                Result.failure(Exception(response.message ?: "Failed to create ${className.lowercase()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun update$className(id: String, request: Update${className}Request): Result<$className> {
        return try {
            val response = api.update$className(id, request)
            if (response.isSuccess()) {
                val updated = response.data!!
                cache.save$className(updated)
                Result.success(updated)
            } else {
                Result.failure(Exception(response.message ?: "Failed to update ${className.lowercase()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun delete$className(id: String): Result<Unit> {
        return try {
            val response = api.delete$className(id)
            if (response.isSuccess()) {
                cache.delete$className(id)
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Failed to delete ${className.lowercase()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }"""
            FeatureType.READONLY -> ""
            FeatureType.CUSTOM -> "// Implement custom repository methods"
        }
        
        return """
package $packageName

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import za.co.quantive.app.data.remote.api.*

/**
 * Backend-driven $className repository implementation
 * All business logic handled by backend, smart caching for performance
 */
class Backend${className}Repository(
    private val api: ${className}Api,
    private val cache: ${className}Cache
) : ${className}Repository {
    
    override suspend fun get${className}s(
        filter: ${className}Filter?,
        forceRefresh: Boolean
    ): Flow<Result<List<$className>>> = flow {
        try {
            // Emit cached data first for better UX
            if (!forceRefresh) {
                val cached${className}s = cache.get${className}s(filter)
                if (cached${className}s.isNotEmpty()) {
                    emit(Result.success(cached${className}s))
                }
            }
            
            // Fetch from backend - all calculations done server-side
            val response = api.get${className}s(
                page = 0,
                limit = 100, // TODO: Implement proper pagination
                filter = filter
            )
            
            if (response.isSuccess()) {
                val ${className.lowercase()}s = response.data!!.data
                // Cache backend-calculated data
                cache.save${className}s(${className.lowercase()}s)
                emit(Result.success(${className.lowercase()}s))
            } else {
                emit(Result.failure(Exception(response.message ?: "Failed to fetch ${className.lowercase()}s")))
            }
        } catch (e: Exception) {
            // Emit cached data as fallback on network error
            val cached${className}s = cache.get${className}s(filter)
            if (cached${className}s.isNotEmpty()) {
                emit(Result.success(cached${className}s))
            } else {
                emit(Result.failure(e))
            }
        }
    }
    
    override suspend fun get$className(id: String): Result<$className> {
        return try {
            val response = api.get$className(id)
            if (response.isSuccess()) {
                val ${className.lowercase()} = response.data!!
                cache.save$className(${className.lowercase()})
                Result.success(${className.lowercase()})
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch ${className.lowercase()}"))
            }
        } catch (e: Exception) {
            // Try cache fallback
            val cached${className} = cache.get$className(id)
            if (cached${className} != null) {
                Result.success(cached${className})
            } else {
                Result.failure(e)
            }
        }
    }$crudMethods
}

/**
 * Cache interface for $className
 */
interface ${className}Cache {
    suspend fun get${className}s(filter: ${className}Filter? = null): List<$className>
    suspend fun get$className(id: String): $className?
    suspend fun save${className}s(${className.lowercase()}s: List<$className>)
    suspend fun save$className(${className.lowercase()}: $className)
    suspend fun delete$className(id: String)
    suspend fun clearCache()
}
""".trimIndent()
    }
    
    private fun generateModuleContent(packageName: String, className: String): String {
        return """
package $packageName

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import za.co.quantive.app.data.remote.SupabaseClient

/**
 * Koin DI module for $className feature
 * Following dependency injection best practices
 */
val ${className.lowercase()}Module = module {
    
    // API layer
    single<${className}Api> { ${className}ApiImpl(get<SupabaseClient>()) }
    
    // Cache layer (when enabled)
    single<${className}Cache> { ${className}CacheImpl() }
    
    // Repository layer
    single<${className}Repository> { Backend${className}Repository(get(), get()) }
    
    // Use cases
    factory { ${className}UseCases(get()) }
    
    // ViewModel
    viewModel { ${className}ViewModel(get()) }
}
""".trimIndent()
    }
    
    // CRITICAL FIX: Add missing UiState generator with proper sealed classes
    private fun generateUiStateContent(packageName: String, className: String): String {
        return """
package $packageName

import za.co.quantive.app.features.${className.lowercase()}.domain.$className

/**
 * $className UI state management following MVI pattern
 * Generated according to enterprise blueprint standards
 */
sealed class ${className}UiState {
    object Loading : ${className}UiState()
    data class Success(val data: List<$className>) : ${className}UiState()
    data class Error(val message: String) : ${className}UiState()
}

sealed class ${className}Intent {
    object LoadData : ${className}Intent()
    object Refresh : ${className}Intent()
    object Retry : ${className}Intent()
}

sealed class ${className}UiEvent {
    object NavigateBack : ${className}UiEvent()
    data class ShowError(val message: String) : ${className}UiEvent()
    data class ShowSnackbar(val message: String) : ${className}UiEvent()
}
""".trimIndent()
    }
    
    // Add missing cache implementation generator
    private fun generateCacheImplContent(packageName: String, className: String): String {
        return """
package $packageName

import za.co.quantive.app.features.${className.lowercase()}.domain.$className
import za.co.quantive.app.features.${className.lowercase()}.domain.${className}Filter

/**
 * TTL-based cache implementation for $className
 * Smart caching for performance (not offline capability)
 */
class ${className}CacheImpl : ${className}Cache {
    private val cache = mutableMapOf<String, $className>()
    private val listCache = mutableMapOf<String, List<$className>>()
    private val cacheTimestamps = mutableMapOf<String, Long>()
    private val ttl = 5 * 60 * 1000L // 5 minutes TTL
    
    override suspend fun get${className}s(filter: ${className}Filter?): List<$className> {
        val key = "list_${'$'}{filter?.hashCode() ?: "all"}"
        return if (isCacheValid(key)) {
            listCache[key] ?: emptyList()
        } else {
            emptyList()
        }
    }
    
    override suspend fun get$className(id: String): $className? {
        return if (isCacheValid(id)) {
            cache[id]
        } else {
            null
        }
    }
    
    override suspend fun save${className}s(${className.lowercase()}s: List<$className>) {
        val key = "list_all"
        listCache[key] = ${className.lowercase()}s
        cacheTimestamps[key] = System.currentTimeMillis()
        
        // Also cache individual items
        ${className.lowercase()}s.forEach { save$className(it) }
    }
    
    override suspend fun save$className(${className.lowercase()}: $className) {
        cache[${className.lowercase()}.id] = ${className.lowercase()}
        cacheTimestamps[${className.lowercase()}.id] = System.currentTimeMillis()
    }
    
    override suspend fun delete$className(id: String) {
        cache.remove(id)
        cacheTimestamps.remove(id)
    }
    
    override suspend fun clearCache() {
        cache.clear()
        listCache.clear()
        cacheTimestamps.clear()
    }
    
    private fun isCacheValid(key: String): Boolean {
        val timestamp = cacheTimestamps[key] ?: return false
        return (System.currentTimeMillis() - timestamp) < ttl
    }
}
""".trimIndent()
    }
    
    // Add missing use cases generator
    private fun generateUseCasesContent(packageName: String, className: String): String {
        return """
package $packageName

import kotlinx.coroutines.flow.Flow
import za.co.quantive.app.features.${className.lowercase()}.domain.$className
import za.co.quantive.app.features.${className.lowercase()}.domain.${className}Repository
import za.co.quantive.app.features.${className.lowercase()}.domain.${className}Filter

/**
 * $className use cases for business logic orchestration
 * Following Clean Architecture principles
 */
class ${className}UseCases(
    private val repository: ${className}Repository
) {
    suspend fun get${className}s(
        filter: ${className}Filter? = null,
        forceRefresh: Boolean = false
    ): Flow<Result<List<$className>>> {
        return repository.get${className}s(filter, forceRefresh)
    }
    
    suspend fun get$className(id: String): Result<$className> {
        return repository.get$className(id)
    }
    
    suspend fun create$className(name: String): Result<$className> {
        return repository.create$className(Create${className}Request(name))
    }
    
    suspend fun update$className(id: String, name: String): Result<$className> {
        return repository.update$className(id, Update${className}Request(name))
    }
    
    suspend fun delete$className(id: String): Result<Unit> {
        return repository.delete$className(id)
    }
}
""".trimIndent()
    }
    
    // Add comprehensive test generators
    private fun generateViewModelTestContent(packageName: String, className: String): String {
        return """
package $packageName

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import za.co.quantive.app.features.${className.lowercase()}.domain.$className
import za.co.quantive.app.features.${className.lowercase()}.domain.${className}Repository
import za.co.quantive.app.features.${className.lowercase()}.presentation.${className}ViewModel
import za.co.quantive.app.features.${className.lowercase()}.presentation.${className}Intent
import za.co.quantive.app.features.${className.lowercase()}.presentation.${className}UiState

/**
 * Comprehensive ViewModel tests following enterprise standards
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ${className}ViewModelTest {
    
    private lateinit var mockRepository: ${className}Repository
    private lateinit var viewModel: ${className}ViewModel
    
    @BeforeTest
    fun setup() {
        mockRepository = mockk()
        viewModel = ${className}ViewModel(mockRepository)
    }
    
    @Test
    fun `loadData updates state to Success when repository returns data`() = runTest {
        // Given
        val mockData = listOf(
            $className(id = "1", name = "Test ${className}"),
            $className(id = "2", name = "Another ${className}")
        )
        coEvery { mockRepository.get${className}s(any(), any()) } returns flowOf(Result.success(mockData))
        
        // When
        viewModel.handleIntent(${className}Intent.LoadData)
        
        // Then
        val state = viewModel.uiState.value
        assertTrue(state is ${className}UiState.Success)
        assertEquals(mockData, state.data)
    }
    
    @Test
    fun `loadData updates state to Error when repository fails`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { mockRepository.get${className}s(any(), any()) } returns flowOf(Result.failure(Exception(errorMessage)))
        
        // When
        viewModel.handleIntent(${className}Intent.LoadData)
        
        // Then
        val state = viewModel.uiState.value
        assertTrue(state is ${className}UiState.Error)
        assertEquals(errorMessage, state.message)
    }
    
    @Test
    fun `refresh forces repository refresh`() = runTest {
        // Given
        coEvery { mockRepository.get${className}s(any(), true) } returns flowOf(Result.success(emptyList()))
        
        // When
        viewModel.handleIntent(${className}Intent.Refresh)
        
        // Then
        coVerify { mockRepository.get${className}s(any(), forceRefresh = true) }
    }
}
""".trimIndent()
    }
    
    private fun generateRepositoryTestContent(packageName: String, className: String): String {
        return """
package $packageName

import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import za.co.quantive.app.features.${className.lowercase()}.data.Backend${className}Repository
import za.co.quantive.app.features.${className.lowercase()}.data.${className}Api
import za.co.quantive.app.features.${className.lowercase()}.data.${className}Cache
import za.co.quantive.app.features.${className.lowercase()}.domain.$className
import za.co.quantive.app.data.remote.api.ApiResponse

/**
 * Repository integration tests following enterprise standards
 */
class ${className}RepositoryTest {
    
    private lateinit var mockApi: ${className}Api
    private lateinit var mockCache: ${className}Cache
    private lateinit var repository: Backend${className}Repository
    
    @BeforeTest
    fun setup() {
        mockApi = mockk()
        mockCache = mockk()
        repository = Backend${className}Repository(mockApi, mockCache)
    }
    
    @Test
    fun `get${className}s returns cached data when available and not forcing refresh`() = runTest {
        // Given
        val cachedData = listOf($className(id = "1", name = "Cached ${className}"))
        coEvery { mockCache.get${className}s(any()) } returns cachedData
        
        // When
        val result = repository.get${className}s(forceRefresh = false).first()
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(cachedData, result.getOrNull())
        coVerify(exactly = 0) { mockApi.get${className}s(any(), any(), any()) }
    }
    
    @Test
    fun `get${className}s fetches from API when cache is empty`() = runTest {
        // Given
        val apiData = listOf($className(id = "2", name = "API ${className}"))
        coEvery { mockCache.get${className}s(any()) } returns emptyList()
        coEvery { mockApi.get${className}s(any(), any(), any()) } returns ApiResponse.success(
            za.co.quantive.app.data.remote.api.PaginatedResponse(apiData, false, 1)
        )
        coEvery { mockCache.save${className}s(any()) } just Runs
        
        // When
        val result = repository.get${className}s().first()
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(apiData, result.getOrNull())
        coVerify { mockCache.save${className}s(apiData) }
    }
}
""".trimIndent()
    }
    
    private fun generateScreenshotTestContent(packageName: String, className: String): String {
        return """
package $packageName

import androidx.compose.ui.test.*
import com.karumi.shot.ScreenshotTest
import kotlin.test.Test
import za.co.quantive.app.features.${className.lowercase()}.domain.$className
import za.co.quantive.app.features.${className.lowercase()}.presentation.${className}Screen
import za.co.quantive.app.features.${className.lowercase()}.presentation.${className}UiState
import za.co.quantive.app.presentation.theme.QuantiveTheme

/**
 * Screenshot regression tests for ${className}Screen
 * Following enterprise testing standards
 */
class ${className}ScreenshotTest : ScreenshotTest {
    
    @Test
    fun ${className.lowercase()}ScreenLoadingState() {
        compareScreenshot(
            composable = {
                QuantiveTheme {
                    ${className}Screen(
                        uiState = ${className}UiState.Loading,
                        onIntent = {},
                        onNavigateBack = {}
                    )
                }
            },
            name = "${className.lowercase()}_loading"
        )
    }
    
    @Test
    fun ${className.lowercase()}ScreenSuccessState() {
        val mockData = listOf(
            $className(id = "1", name = "Sample ${className} 1"),
            $className(id = "2", name = "Sample ${className} 2")
        )
        
        compareScreenshot(
            composable = {
                QuantiveTheme {
                    ${className}Screen(
                        uiState = ${className}UiState.Success(mockData),
                        onIntent = {},
                        onNavigateBack = {}
                    )
                }
            },
            name = "${className.lowercase()}_success"
        )
    }
    
    @Test
    fun ${className.lowercase()}ScreenErrorState() {
        compareScreenshot(
            composable = {
                QuantiveTheme {
                    ${className}Screen(
                        uiState = ${className}UiState.Error("Network connection failed"),
                        onIntent = {},
                        onNavigateBack = {}
                    )
                }
            },
            name = "${className.lowercase()}_error"
        )
    }
}
""".trimIndent()
    }
}