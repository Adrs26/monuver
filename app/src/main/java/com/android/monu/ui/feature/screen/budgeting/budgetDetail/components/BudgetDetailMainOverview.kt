package com.android.monu.ui.feature.screen.budgeting.budgetDetail.components

import androidx.compose.foundation.border
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.domain.model.BudgetState
import com.android.monu.ui.feature.screen.budgeting.components.formatBudgetDate
import com.android.monu.ui.feature.utils.DatabaseCodeMapper

@Composable
fun BudgetDetailMainOverview(
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
            BudgetDetailData(
                title = stringResource(R.string.category),
                content = stringResource(DatabaseCodeMapper.toParentCategoryTitle(budgetState.category))
            )
            BudgetDetailData(
                title = stringResource(R.string.cycle),
                content = stringResource(DatabaseCodeMapper.toCycle(budgetState.cycle)),
                modifier = Modifier.padding(top = 8.dp)
            )
            BudgetDetailData(
                title = stringResource(R.string.budgeting_time_period),
                content = formatBudgetDate(budgetState.startDate, budgetState.endDate),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun BudgetDetailData(
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