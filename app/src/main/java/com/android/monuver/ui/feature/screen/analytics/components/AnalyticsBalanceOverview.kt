package com.android.monuver.ui.feature.screen.analytics.components

import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.unit.sp
import com.android.monuver.R
import com.android.monuver.data.datastore.ThemeSetting
import com.android.monuver.domain.model.TransactionBalanceSummaryState
import com.android.monuver.ui.theme.Green100
import com.android.monuver.ui.theme.Green600
import com.android.monuver.ui.theme.Red100
import com.android.monuver.ui.theme.Red600
import com.android.monuver.utils.NumberHelper

@Composable
fun AnalyticsBalanceOverview(
    amountSummary: TransactionBalanceSummaryState,
    themeSetting: ThemeSetting,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BalanceData(
            title = stringResource(R.string.total_income),
            containerColor = changeIncomeContainerColor(themeSetting),
            totalAmount = amountSummary.totalIncomeAmount,
            averageAmount = amountSummary.averageIncomeAmount,
            modifier = Modifier.weight(1f)
        )
        BalanceData(
            title = stringResource(R.string.total_expense),
            containerColor = changeExpenseContainerColor(themeSetting),
            totalAmount = amountSummary.totalExpenseAmount,
            averageAmount = amountSummary.averageExpenseAmount,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun BalanceData(
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
                text = NumberHelper.formatToRupiah(totalAmount),
                modifier = Modifier.padding(top = 2.dp),
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
            )
            Text(
                text = stringResource(R.string.average_per_day),
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = NumberHelper.formatToRupiah(averageAmount.toLong()),
                modifier = Modifier.padding(top = 2.dp),
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
            )
        }
    }
}

@Composable
private fun changeIncomeContainerColor(themeSetting: ThemeSetting): Color {
    return when (themeSetting) {
        ThemeSetting.LIGHT -> Green100
        ThemeSetting.DARK -> Green600
        ThemeSetting.SYSTEM -> {
            if (isSystemInDarkTheme()) Green600 else Green100
        }
    }
}

@Composable
private fun changeExpenseContainerColor(themeSetting: ThemeSetting): Color {
    return when (themeSetting) {
        ThemeSetting.LIGHT -> Red100
        ThemeSetting.DARK -> Red600
        ThemeSetting.SYSTEM -> {
            if (isSystemInDarkTheme()) Red600 else Red100
        }
    }
}