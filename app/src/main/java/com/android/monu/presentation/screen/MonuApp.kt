package com.android.monu.presentation.screen

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.monu.presentation.components.BottomNavBar
import com.android.monu.presentation.screen.analytics.AnalyticsScreen
import com.android.monu.presentation.screen.home.HomeScreen
import com.android.monu.presentation.screen.reports.ReportsScreen
import com.android.monu.presentation.screen.reports.ReportsViewModel
import com.android.monu.presentation.screen.reports.detail.ReportsDetailScreen
import com.android.monu.presentation.screen.settings.SettingsScreen
import com.android.monu.presentation.screen.transactions.FilterCallbacks
import com.android.monu.presentation.screen.transactions.FilterStateData
import com.android.monu.presentation.screen.transactions.TransactionsScreen
import com.android.monu.presentation.screen.transactions.TransactionsViewModel
import com.android.monu.presentation.screen.transactions.transaction.AddExpenseScreen
import com.android.monu.presentation.screen.transactions.transaction.AddExpenseViewModel
import com.android.monu.presentation.screen.transactions.transaction.AddIncomeScreen
import com.android.monu.presentation.screen.transactions.transaction.AddIncomeViewModel
import com.android.monu.presentation.screen.transactions.transaction.EditTransactionScreen
import com.android.monu.presentation.screen.transactions.transaction.EditTransactionViewModel
import com.android.monu.ui.navigation.Screen
import com.android.monu.ui.theme.LightGrey
import com.android.monu.ui.theme.SoftGrey
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

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
                val availableYears by viewModel.availableTransactionYears.collectAsStateWithLifecycle()

                val filterState = FilterStateData(
                    searchQuery = searchQuery,
                    selectedType = selectedType,
                    availableTransactionYears = availableYears,
                    selectedYear = selectedYear,
                    selectedMonth = selectedMonth
                )

                val filterCallbacks = FilterCallbacks(
                    onSearchQueryChange = viewModel::searchTransactions,
                    onFilterTypeClick = viewModel::selectType,
                    onFilterYearMonthClick = viewModel::loadAvailableTransactionYears,
                    onFilterYearMonthApply = viewModel::selectYearAndMonth
                )

                TransactionsScreen(
                    transactions = transactions,
                    filterState = filterState,
                    filterCallbacks = filterCallbacks,
                    navigateToAddIncome = { navController.navigate(Screen.AddIncome.route) },
                    navigateToAddExpense = { navController.navigate(Screen.AddExpense.route) },
                    navigateToEditTransaction = { transactionId ->
                        navController.navigate(Screen.EditTransaction.createRoute(transactionId))
                    }
                )
            }
            composable(Screen.Reports.route) {
                val viewModel = koinViewModel<ReportsViewModel>()
                val availableYears by viewModel.availableTransactionYears.collectAsStateWithLifecycle()
                val selectedYear by viewModel.selectedYearFilter.collectAsStateWithLifecycle()

                ReportsScreen(
                    availableYears = availableYears,
                    selectedYear = selectedYear,
                    onFilterClick = viewModel::loadAvailableTransactionYears,
                    onYearFilterSelect = viewModel::selectYear,
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
                val insertResult by viewModel.insertResult.collectAsStateWithLifecycle()

                AddIncomeScreen(
                    insertResult = insertResult,
                    onSaveButtonClick = viewModel::insertTransaction,
                    onResetInsertResultValue = viewModel::resetInsertResult,
                    navigateBack = { navController.navigateUp() },
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
                val insertResult by viewModel.insertResult.collectAsStateWithLifecycle()

                AddExpenseScreen(
                    insertResult = insertResult,
                    onSaveButtonClick = viewModel::insertTransaction,
                    onResetInsertResultValue = viewModel::resetInsertResult,
                    navigateBack = { navController.navigateUp() }
                )
            }
            composable(
                route = Screen.EditTransaction.route,
                arguments = listOf(navArgument("transactionId") { type = NavType.LongType }),
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
            ) { backStackEntry ->
                val viewModel = koinViewModel<EditTransactionViewModel>(
                    viewModelStoreOwner = backStackEntry,
                    parameters = { parametersOf(backStackEntry.savedStateHandle) }
                )
                val transaction by viewModel.transaction.collectAsStateWithLifecycle()
                val updateResult by viewModel.updateResult.collectAsStateWithLifecycle()

                transaction?.let {
                    EditTransactionScreen(
                        transaction = it,
                        updateResult = updateResult,
                        onResetUpdateResultValue = viewModel::resetUpdateResult,
                        onSaveButtonClick = viewModel::updateTransaction,
                        onDeleteClick = viewModel::deleteTransaction,
                        navigateBack = { navController.navigateUp() }
                    )
                }
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

@Preview(showBackground = true)
@Composable
fun MonuAppPreview() {
    MonuApp()
}