package com.android.monu.presentation.screen.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.monu.domain.model.transaction.TransactionDailySummary
import com.android.monu.domain.model.transaction.TransactionMonthlyAmountOverview
import com.android.monu.domain.model.transaction.TransactionParentCategorySummary
import com.android.monu.presentation.screen.analytics.components.AnalyticsAmountOverview
import com.android.monu.presentation.screen.analytics.components.AnalyticsAppBar
import com.android.monu.presentation.screen.analytics.components.AnalyticsBarChart
import com.android.monu.presentation.screen.analytics.components.AnalyticsPieChart

@Composable
fun AnalyticsScreen(
    monthValue: Int,
    yearValue: Int,
    typeValue: Int,
    weekValue: Int,
    yearFilterOptions: List<Int>,
    transactionAmountOverview: TransactionMonthlyAmountOverview,
    parentCategoriesSummary: List<TransactionParentCategorySummary>,
    transactionWeeklySummary: List<TransactionDailySummary>,
    onMonthChange: (Int) -> Unit,
    onYearChange: (Int) -> Unit,
    onTypeChange: (Int) -> Unit,
    onWeekChange: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            AnalyticsAppBar(
                monthValue = monthValue,
                yearValue = yearValue,
                yearFilterOptions = yearFilterOptions,
                onMonthChange = onMonthChange,
                onYearChange = onYearChange
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            AnalyticsAmountOverview(
                transactionAmount = transactionAmountOverview,
                modifier = Modifier.padding(16.dp)
            )
            AnalyticsBarChart(
                transactionWeeklySummary = transactionWeeklySummary,
                monthValue = monthValue,
                yearValue = yearValue,
                weekValue = weekValue,
                onWeekChange = onWeekChange,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
            )
            AnalyticsPieChart(
                typeValue = typeValue,
                parentCategoriesSummary = parentCategoriesSummary,
                modifier = Modifier.padding(vertical = 24.dp),
                onTypeChange = onTypeChange
            )
        }
    }
}

data class AnalyticsFilterState(
    val barChartSelectedYear: Int,
    val barChartSelectedType: Int,
    val pieChartSelectedYear: Int,
    val availableYears: List<Int>
)

data class AnalyticsFilterCallbacks(
    val onFilterClick: () -> Unit,
    val onBarChartYearFilterSelect: (Int) -> Unit,
    val onBarChartFilterTypeClick: (Int) -> Unit,
    val onPieChartYearFilterSelect: (Int) -> Unit
)