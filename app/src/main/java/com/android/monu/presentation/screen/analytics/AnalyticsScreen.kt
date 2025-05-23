package com.android.monu.presentation.screen.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.data.model.PieData
import com.android.monu.domain.model.AverageTransactionAmount
import com.android.monu.domain.model.TransactionOverview
import com.android.monu.presentation.screen.analytics.components.AnalyticsAverageAmount
import com.android.monu.presentation.screen.analytics.components.AnalyticsBarChart
import com.android.monu.presentation.screen.analytics.components.AnalyticsPieChartContent
import com.android.monu.presentation.screen.analytics.components.BarChartScaleLabel
import com.android.monu.ui.theme.LightGrey
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    averageTransactionAmount: AverageTransactionAmount,
    filterState: AnalyticsFilterState,
    filterCallbacks: AnalyticsFilterCallbacks,
    transactionsOverview: List<TransactionOverview>,
    barChartScaleLabels: List<BarChartScaleLabel>,
    pieValues: List<PieData>
) {
    val scrollState = rememberScrollState()
    val isScrolled by remember { derivedStateOf { scrollState.value > 45 } }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.analytics_menu),
                            modifier = Modifier.padding(start = 8.dp),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = interFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = LightGrey)
                )
                if (isScrolled) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = SoftGrey
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGrey)
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            AnalyticsAverageAmount(
                averageTransactionAmount = averageTransactionAmount,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
            )
            AnalyticsBarChart(
                transactionsOverview = transactionsOverview,
                scaleLabels = barChartScaleLabels,
                filterState = filterState,
                filterCallbacks = filterCallbacks,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            AnalyticsPieChartContent(
                values = pieValues,
                filterState = filterState,
                filterCallbacks = filterCallbacks,
                modifier = Modifier.padding(16.dp)
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