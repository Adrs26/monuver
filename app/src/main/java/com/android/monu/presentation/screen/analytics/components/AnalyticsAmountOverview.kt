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
import androidx.compose.ui.unit.dp
import com.android.monu.domain.model.transaction.TransactionMonthlyAmountOverview
import com.android.monu.presentation.utils.NumberFormatHelper

@Composable
fun AnalyticsAmountOverview(
    transactionAmount: TransactionMonthlyAmountOverview,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnalyticsAmountData(
            title = "Total pemasukan",
            containerColor = Color(0xFFC8E6C9),
            totalAmount = transactionAmount.totalIncomeAmount,
            averageAmount = transactionAmount.averageIncomeAmount,
            modifier = Modifier.weight(1f)
        )
        AnalyticsAmountData(
            title = "Total pengeluaran",
            containerColor = Color(0xFFFFCDD2),
            totalAmount = transactionAmount.totalExpenseAmount,
            averageAmount = transactionAmount.averageExpenseAmount,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun AnalyticsAmountData(
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
                text = "Rata-rata per hari",
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