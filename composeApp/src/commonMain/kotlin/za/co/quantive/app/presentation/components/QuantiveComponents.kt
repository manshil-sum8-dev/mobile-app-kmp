package za.co.quantive.app.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import za.co.quantive.app.presentation.theme.QuantiveDesignTokens
import za.co.quantive.app.presentation.theme.QuantiveExpressiveTokens
import za.co.quantive.app.presentation.theme.QuantiveTheme

/**
 * Quantive Card Component with Material 3 Expressive features
 * Professional card with shape morphing and expressive animations
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuantiveCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    elevation: androidx.compose.ui.unit.Dp = QuantiveExpressiveTokens.Elevation.ExpressiveLow,
    colors: CardColors = CardDefaults.cardColors(),
    contentDescription: String? = null,
    expressiveMotion: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Expressive animations
    val animatedElevation by animateDpAsState(
        targetValue = if (isPressed && onClick != null) QuantiveExpressiveTokens.Elevation.ExpressiveMedium else elevation,
        animationSpec = tween(
            durationMillis = QuantiveExpressiveTokens.Motion.ExpressiveShort,
            easing = QuantiveExpressiveTokens.Motion.ExpressiveEaseOut,
        ),
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null && expressiveMotion) QuantiveExpressiveTokens.InteractionStates.PressedScale else 1f,
        animationSpec = QuantiveExpressiveTokens.Motion.SpringGentle,
    )

    // Dynamic shape morphing
    val shape by remember {
        derivedStateOf {
            if (onClick != null) {
                QuantiveExpressiveTokens.Shapes.morphingCard(hovered = false, elevated = isPressed)
            } else {
                QuantiveExpressiveTokens.Shapes.ExpressiveMedium
            }
        }
    }

    Card(
        onClick = onClick ?: {},
        modifier = modifier
            .scale(animatedScale)
            .then(
                if (onClick != null) {
                    Modifier.semantics(mergeDescendants = true) {
                        if (contentDescription != null) {
                            this.contentDescription = contentDescription
                        }
                    }
                } else {
                    Modifier
                },
            ),
        enabled = enabled && onClick != null,
        shape = shape,
        colors = colors,
        elevation = CardDefaults.cardElevation(defaultElevation = animatedElevation),
        interactionSource = interactionSource,
    ) {
        Column(
            modifier = Modifier.padding(QuantiveExpressiveTokens.Spacing.CardPadding),
            content = content,
        )
    }
}

/**
 * Quantive Primary Button with Material 3 Expressive features
 * Main action button with shape morphing and spring animations
 */
@Composable
fun QuantivePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null,
    expressiveMotion: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Expressive color animation
    val animatedContainerColor by animateColorAsState(
        targetValue = if (isPressed && enabled && !loading) {
            QuantiveExpressiveTokens.Colors.ExpressivePrimaryVariant
        } else {
            QuantiveExpressiveTokens.Colors.ExpressivePrimary
        },
        animationSpec = tween(
            durationMillis = QuantiveExpressiveTokens.Motion.ExpressiveShort,
            easing = QuantiveExpressiveTokens.Motion.ExpressiveEaseOut,
        ),
    )

    // Spring-based scale animation
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed && enabled && !loading && expressiveMotion) {
            QuantiveExpressiveTokens.InteractionStates.PressedScale
        } else {
            1f
        },
        animationSpec = QuantiveExpressiveTokens.Motion.SpringBouncy,
    )

    // Dynamic shape morphing
    val shape by remember {
        derivedStateOf {
            QuantiveExpressiveTokens.Shapes.morphingButton(pressed = isPressed, selected = false)
        }
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .height(QuantiveDesignTokens.Dimensions.ButtonHeightLarge)
            .scale(animatedScale),
        enabled = enabled && !loading,
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedContainerColor,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        shape = shape,
        contentPadding = PaddingValues(
            horizontal = QuantiveExpressiveTokens.Spacing.ComponentSpacing,
            vertical = QuantiveDesignTokens.Spacing.Small,
        ),
        interactionSource = interactionSource,
    ) {
        if (loading) {
            QuantiveExpressiveLoadingIndicator(
                modifier = Modifier.size(QuantiveDesignTokens.Dimensions.IconSmall),
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Spacer(modifier = Modifier.width(QuantiveDesignTokens.Spacing.Small))
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(QuantiveDesignTokens.Dimensions.IconMedium),
            )
            Spacer(modifier = Modifier.width(QuantiveExpressiveTokens.Spacing.TextSpacing))
        }

        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * Quantive Secondary Button
 * Secondary action button (outlined style)
 */
@Composable
fun QuantiveSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(QuantiveDesignTokens.Dimensions.ButtonHeightLarge),
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary,
        ),
        shape = RoundedCornerShape(QuantiveDesignTokens.Radius.Medium),
        contentPadding = PaddingValues(
            horizontal = QuantiveDesignTokens.Spacing.Medium,
            vertical = QuantiveDesignTokens.Spacing.Small,
        ),
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(QuantiveDesignTokens.Dimensions.IconMedium),
            )
            Spacer(modifier = Modifier.width(QuantiveDesignTokens.Spacing.Small))
        }

        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * Quantive Text Button
 * Tertiary action button (text only)
 */
@Composable
fun QuantiveTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.height(QuantiveDesignTokens.Dimensions.ButtonHeightLarge),
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        contentPadding = PaddingValues(
            horizontal = QuantiveDesignTokens.Spacing.Medium,
            vertical = QuantiveDesignTokens.Spacing.Small,
        ),
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(QuantiveDesignTokens.Dimensions.IconMedium),
            )
            Spacer(modifier = Modifier.width(QuantiveDesignTokens.Spacing.Small))
        }

        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * Quantive Input Field
 * Text input with consistent styling
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuantiveTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    error: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    isPassword: Boolean = false,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = placeholder?.let { { Text(it) } },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = error != null,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            maxLines = maxLines,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(QuantiveDesignTokens.Radius.Medium),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,
            ),
        )

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(
                    start = QuantiveDesignTokens.Spacing.Medium,
                    top = QuantiveDesignTokens.Spacing.Tiny,
                ),
            )
        }
    }
}

/**
 * Quantive Currency Display - Backend Driven
 * Displays pre-formatted currency from backend Money objects
 */
@Composable
fun QuantiveCurrencyText(
    money: za.co.quantive.app.domain.entities.Money,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = QuantiveTheme.extendedColors.currency,
) {
    Text(
        text = money.formattedAmount ?: "R 0.00", // Backend provides formatted string
        modifier = modifier,
        style = style,
        color = color,
    )
}

/**
 * Legacy currency display - DEPRECATED
 * Use Money object version above
 */
@Deprecated("Use Money object version instead - backend provides formatting")
@Composable
fun QuantiveCurrencyText(
    amount: Double,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = QuantiveTheme.extendedColors.currency,
) {
    val formattedAmount = (amount * 100).toInt() / 100.0 // Round to 2 decimal places
    Text(
        text = "R $formattedAmount", // Fallback formatting only
        modifier = modifier,
        style = style,
        color = color,
    )
}

/**
 * Quantive Status Badge
 * Colored badge for status indication
 */
@Composable
fun QuantiveStatusBadge(
    text: String,
    backgroundColor: Color,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(QuantiveDesignTokens.Radius.Small),
            )
            .padding(
                horizontal = QuantiveDesignTokens.Spacing.Small,
                vertical = QuantiveDesignTokens.Spacing.Tiny,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Quantive Section Header
 * Consistent section headers with accessibility
 */
@Composable
fun QuantiveSectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.semantics { heading() },
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = QuantiveDesignTokens.Spacing.Tiny),
                )
            }
        }

        if (action != null) {
            action()
        }
    }
}

/**
 * Quantive Expressive Loading Indicator
 * Enhanced loading with spring animation
 */
@Composable
fun QuantiveExpressiveLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: androidx.compose.ui.unit.Dp = 4.dp,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = QuantiveExpressiveTokens.Motion.ExpressiveMedium,
                easing = QuantiveExpressiveTokens.Motion.ExpressiveEaseInOut,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
    )

    CircularProgressIndicator(
        modifier = modifier.scale(scale),
        color = color,
        strokeWidth = strokeWidth,
    )
}

/**
 * Quantive Loading State with expressive animation
 * Consistent loading indicator with bouncy motion
 */
@Composable
fun QuantiveLoadingState(
    message: String = "Loading...",
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            QuantiveExpressiveLoadingIndicator(
                modifier = Modifier.size(QuantiveDesignTokens.Dimensions.IconLarge),
                color = QuantiveExpressiveTokens.Colors.ExpressivePrimary,
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = QuantiveExpressiveTokens.Spacing.ComponentSpacing),
            )
        }
    }
}

/**
 * Quantive Empty State
 * Consistent empty state display
 */
@Composable
fun QuantiveEmptyState(
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(QuantiveDesignTokens.Dimensions.IconXLarge)
                        .padding(bottom = QuantiveDesignTokens.Spacing.Medium),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = QuantiveDesignTokens.Spacing.Small),
                )
            }

            if (actionText != null && onActionClick != null) {
                QuantivePrimaryButton(
                    text = actionText,
                    onClick = onActionClick,
                    modifier = Modifier.padding(top = QuantiveDesignTokens.Spacing.Large),
                )
            }
        }
    }
}

/**
 * Quantive Button Group - Material 3 Expressive connected buttons
 */
data class ButtonGroupItem(
    val text: String,
    val value: String,
    val enabled: Boolean = true,
)

@Composable
fun QuantiveButtonGroup(
    items: List<ButtonGroupItem>,
    selectedValue: String?,
    onSelectionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(modifier = modifier) {
        items.forEachIndexed { index, item ->
            val isSelected = item.value == selectedValue
            val isFirst = index == 0
            val isLast = index == items.lastIndex

            val shape = when {
                isFirst -> QuantiveExpressiveTokens.Shapes.ConnectedStart
                isLast -> QuantiveExpressiveTokens.Shapes.ConnectedEnd
                else -> QuantiveExpressiveTokens.Shapes.ConnectedMiddle
            }

            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()

            val animatedScale by animateFloatAsState(
                targetValue = if (isPressed && item.enabled && enabled) {
                    QuantiveExpressiveTokens.InteractionStates.PressedScale
                } else {
                    1f
                },
                animationSpec = QuantiveExpressiveTokens.Motion.SpringBouncy,
            )

            val containerColor by animateColorAsState(
                targetValue = when {
                    isSelected -> QuantiveExpressiveTokens.Colors.ExpressivePrimary
                    isPressed && item.enabled && enabled -> QuantiveExpressiveTokens.Colors.ExpressivePrimaryLight
                    else -> MaterialTheme.colorScheme.surface
                },
                animationSpec = tween(
                    durationMillis = QuantiveExpressiveTokens.Motion.ExpressiveShort,
                    easing = QuantiveExpressiveTokens.Motion.ExpressiveEaseOut,
                ),
            )

            val contentColor by animateColorAsState(
                targetValue = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            )

            Button(
                onClick = { onSelectionChange(item.value) },
                modifier = Modifier
                    .weight(1f)
                    .height(QuantiveDesignTokens.Dimensions.ButtonHeightLarge)
                    .scale(animatedScale),
                enabled = item.enabled && enabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor,
                    contentColor = contentColor,
                ),
                shape = shape,
                border = if (!isSelected) {
                    androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                    )
                } else {
                    null
                },
                interactionSource = interactionSource,
            ) {
                Text(
                    text = item.text,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/**
 * Quantive Split Button - Material 3 Expressive split action button
 */
@Composable
fun QuantiveSplitButton(
    primaryText: String,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    dropdownIcon: ImageVector = Icons.Default.KeyboardArrowDown,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPrimaryPressed by interactionSource.collectIsPressedAsState()

    val secondaryInteractionSource = remember { MutableInteractionSource() }
    val isSecondaryPressed by secondaryInteractionSource.collectIsPressedAsState()

    val primaryScale by animateFloatAsState(
        targetValue = if (isPrimaryPressed && enabled) {
            QuantiveExpressiveTokens.InteractionStates.PressedScale
        } else {
            1f
        },
        animationSpec = QuantiveExpressiveTokens.Motion.SpringBouncy,
    )

    val secondaryScale by animateFloatAsState(
        targetValue = if (isSecondaryPressed && enabled) {
            QuantiveExpressiveTokens.InteractionStates.PressedScale
        } else {
            1f
        },
        animationSpec = QuantiveExpressiveTokens.Motion.SpringBouncy,
    )

    Row(modifier = modifier) {
        // Primary Button
        Button(
            onClick = onPrimaryClick,
            modifier = Modifier
                .weight(1f)
                .height(QuantiveDesignTokens.Dimensions.ButtonHeightLarge)
                .scale(primaryScale),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = QuantiveExpressiveTokens.Colors.ExpressivePrimary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            shape = QuantiveExpressiveTokens.Shapes.ConnectedStart,
            interactionSource = interactionSource,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(QuantiveDesignTokens.Dimensions.IconMedium),
                )
                Spacer(modifier = Modifier.width(QuantiveExpressiveTokens.Spacing.TextSpacing))
            }

            Text(
                text = primaryText,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        // Secondary Button (Dropdown)
        Button(
            onClick = onSecondaryClick,
            modifier = Modifier
                .width(48.dp)
                .height(QuantiveDesignTokens.Dimensions.ButtonHeightLarge)
                .scale(secondaryScale),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = QuantiveExpressiveTokens.Colors.ExpressivePrimaryVariant,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            shape = QuantiveExpressiveTokens.Shapes.ConnectedEnd,
            contentPadding = PaddingValues(0.dp),
            interactionSource = secondaryInteractionSource,
        ) {
            Icon(
                imageVector = dropdownIcon,
                contentDescription = "More options",
                modifier = Modifier.size(QuantiveDesignTokens.Dimensions.IconMedium),
            )
        }
    }
}

/**
 * Quantive Floating Action Button with expressive motion
 */
@Composable
fun QuantiveFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String? = null,
    containerColor: Color = QuantiveExpressiveTokens.Colors.ExpressivePrimary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    expressiveMotion: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedElevation by animateDpAsState(
        targetValue = if (isPressed) {
            QuantiveExpressiveTokens.Elevation.FABPressed
        } else {
            QuantiveExpressiveTokens.Elevation.FABResting
        },
        animationSpec = tween(
            durationMillis = QuantiveExpressiveTokens.Motion.ExpressiveShort,
            easing = QuantiveExpressiveTokens.Motion.ExpressiveEaseOut,
        ),
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed && expressiveMotion) {
            QuantiveExpressiveTokens.InteractionStates.PressedScale
        } else {
            1f
        },
        animationSpec = QuantiveExpressiveTokens.Motion.SpringBouncy,
    )

    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.scale(animatedScale),
        shape = QuantiveExpressiveTokens.Shapes.PlayfulPill,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = animatedElevation,
            pressedElevation = QuantiveExpressiveTokens.Elevation.FABPressed,
        ),
        interactionSource = interactionSource,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
        )
    }
}

/**
 * ⚠️ REMOVED - Client-side currency formatting
 * Backend now provides pre-formatted currency strings via Money.formattedAmount
 * Use QuantiveCurrencyText(money: Money) instead
 */
