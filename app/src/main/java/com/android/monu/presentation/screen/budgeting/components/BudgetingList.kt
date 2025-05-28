package com.android.monu.presentation.screen.budgeting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.ui.theme.Green
import com.android.monu.ui.theme.Orange
import com.android.monu.ui.theme.Red
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.utils.DateHelper
import com.android.monu.utils.NumberFormatHelper

@Composable
fun BudgetingList(
    modifier: Modifier = Modifier,
) {
    val dummyBudgeting = listOf(
        DummyBudgeting("Budget makan", DateHelper.formatDateToReadable("2025-05-25"), DateHelper.formatDateToReadable("2025-06-25"), 250000, 1500000),
        DummyBudgeting("Budget perawatan diri", DateHelper.formatDateToReadable("2025-05-25"), DateHelper.formatDateToReadable("2025-06-25"), 120000, 350000),
        DummyBudgeting("Budget jajan", DateHelper.formatDateToReadable("2025-05-25"), DateHelper.formatDateToReadable("2025-06-25"), 200000, 300000),
        DummyBudgeting("Budget internet", DateHelper.formatDateToReadable("2025-05-25"), DateHelper.formatDateToReadable("2025-06-25"), 105000, 105000),
        DummyBudgeting("Budget makan", DateHelper.formatDateToReadable("2025-05-25"), DateHelper.formatDateToReadable("2025-06-25"), 250000, 1500000),
        DummyBudgeting("Budget perawatan diri", DateHelper.formatDateToReadable("2025-05-25"), DateHelper.formatDateToReadable("2025-06-25"), 120000, 350000),
        DummyBudgeting("Budget jajan", DateHelper.formatDateToReadable("2025-05-25"), DateHelper.formatDateToReadable("2025-06-25"), 200000, 300000),
        DummyBudgeting("Budget internet", DateHelper.formatDateToReadable("2025-05-25"), DateHelper.formatDateToReadable("2025-06-25"), 105000, 105000),
        DummyBudgeting("Budget makan", DateHelper.formatDateToReadable("2025-05-25"), DateHelper.formatDateToReadable("2025-06-25"), 250000, 1500000),
        DummyBudgeting("Budget perawatan diri", DateHelper.formatDateToReadable("2025-05-25"), DateHelper.formatDateToReadable("2025-06-25"), 120000, 350000),
        DummyBudgeting("Budget jajan", DateHelper.formatDateToReadable("2025-05-25"), DateHelper.formatDateToReadable("2025-06-25"), 200000, 300000),
        DummyBudgeting("Budget internet", DateHelper.formatDateToReadable("2025-05-25"), DateHelper.formatDateToReadable("2025-06-25"), 105000, 105000)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(top = 16.dp)
    ) {
        if (dummyBudgeting.isEmpty()) {
            Text(
                text = "Belum ada budgeting yang aktif",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(
                    fontSize = 13.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dummyBudgeting.size) { index ->
                    BudgetingListItem(
                        title = dummyBudgeting[index].title,
                        startDate = dummyBudgeting[index].startDate,
                        endDate = dummyBudgeting[index].endDate,
                        amount = dummyBudgeting[index].amount,
                        maxAmount = dummyBudgeting[index].maxAmount
                    )
                }
            }
        }
    }
}

@Composable
fun BudgetingListItem(
    title: String,
    startDate: String,
    endDate: String,
    amount: Long,
    maxAmount: Long,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .border(width = 1.dp, color = SoftGrey, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
            Text(
                text = "$startDate - $endDate",
                modifier = Modifier.padding(top = 4.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = TextStyle(
                    fontSize = 9.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            )
            Text(
                text = "${NumberFormatHelper.formatToConciseRupiah(amount)} / ${NumberFormatHelper.formatToConciseRupiah(maxAmount)}",
                modifier = Modifier.padding(top = 24.dp, bottom = 4.dp),
                style = TextStyle(
                    fontSize = 11.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            )
            LinearProgressIndicator(
                progress = { amount.toFloat() / maxAmount.toFloat() },
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .height(4.dp),
                color = changeProgressBarColor(amount, maxAmount),
                trackColor = Color.LightGray,
                strokeCap = StrokeCap.Square,
                gapSize = 0.dp,
                drawStopIndicator = { null }
            )
        }
    }
}

fun changeProgressBarColor(amount: Long, maxAmount: Long): Color {
    return when {
        amount.toDouble() / maxAmount.toDouble() > 0.8 -> Red
        amount.toDouble() / maxAmount.toDouble() > 0.6 -> Orange
        else -> Green
    }
}

data class DummyBudgeting(
    val title: String,
    val startDate: String,
    val endDate: String,
    val amount: Long,
    val maxAmount: Long,
)

@Preview(showBackground = true)
@Composable
fun BudgetingListItemPreview() {
    BudgetingList()
}