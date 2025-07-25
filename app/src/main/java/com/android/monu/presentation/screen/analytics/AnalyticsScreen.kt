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
import com.android.monu.domain.model.transaction.TransactionAmountSummary
import com.android.monu.domain.model.transaction.TransactionCategorySummary
import com.android.monu.presentation.screen.analytics.components.AnalyticsAmountOverview
import com.android.monu.presentation.screen.analytics.components.AnalyticsAppBar
import com.android.monu.presentation.screen.analytics.components.AnalyticsBarChart
import com.android.monu.presentation.screen.analytics.components.AnalyticsBarChartState
import com.android.monu.presentation.screen.analytics.components.AnalyticsPieChart

@Composable
fun AnalyticsScreen(
    analyticsState: AnalyticsState,
    analyticsActions: AnalyticsActions,
) {
    val analyticsBarChartState = AnalyticsBarChartState(
        monthFilter = analyticsState.monthFilter,
        yearFilter = analyticsState.yearFilter,
        weekFilter = analyticsState.weekFilter
    )

    Scaffold(
        topBar = {
            AnalyticsAppBar(
                monthValue = analyticsState.monthFilter,
                yearValue = analyticsState.yearFilter,
                yearFilterOptions = analyticsState.yearFilterOptions,
                onMonthChange = { analyticsActions.onMonthChange(it) },
                onYearChange = { analyticsActions.onYearChange(it) }
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
                transactionAmount = analyticsState.transactionAmountSummary,
                modifier = Modifier.padding(16.dp)
            )
            AnalyticsBarChart(
                transactionWeeklySummary = analyticsState.transactionWeeklySummary,
                analyticsBarChartState = analyticsBarChartState,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                onWeekChange = { analyticsActions.onWeekChange(it) },
            )
            AnalyticsPieChart(
                typeValue = analyticsState.typeFilter,
                parentCategoriesSummary = analyticsState.parentCategoriesSummary,
                modifier = Modifier.padding(vertical = 24.dp),
                onTypeChange = { analyticsActions.onTypeChange(it) }
            )
        }
    }
}

data class AnalyticsState(
    val monthFilter: Int,
    val yearFilter: Int,
    val typeFilter: Int,
    val weekFilter: Int,
    val yearFilterOptions: List<Int>,
    val transactionAmountSummary: TransactionAmountSummary,
    val parentCategoriesSummary: List<TransactionCategorySummary>,
    val transactionWeeklySummary: List<TransactionDailySummary>,
)

interface AnalyticsActions {
    fun onMonthChange(month: Int)
    fun onYearChange(year: Int)
    fun onTypeChange(type: Int)
    fun onWeekChange(week: Int)
}