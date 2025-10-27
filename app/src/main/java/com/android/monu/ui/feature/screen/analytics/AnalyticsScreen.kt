package com.android.monu.ui.feature.screen.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.monu.data.datastore.ThemeSetting
import com.android.monu.domain.model.TransactionBalanceSummaryState
import com.android.monu.domain.model.TransactionCategorySummaryState
import com.android.monu.domain.model.TransactionDailySummaryState
import com.android.monu.ui.feature.screen.analytics.components.AnalyticsAppBar
import com.android.monu.ui.feature.screen.analytics.components.AnalyticsBalanceOverview
import com.android.monu.ui.feature.screen.analytics.components.AnalyticsBarChart
import com.android.monu.ui.feature.screen.analytics.components.AnalyticsBarChartState
import com.android.monu.ui.feature.screen.analytics.components.AnalyticsPieChart
import com.android.monu.ui.feature.screen.analytics.components.AnalyticsPieChartState

@Composable
fun AnalyticsScreen(
    analyticsState: AnalyticsState,
    analyticsActions: AnalyticsActions,
    themeSetting: ThemeSetting
) {
    val analyticsBarChartState = AnalyticsBarChartState(
        monthFilter = analyticsState.monthFilter,
        yearFilter = analyticsState.yearFilter,
        weekFilter = analyticsState.weekFilter,
        dailySummaries = analyticsState.dailySummaries
    )

    val analyticsPieChartState = AnalyticsPieChartState(
        typeFilter = analyticsState.typeFilter,
        monthFilter = analyticsState.monthFilter,
        yearFilter = analyticsState.yearFilter,
        categorySummaries = analyticsState.categorySummaries
    )

    Scaffold(
        topBar = {
            AnalyticsAppBar(
                monthValue = analyticsState.monthFilter,
                yearValue = analyticsState.yearFilter,
                yearFilterOptions = analyticsState.yearFilterOptions,
                onMonthChange = analyticsActions::onMonthChange,
                onYearChange = analyticsActions::onYearChange
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            AnalyticsBalanceOverview(
                amountSummary = analyticsState.amountSummary,
                themeSetting = themeSetting,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            AnalyticsBarChart(
                barChartState = analyticsBarChartState,
                onWeekChange = analyticsActions::onWeekChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            AnalyticsPieChart(
                pieChartState = analyticsPieChartState,
                onTypeChange = analyticsActions::onTypeChange,
                onNavigateToAnalyticsCategoryTransaction =
                    analyticsActions::onNavigateToAnalyticsCategoryTransaction,
                modifier = Modifier.padding(bottom = 24.dp),
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
    val amountSummary: TransactionBalanceSummaryState,
    val categorySummaries: List<TransactionCategorySummaryState>,
    val dailySummaries: List<TransactionDailySummaryState>,
)

interface AnalyticsActions {
    fun onMonthChange(month: Int)
    fun onYearChange(year: Int)
    fun onTypeChange(type: Int)
    fun onWeekChange(week: Int)
    fun onNavigateToAnalyticsCategoryTransaction(category: Int, month: Int, year: Int)
}