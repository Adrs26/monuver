package com.android.monu.presentation.screen.report.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.monu.presentation.components.ActionButton
import com.android.monu.presentation.components.NormalAppBar
import com.android.monu.presentation.screen.report.detail.components.ReportDetailAmount
import com.android.monu.presentation.screen.report.detail.components.ReportDetailPieChart
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.LightGrey
import com.android.monu.ui.theme.SoftGrey

@Composable
fun ReportsDetailScreen(
    navigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val isScrolled by remember { derivedStateOf { scrollState.value > 45 } }

    Scaffold(
        topBar = {
            Column {
                NormalAppBar(
                    title = "Report Detail (September 2025)",
                    navigateBack = navigateBack
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
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReportDetailAmount(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp))
            ReportDetailPieChart(modifier = Modifier.padding(horizontal = 16.dp))
            ActionButton(
                text = "Download Report Detail to PDF",
                color = Blue,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportDetailScreenPreview() {
    ReportsDetailScreen(
        navigateBack = {}
    )
}