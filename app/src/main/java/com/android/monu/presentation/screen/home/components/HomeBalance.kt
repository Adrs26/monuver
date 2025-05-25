package com.android.monu.presentation.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import com.android.monu.util.CurrencyFormatHelper

@Composable
fun HomeBalance(
    totalIncomeAmount: Long,
    totalExpenseAmount: Long,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Blue),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.your_balance),
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                )
                BalanceFilter()
            }
            Text(
                text = CurrencyFormatHelper.formatToRupiah(totalIncomeAmount - totalExpenseAmount),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            )
            IncomeExpenseCard(
                totalIncomeAmount = totalIncomeAmount,
                totalExpenseAmount = totalExpenseAmount,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun BalanceFilter(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = Blue, shape = CircleShape)
            .border(width = 0.5.dp, color = Color.White, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "All Time",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            style = TextStyle(
                fontSize = 10.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        )
    }
}

@Composable
fun IncomeExpenseCard(
    totalIncomeAmount: Long,
    totalExpenseAmount: Long,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IncomeExpenseItem(
                icon = R.drawable.ic_trending_up,
                color = Green,
                title = stringResource(R.string.income),
                value = CurrencyFormatHelper.formatToRupiah(totalIncomeAmount),
                modifier = Modifier.weight(1f)
            )
            VerticalDivider(
                modifier = Modifier
                    .height(32.dp)
                    .padding(horizontal = 4.dp),
                thickness = 0.5.dp,
                color = Color.LightGray
            )
            IncomeExpenseItem(
                icon = R.drawable.ic_trending_down,
                color = Color.Red,
                title = stringResource(R.string.expense),
                value = CurrencyFormatHelper.formatToRupiah(totalExpenseAmount),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun IncomeExpenseItem(
    icon: Int,
    color: Color,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.padding(start = 4.dp, end = 8.dp),
            tint = color
        )
        Column(
            modifier = Modifier.padding(2.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 11.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            )
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 12.sp,
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
fun HomeBalancePreview() {
    HomeBalance(
        totalIncomeAmount = 1000000L,
        totalExpenseAmount = 500000L
    )
}