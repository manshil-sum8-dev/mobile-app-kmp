import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import java.io.File
import java.io.StringWriter
import java.util.*
import javax.inject.Inject

/**
 * Quantive Feature Generator Plugin
 * Implements enterprise-grade feature scaffolding following blueprint standards
 */
class FeatureGeneratorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("createFeature", CreateFeatureTask::class.java)
        project.tasks.register("scaffoldFeature", InteractiveScaffoldTask::class.java)
    }
}

/**
 * Task for creating new features with full scaffolding
 */
open class CreateFeatureTask : DefaultTask() {
    
    @Input
    var featureName: String = ""
    
    @Input
    @Optional
    var featureType: String = "crud"
    
    @Input
    @Optional
    var includeScreenshots: Boolean? = false
    
    @Input
    @Optional
    var navigationFramework: String = "decompose"
    
    @Input
    @Optional
    var includeCaching: Boolean? = true
    
    @TaskAction
    fun createFeature() {
        // Check for command line properties first
        val nameFromProperty = project.findProperty("featureName")?.toString() ?: featureName
        val typeFromProperty = project.findProperty("featureType")?.toString() ?: featureType
        
        if (nameFromProperty.isEmpty()) {
            throw IllegalArgumentException("Feature name is required. Use -PfeatureName=FeatureName")
        }
        
        val generator = FeatureScaffoldGenerator(project)
        generator.generateFeature(FeatureConfig(
            name = nameFromProperty,
            type = FeatureType.valueOf(typeFromProperty.uppercase()),
            includeScreenshots = includeScreenshots ?: false,
            navigationFramework = NavigationFramework.valueOf(navigationFramework.uppercase()),
            includeCaching = includeCaching ?: true
        ))
        
        println("✅ Feature '$nameFromProperty' generated successfully!")
        println("📁 Location: ${project.projectDir}/src/commonMain/kotlin/za/co/quantive/app/features/${nameFromProperty.lowercase()}")
    }
}

/**
 * Interactive scaffolding task with prompts
 */
open class InteractiveScaffoldTask : DefaultTask() {
    
    @TaskAction
    fun scaffoldFeature() {
        val scanner = Scanner(System.`in`)
        
        println("🚀 Quantive Feature Scaffolding")
        println("===============================")
        
        print("Enter feature name (e.g., UserProfile): ")
        val featureName = scanner.nextLine()
        
        print("Enter feature type (crud/readonly/custom) [crud]: ")
        val featureType = scanner.nextLine().ifEmpty { "crud" }
        
        print("Include screenshot tests? (y/n) [y]: ")
        val includeScreenshots = scanner.nextLine().lowercase().let { it.isEmpty() || it == "y" }
        
        print("Navigation framework (decompose/voyager) [decompose]: ")
        val navigationFramework = scanner.nextLine().ifEmpty { "decompose" }
        
        print("Include caching layer? (y/n) [y]: ")
        val includeCaching = scanner.nextLine().lowercase().let { it.isEmpty() || it == "y" }
        
        println("\n🔧 Generating feature with configuration:")
        println("  Name: $featureName")
        println("  Type: $featureType")
        println("  Screenshots: $includeScreenshots")
        println("  Navigation: $navigationFramework")
        println("  Caching: $includeCaching")
        
        val generator = FeatureScaffoldGenerator(project)
        generator.generateFeature(FeatureConfig(
            name = featureName,
            type = FeatureType.valueOf(featureType.uppercase()),
            includeScreenshots = includeScreenshots,
            navigationFramework = NavigationFramework.valueOf(navigationFramework.uppercase()),
            includeCaching = includeCaching
        ))
        
        println("\n✅ Feature '$featureName' generated successfully!")
    }
}

/**
 * Feature configuration data class
 */
data class FeatureConfig(
    val name: String,
    val type: FeatureType,
    val includeScreenshots: Boolean,
    val navigationFramework: NavigationFramework,
    val includeCaching: Boolean
) {
    val packageName: String = name.lowercase()
    val className: String = name.replaceFirstChar { it.uppercase() }
}

enum class FeatureType { CRUD, READONLY, CUSTOM }
enum class NavigationFramework { DECOMPOSE, VOYAGER }