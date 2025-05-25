package com.android.monu.presentation.screen.analytics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.android.monu.domain.model.TransactionCategoryAmount
import com.android.monu.presentation.components.PieChart
import com.android.monu.presentation.screen.analytics.AnalyticsFilterCallbacks
import com.android.monu.presentation.screen.analytics.AnalyticsFilterState
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.util.CurrencyFormatHelper
import com.android.monu.util.DataHelper
import com.android.monu.util.toCategoryColor
import com.android.monu.util.toCategoryName

@Composable
fun AnalyticsPieChart(
    mostExpenseCategory: List<TransactionCategoryAmount>,
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
                mostExpenseCategory = mostExpenseCategory,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun PieChartContent(
    mostExpenseCategory: List<TransactionCategoryAmount>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (mostExpenseCategory.isEmpty()) {
            Text(
                text = stringResource(R.string.no_transactions_yet),
                modifier = Modifier.padding(top = 48.dp, bottom = 40.dp),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            )
        } else {
            Row(
                modifier = modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PieChart(
                    values = mostExpenseCategory,
                    size = 120,
                    width = 25f,
                    gapDegrees = 12,
                    modifier = Modifier.padding(8.dp)
                )
                PieChartDetail(
                    mostExpenseCategory = mostExpenseCategory,
                    modifier = Modifier.padding(start = 24.dp)
                )
            }
        }
    }
}

@Composable
fun PieChartDetail(
    mostExpenseCategory: List<TransactionCategoryAmount>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        mostExpenseCategory.forEach { data ->
            PieChartDetailData(
                category = data.category,
                amount = data.amount,
                total = mostExpenseCategory.sumOf { it.amount },
                modifier = Modifier.padding(bottom = 8.dp)
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
                    DataHelper.calculateToPercentageValue(amount, total)
                ),
                style = TextStyle(
                    fontSize = 11.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            )
        }
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = stringResource(category.toCategoryName()),
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            )
            Text(
                text = CurrencyFormatHelper.formatToRupiah(amount),
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