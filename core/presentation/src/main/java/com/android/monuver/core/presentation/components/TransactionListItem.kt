package com.android.monuver.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.core.domain.model.TransactionListItemState
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.TransactionChildCategory
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.core.presentation.R
import com.android.monuver.core.presentation.theme.Blue800
import com.android.monuver.core.presentation.theme.Green600
import com.android.monuver.core.presentation.theme.Red600
import com.android.monuver.core.presentation.theme.SoftWhite
import com.android.monuver.core.presentation.util.DatabaseCodeMapper

@Composable
fun TransactionListItem(
    transactionState: TransactionListItemState,
    modifier: Modifier = Modifier,
    isDepositOrWithdraw: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TransactionCategoryIcon(
            icon = DatabaseCodeMapper.toChildCategoryIcon(transactionState.childCategory),
            backgroundColor = DatabaseCodeMapper.toParentCategoryIconBackground(
                transactionState.parentCategory
            ),
            childCategory = transactionState.childCategory,
            isLocked = transactionState.isLocked,
            modifier = Modifier.size(40.dp),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = transactionState.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = stringResource(
                    R.string.transaction_sub_information,
                    transactionState.sourceName,
                    DateHelper.formatToReadable(transactionState.date)
                ),
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 2,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            )
        }
        Text(
            text = transactionState.amount.toRupiah(),
            style = MaterialTheme.typography.labelMedium.copy(
                color = if (isDepositOrWithdraw)
                    depositOrWithdrawTransactionAmountColor(transactionState.childCategory) else
                        transactionAmountColor(transactionState.type)
            )
        )
    }
}

@Composable
private fun TransactionCategoryIcon(
    icon: Int,
    backgroundColor: Color,
    childCategory: Int,
    isLocked: Boolean,
    modifier: Modifier = Modifier
) {
    val iconSize = if (childCategory == TransactionChildCategory.SAVINGS_IN ||
        childCategory == TransactionChildCategory.SAVINGS_OUT) 36 else 24

    Box(
        modifier = modifier.background(
            color = backgroundColor,
            shape = MaterialTheme.shapes.extraSmall
        ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(iconSize.dp),
            tint = SoftWhite
        )
        if (isLocked) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.BottomEnd),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

private fun transactionAmountColor(type: Int): Color {
    return when (type) {
        TransactionType.INCOME -> Green600
        TransactionType.EXPENSE -> Red600
        else -> Blue800
    }
}

private fun depositOrWithdrawTransactionAmountColor(category: Int): Color {
    return when (category) {
        TransactionChildCategory.SAVINGS_IN -> Green600
        TransactionChildCategory.SAVINGS_OUT -> Red600
        else -> Blue800
    }
}
