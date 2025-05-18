package com.android.monu.presentation.screen.analytics.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.data.model.ScaleLabel
import com.android.monu.presentation.screen.analytics.AnalyticsViewModel
import com.android.monu.presentation.components.TypeFilterButton
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.util.CurrencyFormatHelper
import com.android.monu.util.toHighestRangeValue

@Composable
fun AnalyticsBarChartContent(
    viewModel: AnalyticsViewModel,
    selectedType: String,
    values: List<Long>,
    dates: List<String>,
    monthLabels: List<Int>,
    scaleLabels: List<ScaleLabel>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(
            width = 1.dp,
            color = SoftGrey,
            shape = RoundedCornerShape(16.dp)
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ChartTopBar(title = stringResource(R.string.transactions_overview))
            BarChartTypeFilterButton(
                viewModel = viewModel,
                selectedType = selectedType,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
            )
            BarChart(
                values = values,
                dates = dates,
                monthLabels = monthLabels,
                scaleLabels = scaleLabels,
                modifier = Modifier.padding(top = 32.dp)
            )
        }
    }
}

@Composable
fun BarChartTypeFilterButton(
    viewModel: AnalyticsViewModel,
    selectedType: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.background(color = Color.LightGray, shape = RoundedCornerShape(24.dp))
    ) {
        listOf(
            stringResource(R.string.balance),
            stringResource(R.string.income),
            stringResource(R.string.expense)
        ).forEach { type ->
            val isSelected = selectedType == type
            TypeFilterButton(
                transactionsType = type,
                background = if (isSelected) Color.White else Color.LightGray,
                textColor = Color.Black,
                horizontalPadding = 8.dp,
                verticalPadding = 4.dp,
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp),
                onClick = { viewModel.selectType(type) }
            )
        }
    }
}

@Composable
fun BarChart(
    values: List<Long>,
    dates: List<String>,
    monthLabels: List<Int>,
    scaleLabels: List<ScaleLabel>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            BarChartYAxis(
                scaleLabels = scaleLabels,
                modifier = Modifier.padding(end = 8.dp)
            )
            BarChartGraph(
                values = values,
                dates = dates
            )
        }
        BarChartXAxis(
            monthLabels = monthLabels,
            modifier = Modifier.padding(start = 48.dp, top = 8.dp)
        )
    }
}

@Composable
fun BarChartYAxis(
    scaleLabels: List<ScaleLabel>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(40.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        scaleLabels.forEach {
            Column(
                modifier = modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = CurrencyFormatHelper.rupiahToShortFormat(it.amount),
                    style = TextStyle(
                        fontSize = 7.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.fillMaxHeight(it.fraction))
            }
        }
    }
}

@Composable
fun BarChartGraph(
    values: List<Long>,
    dates: List<String>,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEachIndexed { index, value ->
            val isSelected = index == selectedIndex
            val backgroundColor = if (isSelected) Blue else Color.LightGray
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

            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .clip(CircleShape)
                    .weight(1f)
                    .fillMaxHeight(
                        value.toFloat() / values
                            .max()
                            .toHighestRangeValue()
                            .toFloat()
                    )
                    .offset { IntOffset(x = 0, y = offsetY.value.toInt()) }
                    .background(backgroundColor)
                    .clickable { selectedIndex = index }
            )

            DropdownMenu(
                expanded = selectedIndex == index,
                onDismissRequest = { selectedIndex = -1 },
                modifier = Modifier.background(color = Color.White),
                offset = DpOffset(x = 20.dp, y = (-200).dp)
            ) {
                Text(
                    text = "${dates[index]} : ${CurrencyFormatHelper.formatToRupiah(value)}",
                    modifier = Modifier.padding(8.dp),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                )
            }
        }
    }
}

@Composable
fun BarChartXAxis(
    monthLabels: List<Int>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        monthLabels.forEach {
            Text(
                text = stringResource(it),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 7.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
        }
    }
}
