package com.android.monu.ui.feature.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.airbnb.lottie.compose.LottieConstants
import com.android.monu.R
import com.android.monu.domain.model.transaction.TransactionBalanceSummary
import com.android.monu.ui.feature.components.CommonFloatingActionButton
import com.android.monu.ui.feature.components.CommonLottieAnimation
import com.android.monu.ui.feature.screen.analytics.AnalyticsActions
import com.android.monu.ui.feature.screen.analytics.AnalyticsScreen
import com.android.monu.ui.feature.screen.analytics.AnalyticsState
import com.android.monu.ui.feature.screen.analytics.AnalyticsViewModel
import com.android.monu.ui.feature.screen.budgeting.BudgetActions
import com.android.monu.ui.feature.screen.budgeting.BudgetState
import com.android.monu.ui.feature.screen.budgeting.BudgetingScreen
import com.android.monu.ui.feature.screen.budgeting.BudgetingViewModel
import com.android.monu.ui.feature.screen.home.HomeActions
import com.android.monu.ui.feature.screen.home.HomeScreen
import com.android.monu.ui.feature.screen.home.HomeViewModel
import com.android.monu.ui.feature.screen.main.components.BottomNavigationBar
import com.android.monu.ui.feature.screen.main.components.BudgetWarningDialog
import com.android.monu.ui.feature.screen.transaction.TransactionActions
import com.android.monu.ui.feature.screen.transaction.TransactionScreen
import com.android.monu.ui.feature.screen.transaction.TransactionState
import com.android.monu.ui.feature.screen.transaction.TransactionViewModel
import com.android.monu.ui.feature.utils.TransactionType
import com.android.monu.ui.navigation.Analytics
import com.android.monu.ui.navigation.Budgeting
import com.android.monu.ui.navigation.Home
import com.android.monu.ui.navigation.MainAccount
import com.android.monu.ui.navigation.MainAddBudget
import com.android.monu.ui.navigation.MainAddTransaction
import com.android.monu.ui.navigation.MainAnalyticsCategoryTransaction
import com.android.monu.ui.navigation.MainBill
import com.android.monu.ui.navigation.MainBudgetDetail
import com.android.monu.ui.navigation.MainInactiveBudget
import com.android.monu.ui.navigation.MainTransactionDetail
import com.android.monu.ui.navigation.Settings
import com.android.monu.ui.navigation.Transaction
import com.android.monu.ui.navigation.Transfer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    rootNavController: NavHostController
) {
    val mainNavController = rememberNavController()

    val menuItems = listOf("Home", "Transaction", "Budgeting", "Analytics")
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var selectedMenu by rememberSaveable { mutableStateOf(menuItems[0]) }
    var showFab by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showBudgetWarningDialog by remember { mutableStateOf(false) }

    val budgetWarningCondition = rootNavController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("warning_condition", 0)
        ?.collectAsStateWithLifecycle()

    val budgetCategory = rootNavController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("budget_category", 0)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(selectedMenu) {
        showFab = false
        delay(200)
        if (selectedMenu == menuItems[1] || selectedMenu == menuItems[2]) { showFab = true }
    }

    LaunchedEffect(budgetWarningCondition?.value, budgetCategory?.value) {
        val warningValue = budgetWarningCondition?.value
        val categoryValue = budgetCategory?.value

        if (warningValue != 0 && categoryValue != 0) {
            showBudgetWarningDialog = true
        }
    }

    Scaffold(
        bottomBar = {
            Column {
                HorizontalDivider(
                    modifier = Modifier.padding(bottom = 4.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    thickness = 1.dp
                )
                BottomNavigationBar(
                    navController = mainNavController,
                    onNavigate = { selectedMenu = it }
                )
            }
        },
        floatingActionButton = {
            if (selectedMenu == menuItems[1] || selectedMenu == menuItems[2]) {
                AnimatedVisibility(
                    visible = showFab,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(durationMillis = 200)
                    ) + fadeIn(animationSpec = tween(200)),
                    exit = slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(durationMillis = 200)
                    ) + fadeOut(animationSpec = tween(200))
                ) {
                    CommonFloatingActionButton {
                        when (selectedMenu) {
                            menuItems[1] -> showBottomSheet = true
                            menuItems[2] -> rootNavController.navigate(MainAddBudget)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Home> {
                val viewModel = koinViewModel<HomeViewModel>()
                val totalBalance by viewModel.totalAccountBalance.collectAsStateWithLifecycle()
                val recentTransactions by viewModel.recentTransactions.collectAsStateWithLifecycle()
                val budgetSummary by viewModel.budgetSummary.collectAsStateWithLifecycle()

                val homeActions = object : HomeActions {
                    override fun onHandleExpiredBudget() {
                        viewModel.handleExpiredBudget()
                    }

                    override fun onNavigateToBill() {
                        rootNavController.navigate(MainBill)
                    }

                    override fun onNavigateToSettings() {
                        rootNavController.navigate(Settings)
                    }

                    override fun onNavigateToAccount() {
                        rootNavController.navigate(MainAccount)
                    }

                    override fun onNavigateToAddIncomeTransaction() {
                        rootNavController.navigate(MainAddTransaction(TransactionType.INCOME))
                    }

                    override fun onNavigateToAddExpenseTransaction() {
                        rootNavController.navigate(MainAddTransaction(TransactionType.EXPENSE))
                    }

                    override fun onNavigateToTransfer() {
                        rootNavController.navigate(Transfer)
                    }

                    override fun onNavigateToTransaction() {
                        mainNavController.navigate(Transaction) {
                            popUpTo(mainNavController.graph.startDestinationId) { saveState = true }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }

                    override fun onNavigateToTransactionDetail(transactionId: Long) {
                        rootNavController.navigate(MainTransactionDetail(transactionId))
                    }

                    override fun onNavigateToBudgeting() {
                        mainNavController.navigate(Budgeting) {
                            popUpTo(mainNavController.graph.startDestinationId) { saveState = true }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                }

                if (budgetSummary == null) {
                    CommonLottieAnimation(
                        lottieAnimation = R.raw.loading,
                        iterations = LottieConstants.IterateForever
                    )
                } else {
                    HomeScreen(
                        totalBalance = totalBalance,
                        recentTransactions = recentTransactions,
                        budgetSummary = budgetSummary!!,
                        homeActions = homeActions
                    )
                }
            }
            composable<Transaction> {
                val viewModel = koinViewModel<TransactionViewModel>()
                val transactions = viewModel.transactions.collectAsLazyPagingItems()
                val filter by viewModel.filterState.collectAsStateWithLifecycle()
                val yearFilterOptions by viewModel.yearFilterOptions.collectAsStateWithLifecycle()

                val transactionState = TransactionState(
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
                        rootNavController.navigate(MainTransactionDetail(transactionId))
                    }
                }

                TransactionScreen(
                    transactionState = transactionState,
                    transactionActions = transactionActions
                )
            }
            composable<Budgeting> {
                val viewModel = koinViewModel<BudgetingViewModel>()
                val summary by viewModel.budgetSummary.collectAsStateWithLifecycle()
                val budgets by viewModel.budgets.collectAsStateWithLifecycle()

                val budgetState = BudgetState(
                    totalMaxAmount = summary.totalMaxAmount,
                    totalUsedAmount = summary.totalUsedAmount,
                    budgets = budgets
                )

                val budgetActions = object : BudgetActions {
                    override fun onNavigateToInactiveBudget() {
                        rootNavController.navigate(MainInactiveBudget)
                    }

                    override fun onNavigateToBudgetDetail(budgetId: Long) {
                        rootNavController.navigate(MainBudgetDetail(budgetId))
                    }

                    override fun onHandleExpiredBudget() {
                        viewModel.handleExpiredBudget()
                    }
                }

                BudgetingScreen(
                    budgetState = budgetState,
                    budgetActions = budgetActions
                )
            }
            composable<Analytics> {
                val viewModel = koinViewModel<AnalyticsViewModel>()
                val filter by viewModel.filterState.collectAsStateWithLifecycle()
                val yearFilterOptions by viewModel.yearFilterOptions.collectAsStateWithLifecycle()
                val amountSummary by viewModel.transactionAmountSummary
                    .collectAsStateWithLifecycle(initialValue = TransactionBalanceSummary())
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
                        rootNavController.navigate(
                            MainAnalyticsCategoryTransaction(category, month, year)
                        )
                    }
                }

                AnalyticsScreen(
                    analyticsState = analyticsState,
                    analyticsActions = analyticsActions
                )
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                shape = BottomSheetDefaults.HiddenShape,
                containerColor = MaterialTheme.colorScheme.background,
                dragHandle = { null }
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    BottomSheetMenu(
                        title = stringResource(R.string.add_income)
                    ) {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                                rootNavController.navigate(MainAddTransaction(TransactionType.INCOME))
                            }
                        }
                    }
                    BottomSheetMenu(
                        title = stringResource(R.string.add_expense)
                    ) {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                                rootNavController.navigate(MainAddTransaction(TransactionType.EXPENSE))
                            }
                        }
                    }
                    BottomSheetMenu(
                        title = stringResource(R.string.transfer_account)
                    ) {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                                rootNavController.navigate(Transfer)
                            }
                        }
                    }
                }
            }
        }

        if (showBudgetWarningDialog && budgetWarningCondition?.value != 0 && budgetCategory?.value != 0) {
            BudgetWarningDialog(
                budgetCategory = budgetCategory?.value ?: 0,
                budgetWarningCondition = budgetWarningCondition?.value ?: 0,
                onDismissRequest = {
                    showBudgetWarningDialog = false
                    rootNavController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("warning_condition", 0)
                }
            )
        }
    }
}

@Composable
fun BottomSheetMenu(
    title: String,
    onMenuSelect: () -> Unit,
) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMenuSelect() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        style = MaterialTheme.typography.labelMedium
    )
}