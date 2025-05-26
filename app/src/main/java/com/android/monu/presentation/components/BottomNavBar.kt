package com.android.monu.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.android.monu.R
import com.android.monu.ui.navigation.Analytics
import com.android.monu.ui.navigation.BottomNavItem
import com.android.monu.ui.navigation.Home
import com.android.monu.ui.navigation.Reports
import com.android.monu.ui.navigation.Transactions
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.interFontFamily

@Composable
fun BottomNavBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.height(56.dp),
        containerColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val navigationItems = listOf(
            BottomNavItem(
                title = stringResource(R.string.home_menu),
                filledIcon = painterResource(R.drawable.ic_home_filled),
                outlinedIcon = painterResource(R.drawable.ic_home_outlined),
                destination = Home
            ),
            BottomNavItem(
                title = stringResource(R.string.transactions_menu),
                filledIcon = painterResource(R.drawable.ic_receipt_filled),
                outlinedIcon = painterResource(R.drawable.ic_receipt_outlined),
                destination = Transactions
            ),
            BottomNavItem(
                title = stringResource(R.string.reports_menu),
                filledIcon = painterResource(R.drawable.ic_order_filled),
                outlinedIcon = painterResource(R.drawable.ic_order_outlined),
                destination = Reports
            ),
            BottomNavItem(
                title = stringResource(R.string.analytics_menu),
                filledIcon = painterResource(R.drawable.ic_chart_filled),
                outlinedIcon = painterResource(R.drawable.ic_chart_outlined),
                destination = Analytics
            )
        )

        navigationItems.map { item ->
            val selected = currentRoute?.contains(item.destination::class.simpleName ?: "") == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.destination) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                        launchSingleTop = true
                    }
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
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selected) Blue else Color.Gray,
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Blue,
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}

fun shouldShowBottomNav(currentRoute: String?): Boolean {
    return listOf(Home, Transactions, Reports, Analytics).any {
        currentRoute?.contains(it::class.simpleName ?: "") == true
    }
}