package com.android.monu.ui.feature.screen.account.accountDetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.domain.model.AccountState
import com.android.monu.ui.feature.screen.transaction.transactionDetail.components.DataDivider
import com.android.monu.ui.feature.utils.DatabaseCodeMapper
import com.android.monu.utils.NumberHelper

@Composable
fun AccountDetailContent(
    accountState: AccountState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AccountDetailData(
            title = stringResource(R.string.name),
            content = accountState.name
        )
        DataDivider()
        AccountDetailData(
            title = stringResource(R.string.type),
            content = stringResource(DatabaseCodeMapper.toAccountType(accountState.type))
        )
        DataDivider()
        AccountDetailData(
            title = stringResource(R.string.balance),
            content = NumberHelper.formatToRupiah(accountState.balance)
        )
        DataDivider()
        AccountDetailData(
            title = stringResource(R.string.status),
            content = if (accountState.isActive) stringResource(R.string.active) else stringResource(R.string.inactive)
        )
    }
}

@Composable
fun AccountDetailData(
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