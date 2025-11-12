package com.android.monuver.ui.feature.screen.analytics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.R
import com.android.monuver.domain.model.TransactionCategorySummaryState
import com.android.monuver.ui.feature.components.PieChart
import com.android.monuver.utils.DataProvider
import com.android.monuver.ui.feature.utils.DatabaseCodeMapper
import com.android.monuver.utils.NumberHelper
import com.android.monuver.ui.feature.utils.debouncedClickable
import com.android.monuver.ui.theme.SoftWhite

@Composable
fun AnalyticsPieChart(
    pieChartState: AnalyticsPieChartState,
    onTypeChange: (Int) -> Unit,
    onNavigateToAnalyticsCategoryTransaction: (Int, Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.category_recap),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium
            )
            TypeFilterDropdown(
                typeValue = pieChartState.typeFilter,
                onTypeChange = onTypeChange
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            PieChart(values = pieChartState.categorySummaries)
        }
        AnalyticsPieChartDetail(
            categorySummaries = pieChartState.categorySummaries,
            monthValue = pieChartState.monthFilter,
            yearValue = pieChartState.yearFilter,
            onNavigateToAnalyticsCategoryTransaction = onNavigateToAnalyticsCategoryTransaction
        )
    }
}

@Composable
private fun TypeFilterDropdown(
    typeValue: Int,
    onTypeChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val typeFilterOptions = DataProvider.getTransactionTypeFilterOptions().subList(1, 3)

    Box(
        modifier = modifier
    ) {
        AnalyticsFilterDropdown(
            value = stringResource(DatabaseCodeMapper.toTransactionType(typeValue)),
            modifier = Modifier.clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 150.dp),
            shape = MaterialTheme.shapes.small,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            typeFilterOptions.forEach { type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onTypeChange(type)
                            expanded = false
                        }
                        .padding(start = 8.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp),
                        tint = if (type == typeValue) MaterialTheme.colorScheme.onBackground else
                            MaterialTheme.colorScheme.background
                    )
                    Text(
                        text = stringResource(DatabaseCodeMapper.toTransactionType(type)),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun AnalyticsPieChartDetail(
    monthValue: Int,
    yearValue: Int,
    categorySummaries: List<TransactionCategorySummaryState>,
    onNavigateToAnalyticsCategoryTransaction: (Int, Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        categorySummaries.forEach { data ->
            AnalyticsPieChartDetailData(
                category = data.parentCategory,
                amount = data.totalAmount,
                total = categorySummaries.sumOf { it.totalAmount },
                modifier = Modifier
                    .debouncedClickable {
                        onNavigateToAnalyticsCategoryTransaction(
                            data.parentCategory, monthValue, yearValue
                        )
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun AnalyticsPieChartDetailData(
    category: Int,
    amount: Long,
    total: Long,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DatabaseCodeMapper.toParentCategoryIconBackground(category)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(
                    R.string.percentage_value,
                    NumberHelper.formatToPercentageValue(value = amount, total = total)
                ),
                style = MaterialTheme.typography.labelSmall.copy(color = SoftWhite)
            )
        }
        Text(
            text = stringResource(DatabaseCodeMapper.toParentCategoryTitle(category)),
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = NumberHelper.formatToRupiah(amount),
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp)
        )
        Icon(
            painter = painterResource(R.drawable.ic_arrow_forward),
            contentDescription = null,
            modifier = Modifier.padding(start = 4.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

data class AnalyticsPieChartState(
    val typeFilter: Int,
    val monthFilter: Int,
    val yearFilter: Int,
    val categorySummaries: List<TransactionCategorySummaryState>,
)