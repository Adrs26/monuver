package com.android.monu.presentation.screen.analytics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.domain.model.transaction.TransactionAmountSummary
import com.android.monu.presentation.utils.NumberFormatHelper

@Composable
fun AnalyticsBalanceOverview(
    amountSummary: TransactionAmountSummary,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BalanceData(
            title = stringResource(R.string.total_income),
            containerColor = Color(0xFFC8E6C9),
            totalAmount = amountSummary.totalIncomeAmount,
            averageAmount = amountSummary.averageIncomeAmount,
            modifier = Modifier.weight(1f)
        )
        BalanceData(
            title = stringResource(R.string.total_expense),
            containerColor = Color(0xFFFFCDD2),
            totalAmount = amountSummary.totalExpenseAmount,
            averageAmount = amountSummary.averageExpenseAmount,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun BalanceData(
    title: String,
    containerColor: Color,
    totalAmount: Long,
    averageAmount: Double,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = NumberFormatHelper.formatToRupiah(totalAmount),
                modifier = Modifier.padding(top = 2.dp),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = stringResource(R.string.average_per_day),
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = NumberFormatHelper.formatToRupiah(averageAmount.toLong()),
                modifier = Modifier.padding(top = 2.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}