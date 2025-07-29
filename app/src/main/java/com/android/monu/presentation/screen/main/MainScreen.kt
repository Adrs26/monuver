package com.android.monu.presentation.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.monu.R
import com.android.monu.domain.model.transaction.TransactionAmountSummary
import com.android.monu.presentation.components.CommonFloatingActionButton
import com.android.monu.presentation.screen.analytics.AnalyticsActions
import com.android.monu.presentation.screen.analytics.AnalyticsScreen
import com.android.monu.presentation.screen.analytics.AnalyticsState
import com.android.monu.presentation.screen.analytics.AnalyticsViewModel
import com.android.monu.presentation.screen.budgeting.BudgetingScreen
import com.android.monu.presentation.screen.home.HomeScreen
import com.android.monu.presentation.screen.transaction.TransactionActions
import com.android.monu.presentation.screen.transaction.TransactionScreen
import com.android.monu.presentation.screen.transaction.TransactionState
import com.android.monu.presentation.screen.transaction.TransactionViewModel
import com.android.monu.presentation.utils.TransactionType
import com.android.monu.ui.navigation.Account
import com.android.monu.ui.navigation.Analytics
import com.android.monu.ui.navigation.Budgeting
import com.android.monu.ui.navigation.BudgetingDetail
import com.android.monu.ui.navigation.Home
import com.android.monu.ui.navigation.MainAddTransaction
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

    LaunchedEffect(selectedMenu) {
        showFab = false
        delay(200)
        if (selectedMenu == menuItems[1] || selectedMenu == menuItems[2]) {
            showFab = true
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
                    onItemClick = { selectedMenu = it }
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
                            menuItems[2] -> {}
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
                HomeScreen(
                    totalIncomeAmount = 0,
                    totalExpenseAmount = 0,
                    recentTransactions = emptyList(),
                    navigateToSettings = { rootNavController.navigate(Settings) },
                    navigateToBudgeting = { rootNavController.navigate(Account) },
                    navigateToTransactions = {
                        mainNavController.navigate(Transaction) {
                            popUpTo(mainNavController.graph.startDestinationId) { saveState = true }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
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
                        viewModel.applyFilter(type = type, year = year, month = month)
                    }

                    override fun onNavigateToTransactionDetail(transactionId: Long) {
                        rootNavController.navigate(MainTransactionDetail(id = transactionId))
                    }
                }

                TransactionScreen(
                    transactionState = transactionState,
                    transactionActions = transactionActions
                )
            }
            composable<Budgeting> {
                BudgetingScreen(
                    onHistoryClick = { },
                    onItemClick = { rootNavController.navigate(BudgetingDetail) }
                )
            }
            composable<Analytics> {
                val viewModel = koinViewModel<AnalyticsViewModel>()
                val filter by viewModel.filterState.collectAsStateWithLifecycle()
                val yearFilterOptions by viewModel.yearFilterOptions.collectAsStateWithLifecycle()
                val transactionAmountSummary by viewModel.transactionAmountSummary
                    .collectAsStateWithLifecycle(initialValue = TransactionAmountSummary())
                val parentCategoriesSummary by viewModel.transactionParentCategorySummary
                    .collectAsStateWithLifecycle(initialValue = emptyList())
                val transactionWeeklySummary by viewModel.transactionWeeklySummary
                    .collectAsStateWithLifecycle(initialValue = emptyList())

                val analyticsState = AnalyticsState(
                    monthFilter = filter.month,
                    yearFilter = filter.year,
                    typeFilter = filter.type,
                    weekFilter = filter.week,
                    yearFilterOptions = yearFilterOptions,
                    transactionAmountSummary = transactionAmountSummary,
                    parentCategoriesSummary = parentCategoriesSummary,
                    transactionWeeklySummary = transactionWeeklySummary
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
                                rootNavController.navigate(
                                    MainAddTransaction(
                                        type = TransactionType.INCOME
                                    )
                                )
                            }
                        }
                    }
                    BottomSheetMenu(
                        title = stringResource(R.string.add_expense)
                    ) {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                                rootNavController.navigate(
                                    MainAddTransaction(
                                        type = TransactionType.EXPENSE
                                    )
                                )
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
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit
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
                    onItemClick(item.route)
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
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
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