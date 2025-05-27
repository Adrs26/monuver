package com.android.monu.presentation.screen.budgeting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.monu.presentation.components.TypeFilterButton

@Composable
fun BudgetingFilterBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.background(color = Color.LightGray, shape = RoundedCornerShape(24.dp))
    ) {
        listOf("Aktif", "Menunggu pembaruan").forEach { type ->
            val isSelected = true
            TypeFilterButton(
                transactionsType = type,
                background = if (type == "Aktif") Color.White else Color.LightGray,
                textColor = Color.Black,
                horizontalPadding = 8.dp,
                verticalPadding = 6.dp,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                onClick = {  }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetingFilterBarPreview() {
    BudgetingFilterBar()
}