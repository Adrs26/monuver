package com.android.monu.presentation.screen.analytics.components

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
import com.android.monu.R
import com.android.monu.domain.model.transaction.TransactionCategorySummary
import com.android.monu.presentation.components.PieChart
import com.android.monu.presentation.utils.DataProvider
import com.android.monu.presentation.utils.DatabaseCodeMapper
import com.android.monu.presentation.utils.NumberFormatHelper

@Composable
fun AnalyticsPieChart(
    typeValue: Int,
    parentCategoriesSummary: List<TransactionCategorySummary>,
    modifier: Modifier = Modifier,
    onTypeChange: (Int) -> Unit
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
                typeValue = typeValue,
                onTypeChange = onTypeChange
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            PieChart(values = parentCategoriesSummary)
        }
        AnalyticsPieChartDetail(parentCategoriesSummary = parentCategoriesSummary)
    }
}

@Composable
fun TypeFilterDropdown(
    typeValue: Int,
    modifier: Modifier = Modifier,
    onTypeChange: (Int) -> Unit
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
fun AnalyticsPieChartDetail(
    parentCategoriesSummary: List<TransactionCategorySummary>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        parentCategoriesSummary.forEach { data ->
            AnalyticsPieChartDetailData(
                category = data.parentCategory,
                amount = data.totalAmount,
                total = parentCategoriesSummary.sumOf { it.totalAmount },
                modifier = Modifier
                    .clickable { }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun AnalyticsPieChartDetailData(
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
                .width(36.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DatabaseCodeMapper.toParentCategoryIconBackground(category)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(
                    R.string.percentage_value,
                    NumberFormatHelper.formatToPercentageValue(value = amount, total = total)
                ),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.background
                )
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
            text = NumberFormatHelper.formatToRupiah(amount),
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
        )
        Icon(
            painter = painterResource(R.drawable.ic_arrow_forward),
            contentDescription = null,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}