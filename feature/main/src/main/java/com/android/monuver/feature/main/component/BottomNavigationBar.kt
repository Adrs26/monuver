package com.android.monuver.feature.main.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.PieChartOutline
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.android.monuver.core.presentation.navigation.Main
import com.android.monuver.feature.main.R

@Composable
fun MainNavigationBar(
    navController: NavHostController,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        MainNavigationDestination.entries.forEachIndexed { _, destination ->
            val selected = isItemSelected(currentRoute, destination.route.toString())
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                        launchSingleTop = true
                    }
                    onNavigate(destination.route.toString())
                },
                icon = {
                    Icon(
                        imageVector = if (selected) destination.selectedIcon else destination.icon,
                        contentDescription = stringResource(destination.title)
                    )
                },
                label = {
                    Text(
                        text = stringResource(destination.title),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (selected) MaterialTheme.colorScheme.primary else
                                MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondary,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

enum class MainNavigationDestination(
    val title: Int,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val route: Any
) {
    HOME(
        title = R.string.home_menu,
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        route = Main.Home
    ),
    TRANSACTION(
        title = R.string.transaction_menu,
        icon = Icons.Outlined.Receipt,
        selectedIcon = Icons.Filled.Receipt,
        route = Main.Transaction
    ),
    BUDGETING(
        title = R.string.budgeting_menu,
        icon = Icons.Outlined.PieChartOutline,
        selectedIcon = Icons.Filled.PieChart,
        route = Main.Budgeting
    ),
    ANALYTICS(
        title = R.string.analytics_menu,
        icon = Icons.Outlined.InsertChart,
        selectedIcon = Icons.Filled.InsertChart,
        route = Main.Analytics
    )
}

private fun isItemSelected(currentRoute: String?, itemRoute: String): Boolean {
    if (currentRoute == null) return false
    return currentRoute.contains(itemRoute, ignoreCase = true)
}