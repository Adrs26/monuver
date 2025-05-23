package com.android.monu.presentation.screen.analytics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.data.model.PieData
import com.android.monu.presentation.components.PieChart
import com.android.monu.presentation.screen.analytics.AnalyticsFilterCallbacks
import com.android.monu.presentation.screen.analytics.AnalyticsFilterState
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.util.CurrencyFormatHelper
import com.android.monu.util.toHighlightColor

@Composable
fun AnalyticsPieChartContent(
    values: List<PieData>,
    filterState: AnalyticsFilterState,
    filterCallbacks: AnalyticsFilterCallbacks,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = SoftGrey, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AnalyticsChartTopBar(
                title = stringResource(R.string.most_expense_category),
                chartType = 2,
                filterState = filterState,
                filterCallbacks = filterCallbacks,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            PieChartContent(
                values = values,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun PieChartContent(
    values: List<PieData>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PieChart(
            values = values,
            size = 120,
            width = 25f,
            gapDegrees = 12,
            modifier = Modifier.padding(8.dp)
        )
        PieChartDetail(
            values = values,
            modifier = Modifier.padding(start = 24.dp)
        )
    }
}

@Composable
fun PieChartDetail(
    values: List<PieData>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        values.forEach { data ->
            PieChartDetailData(
                title = data.label,
                value = data.value,
                total = values.sumOf { it.value },
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun PieChartDetailData(
    title: String,
    value: Long,
    total: Long,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(title.toHighlightColor())
        )
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            )
            Text(
                text = "${CurrencyFormatHelper.formatToRupiah(value)} (${String.format("%.1f", value.toFloat() / total.toFloat() * 100)}%)",
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
        }
    }
}

data class PieChartData(
    val category: Int,
    val amount: Long
)