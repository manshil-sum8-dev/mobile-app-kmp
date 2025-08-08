package za.co.quantive.app.presentation.onboarding.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import za.co.quantive.app.domain.profile.BusinessType
import za.co.quantive.app.presentation.onboarding.design.OnboardingDesignSystem

data class UserTypeOption(
    val type: BusinessType,
    val title: String,
    val subtitle: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val benefits: List<String>,
)

@Composable
fun UserTypeSelectionScreen(
    selectedBusinessType: BusinessType?,
    onBusinessTypeSelected: (BusinessType) -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = OnboardingDesignSystem.Animations.slowTransition)
    }

    val userTypeOptions = listOf(
        UserTypeOption(
            type = BusinessType.INDIVIDUAL,
            title = "Individual / Freelancer",
            subtitle = "Perfect for solopreneurs",
            description = "Ideal for freelancers, consultants, and individual service providers who need professional invoicing.",
            icon = Icons.Default.Person,
            benefits = listOf(
                "Simple setup and management",
                "Personal tax reporting features",
                "Flexible payment terms",
                "Professional client relationships",
                "Quick invoice generation",
            ),
        ),
        UserTypeOption(
            type = BusinessType.COMPANY,
            title = "Company / Business",
            subtitle = "Built for growing businesses",
            description = "Designed for established companies with multiple employees, complex workflows, and enterprise needs.",
            icon = Icons.Default.Business,
            benefits = listOf(
                "Team collaboration features",
                "Advanced reporting & analytics",
                "Multi-user access control",
                "Enterprise integrations",
                "Bulk invoice processing",
            ),
        ),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceContainerLowest,
                    ),
                ),
            )
            .padding(OnboardingDesignSystem.Spacing.screenHorizontalPadding)
            .alpha(alpha.value),
    ) {
        Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.xl))

        // Professional header with brand emphasis
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Choose Your Business Type",
                style = OnboardingDesignSystem.Typography.displayMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics {
                    contentDescription = "Step 1: Choose your business type"
                },
            )

            Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.sm))

            Text(
                text = "Select the option that best describes your business to customize your invoicing experience.",
                style = OnboardingDesignSystem.Typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = OnboardingDesignSystem.Spacing.md),
            )
        }

        Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.sectionVerticalSpacing))

        // User Type Options with improved spacing
        Column(
            verticalArrangement = Arrangement.spacedBy(OnboardingDesignSystem.Spacing.md),
            modifier = Modifier.weight(1f),
        ) {
            userTypeOptions.forEach { option ->
                UserTypeCard(
                    userTypeOption = option,
                    isSelected = selectedBusinessType == option.type,
                    onSelected = { onBusinessTypeSelected(option.type) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.xl))

        // Premium Continue Button with enhanced styling
        val buttonScale by animateFloatAsState(
            targetValue = if (selectedBusinessType != null) 1f else 0.96f,
            animationSpec = OnboardingDesignSystem.Animations.gentleSpring,
        )

        val buttonElevation by animateFloatAsState(
            targetValue = if (selectedBusinessType != null) 6f else 2f,
            animationSpec = OnboardingDesignSystem.Animations.mediumTransition,
        )

        Button(
            onClick = onContinue,
            enabled = selectedBusinessType != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(OnboardingDesignSystem.Spacing.buttonHeight)
                .scale(buttonScale)
                .semantics {
                    contentDescription = if (selectedBusinessType != null) {
                        "Continue with ${selectedBusinessType.name.lowercase()} setup"
                    } else {
                        "Select a business type to continue"
                    }
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedBusinessType != null) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                contentColor = if (selectedBusinessType != null) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                },
            ),
            shape = OnboardingDesignSystem.Shapes.buttonShape,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = buttonElevation.dp,
                pressedElevation = (buttonElevation + 2f).dp,
                disabledElevation = 0.dp,
            ),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Continue",
                    style = OnboardingDesignSystem.Typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                if (selectedBusinessType != null) {
                    Spacer(modifier = Modifier.width(OnboardingDesignSystem.Spacing.sm))
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(OnboardingDesignSystem.ComponentSizes.iconSizeSmall),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.lg))
    }
}

@Composable
private fun UserTypeCard(
    userTypeOption: UserTypeOption,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardScale by animateFloatAsState(
        targetValue = if (isSelected) OnboardingDesignSystem.Animations.cardSelectScale else 1f,
        animationSpec = OnboardingDesignSystem.Animations.bouncySpring,
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        },
        animationSpec = tween(300),
    )

    val borderWidth by animateFloatAsState(
        targetValue = if (isSelected) 2.5f else 1f,
        animationSpec = tween(300),
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
        } else {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.03f)
        },
        animationSpec = tween(300),
    )

    val elevation by animateFloatAsState(
        targetValue = if (isSelected) OnboardingDesignSystem.Elevation.high.value else OnboardingDesignSystem.Elevation.medium.value,
        animationSpec = tween(300),
    )

    val shadowColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
        },
        animationSpec = tween(300),
    )

    Card(
        modifier = modifier
            .scale(cardScale)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) { onSelected() }
            .semantics {
                contentDescription = "${userTypeOption.title}: ${userTypeOption.subtitle}. ${if (isSelected) "Selected" else "Not selected"}"
            },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
        ),
        border = BorderStroke(
            width = borderWidth.dp,
            color = borderColor,
        ),
        shape = OnboardingDesignSystem.Shapes.cardShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation.dp,
            pressedElevation = (elevation.dp + 2.dp),
            focusedElevation = (elevation.dp + 1.dp),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(OnboardingDesignSystem.Spacing.cardPadding)
                .background(
                    brush = if (isSelected) {
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.02f),
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.06f),
                            ),
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.02f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.04f),
                            ),
                        )
                    },
                    shape = OnboardingDesignSystem.Shapes.cardShape,
                ),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    // Professional Icon with premium background
                    Box(
                        modifier = Modifier
                            .size(OnboardingDesignSystem.ComponentSizes.logoSizeCard + 8.dp)
                            .clip(CircleShape)
                            .background(
                                brush = if (isSelected) {
                                    Brush.radialGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                                        ),
                                        radius = 100f,
                                    )
                                } else {
                                    Brush.radialGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.04f),
                                        ),
                                        radius = 80f,
                                    )
                                },
                            )
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                shape = CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        val iconScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.1f else 1f,
                            animationSpec = OnboardingDesignSystem.Animations.gentleSpring,
                        )

                        Icon(
                            imageVector = userTypeOption.icon,
                            contentDescription = userTypeOption.title,
                            tint = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            },
                            modifier = Modifier
                                .size(OnboardingDesignSystem.ComponentSizes.iconSizeLarge)
                                .scale(iconScale),
                        )
                    }

                    Spacer(modifier = Modifier.width(OnboardingDesignSystem.Spacing.md))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userTypeOption.title,
                            style = OnboardingDesignSystem.Typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                        )

                        Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.xs))

                        Text(
                            text = userTypeOption.subtitle,
                            style = OnboardingDesignSystem.Typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                            ),
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                            },
                        )
                    }
                }

                // Premium Selection Indicator with animation
                val indicatorScale by animateFloatAsState(
                    targetValue = if (isSelected) 1.1f else 1f,
                    animationSpec = OnboardingDesignSystem.Animations.bouncySpring,
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .scale(indicatorScale)
                        .clip(CircleShape)
                        .background(
                            brush = if (isSelected) {
                                Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                    ),
                                )
                            } else {
                                Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.08f),
                                    ),
                                )
                            },
                        )
                        .border(
                            width = if (isSelected) 0.dp else 1.5.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(OnboardingDesignSystem.ComponentSizes.iconSizeSmall + 2.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.md))

            Text(
                text = userTypeOption.description,
                style = OnboardingDesignSystem.Typography.bodyMedium.copy(
                    lineHeight = 22.sp,
                ),
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                },
            )

            Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.md))

            // Premium Benefits List with enhanced styling
            Column(
                verticalArrangement = Arrangement.spacedBy(OnboardingDesignSystem.Spacing.sm + 2.dp),
            ) {
                userTypeOption.benefits.take(4).forEach { benefit ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.padding(vertical = 1.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = if (isSelected) {
                                        Brush.radialGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                            ),
                                        )
                                    } else {
                                        Brush.radialGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                            ),
                                        )
                                    },
                                ),
                        )

                        Spacer(modifier = Modifier.width(OnboardingDesignSystem.Spacing.sm + 2.dp))

                        Text(
                            text = benefit,
                            style = OnboardingDesignSystem.Typography.labelLarge.copy(
                                lineHeight = 20.sp,
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                            ),
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}
