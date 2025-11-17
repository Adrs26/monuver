package com.android.monuver.feature.account.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.presentation.components.AccountListItem
import com.android.monuver.core.presentation.components.CommonLottieAnimation
import com.android.monuver.core.presentation.util.debouncedClickable
import com.android.monuver.feature.account.R

@Composable
internal fun AccountContent(
    accounts: List<AccountState>,
    totalBalance: Long,
    onNavigateToAccountDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when  {
        accounts.isEmpty() -> {
            AccountEmptyListContent(
                totalBalance = totalBalance,
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
        }
        else -> {
            AccountListContent(
                accounts = accounts,
                totalBalance = totalBalance,
                onNavigateToAccountDetail = onNavigateToAccountDetail,
                modifier = modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun AccountListContent(
    accounts: List<AccountState>,
    totalBalance: Long,
    onNavigateToAccountDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            AccountOverview(
                totalBalance = totalBalance,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Text(
                text = stringResource(R.string.your_list_account),
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(
            count = accounts.size,
            key = { index -> accounts[index].id }
        ) { index ->
            AccountListItem(
                accountState = accounts[index],
                modifier = Modifier
                    .debouncedClickable { onNavigateToAccountDetail(accounts[index].id) }
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun AccountEmptyListContent(
    totalBalance: Long,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AccountOverview(totalBalance = totalBalance)
        Text(
            text = stringResource(R.string.your_list_account),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        CommonLottieAnimation(lottieAnimation = R.raw.wallet)
    }
}