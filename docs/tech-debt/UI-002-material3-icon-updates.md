# TECH-DEBT-UI-002: Material 3 Icon Updates

## Overview
**Priority:** Low  
**Category:** UI/UX  
**Assigned Agent:** kmp-ux-designer  
**Estimated Effort:** 1-2 hours  
**Sprint Impact:** None (cosmetic warnings only)

## Description
Update deprecated Material 3 icons to use AutoMirrored versions and fix button border deprecation warnings in Compose components.

## Current Issues
Build warnings for deprecated Material 3 APIs:
- `Icons.Filled.List` → `Icons.AutoMirrored.Filled.List`
- `Icons.Filled.ExitToApp` → `Icons.AutoMirrored.Filled.ExitToApp`
- `outlinedButtonBorder` needs `enabled` parameter

## Impact
- **Functional:** None - app works correctly
- **Build:** Cosmetic deprecation warnings
- **Future:** May break in future Compose updates

## Files Affected
- `composeApp/src/commonMain/kotlin/za/co/quantive/app/presentation/QuantiveApp.kt:101,102,335,438`
- `composeApp/src/commonMain/kotlin/za/co/quantive/app/presentation/components/QuantiveComponents.kt:211,646`

## Acceptance Criteria
- [ ] Replace all deprecated icon references with AutoMirrored versions
- [ ] Update `outlinedButtonBorder` usage to include `enabled` parameter
- [ ] Verify no build warnings for Material 3 deprecations
- [ ] Ensure icon direction is correct for RTL layouts

## Technical Notes
AutoMirrored icons automatically flip for right-to-left languages, improving internationalization support.

## Dependencies
None - standalone UI updates

---
*Created: 2025-08-07*  
*Status: Open*