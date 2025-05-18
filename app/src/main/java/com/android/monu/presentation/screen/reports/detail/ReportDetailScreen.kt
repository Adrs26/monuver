package com.android.monu.presentation.screen.reports.detail

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
import androidx.compose.ui.unit.dp
import com.android.monu.presentation.components.ActionButton
import com.android.monu.presentation.components.Toolbar
import com.android.monu.presentation.screen.reports.detail.components.ReportDetailAmount
import com.android.monu.presentation.screen.reports.detail.components.ReportDetailPieChart
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.LightGrey
import com.android.monu.ui.theme.SoftGrey

@Composable
fun ReportsDetailScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    val listState = rememberLazyListState()
    val showDivider by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 40
        }
    }

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
                Toolbar(
                    title = "Report Detail (September 2025)",
                    navigateBack = navigateBack
                )
                if (showDivider) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = SoftGrey
                    )
                }
            }
        }
        item { ReportDetailAmount(modifier = Modifier.padding(16.dp)) }
        item { ReportDetailPieChart(modifier = Modifier.padding(horizontal = 16.dp)) }
        item {
            ActionButton(
                text = "Download Report Detail to PDF",
                color = Blue,
                modifier = Modifier.padding(16.dp),
                onClick = {}
            )
        }
    }
}