package com.android.monu.presentation.screen.analytics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.presentation.components.PieChart
import com.android.monu.ui.theme.Blue800

@Composable
fun AnalyticsPieChart(
    modifier: Modifier = Modifier
) {
    val dummyList = listOf(500000L, 400000, 300000, 200000, 100000)

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Rekap kategori",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium
            )
            AnalyticsFilterDropdown(
                initialValue = "Pemasukan"
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            PieChart(values = dummyList)
        }
        AnalyticsPieChartDetail(expenseCategory = dummyList)
    }
}

@Composable
fun AnalyticsPieChartDetail(
    expenseCategory: List<Long>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        expenseCategory.forEach { data ->
            AnalyticsPieChartDetailData(
//                category = data.category,
//                amount = data.amount,
//                total = expenseCategory.sumOf { it.amount },
                modifier = Modifier
                    .clickable { }
                    .padding( horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun AnalyticsPieChartDetailData(
//    category: Int,
//    amount: Long,
//    total: Long,
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
                .background(Blue800),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "50%",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
        Text(
            text = "Makanan & Minuman",
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = "Rp500.000",
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