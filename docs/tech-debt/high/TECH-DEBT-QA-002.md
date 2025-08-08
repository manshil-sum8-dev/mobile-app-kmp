# TECH-DEBT-QA-002: Setup Quality Gates and Static Analysis

**Status**: Open  
**Priority**: High  
**Domain**: Quality Assurance  
**Effort Estimate**: 1-2 weeks  
**Assigned Agent**: KMP QA Engineer  
**Created**: 2025-01-08  

## Problem Description

The application lacks quality enforcement mechanisms required by the enterprise blueprint. No static analysis tools, code formatting enforcement, or automated quality checks are configured.

**Current Quality Issues**:
- No ktlint for code formatting enforcement
- No Detekt for static analysis and code quality
- No pre-commit hooks to prevent bad code
- No CI/CD quality gates
- No YAGNI/SOLID principle enforcement
- No automated quality reporting

## Blueprint Violation

**Blueprint Requirement**: "Pre-merge hooks with ktlint + detekt (zero warnings)" and comprehensive quality enforcement:
- ktlint passes with zero warnings
- Detekt passes with zero issues (including KMP-specific rules)
- Pre-commit hooks with quality checks
- CI/CD quality gates integrated into build process
- YAGNI compliance as mandatory PR question

**Current State**: No quality enforcement tools configured

## Affected Files

### Configuration Files (Need Creation)
- `config/detekt/kmp-rules.yml` - KMP-specific Detekt rules
- `config/ktlint/.editorconfig` - Code formatting standards
- `.git/hooks/pre-commit` - Pre-commit quality checks
- `scripts/quality-check.sh` - Quality validation script

### Build Files (Need Updates)
- `/composeApp/build.gradle.kts` - Add quality plugins
- `/gradle/libs.versions.toml` - Add quality tool versions
- `/.github/workflows/` - CI/CD quality pipeline

## Risk Assessment

- **Code Quality Risk**: High - No automated quality enforcement
- **Consistency Risk**: High - No formatting standards enforced
- **Maintainability Risk**: Medium - Code quality may degrade over time
- **Development Velocity Risk**: Medium - Manual quality checks slow development

## Acceptance Criteria

### Static Analysis Tools
- [ ] Configure ktlint with project-specific rules
- [ ] Setup Detekt with KMP-specific custom rules
- [ ] Add NoGlobalCoroutineScope rule enforcement
- [ ] Configure ExpectActualNaming rule for KMP
- [ ] Add SOLID/DRY/KISS/YAGNI violation detection

### Pre-commit Hooks
- [ ] Implement git pre-commit hook with quality checks
- [ ] Add automatic code formatting on commit
- [ ] Prevent commits with quality violations
- [ ] Include test execution in pre-commit checks
- [ ] Add commit message validation

### CI/CD Quality Gates
- [ ] Add quality checks to GitHub Actions pipeline
- [ ] Implement zero-warning policy enforcement
- [ ] Add automated code review comments
- [ ] Create quality metrics reporting
- [ ] Block PR merges on quality failures

### Development Workflow
- [ ] Add quality check commands to Makefile
- [ ] Create IDE integration for quality tools
- [ ] Add developer onboarding for quality standards
- [ ] Create quality metrics dashboard

## Implementation Details

### Ktlint Configuration

#### gradle/libs.versions.toml
```toml
[versions]
ktlint = "0.50.0"
detekt = "1.23.4"

[plugins]
ktlint = { id = "org.jlleitschuh.gradle.ktlint", version.ref = "ktlint" }
detekt = { id = "io.gitlab.arturbosch.detekt", version.ref = "detekt" }
```

#### composeApp/build.gradle.kts
```kotlin
plugins {
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
}

ktlint {
    version.set("0.50.0")
    android.set(true)
    ignoreFailures.set(false)
    disabledRules.set(setOf("no-wildcard-imports", "max-line-length"))
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config = files("$rootDir/config/detekt/detekt.yml")
    baseline = file("$rootDir/config/detekt/baseline.xml")
    
    reports {
        html.enabled = true
        xml.enabled = true
        txt.enabled = false
        sarif.enabled = false
    }
}
```

### Detekt Configuration

#### config/detekt/detekt.yml
```yaml
build:
  maxIssues: 0  # Zero tolerance for quality issues
  excludeCorrectable: false

style:
  MagicNumber:
    active: true
    excludes: ['**/test/**', '**/androidTest/**', '**/commonTest/**']
  
  MaxLineLength:
    active: true
    maxLineLength: 120
    
  UnusedImports:
    active: true

complexity:
  ComplexMethod:
    active: true
    threshold: 15
    
  LongMethod:
    active: true
    threshold: 60
    
  TooManyFunctions:
    active: true
    thresholdInFiles: 20

# KMP-specific rules
KmpSpecificRules:
  ExpectActualNaming:
    active: true
    expectClassSuffix: 'Expected'
    actualClassSuffix: 'Impl'
  
  SharedModuleDependencies:
    active: true
    allowedPlatformDependencies:
      - 'ktor-client-*'
      - 'kotlinx-*' 
      - 'koin-*'
      - 'napier'
  
  CoroutineScopeUsage:
    active: true
    forbiddenScopes:
      - 'GlobalScope'
    allowedScopes:
      - 'viewModelScope'
      - 'lifecycleScope'

# Enterprise standards enforcement
EnterpriseStandards:
  NoHardcodedSecrets:
    active: true
    secretPatterns:
      - 'password'
      - 'secret'
      - 'token'
      - 'api_key'
      
  ProperErrorHandling:
    active: true
    requireResultWrapper: true
    
  BackendDrivenPattern:
    active: true
    forbiddenClientSideBusinessLogic: true
```

### Custom Detekt Rules

#### config/detekt/NoGlobalCoroutineScopeRule.kt
```kotlin
class NoGlobalCoroutineScopeRule : Rule() {
    override val issue = Issue(
        "NoGlobalCoroutineScope",
        Severity.Warning,
        "GlobalScope usage is forbidden, use structured concurrency",
        Debt.TEN_MINS
    )
    
    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)
        
        if (expression.text.contains("GlobalScope")) {
            report(CodeSmell(issue, Entity.from(expression), 
                "Use viewModelScope or structured concurrency instead of GlobalScope"))
        }
    }
}

class BackendDrivenPatternRule : Rule() {
    override val issue = Issue(
        "BackendDrivenPattern",
        Severity.Warning, 
        "Business logic should be handled by backend, not client",
        Debt.TWENTY_MINS
    )
    
    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        
        // Check for client-side calculations
        val functions = klass.declarations.filterIsInstance<KtNamedFunction>()
        functions.forEach { function ->
            if (containsBusinessLogic(function)) {
                report(CodeSmell(issue, Entity.from(function),
                    "Move business calculations to backend"))
            }
        }
    }
    
    private fun containsBusinessLogic(function: KtNamedFunction): Boolean {
        val bodyText = function.bodyExpression?.text ?: return false
        return bodyText.contains(Regex("calculate|compute|sum|total|tax|discount"))
    }
}
```

### Pre-commit Hooks

#### .git/hooks/pre-commit
```bash
#!/bin/sh
# Pre-commit quality checks for Quantive KMP

echo "Running pre-commit quality checks..."

# Kotlin linting
echo "Running ktlint..."
./gradlew ktlintCheck
if [ $? -ne 0 ]; then
    echo "❌ ktlint failed. Run './gradlew ktlintFormat' to fix formatting issues."
    exit 1
fi

# Static analysis
echo "Running detekt..."
./gradlew detekt
if [ $? -ne 0 ]; then
    echo "❌ Detekt failed. Please fix code quality issues."
    exit 1
fi

# YAGNI compliance check
echo "Running YAGNI compliance check..."
./gradlew yagniCheck # Custom task to check for over-engineering
if [ $? -ne 0 ]; then
    echo "❌ YAGNI compliance failed. Remove unnecessary abstractions."
    exit 1
fi

# Run critical tests
echo "Running critical tests..."
./gradlew testDebugUnitTest -q
if [ $? -ne 0 ]; then
    echo "❌ Critical tests failed."
    exit 1
fi

# Security scan
echo "Running security scan..."
./gradlew dependencyCheck
if [ $? -ne 0 ]; then
    echo "⚠️ Security scan found issues. Review before proceeding."
fi

echo "✅ All pre-commit checks passed!"
```

#### scripts/install-hooks.sh
```bash
#!/bin/bash
# Install git hooks for quality enforcement

echo "Installing git hooks..."

# Make hooks executable
chmod +x .git/hooks/pre-commit

# Copy hooks to git directory
cp scripts/pre-commit .git/hooks/pre-commit

echo "✅ Git hooks installed successfully!"
echo "To bypass hooks (not recommended): git commit --no-verify"
```

### CI/CD Quality Pipeline

#### .github/workflows/quality.yml
```yaml
name: Quality Gates

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  quality-checks:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup JDK
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Cache Gradle packages
        uses: actions/cache@v3
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
      
      - name: Run ktlint
        run: ./gradlew ktlintCheck
      
      - name: Run Detekt
        run: ./gradlew detekt
        
      - name: Upload Detekt reports
        uses: actions/upload-artifact@v3
        if: always()
        with:
          name: detekt-reports
          path: |
            **/build/reports/detekt/
      
      - name: YAGNI Compliance Check
        run: ./gradlew yagniCheck
        
      - name: Run Unit Tests
        run: ./gradlew testDebugUnitTest
      
      - name: Generate Test Report
        uses: dorny/test-reporter@v1
        if: success() || failure()
        with:
          name: Test Results
          path: '**/build/test-results/**/*.xml'
          reporter: java-junit
          
      - name: Security Scan
        run: ./gradlew dependencyCheck
        
      - name: Comment PR with Quality Results
        if: github.event_name == 'pull_request'
        uses: actions/github-script@v6
        with:
          script: |
            github.rest.issues.createComment({
              issue_number: context.issue.number,
              owner: context.repo.owner,
              repo: context.repo.repo,
              body: '✅ Quality gates passed! Code meets enterprise standards.'
            })
```

### Makefile Integration

```makefile
# Quality commands
quality-check: ktlint-check detekt
quality-fix: ktlint-format

ktlint-check:
	./gradlew ktlintCheck

ktlint-format:
	./gradlew ktlintFormat

detekt:
	./gradlew detekt

detekt-baseline:
	./gradlew detektBaseline

install-hooks:
	chmod +x scripts/install-hooks.sh
	./scripts/install-hooks.sh

# Combined quality and test check
pre-push: quality-check test
	@echo "✅ Ready for push"
```

### Custom Gradle Tasks

#### buildSrc/src/main/kotlin/YagniComplianceTask.kt
```kotlin
open class YagniComplianceTask : DefaultTask() {
    
    @TaskAction
    fun checkYagniCompliance() {
        val violations = mutableListOf<String>()
        
        // Check for over-abstraction
        val abstractClasses = findAbstractClasses()
        if (abstractClasses.size > 3) {
            violations.add("Too many abstract classes: ${abstractClasses.size} (max: 3)")
        }
        
        // Check for unused interfaces
        val unusedInterfaces = findUnusedInterfaces()
        violations.addAll(unusedInterfaces.map { "Unused interface: $it" })
        
        // Check for speculative features
        val todoComments = findTodoComments()
        val speculativeFeatures = todoComments.filter { it.contains("future", true) }
        violations.addAll(speculativeFeatures.map { "Speculative feature: $it" })
        
        if (violations.isNotEmpty()) {
            throw GradleException("YAGNI violations found:\n${violations.joinToString("\n")}")
        }
        
        println("✅ YAGNI compliance check passed")
    }
}
```

## Dependencies

- **Git hooks**: Requires git repository setup
- **CI/CD Platform**: GitHub Actions configured
- **Development tools**: IDE integration for better developer experience

## Related Issues

- TECH-DEBT-QA-001 (Testing infrastructure works with quality gates)
- TECH-DEBT-ARCH-001 (ViewModel architecture needs quality validation)
- All other tech debt items (quality gates validate all implementations)

## Success Metrics

- [ ] ktlint passes with zero warnings on all code
- [ ] Detekt passes with zero issues including custom rules
- [ ] Pre-commit hooks prevent 100% of quality violations
- [ ] CI/CD pipeline blocks PRs with quality issues
- [ ] Developer productivity increases with automated formatting
- [ ] Code review time decreases with automated quality checks

## Definition of Done

- [ ] ktlint configured and enforcing formatting standards
- [ ] Detekt configured with KMP-specific rules
- [ ] Custom enterprise quality rules implemented
- [ ] Pre-commit hooks installed and working
- [ ] CI/CD quality gates operational
- [ ] YAGNI compliance checking implemented
- [ ] Developer documentation updated
- [ ] Team trained on quality tools and workflow
- [ ] Quality metrics dashboard available
- [ ] Zero-warning policy successfully enforced