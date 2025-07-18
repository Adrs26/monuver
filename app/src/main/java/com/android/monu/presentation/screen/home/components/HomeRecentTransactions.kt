package com.android.monu.presentation.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.presentation.screen.transaction.components.TransactionData
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.Green
import com.android.monu.ui.theme.Red
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.utils.DateHelper
import com.android.monu.utils.NumberFormatHelper
import com.android.monu.utils.extensions.debouncedClickable

@Composable
fun HomeRecentTransactions(
    recentTransactions: List<Transaction>,
    modifier: Modifier = Modifier,
    navigateToTransactions: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.recent_transactions),
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                )
                Text(
                    text = "Lihat semua",
                    modifier = Modifier.debouncedClickable { navigateToTransactions() },
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Blue
                    )
                )
            }
            RecentTransactionList(recentTransactions = recentTransactions)
        }
    }
}

@Composable
fun RecentTransactionList(
    recentTransactions: List<Transaction>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (recentTransactions.isEmpty()) {
            Text(
                text = stringResource(R.string.no_transactions_yet),
                modifier = Modifier.padding(top = 48.dp, bottom = 40.dp),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            )
        } else {
            Column {
                LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
                    items(recentTransactions.size) { index ->
                        val transactionData = TransactionData(
                            id = recentTransactions[index].id,
                            title = recentTransactions[index].title,
                            type = recentTransactions[index].type,
                            category = recentTransactions[index].childCategory,
                            date = recentTransactions[index].date,
                            amount = recentTransactions[index].amount
                        )
                        RecentTransactionsListItem(transactionData = transactionData)
                    }
                }
            }
        }
    }
}

@Composable
fun RecentTransactionsListItem(
    transactionData: TransactionData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Blue,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_other),
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
                    text = transactionData.title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                )
                Text(
                    text = DateHelper.formatDateToReadable(transactionData.date),
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
                text = NumberFormatHelper.formatToRupiah(transactionData.amount),
                modifier = Modifier.padding(horizontal = 8.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = if (transactionData.type == 1) Green else Red
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeRecentTransactionsPreview() {
    HomeRecentTransactions(
        recentTransactions = emptyList(),
        navigateToTransactions = {}
    )
}