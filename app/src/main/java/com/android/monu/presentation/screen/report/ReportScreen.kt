package com.android.monu.presentation.screen.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.monu.domain.model.TransactionMonthlyAmount
import com.android.monu.presentation.screen.report.components.ReportAppBar
import com.android.monu.presentation.screen.report.components.ReportList
import com.android.monu.ui.theme.LightGrey
import com.android.monu.ui.theme.SoftGrey

@Composable
fun ReportScreen(
    listTransactionMonthlyAmount: List<TransactionMonthlyAmount>,
    filterState: ReportFilterState,
    filterCallbacks: ReportFilterCallbacks,
    navigateToDetail: () -> Unit
) {
    val gridState = rememberLazyGridState()
    val isScrolled by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 35
        }
    }

    Scaffold(
        topBar = {
            Column {
                ReportAppBar(
                    filterState = filterState,
                    filterCallbacks = filterCallbacks
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
        ReportList(
            listTransactionMonthlyAmount = listTransactionMonthlyAmount,
            gridState = gridState,
            modifier = Modifier
                .background(LightGrey)
                .padding(innerPadding),
            navigateToDetail = navigateToDetail
        )
    }
}

data class ReportFilterState(
    val selectedYear: Int,
    val availableYears: List<Int>
)

data class ReportFilterCallbacks(
    val onFilterClick: () -> Unit,
    val onYearFilterSelect: (Int) -> Unit
)

@Preview(showBackground = true)
@Composable
fun ReportScreenPreview() {
    ReportScreen(
        listTransactionMonthlyAmount = listOf(),
        filterState = ReportFilterState(
            selectedYear = 2025,
            availableYears = listOf(2025, 2024, 2023)
        ),
        filterCallbacks = ReportFilterCallbacks(
            onFilterClick = { },
            onYearFilterSelect = { }
        ),
        navigateToDetail = { }
    )
}