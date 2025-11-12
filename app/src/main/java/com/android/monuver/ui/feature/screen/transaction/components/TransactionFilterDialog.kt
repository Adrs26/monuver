package com.android.monuver.ui.feature.screen.transaction.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.monuver.R
import com.android.monuver.utils.DataProvider
import com.android.monuver.ui.feature.utils.DatabaseCodeMapper
import com.android.monuver.utils.Month
import com.android.monuver.utils.TransactionType
import kotlin.math.ceil

@Composable
fun TransactionFilterDialog(
    filterState: TransactionFilterState,
    onDismissRequest: () -> Unit,
    onFilterApply: (Int?, Int?, Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            TransactionFilterMenu(
                filterState = filterState,
                onDismissRequest = onDismissRequest,
                onFilterApply = onFilterApply
            )
        }
    }
}

@Composable
private fun TransactionFilterMenu(
    filterState: TransactionFilterState,
    onDismissRequest: () -> Unit,
    onFilterApply: (Int?, Int?, Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    val typeFilterOptions = DataProvider.getTransactionTypeFilterOptions()
    val yearFilterOptions = listOf(0) + filterState.yearFilterOptions
    val monthFilterOptions = DataProvider.getMonthFilterOptions()

    var tempTypeFilter by remember { mutableStateOf(filterState.typeFilter) }
    var tempYearFilter by remember { mutableStateOf(filterState.yearFilter) }
    var tempMonthFilter by remember { mutableStateOf(filterState.monthFilter) }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        RadioGrid(
            title = stringResource(R.string.transaction_type),
            options = typeFilterOptions,
            selectedOption = tempTypeFilter ?: 0,
            onOptionSelect = { type ->
                tempTypeFilter = if (type == TransactionType.ALL) null else type
            },
            itemPerRow = 2
        )
        Spacer(modifier = Modifier.height(24.dp))
        RadioGrid(
            title = stringResource(R.string.transaction_year),
            options = yearFilterOptions,
            selectedOption = tempYearFilter ?: 0,
            onOptionSelect = { year ->
                tempYearFilter = if (year == Month.ALL) null else year
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        RadioGrid(
            title = stringResource(R.string.transaction_month),
            options = monthFilterOptions,
            selectedOption = tempMonthFilter ?: 0,
            onOptionSelect = { month ->
                tempMonthFilter = if (month == Month.ALL) null else month
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onDismissRequest,
                modifier = Modifier.weight(1f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp
                    )
                )
            }
            Button(
                onClick = {
                    onFilterApply(tempTypeFilter, tempYearFilter, tempMonthFilter)
                    onDismissRequest()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.apply),
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
                )
            }
        }
    }
}

@Composable
private fun RadioGrid(
    title: String,
    options: List<Int>,
    selectedOption: Int,
    onOptionSelect: (Int) -> Unit,
    itemPerRow: Int = 3
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium
    )
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.surfaceVariant
    )

    val rows = ceil(options.size / itemPerRow.toDouble()).toInt()
    for (rowIndex in 0 until rows) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (colIndex in 0 until itemPerRow) {
                val index = rowIndex * itemPerRow + colIndex
                if (index < options.size) {
                    val option = options[index]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onOptionSelect(option) }
                    ) {
                        RadioButton(
                            selected = selectedOption == option,
                            onClick = { onOptionSelect(option) },
                            modifier = Modifier.size(40.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            text = when (option) {
                                0 -> stringResource(R.string.all)
                                in 1..12 -> stringResource(
                                    DatabaseCodeMapper.toShortMonth(option)
                                )
                                in 1001..1003 -> stringResource(
                                    DatabaseCodeMapper.toTransactionType(option)
                                )
                                else -> option.toString()
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 13.sp
                            )
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

data class TransactionFilterState(
    val yearFilterOptions: List<Int>,
    val typeFilter: Int?,
    val yearFilter: Int?,
    val monthFilter: Int?
)