package com.android.monu.ui.feature.screen.bill.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.android.monu.R
import com.android.monu.domain.model.bill.Bill
import com.android.monu.ui.feature.components.CommonLottieAnimation

@Composable
fun PaidBillScreen(
    bills: LazyPagingItems<Bill>,
    onNavigateToBillDetail: (Long) -> Unit
) {
    if (bills.itemCount == 0 && bills.loadState.refresh is LoadState.NotLoading) {
        CommonLottieAnimation(lottieAnimation = R.raw.empty)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                count = bills.itemCount,
                key = { index -> bills[index]?.id!! }
            ) { index ->
                bills[index]?.let { bill ->
                    val billState = BillListItemState(
                        id = bill.id,
                        title = bill.title,
                        dueDate = bill.dueDate,
                        paidDate = bill.paidDate ?: "",
                        amount = bill.amount,
                        isRecurring = bill.isRecurring,
                        status = 3,
                        nowPaidPeriod = bill.nowPaidPeriod
                    )

                    BillListItem(
                        billState = billState,
                        modifier = Modifier
                            .clickable {
                                onNavigateToBillDetail(billState.id)
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }

            when (bills.loadState.append) {
                is LoadState.Loading -> {
                    item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                }
                is LoadState.Error -> {
                    val error = (bills.loadState.append as LoadState.Error).error
                    item { Text("Error: ${error.localizedMessage}") }
                }
                else -> {}
            }
        }
    }
}