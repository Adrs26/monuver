package com.android.monuver.feature.transaction.presentation.transactionDetail.components

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.core.presentation.util.DatabaseCodeMapper
import com.android.monuver.feature.transaction.R

@Composable
internal fun TransactionDetailContent(
    transactionState: TransactionState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TransactionDetailData(
            title = stringResource(R.string.title),
            content = transactionState.title
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
        TransactionDetailData(
            title = stringResource(R.string.type),
            content = stringResource(DatabaseCodeMapper.toTransactionType(transactionState.type))
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
        TransactionDetailData(
            title = stringResource(R.string.category),
            content = stringResource(DatabaseCodeMapper.toParentCategoryTitle(transactionState.parentCategory))
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
        TransactionDetailData(
            title = stringResource(R.string.sub_category),
            content = stringResource(DatabaseCodeMapper.toChildCategoryTitle(transactionState.childCategory))
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
        TransactionDetailData(
            title = stringResource(R.string.date),
            content = DateHelper.formatToReadable(transactionState.date)
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
        TransactionDetailData(
            title = stringResource(R.string.amount),
            content = transactionState.amount.toRupiah()
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
        TransactionDetailData(
            title = if (transactionState.type == TransactionType.INCOME)
                stringResource(R.string.destination_account) else
                    stringResource(R.string.source_account),
            content = transactionState.sourceName
        )

        if (transactionState.type == TransactionType.TRANSFER) {
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            TransactionDetailData(
                title = stringResource(R.string.destination_account),
                content = transactionState.destinationName.toString()
            )
        }
    }
}

@Composable
private fun TransactionDetailData(
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
            modifier = Modifier.padding(end = 32.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
        )
        Text(
            text = content,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp)
        )
    }
}