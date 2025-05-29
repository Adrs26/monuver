package com.android.monu.presentation.screen.budgeting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.ui.theme.LightGrey
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.utils.DataHelper

@Composable
fun BudgetingList(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LightGrey)
    ) {
        if (DataHelper.dummyBudgeting.isEmpty()) {
            Text(
                text = "Belum ada budgeting yang aktif",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(DataHelper.dummyBudgeting.size) { index ->
                    BudgetingListItem(
                        title = DataHelper.dummyBudgeting[index].title,
                        startDate = DataHelper.dummyBudgeting[index].startDate,
                        endDate = DataHelper.dummyBudgeting[index].endDate,
                        amount = DataHelper.dummyBudgeting[index].amount,
                        maxAmount = DataHelper.dummyBudgeting[index].maxAmount
                    )
                }
            }
        }
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