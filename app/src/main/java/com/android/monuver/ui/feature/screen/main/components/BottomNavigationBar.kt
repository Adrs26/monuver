package com.android.monuver.ui.feature.screen.main.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.android.monuver.R
import com.android.monuver.ui.navigation.Main

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

        MainNavigationDestination.entries.forEachIndexed { index, destination ->
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
                        painter = if (selected) painterResource(destination.selectedIcon) else
                            painterResource(destination.icon),
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
    val icon: Int,
    val selectedIcon: Int,
    val route: Any
) {
    HOME(
        title = R.string.home_menu,
        icon = R.drawable.ic_home_outlined,
        selectedIcon = R.drawable.ic_home_filled,
        route = Main.Home
    ),
    TRANSACTION(
        title = R.string.transaction_menu,
        icon = R.drawable.ic_receipt_outlined,
        selectedIcon = R.drawable.ic_receipt_filled,
        route = Main.Transaction
    ),
    BUDGETING(
        title = R.string.budgeting_menu,
        icon = R.drawable.ic_budgeting_outlined,
        selectedIcon = R.drawable.ic_budgeting_filled,
        route = Main.Budgeting
    ),
    ANALYTICS(
        title = R.string.analytics_menu,
        icon = R.drawable.ic_analytics_outlined,
        selectedIcon = R.drawable.ic_analytics_filled,
        route = Main.Analytics
    )
}

private fun isItemSelected(currentRoute: String?, itemRoute: String): Boolean {
    if (currentRoute == null) return false
    return currentRoute.contains(itemRoute, ignoreCase = true)
}