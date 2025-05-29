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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.monu.presentation.components.BottomNavBar
import com.android.monu.presentation.components.shouldShowBottomNav
import com.android.monu.presentation.screen.analytics.AnalyticsFilterCallbacks
import com.android.monu.presentation.screen.analytics.AnalyticsFilterState
import com.android.monu.presentation.screen.analytics.AnalyticsScreen
import com.android.monu.presentation.screen.analytics.AnalyticsViewModel
import com.android.monu.presentation.screen.budgeting.BudgetingScreen
import com.android.monu.presentation.screen.budgeting.history.BudgetingHistoryScreen
import com.android.monu.presentation.screen.home.HomeScreen
import com.android.monu.presentation.screen.home.HomeViewModel
import com.android.monu.presentation.screen.report.ReportFilterCallbacks
import com.android.monu.presentation.screen.report.ReportFilterState
import com.android.monu.presentation.screen.report.ReportScreen
import com.android.monu.presentation.screen.report.ReportViewModel
import com.android.monu.presentation.screen.report.detail.ReportsDetailScreen
import com.android.monu.presentation.screen.settings.SettingsScreen
import com.android.monu.presentation.screen.transaction.TransactionFilterCallbacks
import com.android.monu.presentation.screen.transaction.TransactionFilterState
import com.android.monu.presentation.screen.transaction.TransactionScreen
import com.android.monu.presentation.screen.transaction.TransactionsViewModel
import com.android.monu.presentation.screen.transaction.add_expense.AddExpenseScreen
import com.android.monu.presentation.screen.transaction.add_expense.AddExpenseViewModel
import com.android.monu.presentation.screen.transaction.add_income.AddIncomeScreen
import com.android.monu.presentation.screen.transaction.add_income.AddIncomeViewModel
import com.android.monu.presentation.screen.transaction.edit_transaction.EditTransactionScreen
import com.android.monu.presentation.screen.transaction.edit_transaction.EditTransactionViewModel
import com.android.monu.ui.navigation.AddExpense
import com.android.monu.ui.navigation.AddIncome
import com.android.monu.ui.navigation.Analytics
import com.android.monu.ui.navigation.Budgeting
import com.android.monu.ui.navigation.BudgetingHistory
import com.android.monu.ui.navigation.EditTransaction
import com.android.monu.ui.navigation.Home
import com.android.monu.ui.navigation.Report
import com.android.monu.ui.navigation.ReportDetail
import com.android.monu.ui.navigation.Settings
import com.android.monu.ui.navigation.Transaction
import com.android.monu.ui.theme.LightGrey
import com.android.monu.ui.theme.SoftGrey
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MonuApp(
    navController: NavHostController = rememberNavController()
) {
    val systemUiController = rememberSystemUiController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    SideEffect { systemUiController.setStatusBarColor(color = LightGrey, darkIcons = true) }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomNav(currentRoute)) {
                Column {
                    HorizontalDivider(color = SoftGrey, thickness = 1.dp)
                    BottomNavBar(navController)
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Home> {
                val viewModel = koinViewModel<HomeViewModel>()

                val totalIncomeAmount by viewModel.totalIncomeAmount.collectAsStateWithLifecycle()
                val totalExpenseAmount by viewModel.totalExpenseAmount.collectAsStateWithLifecycle()
                val recentTransactions by viewModel.recentTransactions.collectAsStateWithLifecycle()

                HomeScreen(
                    totalIncomeAmount = totalIncomeAmount,
                    totalExpenseAmount = totalExpenseAmount,
                    recentTransactions = recentTransactions,
                    navigateToSettings = { navController.navigate(Settings) },
                    navigateToBudgeting = { navController.navigate(Budgeting) },
                    navigateToTransactions = {
                        navController.navigate(Transaction) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable<Transaction> {
                val viewModel = koinViewModel<TransactionsViewModel>()

                val transactionList = viewModel.transactionList.collectAsLazyPagingItems()
                val searchQuery by viewModel.queryFilter.collectAsStateWithLifecycle()
                val selectedType by viewModel.selectedTypeFilter.collectAsStateWithLifecycle()
                val selectedYear by viewModel.selectedYearFilter.collectAsStateWithLifecycle()
                val selectedMonth by viewModel.selectedMonthFilter.collectAsStateWithLifecycle()
                val availableYears by viewModel.availableTransactionYears.collectAsStateWithLifecycle()

                val filterState = TransactionFilterState(
                    searchQuery = searchQuery,
                    selectedType = selectedType,
                    availableTransactionYears = availableYears,
                    selectedYear = selectedYear,
                    selectedMonth = selectedMonth
                )

                val filterCallbacks = TransactionFilterCallbacks(
                    onSearchQueryChange = viewModel::searchTransactions,
                    onFilterTypeClick = viewModel::selectType,
                    onFilterYearMonthClick = viewModel::loadAvailableTransactionYears,
                    onFilterYearMonthApply = viewModel::selectYearAndMonth
                )

                TransactionScreen(
                    transactionList = transactionList,
                    filterState = filterState,
                    filterCallbacks = filterCallbacks,
                    navigateToAddIncome = { navController.navigate(AddIncome) },
                    navigateToAddExpense = { navController.navigate(AddExpense) },
                    navigateToEditTransaction = { transactionId ->
                        navController.navigate(EditTransaction(transactionId))
                    }
                )
            }
            composable<Report> {
                val viewModel = koinViewModel<ReportViewModel>()

                val listTransactionMonthlyAmount by viewModel
                    .listTransactionMonthlyAmount.collectAsStateWithLifecycle()
                val selectedYear by viewModel.selectedYearFilter.collectAsStateWithLifecycle()
                val availableYears by viewModel.availableTransactionYears.collectAsStateWithLifecycle()

                val filterState = ReportFilterState(
                    selectedYear = selectedYear,
                    availableYears = availableYears
                )

                val filterCallbacks = ReportFilterCallbacks(
                    onFilterClick = viewModel::loadAvailableTransactionYears,
                    onYearFilterSelect = viewModel::selectYear
                )

                ReportScreen(
                    listTransactionMonthlyAmount = listTransactionMonthlyAmount,
                    filterState = filterState,
                    filterCallbacks = filterCallbacks,
                    navigateToDetail = { navController.navigate(ReportDetail) }
                )
            }
            composable<Analytics> {
                val viewModel = koinViewModel<AnalyticsViewModel>()

                val averageTransactionAmount by viewModel
                    .averageTransactionAmount.collectAsStateWithLifecycle()
                val barChartSelectedType by viewModel.
                        barChartSelectedTypeFilter.collectAsStateWithLifecycle()
                val barChartSelectedYear by viewModel.
                        barChartSelectedYearFilter.collectAsStateWithLifecycle()
                val barChartScaleLabels by viewModel.barChartScaleLabels.collectAsStateWithLifecycle()
                val transactionsOverview by viewModel.transactionsOverview.collectAsStateWithLifecycle()
                val pieChartSelectedYear by viewModel.
                        pieChartSelectedYearFilter.collectAsStateWithLifecycle()
                val mostExpenseCategory by viewModel.mostExpenseCategory.collectAsStateWithLifecycle()
                val availableYears by viewModel.availableTransactionYears.collectAsStateWithLifecycle()

                val filterState = AnalyticsFilterState(
                    barChartSelectedYear = barChartSelectedYear,
                    barChartSelectedType = barChartSelectedType,
                    pieChartSelectedYear = pieChartSelectedYear,
                    availableYears = availableYears
                )

                val filterCallbacks = AnalyticsFilterCallbacks(
                    onFilterClick = viewModel::loadAvailableTransactionYears,
                    onBarChartYearFilterSelect = viewModel::selectBarChartYear,
                    onBarChartFilterTypeClick = viewModel::selectType,
                    onPieChartYearFilterSelect = viewModel::selectPieChartYear
                )

                averageTransactionAmount?.let {
                    AnalyticsScreen(
                        averageTransactionAmount = it,
                        filterState = filterState,
                        filterCallbacks = filterCallbacks,
                        transactionsOverview = transactionsOverview,
                        barChartScaleLabels = barChartScaleLabels,
                        mostExpenseCategory = mostExpenseCategory
                    )
                }
            }
            composable<Settings>(
                enterTransition = { NavigationAnimation.enter },
                exitTransition = { NavigationAnimation.exit },
                popEnterTransition = { NavigationAnimation.popEnter },
                popExitTransition = { NavigationAnimation.popExit }
            ) {
                SettingsScreen(
                    navigateBack = { navController.navigateUp() }
                )
            }
            composable<AddIncome>(
                enterTransition = { NavigationAnimation.enter },
                exitTransition = { NavigationAnimation.exit },
                popEnterTransition = { NavigationAnimation.popEnter },
                popExitTransition = { NavigationAnimation.popExit }
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
            composable<AddExpense>(
                enterTransition = { NavigationAnimation.enter },
                exitTransition = { NavigationAnimation.exit },
                popEnterTransition = { NavigationAnimation.popEnter },
                popExitTransition = { NavigationAnimation.popExit }
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
            composable<EditTransaction>(
                enterTransition = { NavigationAnimation.enter },
                exitTransition = { NavigationAnimation.exit },
                popEnterTransition = { NavigationAnimation.popEnter },
                popExitTransition = { NavigationAnimation.popExit }
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
            composable<ReportDetail>(
                enterTransition = { NavigationAnimation.enter },
                exitTransition = { NavigationAnimation.exit },
                popEnterTransition = { NavigationAnimation.popEnter },
                popExitTransition = { NavigationAnimation.popExit }
            ) {
                ReportsDetailScreen(
                    navigateBack = { navController.navigateUp() }
                )
            }
            composable<Budgeting>(
                enterTransition = { NavigationAnimation.enter },
                exitTransition = { NavigationAnimation.exit },
                popEnterTransition = { NavigationAnimation.popEnter },
                popExitTransition = { NavigationAnimation.popExit }
            ) {
                BudgetingScreen(
                    onHistoryClick = { navController.navigate(BudgetingHistory) },
                    navigateBack = { navController.navigateUp() }
                )
            }
            composable<BudgetingHistory>(
                enterTransition = { NavigationAnimation.enter },
                exitTransition = { NavigationAnimation.exit },
                popEnterTransition = { NavigationAnimation.popEnter },
                popExitTransition = { NavigationAnimation.popExit }
            ) {
                BudgetingHistoryScreen(
                    navigateBack = { navController.navigateUp() }
                )
            }
        }
    }
}

private object NavigationAnimation {
    val enter = slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
    val exit = slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
    val popEnter = slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
    val popExit = slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
}

@Preview(showBackground = true)
@Composable
fun MonuAppPreview() {
    MonuApp()
}