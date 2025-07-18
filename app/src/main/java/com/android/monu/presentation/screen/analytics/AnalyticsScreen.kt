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
import com.android.monu.presentation.screen.analytics.components.AnalyticsAmountOverview
import com.android.monu.presentation.screen.analytics.components.AnalyticsAppBar
import com.android.monu.presentation.screen.analytics.components.AnalyticsBarChart
import com.android.monu.presentation.screen.analytics.components.AnalyticsPieChart
import com.android.monu.presentation.screen.analytics.components.BarChartScaleLabel

@Composable
fun AnalyticsScreen() {
    Scaffold(
        topBar = {
            AnalyticsAppBar()
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            AnalyticsAmountOverview(modifier = Modifier.padding(16.dp))
            AnalyticsBarChart(
                scaleLabels = listOf(
                    BarChartScaleLabel(amount = 0, fraction = 0f),
                    BarChartScaleLabel(amount = 100000, fraction = 0.25f),
                    BarChartScaleLabel(amount = 200000, fraction = 0.5f),
                    BarChartScaleLabel(amount = 300000, fraction = 0.75f),
                    BarChartScaleLabel(amount = 400000, fraction = 1f)
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
            )
            AnalyticsPieChart(
                modifier = Modifier.padding(vertical = 24.dp)
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