package com.android.monu.presentation.screen.analytics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.android.monu.domain.model.AverageTransactionAmount
import com.android.monu.ui.theme.Green
import com.android.monu.ui.theme.Red
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.util.CurrencyFormatHelper
import kotlin.math.roundToLong

@Composable
fun AnalyticsAverageAmount(
    averageTransactionAmount: AverageTransactionAmount,
    modifier: Modifier = Modifier
) {
    val dailyAverageIncome = averageTransactionAmount.dailyAverageIncome.roundToLong()
    val monthlyAverageIncome = averageTransactionAmount.monthlyAverageIncome.roundToLong()
    val yearlyAverageIncome = averageTransactionAmount.yearlyAverageIncome.roundToLong()

    val dailyAverageExpense = averageTransactionAmount.dailyAverageExpense.roundToLong()
    val monthlyAverageExpense = averageTransactionAmount.monthlyAverageExpense.roundToLong()
    val yearlyAverageExpense = averageTransactionAmount.yearlyAverageExpense.roundToLong()

    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        AverageAmountCard(
            backgroundColor = Green,
            icon = R.drawable.ic_trending_up,
            iconColor = Green,
            dailyTitle = stringResource(R.string.daily_avg_income),
            dailyAmount = CurrencyFormatHelper.formatToRupiah(dailyAverageIncome),
            monthlyTitle = stringResource(R.string.monthly_avg_income),
            monthlyAmount = CurrencyFormatHelper.formatToRupiah(monthlyAverageIncome),
            yearlyTitle = stringResource(R.string.yearly_avg_income),
            yearlyAmount = CurrencyFormatHelper.formatToRupiah(yearlyAverageIncome),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        AverageAmountCard(
            backgroundColor = Red,
            icon = R.drawable.ic_trending_down,
            iconColor = Red,
            dailyTitle = stringResource(R.string.daily_avg_expense),
            dailyAmount = CurrencyFormatHelper.formatToRupiah(dailyAverageExpense),
            monthlyTitle = stringResource(R.string.monthly_avg_expense),
            monthlyAmount = CurrencyFormatHelper.formatToRupiah(monthlyAverageExpense),
            yearlyTitle = stringResource(R.string.yearly_avg_expense),
            yearlyAmount = CurrencyFormatHelper.formatToRupiah(yearlyAverageExpense),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun AverageAmountCard(
    backgroundColor: Color,
    icon: Int,
    iconColor: Color,
    dailyTitle: String,
    dailyAmount: String,
    monthlyTitle: String,
    monthlyAmount: String,
    yearlyTitle: String,
    yearlyAmount: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = iconColor
                )
            }
            AverageAmountText(
                title = dailyTitle,
                amount = dailyAmount,
                modifier = Modifier.padding(top = 32.dp)
            )
            AverageAmountText(
                title = monthlyTitle,
                amount = monthlyAmount,
                modifier = Modifier.padding(top = 12.dp)
            )
            AverageAmountText(
                title = yearlyTitle,
                amount = yearlyAmount,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
fun AverageAmountText(
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
fun AnalyticsAverageAmountPreview() {
    AnalyticsAverageAmount(
        averageTransactionAmount = AverageTransactionAmount(
            dailyAverageIncome = 0.0,
            monthlyAverageIncome = 0.0,
            yearlyAverageIncome = 0.0,
            dailyAverageExpense = 0.0,
            monthlyAverageExpense = 0.0,
            yearlyAverageExpense = 0.0
        )
    )
}