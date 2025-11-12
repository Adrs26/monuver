package com.android.monuver.ui.feature.screen.billing.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.android.monuver.R
import com.android.monuver.ui.feature.screen.billing.BillUiState
import kotlinx.coroutines.launch

@Composable
fun BillTabRowWithPager(
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
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
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