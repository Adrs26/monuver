package com.android.monu.ui.feature.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.monu.domain.model.TransactionBalanceSummaryState
import com.android.monu.ui.feature.screen.analytics.AnalyticsActions
import com.android.monu.ui.feature.screen.analytics.AnalyticsScreen
import com.android.monu.ui.feature.screen.analytics.AnalyticsState
import com.android.monu.ui.feature.screen.analytics.AnalyticsViewModel
import com.android.monu.ui.feature.screen.budgeting.BudgetActions
import com.android.monu.ui.feature.screen.budgeting.BudgetUiState
import com.android.monu.ui.feature.screen.budgeting.BudgetingScreen
import com.android.monu.ui.feature.screen.budgeting.BudgetingViewModel
import com.android.monu.ui.feature.screen.home.HomeActions
import com.android.monu.ui.feature.screen.home.HomeScreen
import com.android.monu.ui.feature.screen.home.HomeViewModel
import com.android.monu.ui.feature.screen.transaction.TransactionActions
import com.android.monu.ui.feature.screen.transaction.TransactionScreen
import com.android.monu.ui.feature.screen.transaction.TransactionUiState
import com.android.monu.ui.feature.screen.transaction.TransactionViewModel
import com.android.monu.ui.navigation.Account
import com.android.monu.ui.navigation.AddBudget
import com.android.monu.ui.navigation.AddTransaction
import com.android.monu.ui.navigation.AnalyticsCategoryTransaction
import com.android.monu.ui.navigation.Billing
import com.android.monu.ui.navigation.BudgetDetail
import com.android.monu.ui.navigation.InactiveBudget
import com.android.monu.ui.navigation.Main
import com.android.monu.ui.navigation.Saving
import com.android.monu.ui.navigation.Settings
import com.android.monu.ui.navigation.TransactionDetail
import com.android.monu.ui.navigation.Transfer
import com.android.monu.utils.TransactionType
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.mainNavGraph(
    rootNavController: NavHostController,
    mainNavController: NavHostController
) {
    composable<Main.Home> {
        val viewModel = koinViewModel<HomeViewModel>()
        val totalBalance by viewModel.totalAccountBalance.collectAsStateWithLifecycle()
        val recentTransactions by viewModel.recentTransactions.collectAsStateWithLifecycle()
        val budgetSummaryState by viewModel.budgetSummaryState.collectAsStateWithLifecycle()

        val homeActions = object : HomeActions {
            override fun onHandleExpiredBudget() {
                viewModel.handleExpiredBudget()
            }

            override fun onNavigateToBill() {
                rootNavController.navigate(Billing.Main)
            }

            override fun onNavigateToSave() {
                rootNavController.navigate(Saving.Main)
            }

            override fun onNavigateToSettings() {
                rootNavController.navigate(Settings.Main)
            }

            override fun onNavigateToAccount() {
                rootNavController.navigate(Account.Main)
            }

            override fun onNavigateToAddIncomeTransaction() {
                rootNavController.navigate(AddTransaction.Main(TransactionType.INCOME))
            }

            override fun onNavigateToAddExpenseTransaction() {
                rootNavController.navigate(AddTransaction.Main(TransactionType.EXPENSE))
            }

            override fun onNavigateToTransfer() {
                rootNavController.navigate(Transfer.Main)
            }

            override fun onNavigateToAddBudget() {
                rootNavController.navigate(AddBudget.Main)
            }

            override fun onNavigateToTransaction() {
                mainNavController.navigate(Main.Transaction) {
                    popUpTo(mainNavController.graph.startDestinationId) { saveState = true }
                    restoreState = true
                    launchSingleTop = true
                }
            }

            override fun onNavigateToTransactionDetail(transactionId: Long) {
                rootNavController.navigate(TransactionDetail.Main(transactionId))
            }

            override fun onNavigateToBudgeting() {
                mainNavController.navigate(Main.Budgeting) {
                    popUpTo(mainNavController.graph.startDestinationId) { saveState = true }
                    restoreState = true
                    launchSingleTop = true
                }
            }
        }

        if (budgetSummaryState == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            HomeScreen(
                totalBalance = totalBalance,
                recentTransactions = recentTransactions,
                budgetSummaryState = budgetSummaryState!!,
                homeActions = homeActions
            )
        }
    }
    composable<Main.Transaction> {
        val viewModel = koinViewModel<TransactionViewModel>()
        val transactions = viewModel.transactions.collectAsLazyPagingItems()
        val filter by viewModel.filterState.collectAsStateWithLifecycle()
        val yearFilterOptions by viewModel.yearFilterOptions.collectAsStateWithLifecycle()

        val transactionUiState = TransactionUiState(
            queryFilter = filter.query,
            yearFilterOptions = yearFilterOptions,
            typeFilter = filter.type,
            yearFilter = filter.year,
            monthFilter = filter.month,
            transactions = transactions
        )

        val transactionActions = object : TransactionActions {
            override fun onQueryChange(query: String) {
                viewModel.searchTransactions(query)
            }

            override fun onYearFilterOptionsRequest() {
                viewModel.getYearFilterOptions()
            }

            override fun onFilterApply(type: Int?, year: Int?, month: Int?) {
                viewModel.applyFilter(type, year, month)
            }

            override fun onNavigateToTransactionDetail(transactionId: Long) {
                rootNavController.navigate(TransactionDetail.Main(transactionId))
            }
        }

        TransactionScreen(
            transactionUiState = transactionUiState,
            transactionActions = transactionActions
        )
    }
    composable<Main.Budgeting> {
        val viewModel = koinViewModel<BudgetingViewModel>()
        val budgetSummaryState by viewModel.budgetSummaryState.collectAsStateWithLifecycle()
        val budgets by viewModel.budgets.collectAsStateWithLifecycle()

        val budgetUiState = BudgetUiState(
            totalMaxAmount = budgetSummaryState.totalMaxAmount,
            totalUsedAmount = budgetSummaryState.totalUsedAmount,
            budgets = budgets
        )

        val budgetActions = object : BudgetActions {
            override fun onNavigateToInactiveBudget() {
                rootNavController.navigate(InactiveBudget.Main)
            }

            override fun onNavigateToBudgetDetail(budgetId: Long) {
                rootNavController.navigate(BudgetDetail.Main(budgetId))
            }

            override fun onHandleExpiredBudget() {
                viewModel.handleExpiredBudget()
            }
        }

        BudgetingScreen(
            budgetUiState = budgetUiState,
            budgetActions = budgetActions
        )
    }
    composable<Main.Analytics> {
        val viewModel = koinViewModel<AnalyticsViewModel>()
        val themeSetting by viewModel.themeSetting.collectAsStateWithLifecycle()
        val filter by viewModel.filterState.collectAsStateWithLifecycle()
        val yearFilterOptions by viewModel.yearFilterOptions.collectAsStateWithLifecycle()
        val amountSummary by viewModel.transactionAmountSummary
            .collectAsStateWithLifecycle(initialValue = TransactionBalanceSummaryState())
        val categorySummaries by viewModel.transactionCategorySummaries
            .collectAsStateWithLifecycle(initialValue = emptyList())
        val dailySummaries by viewModel.transactionDailySummaries
            .collectAsStateWithLifecycle(initialValue = emptyList())

        val analyticsState = AnalyticsState(
            monthFilter = filter.month,
            yearFilter = filter.year,
            typeFilter = filter.type,
            weekFilter = filter.week,
            yearFilterOptions = yearFilterOptions,
            amountSummary = amountSummary,
            categorySummaries = categorySummaries,
            dailySummaries = dailySummaries
        )

        val analyticsActions = object : AnalyticsActions {
            override fun onMonthChange(month: Int) {
                viewModel.changeMonthFilter(month)
            }

            override fun onYearChange(year: Int) {
                viewModel.changeYearFilter(year)
            }

            override fun onTypeChange(type: Int) {
                viewModel.changeTypeFilter(type)
            }

            override fun onWeekChange(week: Int) {
                viewModel.changeWeekFilter(week)
            }

            override fun onNavigateToAnalyticsCategoryTransaction(
                category: Int, month: Int, year: Int
            ) {
                rootNavController.navigate(AnalyticsCategoryTransaction.Main(category, month, year))
            }
        }

        AnalyticsScreen(
            analyticsState = analyticsState,
            analyticsActions = analyticsActions,
            themeSetting = themeSetting
        )
    }
}