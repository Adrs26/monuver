package com.android.monu.presentation.screen.reports.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.android.monu.domain.model.TransactionMonthlyAmount
import com.android.monu.ui.theme.Green
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.util.CurrencyFormatHelper
import com.android.monu.util.debouncedClickable
import com.android.monu.util.toFullMonthResourceId

@Composable
fun ReportsListItem(
    transactionMonthlyAmount: TransactionMonthlyAmount,
    modifier: Modifier = Modifier,
    navigateToDetail: () -> Unit
) {
    Card(
        modifier = modifier
            .border(width = 1.dp, color = SoftGrey, shape = RoundedCornerShape(16.dp))
            .debouncedClickable { navigateToDetail() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(transactionMonthlyAmount.month.toFullMonthResourceId()),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
            TotalIncomeExpenseCard(
                totalIncome = transactionMonthlyAmount.totalAmountIncome,
                totalExpense = transactionMonthlyAmount.totalAmountExpense,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun TotalIncomeExpenseCard(
    totalIncome: Long,
    totalExpense: Long,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(width = 1.dp, color = SoftGrey, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            IncomeExpenseItem(
                icon = R.drawable.ic_trending_up,
                iconBackgroundColor = Green,
                title = stringResource(R.string.income),
                value = CurrencyFormatHelper.formatToRupiah(totalIncome)
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = SoftGrey
            )
            IncomeExpenseItem(
                icon = R.drawable.ic_trending_down,
                iconBackgroundColor = Color.Red,
                title = stringResource(R.string.expense),
                value = CurrencyFormatHelper.formatToRupiah(totalExpense)
            )
        }
    }
}

@Composable
fun IncomeExpenseItem(
    icon: Int,
    iconBackgroundColor: Color,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(color = iconBackgroundColor, shape = RoundedCornerShape(16.dp))
                .size(22.dp)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Color.White
            )
        }
        Column(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 2.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            )
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportsListItemPreview() {
    ReportsListItem(
        transactionMonthlyAmount = TransactionMonthlyAmount(
            month = 1,
            totalAmountIncome = 100000L,
            totalAmountExpense = 50000L
        ),
        navigateToDetail = { }
    )
}