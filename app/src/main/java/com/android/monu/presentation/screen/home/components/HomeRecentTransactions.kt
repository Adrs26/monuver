package com.android.monu.presentation.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.data.dummy.TransactionsData
import com.android.monu.presentation.components.ActionButton
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.Orange
import com.android.monu.ui.theme.Red
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily

@Composable
fun HomeRecentTransactions(
    modifier: Modifier = Modifier,
    toTransactions: () -> Unit
) {
    Card(
        modifier = modifier.border(width = 1.dp, color = SoftGrey, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.recent_transactions),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
            LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
                items(TransactionsData.listRecentItem, key = { it.id }) {
                    RecentTransactionsListItem()
                }
            }
            ActionButton(
                text = stringResource(R.string.see_all_transactions),
                color = Blue,
                modifier = Modifier.padding(top = 16.dp),
                onClick = toTransactions
            )
        }
    }
}

@Composable
fun RecentTransactionsListItem(
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
                    .background(color = Orange, shape = RoundedCornerShape(12.dp))
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_expense_food),
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
                    text = "Breakfast",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                )
                Text(
                    text = "Food & Beverages",
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
            }
            Text(
                text = "Rp50.000",
                modifier = Modifier.padding(horizontal = 8.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Red
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeRecentTransactionsPreview() {
    HomeRecentTransactions(
        toTransactions = {}
    )
}