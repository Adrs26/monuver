package com.android.monu.presentation.screen.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.android.monu.R
import com.android.monu.ui.navigation.Analytics
import com.android.monu.ui.navigation.Budgeting
import com.android.monu.ui.navigation.Home
import com.android.monu.ui.navigation.Transaction

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        modifier = modifier
            .height(60.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 8.dp),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val navigationItems = listOf(
            BottomNavigationItem(
                title = stringResource(R.string.home_menu),
                filledIcon = painterResource(R.drawable.ic_home_filled),
                outlinedIcon = painterResource(R.drawable.ic_home_outlined),
                destination = Home,
                route = "Home"
            ),
            BottomNavigationItem(
                title = stringResource(R.string.transaction_menu),
                filledIcon = painterResource(R.drawable.ic_receipt_filled),
                outlinedIcon = painterResource(R.drawable.ic_receipt_outlined),
                destination = Transaction,
                route = "Transaction"
            ),
            BottomNavigationItem(
                title = stringResource(R.string.budgeting_menu),
                filledIcon = painterResource(R.drawable.ic_budgeting_filled),
                outlinedIcon = painterResource(R.drawable.ic_budgeting_outlined),
                destination = Budgeting,
                route = "Budgeting"
            ),
            BottomNavigationItem(
                title = stringResource(R.string.analytics_menu),
                filledIcon = painterResource(R.drawable.ic_analytics_filled),
                outlinedIcon = painterResource(R.drawable.ic_analytics_outlined),
                destination = Analytics,
                route = "Analytics"
            )
        )

        navigationItems.map { item ->
            val selected = isItemSelected(currentRoute, item.route)
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.destination) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                        launchSingleTop = true
                    }
                    onNavigate(item.route)
                },
                icon = {
                    Icon(
                        painter = if (selected) item.filledIcon else item.outlinedIcon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (selected) MaterialTheme.colorScheme.primary else
                                MaterialTheme.colorScheme.onSurface
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

private fun isItemSelected(currentRoute: String?, itemRoute: String): Boolean {
    if (currentRoute == null) return false
    return currentRoute.contains(itemRoute, ignoreCase = true)
}

data class BottomNavigationItem(
    val title: String,
    val filledIcon: Painter,
    val outlinedIcon: Painter,
    val destination: Any,
    val route: String
)