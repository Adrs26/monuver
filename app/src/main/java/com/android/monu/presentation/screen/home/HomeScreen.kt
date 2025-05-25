package com.android.monu.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.monu.domain.model.TransactionConcise
import com.android.monu.presentation.screen.home.components.HomeBalance
import com.android.monu.presentation.screen.home.components.HomeFinancialInsight
import com.android.monu.presentation.screen.home.components.HomeMenuButtonBar
import com.android.monu.presentation.screen.home.components.HomeRecentTransactions
import com.android.monu.presentation.screen.home.components.HomeTopBar
import com.android.monu.ui.theme.LightGrey

@Composable
fun HomeScreen(
    totalIncomeAmount: Long,
    totalExpenseAmount: Long,
    recentTransactions: List<TransactionConcise>,
    navigateToSettings: () -> Unit,
    navigateToTransactions: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGrey)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HomeTopBar(
            modifier = Modifier.padding(horizontal = 12.dp),
            toSettings = navigateToSettings
        )
        HomeBalance(
            totalIncomeAmount = totalIncomeAmount,
            totalExpenseAmount = totalExpenseAmount,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        HomeFinancialInsight(modifier = Modifier.padding(horizontal = 16.dp))
        HomeMenuButtonBar(modifier = Modifier.padding(horizontal = 12.dp))
        HomeRecentTransactions(
            recentTransactions = recentTransactions,
            modifier = Modifier.padding(horizontal = 16.dp),
            navigateToTransactions = navigateToTransactions
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        totalIncomeAmount = 1000000L,
        totalExpenseAmount = 500000L,
        recentTransactions = emptyList(),
        navigateToSettings = {},
        navigateToTransactions = {}
    )
}