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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.android.monu.ui.theme.Blue
import com.android.monu.presentation.utils.NumberFormatHelper

@Composable
fun AnalyticsBarChart(
    scaleLabels: List<BarChartScaleLabel>,
    modifier: Modifier = Modifier
) {
    val dummyList = listOf(1, 1, 1, 1, 1, 1, 1)

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
            AnalyticsFilterDropdown(value = "Minggu ke-1")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            BarChartYAxis(
                scaleLabels = scaleLabels,
                modifier = Modifier.padding(end = 4.dp)
            )
            BarChartGraph(dummyList = dummyList)
        }
        BarChartXAxis(
            dummyList = dummyList,
            modifier = Modifier.padding(start = 52.dp, end = 4.dp, top = 4.dp)
        )
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
    scaleLabels: List<BarChartScaleLabel>,
    modifier: Modifier = Modifier
) {
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
    dummyList: List<Int>,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        dummyList.forEachIndexed { index, value ->
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
                        .fillMaxHeight(0.75f)
                        .offset { IntOffset(x = 0, y = offsetY.value.toInt()) }
                        .background(if (isSelected) Blue else Color(0xFF66BB6A))
                )
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .weight(1f)
                        .fillMaxHeight(0.5f)
                        .offset { IntOffset(x = 0, y = offsetY.value.toInt()) }
                        .background(if (isSelected) Blue else Color(0xFFEF5350))
                )
            }
            BarChartGraphInformation(
                selectedIndex = selectedIndex,
                index = index,
                onSelectedIndexReset = { selectedIndex = -1 }
            )
        }
    }
}

@Composable
fun BarChartGraphInformation(
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
                text = "Pemasukan: Rp300.000",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp
                )
            )
            Text(
                text = "Pengeluaran: Rp200.000",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp
                )
            )
            Text(
                text = "13 Juni 2025",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun BarChartXAxis(
    dummyList: List<Int>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        dummyList.forEach {
            Text(
                text = "13/06",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

data class BarChartScaleLabel(
    val amount: Long,
    val fraction: Float
)
