package com.android.monu.presentation.screen.reports.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.data.model.PieData
import com.android.monu.presentation.components.PieChart
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.Green
import com.android.monu.ui.theme.Orange
import com.android.monu.ui.theme.Red
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.util.CurrencyFormatHelper
import com.android.monu.util.toHighlightColor

@Composable
fun ReportDetailPieChart(
    modifier: Modifier = Modifier
) {
    val pieValues = listOf(
        PieData("Food & Beverages", 80000000L, Blue),
        PieData("Education", 70000000L, Red),
        PieData("Shopping", 60000000L, Green),
        PieData("Health & Personal care", 50000000L, Orange),
        PieData("Entertainment", 40000000L, Color.Black),
        PieData("Transportation", 30000000L, Color.Gray),
        PieData("Insurance", 25000000L, Color.Magenta),
        PieData("Investment", 20000000L, Color.Cyan)
    )

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
            Text(
                text = "Most Expense Category",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
            PieChartContent(
                pieValues = pieValues,
                modifier = Modifier.padding(top = 32.dp)
            )
        }
    }
}

@Composable
fun PieChartContent(
    pieValues: List<PieData>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        ) {
            PieChart(
                values = pieValues,
                size = 150,
                width = 30f,
                gapDegrees = 10
            )
        }
        PieChartDetail(
            values = pieValues,
            modifier = Modifier.padding(top = 32.dp)
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
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { }
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
                .width(36.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(title.toHighlightColor()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${String.format("%.0f", value.toFloat() / total.toFloat() * 100)}%",
                style = TextStyle(
                    fontSize = 11.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            )
        }
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )
        Text(
            text = CurrencyFormatHelper.formatToRupiah(value),
            style = TextStyle(
                fontSize = 11.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        )
        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReportDetailPieChartPreview() {
    ReportDetailPieChart()
}