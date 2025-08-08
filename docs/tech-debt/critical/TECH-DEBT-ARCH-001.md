# TECH-DEBT-ARCH-001: Implement ViewModel Architecture

**Status**: Open  
**Priority**: Critical  
**Domain**: Architecture  
**Effort Estimate**: 2-3 weeks  
**Assigned Agent**: KMP Mobile Architect  
**Created**: 2025-01-08  

## Problem Description

The application currently lacks proper ViewModel architecture, managing state directly in Composables using `remember` and `mutableStateOf`. This violates the enterprise blueprint's MVVM requirements and leads to poor state management, memory leaks, and testing difficulties.

**Current Issues**:
- No ViewModel implementations found across the codebase
- Direct state management in Composables
- No structured concurrency with lifecycle-aware scopes
- Missing MVI/Redux patterns for state management
- No proper intent handling system

## Blueprint Violation

**Blueprint Requirement**: "ViewModels: State management + UI events" with sealed class UiState management  
**Current State**: Direct state management in UI layer without proper architecture

## Affected Files

- All files in `/composeApp/src/commonMain/kotlin/za/co/quantive/app/presentation/`
- `/composeApp/src/commonMain/kotlin/za/co/quantive/app/presentation/QuantiveApp.kt`
- Any Composable functions managing state directly

## Risk Assessment

- **Architecture Risk**: Critical - Violates fundamental MVVM principles
- **Maintainability Risk**: High - Poor state management patterns
- **Testing Risk**: High - ViewModels required for proper unit testing
- **Performance Risk**: Medium - Potential memory leaks and inefficient recomposition

## Acceptance Criteria

### Base ViewModel Implementation
- [ ] Create `BaseViewModel` abstract class following blueprint template
- [ ] Implement sealed class `UiState` pattern
- [ ] Add `Intent` system for user actions
- [ ] Implement `UiEvent` system for navigation/side effects
- [ ] Use `viewModelScope` for structured concurrency

### Feature-Specific ViewModels
- [ ] Implement `AuthViewModel` for authentication flows
- [ ] Create `InvoiceListViewModel` for invoice management
- [ ] Add `ContactListViewModel` for contact management  
- [ ] Implement `BusinessProfileViewModel` for profile management
- [ ] Create `DashboardViewModel` for overview screen

### Integration Requirements
- [ ] Integrate ViewModels with existing repositories
- [ ] Update Composables to use ViewModel state
- [ ] Implement proper lifecycle management
- [ ] Add ViewModel injection through DI system
- [ ] Remove direct state management from Composables

## Implementation Details

### Base ViewModel Template (from Blueprint)
```kotlin
abstract class BaseViewModel : ViewModel() {
    protected val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    protected val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()
    
    abstract fun createInitialState(): UiState
    abstract fun handleIntent(intent: Intent)
    
    protected fun updateState(update: UiState.() -> UiState) {
        _uiState.value = _uiState.value.update()
    }
    
    protected fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvents.send(event)
        }
    }
}
```

### Example Feature Implementation
```kotlin
sealed class InvoiceListUiState : UiState {
    object Loading : InvoiceListUiState()
    data class Success(val invoices: List<Invoice>) : InvoiceListUiState()
    data class Error(val message: String) : InvoiceListUiState()
}

sealed class InvoiceListIntent : Intent {
    object LoadInvoices : InvoiceListIntent()
    data class DeleteInvoice(val id: String) : InvoiceListIntent()
    object RefreshData : InvoiceListIntent()
}

sealed class InvoiceListUiEvent : UiEvent {
    object NavigateToCreateInvoice : InvoiceListUiEvent()
    data class ShowSnackbar(val message: String) : InvoiceListUiEvent()
}
```

## Dependencies

- Requires ViewModel library addition to dependencies
- Should be implemented alongside TECH-DEBT-ARCH-003 (Koin DI) for proper injection
- Depends on existing repository implementations

## Related Issues

- TECH-DEBT-ARCH-003 (Koin dependency injection)
- TECH-DEBT-QA-001 (Testing infrastructure - needs ViewModels for proper testing)

## Gradle Dependencies Required

```kotlin
// Add to commonMain dependencies
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
```

## Implementation Plan

### Phase 1 (Week 1)
- Create BaseViewModel and common interfaces
- Implement AuthViewModel and integrate with authentication flow
- Update QuantiveApp to use ViewModels

### Phase 2 (Week 2)  
- Implement InvoiceListViewModel and ContactListViewModel
- Create BusinessProfileViewModel
- Update all major screens to use ViewModel pattern

### Phase 3 (Week 3)
- Add comprehensive ViewModel testing
- Optimize state management and performance
- Complete integration and documentation

## Definition of Done

- [ ] All major screens use ViewModel architecture
- [ ] No direct state management in Composables
- [ ] Structured concurrency with viewModelScope
- [ ] Comprehensive ViewModel unit tests
- [ ] Proper lifecycle management implemented
- [ ] Code review completed by architecture specialist
- [ ] Documentation updated with ViewModel patterns
- [ ] Performance testing shows no regressions