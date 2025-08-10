package com.android.monu.presentation.screen.budgeting.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.presentation.utils.DatabaseCodeMapper
import com.android.monu.presentation.utils.DateHelper
import com.android.monu.presentation.utils.NumberFormatHelper

@Composable
fun BudgetingListItem(
    budgetingState: BudgetingListItemState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BudgetingListItemIcon(
                color = DatabaseCodeMapper.toParentCategoryIconBackground(budgetingState.category),
                usedAmount = budgetingState.usedAmount,
                maxAmount = budgetingState.maxAmount
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(DatabaseCodeMapper.toParentCategoryTitle(budgetingState.category)),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    text = formatBudgetingDate(
                        startDate = budgetingState.startDate,
                        endDate = budgetingState.endDate
                    ),
                    modifier = Modifier.padding(top = 4.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = NumberFormatHelper.formatToRupiah(budgetingState.usedAmount),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 13.sp
                        )
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(12.dp)
                            .padding(bottom = 2.dp),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                    Text(
                        text = NumberFormatHelper.formatToRupiah(budgetingState.maxAmount),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.padding(start = 16.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun BudgetingListItemIcon(
    color: Color,
    usedAmount: Long,
    maxAmount: Long
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { calculateProgressBar(usedAmount, maxAmount) },
            modifier = Modifier.size(48.dp),
            color = color,
            strokeWidth = 4.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Square,
            gapSize = 0.dp
        )
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraSmall)
                .size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(
                    R.string.percentage_value,
                    NumberFormatHelper.formatToPercentageValue(usedAmount, maxAmount)
                ),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

private fun formatBudgetingDate(startDate: String, endDate: String): String {
    return if (startDate == endDate) {
        DateHelper.formatToShortDate(startDate)
    } else {
        "${DateHelper.formatToShortDate(startDate)} - ${DateHelper.formatToShortDate(endDate)}"
    }
}

data class BudgetingListItemState(
    val id: Long,
    val category: Int,
    val startDate: String,
    val endDate: String,
    val maxAmount: Long,
    val usedAmount: Long,
)