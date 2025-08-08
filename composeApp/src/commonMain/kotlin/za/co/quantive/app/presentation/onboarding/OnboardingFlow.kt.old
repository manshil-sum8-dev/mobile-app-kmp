package za.co.quantive.app.presentation.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import za.co.quantive.app.domain.profile.BusinessProfile
import za.co.quantive.app.presentation.components.QuantiveCard
import za.co.quantive.app.presentation.components.QuantiveLoadingState
import za.co.quantive.app.presentation.components.QuantivePrimaryButton
import za.co.quantive.app.presentation.components.QuantiveSecondaryButton
import za.co.quantive.app.presentation.components.QuantiveSectionHeader
import za.co.quantive.app.presentation.theme.QuantiveDesignTokens
import za.co.quantive.app.presentation.theme.QuantiveExpressiveTokens

/**
 * Main Onboarding Flow Coordinator
 * Manages the complete onboarding experience from welcome to completion
 */
@Composable
fun QuantiveOnboardingFlow(
    onOnboardingComplete: () -> Unit,
) {
    var currentStep by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }
    var businessProfile by remember { mutableStateOf<BusinessProfile?>(null) }

    // Progress tracking
    val totalSteps = 6
    val progress = (currentStep + 1).toFloat() / totalSteps

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        // Progress indicator
        OnboardingProgressIndicator(
            progress = progress,
            currentStep = currentStep + 1,
            totalSteps = totalSteps,
        )

        // Main content
        Box(modifier = Modifier.weight(1f)) {
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { if (targetState > initialState) it else -it },
                        animationSpec = tween(QuantiveExpressiveTokens.Motion.ExpressiveMedium),
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { if (targetState > initialState) -it else it },
                        animationSpec = tween(QuantiveExpressiveTokens.Motion.ExpressiveMedium),
                    )
                },
                label = "onboarding_content",
            ) { step ->
                when (step) {
                    0 -> WelcomeScreen(
                        onNext = { currentStep = 1 },
                    )
                    1 -> FeatureHighlightsScreen(
                        onNext = { currentStep = 2 },
                        onBack = { currentStep = 0 },
                    )
                    2 -> za.co.quantive.app.presentation.onboarding.PermissionsScreen(
                        onNext = { currentStep = 3 },
                        onBack = { currentStep = 1 },
                    )
                    3 -> za.co.quantive.app.presentation.onboarding.BusinessProfileSetupScreen(
                        onNext = { profile ->
                            businessProfile = profile
                            currentStep = 4
                        },
                        onBack = { currentStep = 2 },
                    )
                    4 -> za.co.quantive.app.presentation.onboarding.PersonalizationScreen(
                        businessProfile = businessProfile,
                        onNext = { currentStep = 5 },
                        onBack = { currentStep = 3 },
                    )
                    5 -> CompletionScreen(
                        businessProfile = businessProfile,
                        onComplete = {
                            isLoading = true
                            onOnboardingComplete()
                        },
                    )
                }
            }

            // Loading overlay
            androidx.compose.animation.AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                ) {
                    QuantiveLoadingState(
                        message = "Setting up your Quantive workspace...",
                    )
                }
            }
        }
    }
}

/**
 * Progress Indicator Component
 */
@Composable
private fun OnboardingProgressIndicator(
    progress: Float,
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(QuantiveDesignTokens.Spacing.Medium),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Setup Progress",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "$currentStep of $totalSteps",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
            )
        }

        Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Small))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(QuantiveExpressiveTokens.Shapes.PlayfulPill),
            color = QuantiveExpressiveTokens.Colors.ExpressivePrimary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

/**
 * Welcome Screen - Brand introduction and value proposition
 */
@Composable
private fun WelcomeScreen(
    onNext: () -> Unit,
) {
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(QuantiveDesignTokens.Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(
                animationSpec = tween(QuantiveExpressiveTokens.Motion.ExpressiveLong),
            ) + slideInHorizontally(
                initialOffsetX = { it / 3 },
                animationSpec = tween(QuantiveExpressiveTokens.Motion.ExpressiveLong),
            ),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Brand Logo/Icon - Professional gradient design
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    ProfessionalLogoPlaceholder(
                        modifier = Modifier.size(120.dp),
                    )
                }

                Spacer(modifier = Modifier.height(QuantiveExpressiveTokens.Spacing.ComponentSpacing))

                Text(
                    text = "Welcome to Quantive",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Medium))

                Text(
                    text = "Professional invoicing and business management",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal,
                )

                Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Small))

                Text(
                    text = "Built for South African businesses",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Value Proposition Cards
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(
                animationSpec = tween(
                    QuantiveExpressiveTokens.Motion.ExpressiveLong,
                    delayMillis = 200,
                ),
            ),
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(QuantiveDesignTokens.Spacing.Small),
                ) {
                    ValuePropCard(
                        title = "Professional",
                        subtitle = "VAT compliant",
                        icon = Icons.Default.DocumentScanner,
                        modifier = Modifier.weight(1f),
                    )
                    ValuePropCard(
                        title = "Secure",
                        subtitle = "Bank-grade",
                        icon = Icons.Default.Security,
                        modifier = Modifier.weight(1f),
                    )
                    ValuePropCard(
                        title = "Smart",
                        subtitle = "AI insights",
                        icon = Icons.Default.Insights,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(modifier = Modifier.height(QuantiveExpressiveTokens.Spacing.ComponentSpacing))

                QuantivePrimaryButton(
                    text = "Get Started",
                    onClick = onNext,
                    icon = Icons.Default.ArrowForward,
                    modifier = Modifier.fillMaxWidth(),
                    expressiveMotion = true,
                )
            }
        }
    }
}

/**
 * Feature Highlights Screen - Core capabilities showcase
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FeatureHighlightsScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val features = remember {
        listOf(
            FeatureHighlight(
                title = "Smart Invoicing",
                description = "Create professional invoices with automatic VAT calculations and compliance checking",
                icon = Icons.Default.Receipt,
                color = QuantiveExpressiveTokens.Colors.ExpressivePrimary,
            ),
            FeatureHighlight(
                title = "Contact Management",
                description = "Organize customers and suppliers with complete business relationship tracking",
                icon = Icons.Default.Group,
                color = QuantiveExpressiveTokens.Colors.Growth,
            ),
            FeatureHighlight(
                title = "Business Analytics",
                description = "Get insights into your cash flow, payment patterns, and business performance",
                icon = Icons.Default.TrendingUp,
                color = QuantiveExpressiveTokens.Colors.Trust,
            ),
            FeatureHighlight(
                title = "Tax Compliance",
                description = "Automatic VAT calculations and SARS-compliant reporting for South African businesses",
                icon = Icons.Default.Business,
                color = QuantiveExpressiveTokens.Colors.ExpressiveWarning,
            ),
        )
    }

    val pagerState = rememberPagerState(pageCount = { features.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(QuantiveDesignTokens.Spacing.Large),
    ) {
        QuantiveSectionHeader(
            title = "Powerful Features",
            subtitle = "Everything you need to manage your business",
        )

        Spacer(modifier = Modifier.height(QuantiveExpressiveTokens.Spacing.ComponentSpacing))

        // Feature pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = QuantiveDesignTokens.Spacing.Small),
        ) { page ->
            FeatureCard(
                feature = features[page],
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // Page indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = QuantiveDesignTokens.Spacing.Medium),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(features.size) { index ->
                val isSelected = pagerState.currentPage == index
                Surface(
                    modifier = Modifier
                        .size(if (isSelected) 12.dp else 8.dp)
                        .padding(2.dp),
                    shape = CircleShape,
                    color = if (isSelected) {
                        QuantiveExpressiveTokens.Colors.ExpressivePrimary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                ) {}
            }
        }

        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(QuantiveDesignTokens.Spacing.Medium),
        ) {
            QuantiveSecondaryButton(
                text = "Back",
                onClick = onBack,
                modifier = Modifier.weight(1f),
            )
            QuantivePrimaryButton(
                text = "Continue",
                onClick = onNext,
                icon = Icons.Default.ArrowForward,
                modifier = Modifier.weight(2f),
            )
        }
    }
}

/**
 * Data class for feature highlights
 */
private data class FeatureHighlight(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
)

/**
 * Individual feature card
 */
@Composable
private fun FeatureCard(
    feature: FeatureHighlight,
    modifier: Modifier = Modifier,
) {
    QuantiveCard(
        modifier = modifier.height(300.dp),
        expressiveMotion = true,
        colors = CardDefaults.cardColors(
            containerColor = feature.color.copy(alpha = 0.1f),
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center,
            ) {
                FeatureIllustrationPlaceholder(
                    feature = feature,
                    modifier = Modifier.size(100.dp),
                )
            }

            Spacer(modifier = Modifier.height(QuantiveExpressiveTokens.Spacing.ComponentSpacing))

            Text(
                text = feature.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Small))

            Text(
                text = feature.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * Value proposition card
 */
@Composable
private fun ValuePropCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    QuantiveCard(
        modifier = modifier,
        expressiveMotion = true,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = QuantiveExpressiveTokens.Colors.ExpressivePrimary,
            )

            Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Small))

            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

// Note: Detailed implementations for PermissionsScreen, BusinessProfileSetupScreen,
// and PersonalizationScreen are in OnboardingScreens.kt

@Composable
private fun CompletionScreen(
    businessProfile: BusinessProfile?,
    onComplete: () -> Unit,
) {
    var showContent by remember { mutableStateOf(false) }
    var isCompleting by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(500) // Let the previous animation finish
        showContent = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(QuantiveDesignTokens.Spacing.Large),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(
                animationSpec = tween(QuantiveExpressiveTokens.Motion.ExpressiveLong),
            ) + slideInVertically(
                initialOffsetY = { it / 3 },
                animationSpec = tween(QuantiveExpressiveTokens.Motion.ExpressiveLong),
            ),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Success animation placeholder - could be enhanced with Lottie
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CompletionSuccessPlaceholder(
                        modifier = Modifier.size(120.dp),
                    )
                }

                Spacer(modifier = Modifier.height(QuantiveExpressiveTokens.Spacing.ComponentSpacing))

                Text(
                    text = "Welcome to Quantive!",
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Medium))

                Text(
                    text = if (businessProfile != null) {
                        "${businessProfile.name} is ready for professional invoicing"
                    } else {
                        "Your professional invoicing workspace is ready"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Small))

                // Feature completion checklist
                QuantiveCard(
                    modifier = Modifier.fillMaxWidth(),
                    expressiveMotion = true,
                    colors = CardDefaults.cardColors(
                        containerColor = QuantiveExpressiveTokens.Colors.Growth.copy(alpha = 0.1f),
                    ),
                ) {
                    Column {
                        CompletionChecklistItem("Business profile configured")
                        CompletionChecklistItem("Preferences personalized")
                        CompletionChecklistItem("Security settings enabled")
                        CompletionChecklistItem("Ready to create invoices!")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Start button with loading state
        QuantivePrimaryButton(
            text = if (isCompleting) "Setting up..." else "Start Using Quantive",
            onClick = {
                scope.launch {
                    try {
                        isCompleting = true
                        // Final setup tasks could be done here
                        // e.g., sync with backend, initialize analytics, etc.
                        delay(1000) // Simulate final setup
                        onComplete()
                    } catch (e: Exception) {
                        // Handle completion error
                        isCompleting = false
                    }
                }
            },
            loading = isCompleting,
            enabled = showContent && !isCompleting,
            modifier = Modifier.fillMaxWidth(),
            expressiveMotion = true,
        )
    }
}

/**
 * Completion checklist item
 */
@Composable
private fun CompletionChecklistItem(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = QuantiveDesignTokens.Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = QuantiveExpressiveTokens.Colors.Growth,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(QuantiveDesignTokens.Spacing.Small))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

/**
 * Completion success animation placeholder
 */
@Composable
private fun CompletionSuccessPlaceholder(
    modifier: Modifier = Modifier,
) {
    val primaryColor = QuantiveExpressiveTokens.Colors.Growth
    val secondaryColor = QuantiveExpressiveTokens.Colors.ExpressivePrimary

    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.minDimension / 3

        // Outer celebration ring
        drawCircle(
            color = primaryColor.copy(alpha = 0.2f),
            center = Offset(centerX, centerY),
            radius = size.minDimension / 2.2f,
        )

        // Success circle
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    primaryColor.copy(alpha = 0.8f),
                    primaryColor.copy(alpha = 0.4f),
                ),
                center = Offset(centerX, centerY),
                radius = radius,
            ),
            center = Offset(centerX, centerY),
            radius = radius,
        )

        // Large checkmark
        val checkPath = Path()
        val checkScale = radius * 0.5f
        val checkStartX = centerX - checkScale * 0.3f
        val checkStartY = centerY
        val checkMidX = centerX - checkScale * 0.1f
        val checkMidY = centerY + checkScale * 0.3f
        val checkEndX = centerX + checkScale * 0.4f
        val checkEndY = centerY - checkScale * 0.3f

        checkPath.moveTo(checkStartX, checkStartY)
        checkPath.lineTo(checkMidX, checkMidY)
        checkPath.lineTo(checkEndX, checkEndY)

        drawPath(
            path = checkPath,
            color = Color.White, // Using explicit color instead of MaterialTheme in Canvas
            style = Stroke(
                width = radius * 0.2f,
                cap = androidx.compose.ui.graphics.StrokeCap.Round,
            ),
        )

        // Celebration sparkles
        repeat(6) { index ->
            val angle = (index * 60f) * (kotlin.math.PI / 180f).toFloat()
            val sparkleRadius = size.minDimension * 0.4f
            val sparkleX = centerX + kotlin.math.cos(angle) * sparkleRadius
            val sparkleY = centerY + kotlin.math.sin(angle) * sparkleRadius

            drawCircle(
                color = secondaryColor.copy(alpha = 0.6f - index * 0.08f),
                radius = 3.dp.toPx() + (index % 3) * 2.dp.toPx(),
                center = Offset(sparkleX, sparkleY),
            )
        }
    }
}

/**
 * Professional Logo Placeholder - Sophisticated gradient design
 */
@Composable
private fun ProfessionalLogoPlaceholder(
    modifier: Modifier = Modifier,
) {
    val primaryColor = QuantiveExpressiveTokens.Colors.ExpressivePrimary
    val secondaryColor = QuantiveExpressiveTokens.Colors.Trust
    val accentColor = QuantiveExpressiveTokens.Colors.Growth

    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.minDimension / 3

        // Background circle with gradient
        drawCircle(
            brush = Brush.linearGradient(
                colors = listOf(
                    primaryColor.copy(alpha = 0.2f),
                    secondaryColor.copy(alpha = 0.1f),
                ),
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height),
            ),
            center = Offset(centerX, centerY),
            radius = size.minDimension / 2.2f,
        )

        // Main "Q" letter with modern styling
        val qPath = Path().apply {
            // Outer circle of Q
            addArc(
                oval = androidx.compose.ui.geometry.Rect(
                    offset = Offset(centerX - radius, centerY - radius),
                    size = Size(radius * 2, radius * 2),
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 360f,
            )

            // Inner circle (hole)
            val innerRadius = radius * 0.6f
            addArc(
                oval = androidx.compose.ui.geometry.Rect(
                    offset = Offset(centerX - innerRadius, centerY - innerRadius),
                    size = Size(innerRadius * 2, innerRadius * 2),
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = -360f,
            )
        }

        drawPath(
            path = qPath,
            color = primaryColor,
        )

        // Q tail - diagonal line
        val tailStart = Offset(
            centerX + radius * 0.4f,
            centerY + radius * 0.4f,
        )
        val tailEnd = Offset(
            centerX + radius * 0.9f,
            centerY + radius * 0.9f,
        )

        drawLine(
            color = primaryColor,
            start = tailStart,
            end = tailEnd,
            strokeWidth = radius * 0.3f,
            cap = androidx.compose.ui.graphics.StrokeCap.Round,
        )

        // Accent dots for modern touch
        val dotRadius = radius * 0.15f
        drawCircle(
            color = accentColor,
            radius = dotRadius,
            center = Offset(centerX - radius * 1.2f, centerY - radius * 0.8f),
        )
        drawCircle(
            color = secondaryColor,
            radius = dotRadius * 0.8f,
            center = Offset(centerX + radius * 1.1f, centerY - radius * 1.1f),
        )
    }
}

/**
 * Feature Illustration Placeholder - Abstract business graphics
 */
@Composable
private fun FeatureIllustrationPlaceholder(
    feature: FeatureHighlight,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.size(200.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        when (feature.title) {
            "Smart Invoicing" -> {
                // Document illustration
                val docWidth = size.width * 0.6f
                val docHeight = size.height * 0.7f
                val docX = centerX - docWidth / 2
                val docY = centerY - docHeight / 2

                // Document background
                drawRoundRect(
                    color = feature.color.copy(alpha = 0.1f),
                    topLeft = Offset(docX, docY),
                    size = Size(docWidth, docHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx()),
                )

                // Document lines
                repeat(5) { index ->
                    val lineY = docY + (docHeight * 0.2f) + (index * docHeight * 0.12f)
                    val lineWidth = if (index == 4) docWidth * 0.4f else docWidth * 0.8f
                    drawRoundRect(
                        color = feature.color.copy(alpha = 0.3f),
                        topLeft = Offset(docX + docWidth * 0.1f, lineY),
                        size = Size(lineWidth, 4.dp.toPx()),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx()),
                    )
                }
            }

            "Contact Management" -> {
                // People/network illustration
                repeat(3) { index ->
                    val angle = (index * 120f) * (kotlin.math.PI / 180f).toFloat()
                    val radius = size.minDimension * 0.25f
                    val x = centerX + kotlin.math.cos(angle) * radius
                    val y = centerY + kotlin.math.sin(angle) * radius

                    drawCircle(
                        color = feature.color.copy(alpha = 0.8f - index * 0.2f),
                        radius = 25.dp.toPx() + index * 5.dp.toPx(),
                        center = Offset(x, y),
                    )

                    // Connection lines
                    if (index > 0) {
                        val prevAngle = ((index - 1) * 120f) * (kotlin.math.PI / 180f).toFloat()
                        val prevX = centerX + kotlin.math.cos(prevAngle) * radius
                        val prevY = centerY + kotlin.math.sin(prevAngle) * radius

                        drawLine(
                            color = feature.color.copy(alpha = 0.3f),
                            start = Offset(prevX, prevY),
                            end = Offset(x, y),
                            strokeWidth = 2.dp.toPx(),
                        )
                    }
                }
            }

            "Business Analytics" -> {
                // Chart illustration
                val barCount = 5
                val barWidth = size.width / (barCount * 2)
                val maxHeight = size.height * 0.6f

                repeat(barCount) { index ->
                    val barHeight = maxHeight * (0.3f + index * 0.15f + kotlin.math.sin(index.toFloat()) * 0.1f)
                    val barX = (index * size.width / barCount) + (size.width / barCount - barWidth) / 2
                    val barY = centerY + maxHeight / 2 - barHeight

                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                feature.color.copy(alpha = 0.8f),
                                feature.color.copy(alpha = 0.4f),
                            ),
                        ),
                        topLeft = Offset(barX, barY),
                        size = Size(barWidth, barHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx()),
                    )
                }

                // Trend line
                val trendPath = Path()
                repeat(barCount) { index ->
                    val x = (index * size.width / barCount) + size.width / barCount / 2
                    val y = centerY - (index * 10.dp.toPx()) + kotlin.math.sin(index.toFloat()) * 20.dp.toPx()

                    if (index == 0) {
                        trendPath.moveTo(x, y)
                    } else {
                        trendPath.lineTo(x, y)
                    }
                }

                drawPath(
                    path = trendPath,
                    color = QuantiveExpressiveTokens.Colors.Growth,
                    style = Stroke(width = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round),
                )
            }

            "Tax Compliance" -> {
                // Shield/checkmark illustration
                val shieldPath = Path()
                val shieldWidth = size.width * 0.5f
                val shieldHeight = size.height * 0.6f
                val shieldX = centerX - shieldWidth / 2
                val shieldY = centerY - shieldHeight / 2

                shieldPath.moveTo(centerX, shieldY)
                shieldPath.lineTo(shieldX + shieldWidth, shieldY + shieldHeight * 0.3f)
                shieldPath.lineTo(shieldX + shieldWidth, shieldY + shieldHeight * 0.7f)
                shieldPath.lineTo(centerX, shieldY + shieldHeight)
                shieldPath.lineTo(shieldX, shieldY + shieldHeight * 0.7f)
                shieldPath.lineTo(shieldX, shieldY + shieldHeight * 0.3f)
                shieldPath.close()

                drawPath(
                    path = shieldPath,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            feature.color.copy(alpha = 0.7f),
                            feature.color.copy(alpha = 0.3f),
                        ),
                    ),
                )

                // Checkmark inside shield
                val checkPath = Path()
                val checkStartX = centerX - shieldWidth * 0.15f
                val checkStartY = centerY
                val checkMidX = centerX - shieldWidth * 0.05f
                val checkMidY = centerY + shieldHeight * 0.1f
                val checkEndX = centerX + shieldWidth * 0.2f
                val checkEndY = centerY - shieldHeight * 0.15f

                checkPath.moveTo(checkStartX, checkStartY)
                checkPath.lineTo(checkMidX, checkMidY)
                checkPath.lineTo(checkEndX, checkEndY)

                drawPath(
                    path = checkPath,
                    color = QuantiveExpressiveTokens.Colors.Growth,
                    style = Stroke(width = 4.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round),
                )
            }
        }
    }
}
