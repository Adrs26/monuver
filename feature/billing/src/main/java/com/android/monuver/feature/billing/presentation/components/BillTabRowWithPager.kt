package com.android.monuver.feature.billing.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.android.monuver.feature.billing.R
import com.android.monuver.feature.billing.presentation.BillUiState
import kotlinx.coroutines.launch

@Composable
internal fun BillTabRowWithPager(
    billUiState: BillUiState,
    onNavigateToBillDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
    val tabTitles = listOf(
        stringResource(R.string.pending),
        stringResource(R.string.due),
        stringResource(R.string.paid)
    )

    Column(
        modifier = modifier
    ) {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(pagerState.currentPage, true),
                    width = Dp.Unspecified,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    text = {
                        Text(
                            text = title,
                            color = if (pagerState.currentPage == index)
                                MaterialTheme.colorScheme.primary else
                                    MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp)
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> {
                    PendingBillScreen(
                        bills = billUiState.pendingBills,
                        onNavigateToBillDetail = onNavigateToBillDetail
                    )
                }
                1 -> {
                    DueBillScreen(
                        bills = billUiState.dueBills,
                        onNavigateToBillDetail = onNavigateToBillDetail
                    )
                }
                2 -> {
                    PaidBillScreen(
                        bills = billUiState.paidBills,
                        onNavigateToBillDetail = onNavigateToBillDetail
                    )
                }
            }
        }
    }
}