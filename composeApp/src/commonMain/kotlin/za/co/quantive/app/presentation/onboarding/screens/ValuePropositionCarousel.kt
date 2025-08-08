package za.co.quantive.app.presentation.onboarding.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import za.co.quantive.app.presentation.onboarding.design.OnboardingDesignSystem

data class ValueProposition(
    val title: String,
    val subtitle: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val primaryColor: Color,
    val backgroundColor: Color,
)

@Composable
fun ValuePropositionCarousel(
    onContinue: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val valuePropositions = listOf(
        ValueProposition(
            title = "Invoice in Seconds",
            subtitle = "Create Professional Invoices Instantly",
            description = "Generate beautiful, professional invoices in seconds with our smart templates and automated calculations. Perfect for busy professionals.",
            icon = Icons.Default.Receipt,
            primaryColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        ValueProposition(
            title = "Track Your Cashflow",
            subtitle = "Monitor Your Business Health",
            description = "Get real-time insights into your cash flow, outstanding payments, and revenue trends with powerful dashboard analytics.",
            icon = Icons.Default.TrendingUp,
            primaryColor = MaterialTheme.colorScheme.secondary,
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        ValueProposition(
            title = "Get Paid Faster",
            subtitle = "Accelerate Your Revenue Collection",
            description = "Send automated payment reminders and offer multiple payment options to get paid faster. Reduce outstanding payments by up to 40%.",
            icon = Icons.Default.Payment,
            primaryColor = MaterialTheme.colorScheme.tertiary,
            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
        ValueProposition(
            title = "Smart Analytics",
            subtitle = "Make Data-Driven Decisions",
            description = "Understand your business performance with detailed analytics and actionable insights. See which clients pay on time and optimize your workflow.",
            icon = Icons.Default.Analytics,
            primaryColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    )

    val pagerState = rememberPagerState(pageCount = { valuePropositions.size })
    val scope = rememberCoroutineScope()
    var isPaused by remember { mutableStateOf(false) }

    // Auto-advance pages with pause capability
    LaunchedEffect(pagerState.currentPage, isPaused) {
        if (!isPaused) {
            delay(5000) // 5 seconds per page for better readability
            val nextPage = (pagerState.currentPage + 1) % valuePropositions.size
            scope.launch {
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            // Header with skip button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(OnboardingDesignSystem.Spacing.screenHorizontalPadding)
                    .padding(top = OnboardingDesignSystem.Spacing.md),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Welcome to Quantive",
                    style = OnboardingDesignSystem.Typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                TextButton(
                    onClick = onSkip,
                    modifier = Modifier.semantics {
                        contentDescription = "Skip onboarding introduction"
                    },
                ) {
                    Text(
                        text = "Skip",
                        style = OnboardingDesignSystem.Typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    )
                }
            }

            Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.md))

            // Carousel content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                    ) {
                        isPaused = !isPaused // Tap to pause/resume auto-advance
                    },
            ) { pageIndex ->
                ValuePropositionPage(
                    valueProposition = valuePropositions[pageIndex],
                    modifier = Modifier.fillMaxSize(),
                )
            }

            // Page indicators with progress
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = OnboardingDesignSystem.Spacing.md),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(valuePropositions.size) { index ->
                    val isSelected = index == pagerState.currentPage
                    val animatedWidth by animateFloatAsState(
                        targetValue = if (isSelected) 1f else 0.4f,
                        animationSpec = OnboardingDesignSystem.Animations.mediumTransition,
                    )

                    Box(
                        modifier = Modifier
                            .height(OnboardingDesignSystem.Spacing.xs)
                            .width(OnboardingDesignSystem.ComponentSizes.progressIndicatorWidth * animatedWidth)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                },
                            )
                            .clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                    )
                    if (index < valuePropositions.size - 1) {
                        Spacer(modifier = Modifier.width(OnboardingDesignSystem.Spacing.sm))
                    }
                }
            }

            // Continue button
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = OnboardingDesignSystem.Spacing.screenHorizontalPadding)
                    .height(OnboardingDesignSystem.Spacing.buttonHeight)
                    .semantics {
                        contentDescription = "Continue to next step of onboarding"
                    },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                shape = OnboardingDesignSystem.Shapes.buttonShape,
            ) {
                Text(
                    text = "Get Started",
                    style = OnboardingDesignSystem.Typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }

            Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.lg))
        }
    }
}

@Composable
private fun ValuePropositionPage(
    valueProposition: ValueProposition,
    modifier: Modifier = Modifier,
) {
    val alpha = remember { Animatable(0f) }
    val translateY = remember { Animatable(20f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = OnboardingDesignSystem.Animations.slowTransition)
        translateY.animateTo(0f, animationSpec = OnboardingDesignSystem.Animations.gentleSpring)
    }

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(OnboardingDesignSystem.Spacing.screenHorizontalPadding)
            .alpha(alpha.value),
        colors = CardDefaults.cardColors(
            containerColor = valueProposition.backgroundColor,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = OnboardingDesignSystem.Elevation.low,
        ),
        shape = OnboardingDesignSystem.Shapes.large,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(OnboardingDesignSystem.Spacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Icon with gradient background
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                valueProposition.primaryColor.copy(alpha = 0.1f),
                                valueProposition.primaryColor.copy(alpha = 0.05f),
                            ),
                        ),
                    )
                    .padding(OnboardingDesignSystem.Spacing.lg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = valueProposition.icon,
                    contentDescription = valueProposition.title,
                    tint = valueProposition.primaryColor,
                    modifier = Modifier
                        .size(OnboardingDesignSystem.ComponentSizes.iconSizeLarge + 24.dp)
                        .scale(alpha.value),
                )
            }

            Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.xl))

            // Title with better typography
            Text(
                text = valueProposition.title,
                style = OnboardingDesignSystem.Typography.displayMedium,
                color = valueProposition.primaryColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics {
                    contentDescription = "Feature: ${valueProposition.title}"
                },
            )

            Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.sm))

            // Subtitle
            Text(
                text = valueProposition.subtitle,
                style = OnboardingDesignSystem.Typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.md))

            // Description with better readability
            Text(
                text = valueProposition.description,
                style = OnboardingDesignSystem.Typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = OnboardingDesignSystem.Spacing.md),
            )
        }
    }
}
