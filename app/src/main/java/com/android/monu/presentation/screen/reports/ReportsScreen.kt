package com.android.monu.presentation.screen.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.android.monu.data.dummy.TransactionsData
import com.android.monu.presentation.screen.reports.components.ReportsAppBar
import com.android.monu.presentation.screen.reports.components.ReportsListItem
import com.android.monu.ui.theme.LightGrey
import com.android.monu.ui.theme.SoftGrey

@Composable
fun ReportsScreen(
    availableYears: List<Int>,
    selectedYear: Int,
    onFilterClick: () -> Unit,
    onYearFilterSelect: (Int) -> Unit,
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
                ReportsAppBar(
                    availableYears = availableYears,
                    selectedYear = selectedYear,
                    onFilterClick = onFilterClick,
                    onYearFilterSelect = onYearFilterSelect
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(LightGrey)
                .padding(innerPadding),
            state = gridState,
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(TransactionsData.listMonth, key = { it.id }) {
                ReportsListItem(
                    title = it.name,
                    navigateToDetail = navigateToDetail
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportsScreenPreview() {
    ReportsScreen(
        availableYears = listOf(2025, 2024, 2023),
        selectedYear = 2025,
        onFilterClick = { },
        onYearFilterSelect = { },
        navigateToDetail = { }
    )
}