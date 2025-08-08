package za.co.quantive.app.presentation.onboarding.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import za.co.quantive.app.domain.profile.BusinessType
import za.co.quantive.app.domain.profile.EmployeeCount
import za.co.quantive.app.presentation.onboarding.design.OnboardingDesignSystem

data class FormSection(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val isCompleted: Boolean,
    val isRequired: Boolean = true,
)

@Composable
fun BusinessInfoScreen(
    businessName: String,
    onBusinessNameChange: (String) -> Unit,
    businessType: BusinessType?,
    companyRegistrationNumber: String?,
    onCompanyRegistrationNumberChange: (String) -> Unit,
    website: String?,
    onWebsiteChange: (String) -> Unit,
    industry: String?,
    onIndustryChange: (String) -> Unit,
    employeeCount: EmployeeCount?,
    onEmployeeCountChange: (EmployeeCount) -> Unit,
    logoPath: String?,
    onLogoUpload: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    val industries = listOf(
        "Consulting & Professional Services",
        "Technology & Software",
        "Creative & Design",
        "Marketing & Advertising",
        "Healthcare & Medical",
        "Education & Training",
        "Finance & Accounting",
        "Legal Services",
        "Construction & Engineering",
        "Real Estate",
        "Retail & E-commerce",
        "Manufacturing",
        "Transportation & Logistics",
        "Food & Beverage",
        "Other",
    )

    // Form completion tracking for modern UX
    val formSections = listOf(
        FormSection(
            id = "basic",
            title = "Basic Information",
            subtitle = "Your business fundamentals",
            icon = Icons.Default.Business,
            isCompleted = businessName.isNotBlank() && logoPath != null,
        ),
        FormSection(
            id = "legal",
            title = "Legal Details",
            subtitle = "Registration and compliance",
            icon = Icons.Default.Domain,
            isCompleted = businessType == BusinessType.INDIVIDUAL ||
                (businessType == BusinessType.COMPANY && !companyRegistrationNumber.isNullOrBlank()),
            isRequired = businessType == BusinessType.COMPANY,
        ),
        FormSection(
            id = "profile",
            title = "Business Profile",
            subtitle = "Industry and company details",
            icon = Icons.Default.People,
            isCompleted = !industry.isNullOrBlank() && employeeCount != null,
            isRequired = false,
        ),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.02f),
                    ),
                ),
            )
            .verticalScroll(scrollState)
            .padding(OnboardingDesignSystem.Spacing.screenHorizontalPadding),
    ) {
        Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.xl))

        // Modern Hero Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Tell us about your business",
                style = OnboardingDesignSystem.Typography.displayMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = "Business information form"
                    },
            )

            Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.sm))

            Text(
                text = "We'll customize your invoicing experience based on your business needs",
                style = OnboardingDesignSystem.Typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = OnboardingDesignSystem.Spacing.md),
            )
        }

        Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.sectionVerticalSpacing))

        // Progress Indicator with Modern Styling
        ProgressIndicatorRow(
            sections = formSections,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.xl))

        // Section 1: Basic Information Card
        InfoSectionCard(
            section = formSections[0],
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Modern Logo Upload Component
            ModernLogoUpload(
                logoPath = logoPath,
                onLogoUpload = onLogoUpload,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.lg))

            // Enhanced Business Name Input
            ModernTextField(
                value = businessName,
                onValueChange = onBusinessNameChange,
                label = "Business Name",
                placeholder = "Enter your business name",
                leadingIcon = Icons.Default.Business,
                isRequired = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.lg))

        // Section 2: Legal Details Card (Company-specific)
        AnimatedVisibility(
            visible = businessType == BusinessType.COMPANY,
        ) {
            Column {
                Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.lg))

                InfoSectionCard(
                    section = formSections[1],
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ModernTextField(
                        value = companyRegistrationNumber ?: "",
                        onValueChange = onCompanyRegistrationNumberChange,
                        label = "Company Registration Number",
                        placeholder = "Enter your registration number",
                        leadingIcon = Icons.Default.Domain,
                        isRequired = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.lg))

        // Section 3: Business Profile Card
        InfoSectionCard(
            section = formSections[2],
            modifier = Modifier.fillMaxWidth(),
        ) {
            ModernTextField(
                value = website ?: "",
                onValueChange = onWebsiteChange,
                label = "Website",
                placeholder = "https://www.yourwebsite.com",
                leadingIcon = Icons.Default.Language,
                keyboardType = KeyboardType.Uri,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.md))

            // Modern Industry Dropdown
            var industryDropdownExpanded by remember { mutableStateOf(false) }
            ModernDropdownField(
                value = industry ?: "",
                label = "Industry",
                placeholder = "Select your industry",
                leadingIcon = Icons.Default.Business,
                expanded = industryDropdownExpanded,
                onExpandedChange = { industryDropdownExpanded = it },
                modifier = Modifier.fillMaxWidth(),
            ) {
                industries.forEach { industryOption ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = industryOption,
                                style = OnboardingDesignSystem.Typography.bodyMedium,
                            )
                        },
                        onClick = {
                            onIndustryChange(industryOption)
                            industryDropdownExpanded = false
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.md))

            // Modern Employee Count Dropdown
            var employeeDropdownExpanded by remember { mutableStateOf(false) }
            ModernDropdownField(
                value = employeeCount?.displayName ?: "",
                label = "Company Size",
                placeholder = "Select company size",
                leadingIcon = Icons.Default.People,
                expanded = employeeDropdownExpanded,
                onExpandedChange = { employeeDropdownExpanded = it },
                modifier = Modifier.fillMaxWidth(),
            ) {
                EmployeeCount.values().forEach { count ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = count.displayName,
                                style = OnboardingDesignSystem.Typography.bodyMedium,
                            )
                        },
                        onClick = {
                            onEmployeeCountChange(count)
                            employeeDropdownExpanded = false
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.xxl))

        // Modern Continue Button with Enhanced Styling
        val completedSections = formSections.count { it.isCompleted || !it.isRequired }
        val canContinue = businessName.isNotBlank()

        val buttonScale by animateFloatAsState(
            targetValue = if (canContinue) 1f else 0.96f,
            animationSpec = OnboardingDesignSystem.Animations.gentleSpring,
        )

        Button(
            onClick = onContinue,
            enabled = canContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(OnboardingDesignSystem.Spacing.buttonHeight)
                .scale(buttonScale)
                .semantics {
                    contentDescription = if (canContinue) {
                        "Continue to next step"
                    } else {
                        "Enter business name to continue"
                    }
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            ),
            shape = OnboardingDesignSystem.Shapes.buttonShape,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = if (canContinue) 4.dp else 0.dp,
                pressedElevation = if (canContinue) 8.dp else 0.dp,
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
                if (canContinue) {
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

        // Completion Status Summary
        CompletionStatusSummary(
            sections = formSections,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.lg))
    }
}

@Composable
private fun ProgressIndicatorRow(
    sections: List<FormSection>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        sections.forEachIndexed { index, section ->
            ProgressIndicatorItem(
                section = section,
                isLast = index == sections.size - 1,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ProgressIndicatorItem(
    section: FormSection,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Status Indicator
        val backgroundColor by animateColorAsState(
            targetValue = when {
                section.isCompleted -> MaterialTheme.colorScheme.primary
                section.isRequired -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            },
            animationSpec = tween(300),
        )

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            if (section.isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "${section.title} completed",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp),
                )
            } else {
                Icon(
                    imageVector = section.icon,
                    contentDescription = section.title,
                    tint = if (section.isRequired) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    modifier = Modifier.size(12.dp),
                )
            }
        }

        // Connector Line
        if (!isLast) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(2.dp)
                    .padding(horizontal = 8.dp)
                    .background(
                        color = if (section.isCompleted) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        } else {
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        },
                        shape = RoundedCornerShape(1.dp),
                    ),
            )
        }
    }
}

@Composable
private fun InfoSectionCard(
    section: FormSection,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = if (section.isCompleted) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        } else if (section.isRequired) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        } else {
            MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        },
        animationSpec = tween(300),
    )

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (section.isCompleted) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.04f)
            } else {
                MaterialTheme.colorScheme.surface
            },
        ),
        border = BorderStroke(
            width = if (section.isCompleted) 2.dp else 1.dp,
            color = borderColor,
        ),
        shape = OnboardingDesignSystem.Shapes.cardShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (section.isCompleted) 4.dp else 2.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(OnboardingDesignSystem.Spacing.cardPadding),
        ) {
            // Section Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        imageVector = section.icon,
                        contentDescription = null,
                        tint = if (section.isCompleted) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.size(20.dp),
                    )

                    Spacer(modifier = Modifier.width(OnboardingDesignSystem.Spacing.sm))

                    Column {
                        Text(
                            text = section.title,
                            style = OnboardingDesignSystem.Typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                            color = if (section.isCompleted) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                        )

                        Text(
                            text = section.subtitle,
                            style = OnboardingDesignSystem.Typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                if (section.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Section completed",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.md))

            content()
        }
    }
}

@Composable
private fun ModernLogoUpload(
    logoPath: String?,
    onLogoUpload: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        val logoScale by animateFloatAsState(
            targetValue = if (logoPath.isNullOrEmpty()) 1f else 1.05f,
            animationSpec = OnboardingDesignSystem.Animations.gentleSpring,
        )

        Box(
            modifier = Modifier
                .size(88.dp)
                .scale(logoScale)
                .clip(CircleShape)
                .background(
                    brush = if (logoPath.isNullOrEmpty()) {
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.04f),
                            ),
                        )
                    } else {
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                            ),
                        )
                    },
                )
                .border(
                    width = if (logoPath.isNullOrEmpty()) 2.dp else 3.dp,
                    color = if (logoPath.isNullOrEmpty()) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    shape = CircleShape,
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) { onLogoUpload() }
                .semantics {
                    contentDescription = if (logoPath.isNullOrEmpty()) {
                        "Upload business logo"
                    } else {
                        "Change business logo"
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            if (logoPath.isNullOrEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        imageVector = Icons.Default.Camera,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp),
                    )
                }
            } else {
                // TODO: Show actual logo image
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(OnboardingDesignSystem.Spacing.sm))

        Text(
            text = if (logoPath.isNullOrEmpty()) "Add Business Logo" else "Change Logo",
            style = OnboardingDesignSystem.Typography.labelMedium.copy(
                fontWeight = FontWeight.Medium,
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onLogoUpload() },
        )

        Text(
            text = "Optional • JPG, PNG up to 5MB",
            style = OnboardingDesignSystem.Typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(modifier = modifier) {
        Text(
            text = label + if (isRequired) " *" else "",
            style = OnboardingDesignSystem.Typography.labelMedium.copy(
                fontWeight = FontWeight.Medium,
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp),
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = OnboardingDesignSystem.Typography.bodyMedium,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (value.isNotBlank()) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(20.dp),
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            ),
            shape = OnboardingDesignSystem.Shapes.medium,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        )
    }
}

@Composable
private fun ModernDropdownField(
    value: String,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = OnboardingDesignSystem.Typography.labelMedium.copy(
                fontWeight = FontWeight.Medium,
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp),
        )

        Box {
            OutlinedTextField(
                value = value,
                onValueChange = { },
                readOnly = true,
                placeholder = {
                    Text(
                        text = placeholder,
                        style = OnboardingDesignSystem.Typography.bodyMedium,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = if (value.isNotBlank()) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.size(20.dp),
                    )
                },
                trailingIcon = {
                    val rotation by animateFloatAsState(
                        targetValue = if (expanded) 180f else 0f,
                        animationSpec = tween(300),
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(24.dp)
                            .offset(y = (-2).dp)
                            .clickable { onExpandedChange(!expanded) },
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandedChange(true) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                ),
                shape = OnboardingDesignSystem.Shapes.medium,
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                content()
            }
        }
    }
}

@Composable
private fun CompletionStatusSummary(
    sections: List<FormSection>,
    modifier: Modifier = Modifier,
) {
    val completedCount = sections.count { it.isCompleted }
    val totalSections = sections.size
    val requiredCompletedCount = sections.count { it.isCompleted && it.isRequired }
    val totalRequiredSections = sections.count { it.isRequired }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
        shape = OnboardingDesignSystem.Shapes.cardShape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(OnboardingDesignSystem.Spacing.md),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "Progress",
                    style = OnboardingDesignSystem.Typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "$requiredCompletedCount of $totalRequiredSections required • $completedCount of $totalSections total",
                    style = OnboardingDesignSystem.Typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (requiredCompletedCount == totalRequiredSections) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (requiredCompletedCount == totalRequiredSections) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "All required sections completed",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp),
                    )
                } else {
                    Text(
                        text = "$requiredCompletedCount",
                        style = OnboardingDesignSystem.Typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}
