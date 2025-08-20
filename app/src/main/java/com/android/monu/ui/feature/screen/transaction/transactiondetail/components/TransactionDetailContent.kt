package com.android.monu.ui.feature.screen.transaction.transactiondetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.ui.feature.utils.DatabaseCodeMapper
import com.android.monu.ui.feature.utils.DateHelper
import com.android.monu.ui.feature.utils.NumberFormatHelper
import com.android.monu.ui.feature.utils.TransactionType

@Composable
fun TransactionDetailContent(
    transaction: Transaction,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DetailTransactionData(
            title = stringResource(R.string.title),
            content = transaction.title
        )
        DataDivider()
        DetailTransactionData(
            title = stringResource(R.string.type),
            content = stringResource(DatabaseCodeMapper.toTransactionType(transaction.type))
        )
        DataDivider()
        DetailTransactionData(
            title = stringResource(R.string.category),
            content = stringResource(DatabaseCodeMapper.toParentCategoryTitle(transaction.parentCategory))
        )
        DataDivider()
        DetailTransactionData(
            title = stringResource(R.string.sub_category),
            content = stringResource(DatabaseCodeMapper.toChildCategoryTitle(transaction.childCategory))
        )
        DataDivider()
        DetailTransactionData(
            title = stringResource(R.string.date),
            content = DateHelper.formatDateToReadable(transaction.date)
        )
        DataDivider()
        DetailTransactionData(
            title = stringResource(R.string.amount),
            content = NumberFormatHelper.formatToRupiah(transaction.amount)
        )
        DataDivider()
        DetailTransactionData(
            title = stringResource(R.string.source),
            content = transaction.sourceName
        )

        if (transaction.type == TransactionType.TRANSFER) {
            DataDivider()
            DetailTransactionData(
                title = stringResource(R.string.destination),
                content = transaction.destinationName.toString()
            )
        }
    }
}

@Composable
fun DetailTransactionData(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
        )
        Text(
            text = content,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp)
        )
    }
}

@Composable
fun DataDivider(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.surfaceVariant
    )
}