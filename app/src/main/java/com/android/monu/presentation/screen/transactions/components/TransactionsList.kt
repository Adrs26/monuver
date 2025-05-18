package com.android.monu.presentation.screen.transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.android.monu.domain.model.TransactionConcise
import com.android.monu.ui.theme.Green
import com.android.monu.ui.theme.Red
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.util.CurrencyFormatHelper
import com.android.monu.util.DateHelper
import com.android.monu.util.debouncedClickable
import com.android.monu.util.toCategoryColor
import com.android.monu.util.toCategoryIcon

@Composable
fun TransactionsList(
    transactions: LazyPagingItems<TransactionConcise>,
    modifier: Modifier = Modifier,
    navigateToEditTransaction: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (transactions.itemCount == 0 && transactions.loadState.refresh is LoadState.NotLoading) {
            Text(
                text = "No transactions yet",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = transactions.itemCount,
                    key = { index -> transactions[index]?.id!! }
                ) { index ->
                    transactions[index]?.let { transaction ->
                        TransactionsListItem(
                            title = transaction.title,
                            type = transaction.type,
                            category = transaction.category,
                            date = transaction.date,
                            amount = transaction.amount,
                            modifier = Modifier.animateItem(),
                            navigateToEditTransaction = navigateToEditTransaction
                        )
                    }
                }

                when (transactions.loadState.append) {
                    is LoadState.Loading -> {
                        item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                    }
                    is LoadState.Error -> {
                        val error = (transactions.loadState.append as LoadState.Error).error
                        item { Text("Error: ${error.localizedMessage}") }
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun TransactionsListItem(
    title: String,
    type: Int,
    category: Int,
    date: String,
    amount: Long,
    modifier: Modifier = Modifier,
    navigateToEditTransaction: () -> Unit
) {
    Card(
        modifier = modifier
            .border(width = 1.dp, color = SoftGrey, shape = RoundedCornerShape(16.dp))
            .debouncedClickable { navigateToEditTransaction() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = category.toCategoryColor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(category.toCategoryIcon()),
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                )
                Text(
                    text = DateHelper.formatDateToReadable(date),
                    modifier = Modifier.padding(top = 2.dp),
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
            }
            Text(
                text = CurrencyFormatHelper.formatToRupiah(amount),
                modifier = Modifier.padding(horizontal = 8.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = if (type == 1) Green else Red
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionsListItemPreview() {
    TransactionsListItem(
        title = "Breakfast",
        type = 2,
        category = 1,
        date = "2025-18-5",
        amount = 15000L,
        navigateToEditTransaction = {}
    )
}