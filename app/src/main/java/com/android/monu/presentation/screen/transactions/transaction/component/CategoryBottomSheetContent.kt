package com.android.monu.presentation.screen.transactions.transaction.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.util.DataHelper
import com.android.monu.util.toHighlightColor
import com.android.monu.util.toHighlightIcon

@Composable
fun CategoryBottomSheetContent(
    categoryType: String,
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit
) {
    val listCategory = when (categoryType) {
        "Income" -> DataHelper.incomeCategory
        "Expense" -> DataHelper.expenseCategory
        else -> emptyList()
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(listCategory.size) { index ->
            val categoryTitle = stringResource(listCategory[index])
            CategoryListItem(
                categoryTitle = categoryTitle,
                modifier = Modifier
                    .clickable { onItemClick(listCategory[index]) }
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun CategoryListItem(
    categoryTitle: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = categoryTitle.toHighlightColor(),
                    shape = RoundedCornerShape(8.dp)
                )
                .size(36.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(categoryTitle.toHighlightIcon()),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
        }
        Text(
            text = categoryTitle,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            style = TextStyle(
                fontSize = 13.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryBottomSheetPreview() {
    CategoryBottomSheetContent(
        categoryType = "Expense",
        onItemClick = {}
    )
}