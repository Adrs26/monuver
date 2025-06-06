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
import androidx.compose.foundation.layout.fillMaxSize
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
import com.android.monu.domain.model.TransactionOverview
import com.android.monu.presentation.components.TypeFilterButton
import com.android.monu.presentation.screen.analytics.AnalyticsFilterCallbacks
import com.android.monu.presentation.screen.analytics.AnalyticsFilterState
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.utils.DataHelper
import com.android.monu.utils.NumberFormatHelper
import com.android.monu.utils.extensions.toFullMonthResourceId
import com.android.monu.utils.extensions.toHighestRangeValue
import com.android.monu.utils.extensions.toTransactionType
import com.android.monu.utils.extensions.toTransactionTypeCode

@Composable
fun AnalyticsBarChart(
    transactionsOverview: List<TransactionOverview>,
    scaleLabels: List<BarChartScaleLabel>,
    filterState: AnalyticsFilterState,
    filterCallbacks: AnalyticsFilterCallbacks,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AnalyticsChartTopBar(
                title = stringResource(R.string.transactions_overview),
                chartType = 1,
                filterState = filterState,
                filterCallbacks = filterCallbacks
            )
            BarChartTypeFilterButton(
                selectedType = filterState.barChartSelectedType,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
                onFilterTypeClick = filterCallbacks.onBarChartFilterTypeClick
            )
            BarChart(
                transactionsOverview = transactionsOverview,
                scaleLabels = scaleLabels,
                modifier = Modifier.padding(top = 32.dp)
            )
        }
    }
}

@Composable
fun BarChartTypeFilterButton(
    selectedType: Int,
    modifier: Modifier = Modifier,
    onFilterTypeClick: (Int) -> Unit
) {
    Row(
        modifier = modifier.background(color = Color.LightGray, shape = RoundedCornerShape(24.dp))
    ) {
        listOf(R.string.income, R.string.expense).forEach { type ->
            val isSelected = stringResource(selectedType.toTransactionType()) ==
                    stringResource(type)
            TypeFilterButton(
                transactionsType = stringResource(type),
                background = if (isSelected) Color.White else Color.LightGray,
                textColor = Color.Black,
                horizontalPadding = 8.dp,
                verticalPadding = 8.dp,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                onClick = { onFilterTypeClick(type.toTransactionTypeCode() ?: 0) }
            )
        }
    }
}

@Composable
fun BarChart(
    transactionsOverview: List<TransactionOverview>,
    scaleLabels: List<BarChartScaleLabel>,
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
            BarChartGraph(transactionsOverview = transactionsOverview)
        }
        BarChartXAxis(modifier = Modifier.padding(start = 48.dp, top = 8.dp))
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
            .width(40.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        scaleLabels.forEach {
            Column(
                modifier = modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = NumberFormatHelper.formatToShortRupiah(it.amount),
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
    transactionsOverview: List<TransactionOverview>,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        transactionsOverview.forEachIndexed { index, value ->
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
                        value.amount.toFloat() /
                                (transactionsOverview.maxOfOrNull { it.amount } ?: 0L)
                                    .toHighestRangeValue()
                                    .toFloat()
                    )
                    .offset { IntOffset(x = 0, y = offsetY.value.toInt()) }
                    .background(backgroundColor)
                    .clickable { selectedIndex = index }
            )
            BarChartGraphInformation(
                transactionsOverview = transactionsOverview,
                selectedIndex = selectedIndex,
                index = index,
                onSelectedIndexReset = { selectedIndex = -1 }
            )
        }
    }
}

@Composable
fun BarChartGraphInformation(
    transactionsOverview: List<TransactionOverview>,
    selectedIndex: Int,
    index: Int,
    onSelectedIndexReset: () -> Unit
) {
    DropdownMenu(
        expanded = selectedIndex == index,
        onDismissRequest = onSelectedIndexReset,
        modifier = Modifier.background(color = Color.White),
        offset = DpOffset(x = 50.dp, y = (-210).dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = NumberFormatHelper.formatToRupiah(transactionsOverview[index].amount),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(
                        R.string.transaction_overview_information,
                        stringResource(transactionsOverview[index].month.toFullMonthResourceId()),
                        transactionsOverview[index].year
                    ),
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
            }
        }
    }
}

@Composable
fun BarChartXAxis(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        DataHelper.monthLabels.forEach {
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

data class BarChartScaleLabel(
    val amount: Long,
    val fraction: Float
)
