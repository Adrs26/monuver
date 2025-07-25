package com.android.monu.presentation.screen.analytics.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.domain.model.transaction.TransactionDailySummary
import com.android.monu.presentation.utils.DateHelper
import com.android.monu.presentation.utils.NumberFormatHelper
import com.android.monu.presentation.utils.toHighestRangeValue
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.Green600
import com.android.monu.ui.theme.Red600

@Composable
fun AnalyticsBarChart(
    transactionWeeklySummary: List<TransactionDailySummary>,
    analyticsBarChartState: AnalyticsBarChartState,
    modifier: Modifier = Modifier,
    onWeekChange: (Int) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Rekap transaksi",
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BarChartLegend(
                    legendColor = Color(0xFF66BB6A),
                    legendLabel = stringResource(R.string.income)
                )
                BarChartLegend(
                    legendColor = Color(0xFFEF5350),
                    legendLabel = stringResource(R.string.expense),
                )
            }
            WeekFilterDropdown(
                analyticsBarChartState = analyticsBarChartState,
                onWeekChange = onWeekChange
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            BarChartYAxis(
                transactionWeeklySummary = transactionWeeklySummary,
                modifier = Modifier.padding(end = 4.dp)
            )
            BarChartGraph(transactionWeeklySummary = transactionWeeklySummary)
        }
        BarChartXAxis(
            transactionWeeklySummary = transactionWeeklySummary,
            modifier = Modifier.padding(start = 52.dp, end = 4.dp, top = 4.dp)
        )
    }
}

@Composable
fun WeekFilterDropdown(
    analyticsBarChartState: AnalyticsBarChartState,
    modifier: Modifier = Modifier,
    onWeekChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val weekFilterOptions = DateHelper.getWeekOptions(
        month = analyticsBarChartState.monthFilter,
        year = analyticsBarChartState.yearFilter
    )

    Box(
        modifier = modifier
    ) {
        AnalyticsFilterDropdown(
            value = DateHelper.formatToWeekString(weekNumber = analyticsBarChartState.weekFilter),
            modifier = Modifier.clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 150.dp),
            shape = MaterialTheme.shapes.small,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            weekFilterOptions.forEach { week ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onWeekChange(week)
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
                        tint = if (week == analyticsBarChartState.weekFilter)
                            MaterialTheme.colorScheme.onBackground else
                                MaterialTheme.colorScheme.background
                    )
                    Text(
                        text = DateHelper.formatToWeekString(week),
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
fun BarChartLegend(
    legendColor: Color,
    legendLabel: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = modifier
                .size(8.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(legendColor)
        )
        Text(
            text = legendLabel,
            modifier = Modifier.padding(start = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun BarChartYAxis(
    transactionWeeklySummary: List<TransactionDailySummary>,
    modifier: Modifier = Modifier
) {
    val maxIncome = transactionWeeklySummary.maxOfOrNull { it.totalIncome } ?: 0
    val maxExpense = transactionWeeklySummary.maxOfOrNull { it.totalExpense } ?: 0
    val max = maxOf(maxIncome, maxExpense).toHighestRangeValue()
    val scaleLabels = calculateScaleLabel(max)

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(44.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        scaleLabels.forEach {
            Column(
                modifier = modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = NumberFormatHelper.formatToShortRupiah(it.amount),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp)
                )
                Spacer(modifier = Modifier.fillMaxHeight(it.fraction))
            }
        }
    }
}

@Composable
fun BarChartGraph(
    transactionWeeklySummary: List<TransactionDailySummary>,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }

    val maxIncome = transactionWeeklySummary.maxOfOrNull { it.totalIncome } ?: 0
    val maxExpense = transactionWeeklySummary.maxOfOrNull { it.totalExpense } ?: 0
    val max = maxOf(maxIncome, maxExpense).toHighestRangeValue()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        transactionWeeklySummary.forEachIndexed { index, value ->
            val incomeGraphValue = value.totalIncome.toFloat() / max
            val expenseGraphValue = value.totalExpense.toFloat() / max

            val isSelected = index == selectedIndex
            val offsetY = remember { Animatable(300f) }
            LaunchedEffect(Unit) {
                offsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = FastOutSlowInEasing
                    )
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { selectedIndex = index }
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .weight(1f)
                        .fillMaxHeight(incomeGraphValue)
                        .offset { IntOffset(x = 0, y = offsetY.value.toInt()) }
                        .background(if (isSelected) Blue else Green600)
                )
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .weight(1f)
                        .fillMaxHeight(expenseGraphValue)
                        .offset { IntOffset(x = 0, y = offsetY.value.toInt()) }
                        .background(if (isSelected) Blue else Red600)
                )
            }
            BarChartGraphInformation(
                transactionWeeklySummary = transactionWeeklySummary,
                selectedIndex = selectedIndex,
                index = index,
                onSelectedIndexReset = { selectedIndex = -1 }
            )
        }
    }
}

@Composable
fun BarChartGraphInformation(
    transactionWeeklySummary: List<TransactionDailySummary>,
    selectedIndex: Int,
    index: Int,
    onSelectedIndexReset: () -> Unit
) {
    DropdownMenu(
        expanded = selectedIndex == index,
        onDismissRequest = onSelectedIndexReset,
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
        offset = DpOffset(x = 30.dp, y = (-180).dp),
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Pemasukan: ${NumberFormatHelper.formatToRupiah(transactionWeeklySummary[index].totalIncome)}",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp
                )
            )
            Text(
                text = "Pengeluaran: ${NumberFormatHelper.formatToRupiah(transactionWeeklySummary[index].totalExpense)}",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp
                )
            )
            Text(
                text = DateHelper.formatDateToReadable(transactionWeeklySummary[index].date),
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun BarChartXAxis(
    transactionWeeklySummary: List<TransactionDailySummary>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        transactionWeeklySummary.forEach {
            Text(
                text = DateHelper.formatToShortDate(it.date),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

private fun calculateScaleLabel(value: Long): List<BarChartScaleLabel> {
    val firstScale = 0L
    val secondScale = value / 4
    val thirdScale = value / 2
    val fourthScale = value - secondScale
    val lastScale = value

    return listOf(
        BarChartScaleLabel(amount = firstScale, fraction = 0f),
        BarChartScaleLabel(amount = secondScale, fraction = 0.25f),
        BarChartScaleLabel(amount = thirdScale, fraction = 0.5f),
        BarChartScaleLabel(amount = fourthScale, fraction = 0.75f),
        BarChartScaleLabel(amount = lastScale, fraction = 1f)
    )
}

data class AnalyticsBarChartState(
    val monthFilter: Int,
    val yearFilter: Int,
    val weekFilter: Int,
)

data class BarChartScaleLabel(
    val amount: Long,
    val fraction: Float
)
