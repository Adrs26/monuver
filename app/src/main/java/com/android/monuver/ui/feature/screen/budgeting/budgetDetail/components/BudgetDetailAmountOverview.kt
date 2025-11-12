package com.android.monuver.ui.feature.screen.budgeting.budgetDetail.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.R
import com.android.monuver.domain.model.BudgetState
import com.android.monuver.ui.feature.screen.budgeting.components.calculateProgressBar
import com.android.monuver.ui.feature.utils.DatabaseCodeMapper
import com.android.monuver.utils.NumberHelper

@Composable
fun BudgetDetailAmountOverview(
    budgetState: BudgetState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = {
                        calculateProgressBar(budgetState.usedAmount, budgetState.maxAmount)
                    },
                    modifier = Modifier
                        .weight(0.8f)
                        .clip(CircleShape)
                        .height(10.dp),
                    color = DatabaseCodeMapper.toParentCategoryIconBackground(budgetState.category),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Square,
                    gapSize = 0.dp,
                    drawStopIndicator = { null }
                )
                Text(
                    text = stringResource(
                        R.string.percentage_value,
                        NumberHelper.formatToPercentageValue(
                            value = budgetState.usedAmount,
                            total = budgetState.maxAmount
                        )
                    ),
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp)
                )
            }
            Row(
                modifier = Modifier.padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.used),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.maximum_amount),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = NumberHelper.formatToRupiah(budgetState.usedAmount),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = NumberHelper.formatToRupiah(budgetState.maxAmount),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
                )
            }
        }
    }
}