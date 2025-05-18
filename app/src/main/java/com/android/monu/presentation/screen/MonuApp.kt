package com.android.monu.presentation.screen

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.monu.R
import com.android.monu.presentation.screen.analytics.AnalyticsScreen
import com.android.monu.presentation.screen.home.HomeScreen
import com.android.monu.presentation.screen.reports.ReportsScreen
import com.android.monu.presentation.screen.reports.detail.ReportsDetailScreen
import com.android.monu.presentation.screen.settings.SettingsScreen
import com.android.monu.presentation.screen.transactions.TransactionsScreen
import com.android.monu.presentation.screen.transactions.TransactionsViewModel
import com.android.monu.presentation.screen.transactions.transaction.AddExpenseScreen
import com.android.monu.presentation.screen.transactions.transaction.AddExpenseViewModel
import com.android.monu.presentation.screen.transactions.transaction.AddIncomeScreen
import com.android.monu.presentation.screen.transactions.transaction.AddIncomeViewModel
import com.android.monu.presentation.screen.transactions.transaction.EditTransactionScreen
import com.android.monu.ui.navigation.BottomNavItem
import com.android.monu.ui.navigation.Screen
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.LightGrey
import com.android.monu.ui.theme.SoftGrey
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel

@Composable
fun MonuApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val systemUiController = rememberSystemUiController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val mainMenuRoute = listOf(
        Screen.Home.route,
        Screen.Transactions.route,
        Screen.Reports.route,
        Screen.Analytics.route
    )

    SideEffect {
        systemUiController.setStatusBarColor(
            color = LightGrey,
            darkIcons = true
        )
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (currentRoute in mainMenuRoute) {
                Column {
                    HorizontalDivider(color = SoftGrey, thickness = 1.dp)
                    BottomNavBar(navController)
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToSettings = { navController.navigate(Screen.Settings.route) },
                    navigateToTransactions = {
                        navController.navigate(Screen.Transactions.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Screen.Transactions.route) {
                val viewModel = koinViewModel<TransactionsViewModel>()
                val transactions = viewModel.filteredPagingData.collectAsLazyPagingItems()
                val searchQuery by viewModel.queryFilter.collectAsStateWithLifecycle()
                val selectedType by viewModel.selectedTypeFilter.collectAsStateWithLifecycle()
                val selectedYear by viewModel.selectedYearFilter.collectAsStateWithLifecycle()
                val selectedMonth by viewModel.selectedMonthFilter.collectAsStateWithLifecycle()

                TransactionsScreen(
                    transactions = transactions,
                    searchQuery = searchQuery,
                    selectedType = selectedType,
                    selectedYear = selectedYear,
                    selectedMonth = selectedMonth,
                    onSearchQueryChange = viewModel::searchTransactions,
                    onFilterTypeClick = viewModel::selectType,
                    onFilterYearMonthApply = viewModel::selectYearAndMonth,
                    navigateToAddIncome = { navController.navigate(Screen.AddIncome.route) },
                    navigateToAddExpense = { navController.navigate(Screen.AddExpense.route) },
                    navigateToEditTransaction = { navController.navigate(Screen.EditTransaction.route) }
                )
            }
            composable(Screen.Reports.route) {
                ReportsScreen(
                    navigateToDetail = { navController.navigate(Screen.ReportDetail.route) }
                )
            }
            composable(Screen.Analytics.route) { AnalyticsScreen() }
            composable(
                route = Screen.Settings.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) {
                SettingsScreen(
                    navigateBack = { navController.navigateUp() }
                )
            }
            composable(
                route = Screen.AddIncome.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) {
                val viewModel = koinViewModel<AddIncomeViewModel>()
                val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
                val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
                val insertResult by viewModel.insertResult.collectAsStateWithLifecycle()

                AddIncomeScreen(
                    category = selectedCategory,
                    date = selectedDate,
                    insertResult = insertResult,
                    navigateBack = { navController.navigateUp() },
                    onResetInsertResultValue = { viewModel.resetInsertResult() },
                    onCategoryChange = viewModel::selectCategory,
                    onDateChange = viewModel::selectDate,
                    onSaveButtonClick = viewModel::insertTransaction
                )
            }
            composable(
                route = Screen.AddExpense.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) {
                val viewModel = koinViewModel<AddExpenseViewModel>()
                val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
                val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
                val budgetingId by viewModel.selectedBudgetingId.collectAsStateWithLifecycle()
                val budgetingTitle by viewModel.selectedBudgetingTitle.collectAsStateWithLifecycle()
                val insertResult by viewModel.insertResult.collectAsStateWithLifecycle()

                AddExpenseScreen(
                    category = selectedCategory,
                    date = selectedDate,
                    budgetingId = budgetingId,
                    budgetingTitle = budgetingTitle,
                    insertResult = insertResult,
                    navigateBack = { navController.navigateUp() },
                    onResetInsertResultValue = { viewModel.resetInsertResult() },
                    onCategoryChange = viewModel::selectCategory,
                    onDateChange = viewModel::selectDate,
                    onSaveButtonClick = viewModel::insertTransaction
                )
            }
            composable(
                route = Screen.EditTransaction.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) {
                EditTransactionScreen(
                    navigateBack = { navController.navigateUp() }
                )
            }
            composable(
                route = Screen.ReportDetail.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) {
                ReportsDetailScreen(
                    navigateBack = { navController.navigateUp() }
                )
            }
        }
    }
}

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
                screen = Screen.Home
            ),
            BottomNavItem(
                title = stringResource(R.string.transactions_menu),
                filledIcon = painterResource(R.drawable.ic_receipt_filled),
                outlinedIcon = painterResource(R.drawable.ic_receipt_outlined),
                screen = Screen.Transactions
            ),
            BottomNavItem(
                title = stringResource(R.string.reports_menu),
                filledIcon = painterResource(R.drawable.ic_order_filled),
                outlinedIcon = painterResource(R.drawable.ic_order_outlined),
                screen = Screen.Reports
            ),
            BottomNavItem(
                title = stringResource(R.string.analytics_menu),
                filledIcon = painterResource(R.drawable.ic_chart_filled),
                outlinedIcon = painterResource(R.drawable.ic_chart_outlined),
                screen = Screen.Analytics
            )
        )

        navigationItems.map { item ->
            val selected = currentRoute == item.screen.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.screen.route) {
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
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Blue,
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MonuAppPreview() {
    MonuApp()
}