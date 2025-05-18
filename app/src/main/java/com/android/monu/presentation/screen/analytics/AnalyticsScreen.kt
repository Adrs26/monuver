package com.android.monu.presentation.screen.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.monu.presentation.screen.analytics.components.AnalyticsAverageAmountContent
import com.android.monu.presentation.screen.analytics.components.AnalyticsBarChartContent
import com.android.monu.presentation.screen.analytics.components.AnalyticsPieChartContent
import com.android.monu.presentation.screen.analytics.components.AnalyticsTopBar
import com.android.monu.ui.theme.LightGrey
import com.android.monu.ui.theme.SoftGrey

@Composable
fun AnalyticsScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = viewModel<AnalyticsViewModel>()

    val listState = rememberLazyListState()
    val showDivider by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 40
        }
    }

    val selectedType by viewModel.selectedType.collectAsStateWithLifecycle()
    val monthLabels by viewModel.monthLabels.collectAsStateWithLifecycle()
    val scaleLabels by viewModel.scaleLabels.collectAsStateWithLifecycle()
    val barValues by viewModel.barValues.collectAsStateWithLifecycle()
    val barDates by viewModel.barDates.collectAsStateWithLifecycle()
    val pieValues by viewModel.pieValues.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightGrey),
        state = listState
    ) {
        stickyHeader {
            Column(
                modifier = Modifier.background(LightGrey)
            ) {
                AnalyticsTopBar(modifier = Modifier.padding(16.dp))
                if (showDivider) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = SoftGrey
                    )
                }
            }
        }
        item {
            AnalyticsAverageAmountContent(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
            )
        }
        item {
            AnalyticsBarChartContent(
                viewModel = viewModel,
                selectedType = selectedType,
                values = barValues,
                dates = barDates,
                monthLabels = monthLabels,
                scaleLabels = scaleLabels,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            AnalyticsPieChartContent(
                values = pieValues,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnalyticsScreenPreview() {
    AnalyticsScreen()
}