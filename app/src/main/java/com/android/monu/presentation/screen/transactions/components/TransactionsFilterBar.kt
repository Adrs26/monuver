package com.android.monu.presentation.screen.transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.presentation.components.TypeFilterButton
import com.android.monu.presentation.screen.transactions.TransactionFilterData
import com.android.monu.ui.theme.Blue
import com.android.monu.utils.extensions.toTransactionType
import com.android.monu.utils.extensions.toTransactionTypeCode

@Composable
fun TransactionsFilterBar(
    transactionFilterData: TransactionFilterData,
    modifier: Modifier = Modifier,
    onFilterTypeClick: (Int?) -> Unit,
    onFilterIconClick: () -> Unit
) {
    val isFilterSelected = transactionFilterData.selectedYear != null ||
            transactionFilterData.selectedMonth != null

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TypeButtonMenu(
            selectedType = transactionFilterData.selectedType,
            modifier = Modifier.weight(1f).padding(end = 4.dp),
            onFilterTypeClick = { type -> onFilterTypeClick(type) }
        )
        IconButton(
            onClick = onFilterIconClick,
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = if (isFilterSelected) Blue else Color.LightGray,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun TypeButtonMenu(
    selectedType: Int?,
    modifier: Modifier = Modifier,
    onFilterTypeClick: (Int?) -> Unit
) {
    Row(
        modifier = modifier.background(color = Color.LightGray, shape = RoundedCornerShape(24.dp))
    ) {
        listOf(
            R.string.all,
            R.string.income,
            R.string.expense
        ).forEach { type ->
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
                onClick = { onFilterTypeClick(type.toTransactionTypeCode()) }
            )
        }
    }
}