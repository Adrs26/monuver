package com.android.monu.presentation.screen.report.detail.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.domain.model.TransactionCategoryAmount
import com.android.monu.presentation.components.PieChart
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.utils.NumberFormatHelper
import com.android.monu.utils.extensions.toCategoryColor
import com.android.monu.utils.extensions.toCategoryName

@Composable
fun ReportDetailPieChart(
    modifier: Modifier = Modifier
) {
    val pieValues = listOf(
        TransactionCategoryAmount(1, 80000000L),
        TransactionCategoryAmount(5, 70000000L),
        TransactionCategoryAmount(7, 60000000L),
        TransactionCategoryAmount(3, 50000000L),
        TransactionCategoryAmount(6, 40000000L),
        TransactionCategoryAmount(2, 30000000L),
        TransactionCategoryAmount(10, 25000000L),
        TransactionCategoryAmount(8, 20000000L)
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
                expenseCategory = pieValues,
                modifier = Modifier.padding(top = 32.dp)
            )
        }
    }
}

@Composable
fun PieChartContent(
    expenseCategory: List<TransactionCategoryAmount>,
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
                values = expenseCategory,
                size = 150,
                width = 30f,
                gapDegrees = 10
            )
        }
        PieChartDetail(
            expenseCategory = expenseCategory,
            modifier = Modifier.padding(top = 32.dp)
        )
    }
}

@Composable
fun PieChartDetail(
    expenseCategory: List<TransactionCategoryAmount>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        expenseCategory.forEach { data ->
            PieChartDetailData(
                category = data.category,
                amount = data.amount,
                total = expenseCategory.sumOf { it.amount },
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
                .background(category.toCategoryColor()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(
                    R.string.percentage_value,
                    NumberFormatHelper.formatToPercentageValue(amount, total)
                ),
                style = TextStyle(
                    fontSize = 11.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            )
        }
        Text(
            text = stringResource(category.toCategoryName()),
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )
        Text(
            text = NumberFormatHelper.formatToConciseRupiah(amount),
            modifier = Modifier.padding(start = 16.dp),
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