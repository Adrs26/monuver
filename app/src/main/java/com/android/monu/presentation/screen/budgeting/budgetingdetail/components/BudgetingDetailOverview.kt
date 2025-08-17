package com.android.monu.presentation.screen.budgeting.budgetingdetail.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.presentation.screen.budgeting.budgetingdetail.BudgetingDetailState
import com.android.monu.presentation.screen.budgeting.components.calculateProgressBar
import com.android.monu.presentation.screen.budgeting.components.formatBudgetingDate
import com.android.monu.presentation.utils.DatabaseCodeMapper
import com.android.monu.presentation.utils.NumberFormatHelper

@Composable
fun BudgetingDetailOverview(
    budgetingState: BudgetingDetailState,
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
            BudgetingDetailData(
                title = stringResource(R.string.category),
                content = stringResource(DatabaseCodeMapper.toParentCategoryTitle(budgetingState.category))
            )
            BudgetingDetailData(
                title = stringResource(R.string.budgeting_period),
                content = stringResource(DatabaseCodeMapper.toBudgetingPeriod(budgetingState.period)),
                modifier = Modifier.padding(top = 8.dp)
            )
            BudgetingDetailData(
                title = stringResource(R.string.budgeting_time_period),
                content = formatBudgetingDate(budgetingState.startDate, budgetingState.endDate),
                modifier = Modifier.padding(top = 8.dp)
            )
            BudgetingDetailData(
                title = stringResource(R.string.maximum_amount),
                content = NumberFormatHelper.formatToRupiah(budgetingState.maxAmount),
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = {
                        calculateProgressBar(budgetingState.usedAmount, budgetingState.maxAmount)
                    },
                    modifier = Modifier
                        .weight(0.8f)
                        .clip(CircleShape)
                        .height(10.dp),
                    color = DatabaseCodeMapper.toParentCategoryIconBackground(budgetingState.category),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Square,
                    gapSize = 0.dp,
                    drawStopIndicator = { null }
                )
                Text(
                    text = stringResource(
                        R.string.percentage_value,
                        NumberFormatHelper.formatToPercentageValue(
                            budgetingState.usedAmount,
                            budgetingState.maxAmount
                        )
                    ),
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.used),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.remain),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
            }
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = NumberFormatHelper.formatToRupiah(budgetingState.usedAmount),
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = NumberFormatHelper.formatToRupiah(
                        if (budgetingState.maxAmount - budgetingState.usedAmount < 0) 0
                        else budgetingState.maxAmount - budgetingState.usedAmount
                    ),
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp)
                )
            }
        }
    }
}

@Composable
fun BudgetingDetailData(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1.5f),
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        )
        Text(
            text = ": $content",
            modifier = Modifier.weight(2.5f),
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
        )
    }
}