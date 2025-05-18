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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.monu.data.dummy.TransactionsData
import com.android.monu.presentation.screen.reports.components.ReportsListItem
import com.android.monu.presentation.screen.reports.components.ReportsTopBar
import com.android.monu.ui.theme.LightGrey
import com.android.monu.ui.theme.SoftGrey

@Composable
fun ReportsScreen(
    modifier: Modifier = Modifier,
    navigateToDetail: () -> Unit
) {
    val gridState = rememberLazyGridState()
    val showDivider by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 35
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightGrey),
    ) {
        ReportsTopBar(modifier = Modifier.padding(16.dp))
        if (showDivider) {
            HorizontalDivider(
                thickness = 1.dp,
                color = SoftGrey
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
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
        navigateToDetail = {}
    )
}