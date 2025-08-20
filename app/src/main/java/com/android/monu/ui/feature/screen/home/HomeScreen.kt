package com.android.monu.ui.feature.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.monu.domain.model.budget.BudgetSummary
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.ui.feature.screen.home.components.HomeAppBar
import com.android.monu.ui.feature.screen.home.components.HomeBalanceOverview
import com.android.monu.ui.feature.screen.home.components.HomeBudgetOverview
import com.android.monu.ui.feature.screen.home.components.HomeMenuButtonBar
import com.android.monu.ui.feature.screen.home.components.HomeRecentTransactions

@Composable
fun HomeScreen(
    totalBalance: Long,
    recentTransactions: List<Transaction>,
    budgetSummary: BudgetSummary,
    homeActions: HomeActions
) {
    LaunchedEffect(Unit) {
        homeActions.onHandleExpiredBudget()
    }

    Scaffold(
        topBar = {
            HomeAppBar(
                onNavigateToBill = homeActions::onNavigateToBill,
                onNavigateToSave = {},
                onNavigateToSettings = homeActions::onNavigateToSettings
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            HomeBalanceOverview(
                totalBalance = totalBalance,
                onNavigateToAccount = homeActions::onNavigateToAccount,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            HomeMenuButtonBar(
                onNavigateToAddIncomeTransaction = homeActions::onNavigateToAddIncomeTransaction,
                onNavigateToAddExpenseTransaction = homeActions::onNavigateToAddExpenseTransaction,
                onNavigateToTransfer = homeActions::onNavigateToTransfer,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 24.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            HomeRecentTransactions(
                recentTransactions = recentTransactions,
                onNavigateToTransaction = homeActions::onNavigateToTransaction,
                onNavigateToTransactionDetail = homeActions::onNavigateToTransactionDetail
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            HomeBudgetOverview(
                totalUsedAmount = budgetSummary.totalUsedAmount,
                totalMaxAmount = budgetSummary.totalMaxAmount,
                onNavigateToBudgeting = homeActions::onNavigateToBudgeting,
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .padding(bottom = 32.dp)
            )
        }
    }
}

interface HomeActions {
    fun onHandleExpiredBudget()
    fun onNavigateToBill()
    fun onNavigateToSettings()
    fun onNavigateToAccount()
    fun onNavigateToAddIncomeTransaction()
    fun onNavigateToAddExpenseTransaction()
    fun onNavigateToTransfer()
    fun onNavigateToTransaction()
    fun onNavigateToTransactionDetail(transactionId: Long)
    fun onNavigateToBudgeting()
}