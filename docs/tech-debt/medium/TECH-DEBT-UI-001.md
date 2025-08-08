# TECH-DEBT-UI-001: Implement Material 3 Expressive Design System

**Status**: Open  
**Priority**: Medium  
**Domain**: UI/UX Design  
**Effort Estimate**: 2-3 weeks  
**Assigned Agent**: KMP UX Designer  
**Created**: 2025-01-08  

## Problem Description

The application lacks a comprehensive Material 3 Expressive design system as required by the enterprise blueprint. Current UI implementation uses basic Material Design without the expressive theming, advanced components, and design tokens needed for enterprise-grade applications.

**Current Design Issues**:
- No Material 3 Expressive theme implementation
- Missing design tokens and semantic color system
- No typography scale following Material 3 guidelines
- Basic component styling without expressive elements
- No dark theme support
- No accessibility considerations in design system

## Blueprint Violation

**Blueprint Requirement**: "UI Framework: Compose Multiplatform with Material 3 Expressive design" and comprehensive design system:
- Material 3 Expressive: Advanced theming with expressive elements
- Design tokens: Semantic color, typography, spacing systems
- Accessibility: WCAG 2.1 AA compliance
- Responsive design: Adaptive layouts for different screen sizes
- Theme customization: Light/dark themes with brand colors

**Current State**: Basic Material Design implementation without expressive elements

## Affected Files

### Theme and Design System Files (Need Creation)
- `src/commonMain/kotlin/za/co/quantive/app/ui/theme/QuantiveTheme.kt`
- `src/commonMain/kotlin/za/co/quantive/app/ui/theme/Color.kt`
- `src/commonMain/kotlin/za/co/quantive/app/ui/theme/Typography.kt`
- `src/commonMain/kotlin/za/co/quantive/app/ui/theme/Spacing.kt`
- `src/commonMain/kotlin/za/co/quantive/app/ui/theme/Shapes.kt`

### Component Library (Need Creation)
- `src/commonMain/kotlin/za/co/quantive/app/ui/components/buttons/`
- `src/commonMain/kotlin/za/co/quantive/app/ui/components/cards/`
- `src/commonMain/kotlin/za/co/quantive/app/ui/components/forms/`
- `src/commonMain/kotlin/za/co/quantive/app/ui/components/navigation/`

### Current UI Files (Need Updates)
- `/composeApp/src/commonMain/kotlin/za/co/quantive/app/presentation/QuantiveApp.kt`
- All existing screen composables

## Risk Assessment

- **User Experience Risk**: Medium - Inconsistent UI impacts usability
- **Brand Risk**: Medium - No cohesive visual identity
- **Accessibility Risk**: High - Non-compliant design excludes users
- **Development Velocity Risk**: Medium - No reusable components slow development

## Acceptance Criteria

### Material 3 Expressive Theme
- [ ] Implement Material 3 color system with expressive palettes
- [ ] Create semantic color tokens for business contexts
- [ ] Add dynamic color support for Android 12+
- [ ] Implement comprehensive light and dark themes
- [ ] Add brand color customization system

### Typography and Spacing
- [ ] Implement Material 3 typography scale
- [ ] Create semantic typography tokens (headings, body, captions)
- [ ] Define consistent spacing system (4dp grid)
- [ ] Add responsive typography for different screen sizes
- [ ] Implement accessibility-friendly font sizes

### Component Library
- [ ] Create reusable button components with expressive styling
- [ ] Implement card components for data display
- [ ] Build form components with validation styling
- [ ] Create navigation components (bottom bar, rail, drawer)
- [ ] Add loading and progress components

### Accessibility Compliance
- [ ] Ensure WCAG 2.1 AA color contrast compliance
- [ ] Add semantic markup for screen readers
- [ ] Implement focus management and keyboard navigation
- [ ] Add accessibility labels and descriptions
- [ ] Test with accessibility tools

## Implementation Strategy

### Phase 1: Core Theme System (Week 1)

#### Color System Implementation
```kotlin
// Color.kt - Material 3 Expressive Colors
object QuantiveColors {
    // Primary brand colors
    val QuantiveBlue = Color(0xFF1E3A8A)
    val QuantiveBlueVariant = Color(0xFF3B82F6)
    val QuantiveGreen = Color(0xFF059669)
    val QuantiveGreenVariant = Color(0xFF10B981)
    
    // Business context colors
    val Revenue = Color(0xFF059669)    // Green for positive revenue
    val Expense = Color(0xFFDC2626)    // Red for expenses
    val Pending = Color(0xFFF59E0B)    // Amber for pending items
    val Overdue = Color(0xFFEF4444)    // Red for overdue items
    val Paid = Color(0xFF10B981)       // Green for paid items
    
    // Semantic color tokens
    val Success = Color(0xFF059669)
    val Warning = Color(0xFFF59E0B)
    val Error = Color(0xFFDC2626)
    val Info = Color(0xFF3B82F6)
}

// Light theme color scheme
val LightQuantiveColorScheme = lightColorScheme(
    primary = QuantiveColors.QuantiveBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBeafe),
    onPrimaryContainer = Color(0xFF1E3A8A),
    
    secondary = QuantiveColors.QuantiveGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCFCE7),
    onSecondaryContainer = Color(0xFF059669),
    
    tertiary = Color(0xFF7C3AED),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFEDE9FE),
    onTertiaryContainer = Color(0xFF5B21B6),
    
    error = QuantiveColors.Error,
    onError = Color.White,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFFDC2626),
    
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    scrim = Color(0xFF000000)
)

// Dark theme color scheme
val DarkQuantiveColorScheme = darkColorScheme(
    primary = Color(0xFF60A5FA),
    onPrimary = Color(0xFF1E3A8A),
    primaryContainer = Color(0xFF1E40AF),
    onPrimaryContainer = Color(0xFFDBEAFE),
    
    secondary = Color(0xFF34D399),
    onSecondary = Color(0xFF059669),
    secondaryContainer = Color(0xFF047857),
    onSecondaryContainer = Color(0xFFDCFCE7),
    
    tertiary = Color(0xFFA855F7),
    onTertiary = Color(0xFF5B21B6),
    tertiaryContainer = Color(0xFF6D28D9),
    onTertiaryContainer = Color(0xFFEDE9FE),
    
    error = Color(0xFFEF4444),
    onError = Color(0xFFDC2626),
    errorContainer = Color(0xFFB91C1C),
    onErrorContainer = Color(0xFFFFECEE),
    
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFE2E8F0),
    surface = Color(0xFF0F172A),
    onSurface = Color(0xFFE2E8F0),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFF94A3B8),
    
    outline = Color(0xFF64748B),
    outlineVariant = Color(0xFF334155),
    scrim = Color(0xFF000000)
)
```

#### Typography System
```kotlin
// Typography.kt - Material 3 Typography Scale
val QuantiveTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
```

### Phase 2: Component Library (Week 2)

#### Button Components
```kotlin
// buttons/QuantiveButton.kt
@Composable
fun QuantiveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ButtonVariant = ButtonVariant.Primary,
    size: ButtonSize = ButtonSize.Medium,
    loading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    val buttonColors = when (variant) {
        ButtonVariant.Primary -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
        ButtonVariant.Secondary -> ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        )
        ButtonVariant.Success -> ButtonDefaults.buttonColors(
            containerColor = QuantiveColors.Success,
            contentColor = Color.White
        )
        ButtonVariant.Error -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        )
    }
    
    val buttonShape = when (size) {
        ButtonSize.Small -> RoundedCornerShape(8.dp)
        ButtonSize.Medium -> RoundedCornerShape(12.dp)
        ButtonSize.Large -> RoundedCornerShape(16.dp)
    }
    
    val contentPadding = when (size) {
        ButtonSize.Small -> PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ButtonSize.Medium -> PaddingValues(horizontal = 24.dp, vertical = 12.dp)
        ButtonSize.Large -> PaddingValues(horizontal = 32.dp, vertical = 16.dp)
    }
    
    Button(
        onClick = onClick,
        modifier = modifier.semantics {
            if (loading) {
                contentDescription = "Loading"
            }
        },
        enabled = enabled && !loading,
        colors = buttonColors,
        shape = buttonShape,
        contentPadding = contentPadding
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            leadingIcon?.let { icon ->
                icon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            
            content()
            
            trailingIcon?.let { icon ->
                Spacer(modifier = Modifier.width(8.dp))
                icon()
            }
        }
    }
}

enum class ButtonVariant {
    Primary, Secondary, Success, Error
}

enum class ButtonSize {
    Small, Medium, Large
}
```

#### Card Components
```kotlin
// cards/QuantiveCard.kt
@Composable
fun QuantiveCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    colors: CardColors = CardDefaults.cardColors(),
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.then(
            if (onClick != null) {
                Modifier.clickable { onClick() }
            } else Modifier
        ),
        elevation = elevation,
        colors = colors,
        border = border,
        content = content
    )
}

@Composable
fun InvoiceCard(
    invoice: Invoice,
    onCardClick: () -> Unit,
    onStatusClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    QuantiveCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onCardClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = invoice.number,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                StatusChip(
                    status = invoice.status,
                    onClick = onStatusClick
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = invoice.customer.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatCurrency(invoice.total.amount),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = formatDate(invoice.dueDate),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (invoice.isOverdue) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}
```

### Phase 3: Theme Integration (Week 3)

#### Main Theme Implementation
```kotlin
// QuantiveTheme.kt
@Composable
fun QuantiveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) 
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkQuantiveColorScheme
        else -> LightQuantiveColorScheme
    }
    
    val shapes = Shapes(
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp),
        extraLarge = RoundedCornerShape(28.dp)
    )
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = QuantiveTypography,
        shapes = shapes,
        content = content
    )
}

// Theme extensions for business contexts
@Composable
fun statusColor(status: InvoiceStatus): Color {
    return when (status) {
        InvoiceStatus.PAID -> QuantiveColors.Paid
        InvoiceStatus.PENDING -> QuantiveColors.Pending
        InvoiceStatus.OVERDUE -> QuantiveColors.Overdue
        InvoiceStatus.DRAFT -> MaterialTheme.colorScheme.outline
    }
}

// Accessibility helpers
@Composable
fun isHighContrastMode(): Boolean {
    // Platform-specific implementation for high contrast detection
    return false // Placeholder
}

@Composable
fun accessibleColors(): Boolean {
    return isHighContrastMode()
}
```

## Dependencies

- **Material 3 Compose**: Latest version with expressive theming support
- **Platform-specific**: Dark theme detection and dynamic colors
- **Accessibility services**: Screen reader and contrast detection

## Related Issues

- TECH-DEBT-QA-001 (Testing infrastructure - UI testing needs design system)
- TECH-DEBT-ARCH-001 (ViewModels - proper state management for theme)
- All other tech debt items (consistent UI across all features)

## Testing Strategy

### Visual Regression Testing
```kotlin
@Test
fun materialThemeScreenshots() {
    composeRule.setContent {
        QuantiveTheme {
            // Test light theme
            TestScreen()
        }
    }
    composeRule.onRoot().captureScreenshot("theme_light")
    
    composeRule.setContent {
        QuantiveTheme(darkTheme = true) {
            // Test dark theme
            TestScreen()
        }
    }
    composeRule.onRoot().captureScreenshot("theme_dark")
}
```

### Accessibility Testing
```kotlin
@Test
fun colorContrastCompliance() {
    val lightScheme = LightQuantiveColorScheme
    
    // Test primary color contrast
    val contrast = calculateContrast(
        lightScheme.primary, 
        lightScheme.onPrimary
    )
    
    assertTrue("Primary color contrast ratio $contrast < 4.5", contrast >= 4.5)
}
```

## Success Metrics

- [ ] Material 3 Expressive theme fully implemented
- [ ] WCAG 2.1 AA color contrast compliance achieved (>4.5:1 ratio)
- [ ] Component library provides 80% of needed UI components
- [ ] Dark theme support with proper color mappings
- [ ] Design consistency score >90% across all screens
- [ ] Performance impact <5% due to theming overhead

## Definition of Done

- [ ] Complete Material 3 Expressive color system implemented
- [ ] Typography scale following Material 3 guidelines
- [ ] Comprehensive component library created
- [ ] Light and dark theme support functional
- [ ] Accessibility compliance achieved (WCAG 2.1 AA)
- [ ] Visual regression testing setup
- [ ] Design tokens documented and standardized
- [ ] All existing screens updated to use new theme
- [ ] Code review completed by UX designer
- [ ] Design system documentation created