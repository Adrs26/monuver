package com.android.monu.presentation.screen.reports.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.Green
import com.android.monu.ui.theme.Red
import com.android.monu.ui.theme.interFontFamily

@Composable
fun ReportDetailAmount(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        BalanceCard()
        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IncomeCard(
                totalIncome = "Rp100.000.000",
                dailyAvgIncome = "Rp100.000.000",
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            ExpenseCard(
                totalExpense = "Rp100.000.000",
                dailyAvgExpense = "Rp100.000.000",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun BalanceCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Blue),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.your_balance),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rp100.000.000",
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
                AmountTrend()
            }
        }
    }
}

@Composable
fun IncomeCard(
    totalIncome: String,
    dailyAvgIncome: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Green),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_trending_up),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Green
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                AmountTrend()
            }
            AmountText(
                title = "Total Income",
                amount = totalIncome,
                modifier = Modifier.padding(top = 32.dp)
            )
            AmountText(
                title = stringResource(R.string.daily_avg_income),
                amount = dailyAvgIncome,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
fun ExpenseCard(
    totalExpense: String,
    dailyAvgExpense: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Red),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_trending_down),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Red
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                AmountTrend()
            }
            AmountText(
                title = "Total Expense",
                amount = totalExpense,
                modifier = Modifier.padding(top = 32.dp)
            )
            AmountText(
                title = stringResource(R.string.daily_avg_expense),
                amount = dailyAvgExpense,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
fun AmountTrend(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_upward),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Green
            )
            Text(
                text = "10.58%",
                modifier = Modifier.padding(start = 4.dp),
                style = TextStyle(
                    fontSize = 11.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Green
                )
            )
        }
    }
}

@Composable
fun AmountText(
    title: String,
    amount: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 11.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
        )
        Text(
            text = amount,
            modifier = Modifier.padding(top = 2.dp),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReportDetailAmountPreview() {
    ReportDetailAmount()
}