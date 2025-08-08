package za.co.quantive.app.presentation.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import za.co.quantive.app.domain.profile.BusinessProfile
import za.co.quantive.app.presentation.components.QuantiveCard
import za.co.quantive.app.presentation.components.QuantivePrimaryButton
import za.co.quantive.app.presentation.components.QuantiveSecondaryButton
import za.co.quantive.app.presentation.components.QuantiveSectionHeader
import za.co.quantive.app.presentation.components.QuantiveTextButton
import za.co.quantive.app.presentation.components.QuantiveTextField
import za.co.quantive.app.presentation.theme.QuantiveDesignTokens
import za.co.quantive.app.presentation.theme.QuantiveExpressiveTokens

/**
 * Permissions Screen - Request app permissions with clear value explanations
 */
@Composable
fun PermissionsScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showContent by remember { mutableStateOf(false) }
    var notificationPermissionGranted by remember { mutableStateOf(false) }
    var cameraPermissionGranted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        showContent = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(QuantiveDesignTokens.Spacing.Large)
            .verticalScroll(rememberScrollState()),
    ) {
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(tween(QuantiveExpressiveTokens.Motion.ExpressiveMedium)) +
                slideInVertically(
                    initialOffsetY = { it / 4 },
                    animationSpec = tween(QuantiveExpressiveTokens.Motion.ExpressiveMedium),
                ),
        ) {
            Column {
                QuantiveSectionHeader(
                    title = "App Permissions",
                    subtitle = "Help us provide the best experience",
                )

                Spacer(modifier = Modifier.height(QuantiveExpressiveTokens.Spacing.ComponentSpacing))

                // Notifications Permission
                PermissionCard(
                    title = "Notifications",
                    description = "Get notified when invoices are viewed or paid, and never miss important business updates",
                    icon = Icons.Default.Notifications,
                    isGranted = notificationPermissionGranted,
                    onToggle = { notificationPermissionGranted = !notificationPermissionGranted },
                    benefits = listOf(
                        "Payment notifications",
                        "Invoice status updates",
                        "Overdue reminders",
                    ),
                )

                Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Medium))

                // Camera Permission
                PermissionCard(
                    title = "Camera Access",
                    description = "Scan business documents, capture receipts, and add your business logo for professional invoices",
                    icon = Icons.Default.Camera,
                    isGranted = cameraPermissionGranted,
                    onToggle = { cameraPermissionGranted = !cameraPermissionGranted },
                    benefits = listOf(
                        "Document scanning",
                        "Receipt capture",
                        "Business logo upload",
                    ),
                )

                Spacer(modifier = Modifier.height(QuantiveExpressiveTokens.Spacing.ComponentSpacing))

                // Privacy assurance
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.padding(QuantiveDesignTokens.Spacing.Medium),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = QuantiveExpressiveTokens.Colors.Trust,
                            modifier = Modifier.size(24.dp),
                        )
                        Spacer(modifier = Modifier.width(QuantiveDesignTokens.Spacing.Small))
                        Text(
                            text = "Your privacy is protected. You can change these permissions anytime in Settings.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

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
                modifier = Modifier.weight(2f),
            )
        }
    }
}

/**
 * Individual permission request card
 */
@Composable
private fun PermissionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isGranted: Boolean,
    onToggle: () -> Unit,
    benefits: List<String>,
    modifier: Modifier = Modifier,
) {
    QuantiveCard(
        modifier = modifier.fillMaxWidth(),
        expressiveMotion = true,
        colors = CardDefaults.cardColors(
            containerColor = if (isGranted) {
                QuantiveExpressiveTokens.Colors.Growth.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surface
            },
        ),
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isGranted) QuantiveExpressiveTokens.Colors.Growth else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(32.dp),
                    )
                    Spacer(modifier = Modifier.width(QuantiveDesignTokens.Spacing.Medium))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium,
                    )
                }

                Switch(
                    checked = isGranted,
                    onCheckedChange = { onToggle() },
                )
            }

            Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Small))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (benefits.isNotEmpty()) {
                Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Small))
                benefits.forEach { benefit ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp),
                    ) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = QuantiveExpressiveTokens.Colors.ExpressivePrimary,
                        )
                        Spacer(modifier = Modifier.width(QuantiveDesignTokens.Spacing.Small))
                        Text(
                            text = benefit,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Business Profile Setup Screen - Collect essential business information
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessProfileSetupScreen(
    onNext: (BusinessProfile) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showContent by remember { mutableStateOf(false) }
    var businessName by remember { mutableStateOf("") }
    var businessType by remember { mutableStateOf("Freelancer") }
    var vatNumber by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("ZAR") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Validation states
    var businessNameError by remember { mutableStateOf<String?>(null) }

    val businessTypes = listOf(
        "Freelancer",
        "Consultant",
        "Small Business",
        "Retailer",
        "Service Provider",
        "Other",
    )

    LaunchedEffect(Unit) {
        delay(200)
        showContent = true
    }

    // Validation
    LaunchedEffect(businessName) {
        businessNameError = when {
            businessName.isBlank() -> "Business name is required"
            businessName.length < 2 -> "Business name too short"
            businessName.length > 150 -> "Business name too long"
            else -> null
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(QuantiveDesignTokens.Spacing.Large)
            .verticalScroll(rememberScrollState()),
    ) {
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(tween(QuantiveExpressiveTokens.Motion.ExpressiveMedium)) +
                slideInVertically(
                    initialOffsetY = { it / 4 },
                    animationSpec = tween(QuantiveExpressiveTokens.Motion.ExpressiveMedium),
                ),
        ) {
            Column {
                QuantiveSectionHeader(
                    title = "Set Up Your Business",
                    subtitle = "Tell us about your business to personalize your experience",
                )

                Spacer(modifier = Modifier.height(QuantiveExpressiveTokens.Spacing.ComponentSpacing))

                // Business Name (Required)
                QuantiveTextField(
                    value = businessName,
                    onValueChange = { businessName = it },
                    label = "Business Name",
                    placeholder = "Enter your business name",
                    error = businessNameError,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Medium))

                // Business Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = isDropdownExpanded,
                    onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    QuantiveTextField(
                        value = businessType,
                        onValueChange = { },
                        label = "Business Type",
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                    )

                    ExposedDropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                    ) {
                        businessTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    businessType = type
                                    isDropdownExpanded = false
                                },
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Medium))

                // VAT Registration (Optional)
                QuantiveTextField(
                    value = vatNumber,
                    onValueChange = { vatNumber = it },
                    label = "VAT Registration Number",
                    placeholder = "4123456789 (Optional)",
                    modifier = Modifier.fillMaxWidth(),
                )

                Text(
                    text = "Optional - Required for VAT-compliant invoicing",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(
                        start = QuantiveDesignTokens.Spacing.Medium,
                        top = QuantiveDesignTokens.Spacing.Tiny,
                    ),
                )

                Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Medium))

                // Currency Selection
                QuantiveTextField(
                    value = "ZAR (South African Rand)",
                    onValueChange = { },
                    label = "Preferred Currency",
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(QuantiveExpressiveTokens.Spacing.ComponentSpacing))

                // Help text
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    ),
                ) {
                    Text(
                        text = "This information will be used on your invoices and for compliance purposes. You can update it later in Settings.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(QuantiveDesignTokens.Spacing.Medium),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(QuantiveDesignTokens.Spacing.Medium),
        ) {
            QuantiveSecondaryButton(
                text = "Back",
                onClick = onBack,
                modifier = Modifier.weight(1f),
                enabled = !isLoading,
            )
            QuantivePrimaryButton(
                text = "Continue",
                onClick = {
                    if (businessNameError == null && businessName.isNotBlank()) {
                        scope.launch {
                            try {
                                isLoading = true
                                val profile = BusinessProfile(
                                    name = businessName.trim(),
                                    currency = currency,
                                    tax_id = vatNumber.takeIf { it.isNotBlank() },
                                )
                                // Save the business profile to secure store for later use
                                za.co.quantive.app.security.SecureStore.saveUserProfile(
                                    "Business Owner", // Default name - will be collected later if needed
                                    businessName.trim(),
                                )
                                onNext(profile)
                            } catch (e: Exception) {
                                // Handle error - could show a toast or error message
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier.weight(2f),
                enabled = businessNameError == null && businessName.isNotBlank() && !isLoading,
                loading = isLoading,
            )
        }
    }
}

/**
 * Personalization Screen - Configure features and preferences
 */
@Composable
fun PersonalizationScreen(
    businessProfile: BusinessProfile?,
    onNext: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showContent by remember { mutableStateOf(false) }

    // Preferences state
    var invoiceTemplate by remember { mutableStateOf("detailed") }
    var paymentNotifications by remember { mutableStateOf(true) }
    var statusUpdates by remember { mutableStateOf(true) }
    var weeklySummary by remember { mutableStateOf(false) }
    var setupBranding by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        showContent = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(QuantiveDesignTokens.Spacing.Large)
            .verticalScroll(rememberScrollState()),
    ) {
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(tween(QuantiveExpressiveTokens.Motion.ExpressiveMedium)) +
                slideInVertically(
                    initialOffsetY = { it / 4 },
                    animationSpec = tween(QuantiveExpressiveTokens.Motion.ExpressiveMedium),
                ),
        ) {
            Column {
                QuantiveSectionHeader(
                    title = "Customize Your Setup",
                    subtitle = "Configure Quantive for your business needs",
                )

                Spacer(modifier = Modifier.height(QuantiveExpressiveTokens.Spacing.ComponentSpacing))

                // Invoice Preferences
                PreferenceSectionCard(
                    title = "Invoice Preferences",
                    icon = Icons.Default.CheckCircle,
                ) {
                    Column {
                        Text(
                            text = "Choose your default invoice style",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.Small),
                        )

                        InvoiceTemplateOption(
                            title = "Simple",
                            description = "Basic invoices for quick billing",
                            isSelected = invoiceTemplate == "simple",
                            onClick = { invoiceTemplate = "simple" },
                        )

                        InvoiceTemplateOption(
                            title = "Detailed",
                            description = "Line items with descriptions and totals",
                            isSelected = invoiceTemplate == "detailed",
                            onClick = { invoiceTemplate = "detailed" },
                        )

                        InvoiceTemplateOption(
                            title = "Service-based",
                            description = "Time tracking and hourly billing",
                            isSelected = invoiceTemplate == "service",
                            onClick = { invoiceTemplate = "service" },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Medium))

                // Notification Preferences
                PreferenceSectionCard(
                    title = "Notification Settings",
                    icon = Icons.Default.Notifications,
                ) {
                    Column {
                        PreferenceToggle(
                            title = "Payment Notifications",
                            description = "Get notified when invoices are paid",
                            checked = paymentNotifications,
                            onCheckedChange = { paymentNotifications = it },
                        )

                        PreferenceToggle(
                            title = "Invoice Status Updates",
                            description = "Track when invoices are viewed or sent",
                            checked = statusUpdates,
                            onCheckedChange = { statusUpdates = it },
                        )

                        PreferenceToggle(
                            title = "Weekly Business Summary",
                            description = "Weekly reports on your business performance",
                            checked = weeklySummary,
                            onCheckedChange = { weeklySummary = it },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Medium))

                // Branding Options
                PreferenceSectionCard(
                    title = "Branding Setup",
                    icon = Icons.Default.Photo,
                ) {
                    Column {
                        Text(
                            text = "Add your business branding to invoices",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.Small),
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(QuantiveDesignTokens.Spacing.Small),
                        ) {
                            QuantiveSecondaryButton(
                                text = "Add Logo",
                                onClick = { /* Handle logo upload */ },
                                icon = Icons.Default.CameraAlt,
                                modifier = Modifier.weight(1f),
                            )
                            QuantiveSecondaryButton(
                                text = "Set Colors",
                                onClick = { /* Handle color theme */ },
                                modifier = Modifier.weight(1f),
                            )
                        }

                        Spacer(modifier = Modifier.height(QuantiveDesignTokens.Spacing.Small))

                        QuantiveTextButton(
                            text = "Skip for now",
                            onClick = { /* Skip branding setup */ },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

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
                text = "Complete Setup",
                onClick = onNext,
                modifier = Modifier.weight(2f),
            )
        }
    }
}

/**
 * Preference section card wrapper
 */
@Composable
private fun PreferenceSectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit,
) {
    QuantiveCard(
        modifier = Modifier.fillMaxWidth(),
        expressiveMotion = true,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = QuantiveDesignTokens.Spacing.Medium),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = QuantiveExpressiveTokens.Colors.ExpressivePrimary,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(QuantiveDesignTokens.Spacing.Small))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                )
            }
            content()
        }
    }
}

/**
 * Invoice template selection option
 */
@Composable
private fun InvoiceTemplateOption(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton,
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                QuantiveExpressiveTokens.Colors.ExpressivePrimary.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            },
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(QuantiveDesignTokens.Spacing.Medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
            )
            Spacer(modifier = Modifier.width(QuantiveDesignTokens.Spacing.Small))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/**
 * Preference toggle item
 */
@Composable
private fun PreferenceToggle(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = QuantiveDesignTokens.Spacing.Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(modifier = Modifier.width(QuantiveDesignTokens.Spacing.Small))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}
