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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.domain.model.AverageTransactionAmount
import com.android.monu.ui.theme.Green
import com.android.monu.ui.theme.Red
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.utils.NumberFormatHelper
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

    val analyticsAverageIncomeAmountCardData = AnalyticsAverageAmountCardData(
        dailyTitle = stringResource(R.string.daily_avg_income),
        dailyAmount = dailyAverageIncome,
        monthlyTitle = stringResource(R.string.monthly_avg_income),
        monthlyAmount = monthlyAverageIncome,
        yearlyTitle = stringResource(R.string.yearly_avg_income),
        yearlyAmount = yearlyAverageIncome,
    )

    val analyticsAverageExpenseAmountCardData = AnalyticsAverageAmountCardData(
        dailyTitle = stringResource(R.string.daily_avg_expense),
        dailyAmount = dailyAverageExpense,
        monthlyTitle = stringResource(R.string.monthly_avg_expense),
        monthlyAmount = monthlyAverageExpense,
        yearlyTitle = stringResource(R.string.yearly_avg_expense),
        yearlyAmount = yearlyAverageExpense,
    )

    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        AverageAmountCard(
            icon = R.drawable.ic_trending_up,
            iconBackgroundColor = Green,
            analyticsAverageAmountCardData = analyticsAverageIncomeAmountCardData,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 4.dp)
        )
        AverageAmountCard(
            icon = R.drawable.ic_trending_down,
            iconBackgroundColor = Red,
            analyticsAverageAmountCardData = analyticsAverageExpenseAmountCardData,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp, end = 8.dp)
        )
    }
}

@Composable
fun AverageAmountCard(
    icon: Int,
    iconBackgroundColor: Color,
    analyticsAverageAmountCardData: AnalyticsAverageAmountCardData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(color = iconBackgroundColor, shape = RoundedCornerShape(16.dp))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
            AverageAmountText(
                title = analyticsAverageAmountCardData.dailyTitle,
                amount = analyticsAverageAmountCardData.dailyAmount,
                textColor = iconBackgroundColor,
                modifier = Modifier.padding(top = 32.dp)
            )
            AverageAmountText(
                title = analyticsAverageAmountCardData.monthlyTitle,
                amount = analyticsAverageAmountCardData.monthlyAmount,
                textColor = iconBackgroundColor,
                modifier = Modifier.padding(top = 12.dp)
            )
            AverageAmountText(
                title = analyticsAverageAmountCardData.yearlyTitle,
                amount = analyticsAverageAmountCardData.yearlyAmount,
                textColor = iconBackgroundColor,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
fun AverageAmountText(
    title: String,
    amount: Long,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )
        Text(
            text = NumberFormatHelper.formatToRupiah(amount),
            modifier = Modifier.padding(top = 2.dp),
            style = TextStyle(
                fontSize = changeFontSize(amount),
                fontFamily = interFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        )
    }
}

fun changeFontSize(amount: Long): TextUnit {
    return when {
        amount < 1_000_000_000 -> 16.sp
        amount < 10_000_000_000 -> 15.sp
        amount < 100_000_000_000 -> 14.sp
        else -> 13.sp
    }
}

data class AnalyticsAverageAmountCardData(
    val dailyTitle: String,
    val dailyAmount: Long,
    val monthlyTitle: String,
    val monthlyAmount: Long,
    val yearlyTitle: String,
    val yearlyAmount: Long,
)

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