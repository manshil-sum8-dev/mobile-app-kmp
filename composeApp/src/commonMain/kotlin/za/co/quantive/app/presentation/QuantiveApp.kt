package za.co.quantive.app.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import za.co.quantive.app.app.AppServices
import za.co.quantive.app.auth.Session
import za.co.quantive.app.presentation.components.*
import za.co.quantive.app.presentation.theme.QuantiveExpressiveTokens
import za.co.quantive.app.presentation.theme.QuantiveDesignTokens
import za.co.quantive.app.security.SecureStore
import kotlinx.coroutines.launch

/**
 * Quantive Main App Content
 * Implements the main navigation and authentication flow
 */
@Composable
fun QuantiveAppContent() {
    var session by remember { mutableStateOf<Session?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var onboardingCompleted by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            session = SecureStore.getSession()
            onboardingCompleted = SecureStore.isOnboardingCompleted()
            isLoading = false
        } catch (e: Exception) {
            loadError = "Failed to load session: ${e.message}"
            isLoading = false
        }
    }

    when {
        isLoading -> QuantiveLoadingState("Initializing Quantive...")
        loadError != null -> QuantiveErrorScreen(
            error = loadError!!,
            onRetry = { 
                loadError = null
                isLoading = true
            }
        )
        session == null -> QuantiveAuthScreen(
            onSignUpSuccess = { newSession ->
                session = newSession
                onboardingCompleted = false
            }
        )
        !onboardingCompleted -> QuantiveOnboardingFlow(
            onOnboardingComplete = {
                scope.launch {
                    SecureStore.setOnboardingCompleted(true)
                    onboardingCompleted = true
                }
            }
        )
        else -> QuantiveMainNavigation(
            session = session!!,
            onLogout = {
                scope.launch {
                    try {
                        AppServices.clearSession()
                        SecureStore.setOnboardingCompleted(false)
                        session = null
                        onboardingCompleted = false
                    } catch (e: Exception) {
                        // Handle logout error
                    }
                }
            }
        )
    }
}

/**
 * Quantive Main Navigation with Bottom Tab Bar
 */
@Composable
fun QuantiveMainNavigation(
    session: Session,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    val navigationItems = listOf(
        NavigationItem(
            title = "Home",
            icon = Icons.Default.Home,
            selectedIcon = Icons.Default.Home
        ),
        NavigationItem(
            title = "Invoices", 
            icon = Icons.Default.List,
            selectedIcon = Icons.Default.List
        ),
        NavigationItem(
            title = "Contacts",
            icon = Icons.Default.Person,
            selectedIcon = Icons.Default.Person
        ),
        NavigationItem(
            title = "More",
            icon = Icons.Default.Menu,
            selectedIcon = Icons.Default.Menu
        )
    )

    Scaffold(
        bottomBar = {
            QuantiveBottomNavigation(
                items = navigationItems,
                selectedIndex = selectedTab,
                onItemSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> QuantiveDashboardScreen(session = session)
                1 -> QuantiveInvoicesScreen()
                2 -> QuantiveContactsScreen()
                3 -> QuantiveMoreScreen(onLogout = onLogout)
            }
        }
    }
}

/**
 * Navigation Item data class
 */
data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

/**
 * Quantive Bottom Navigation Component
 */
@Composable
fun QuantiveBottomNavigation(
    items: List<NavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        modifier = Modifier.height(QuantiveDesignTokens.Dimensions.BottomNavigationHeight),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = QuantiveDesignTokens.Elevation.Small
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (selectedIndex == index) item.selectedIcon else item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

/**
 * Placeholder Dashboard Screen
 */
@Composable
fun QuantiveDashboardScreen(session: Session) {
    var userProfile by remember { mutableStateOf<Pair<String, String>?>(null) }
    
    LaunchedEffect(Unit) {
        userProfile = SecureStore.getUserProfile()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(QuantiveDesignTokens.Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(QuantiveDesignTokens.Spacing.Medium)
    ) {
        // Welcome Section
        QuantiveSectionHeader(
            title = if (userProfile != null) "Welcome back, ${userProfile!!.first}!" else "Welcome to Quantive!",
            subtitle = "Your business management dashboard"
        )
        
        // Quick Stats Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(QuantiveDesignTokens.Spacing.Small)
        ) {
            QuantiveCard(
                modifier = Modifier.weight(1f),
                onClick = { /* Navigate to invoices */ }
            ) {
                Text(
                    text = "R 0.00",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Outstanding",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            QuantiveCard(
                modifier = Modifier.weight(1f),
                onClick = { /* Navigate to invoices */ }
            ) {
                Text(
                    text = "0",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Invoices",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Quick Actions with Material 3 Expressive components
        QuantiveSectionHeader(
            title = "Quick Actions",
            subtitle = "Common tasks and shortcuts"
        )
        
        Column(
            verticalArrangement = Arrangement.spacedBy(QuantiveDesignTokens.Spacing.Small)
        ) {
            // Enhanced split button for main invoice action
            QuantiveSplitButton(
                primaryText = "Create Invoice",
                onPrimaryClick = { /* Navigate to invoice creation */ },
                onSecondaryClick = { /* Show invoice options menu */ },
                icon = Icons.Default.Add,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Button group for quick actions
            QuantiveButtonGroup(
                items = listOf(
                    ButtonGroupItem("Contact", "contact"),
                    ButtonGroupItem("Reports", "reports"),
                    ButtonGroupItem("Settings", "settings")
                ),
                selectedValue = null, // No default selection
                onSelectionChange = { action ->
                    when (action) {
                        "contact" -> { /* Navigate to contact creation */ }
                        "reports" -> { /* Navigate to reports */ }
                        "settings" -> { /* Navigate to settings */ }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // Recent Activity Card with enhanced styling
        QuantiveCard(
            modifier = Modifier.fillMaxWidth(),
            expressiveMotion = true
        ) {
            QuantiveSectionHeader(
                title = "Recent Activity",
                subtitle = "Latest business updates"
            )
            
            QuantiveEmptyState(
                title = "No recent activity",
                subtitle = "Start by creating your first invoice",
                icon = Icons.Default.Refresh,
                actionText = "Create Invoice",
                onActionClick = { /* Navigate to invoice creation */ }
            )
        }
        
        // Floating Action Button for quick invoice creation
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            QuantiveFloatingActionButton(
                onClick = { /* Quick create invoice */ },
                icon = Icons.Default.Add,
                contentDescription = "Quick create invoice",
                modifier = Modifier.padding(QuantiveDesignTokens.Spacing.Medium)
            )
        }
    }
}

/**
 * Placeholder Invoices Screen
 */
@Composable
fun QuantiveInvoicesScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        QuantiveEmptyState(
            title = "No invoices yet",
            subtitle = "Create your first invoice to get started",
            icon = Icons.Default.List,
            actionText = "Create Invoice",
            onActionClick = { /* Navigate to invoice creation */ }
        )
    }
}

/**
 * Placeholder Contacts Screen
 */
@Composable
fun QuantiveContactsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        QuantiveEmptyState(
            title = "No contacts yet",
            subtitle = "Add customers and suppliers to manage your business relationships",
            icon = Icons.Default.Person,
            actionText = "Add Contact",
            onActionClick = { /* Navigate to contact creation */ }
        )
    }
}

/**
 * Placeholder More Screen
 */
@Composable
fun QuantiveMoreScreen(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(QuantiveDesignTokens.Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(QuantiveDesignTokens.Spacing.Medium)
    ) {
        QuantiveSectionHeader(
            title = "Settings & More",
            subtitle = "Manage your Quantive experience"
        )
        
        // Settings Options
        Column(
            verticalArrangement = Arrangement.spacedBy(QuantiveDesignTokens.Spacing.Small)
        ) {
            QuantiveCard(
                onClick = { /* Navigate to business profile */ }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = null,
                        modifier = Modifier.padding(end = QuantiveDesignTokens.Spacing.Medium)
                    )
                    Column {
                        Text(
                            text = "Business Profile",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Manage your business information",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            QuantiveCard(
                onClick = { /* Navigate to settings */ }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.padding(end = QuantiveDesignTokens.Spacing.Medium)
                    )
                    Column {
                        Text(
                            text = "App Settings",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Customize your app experience",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Logout Button
        QuantiveSecondaryButton(
            text = "Sign Out",
            onClick = onLogout,
            icon = Icons.Default.ExitToApp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}