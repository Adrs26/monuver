package com.android.monu.ui.feature.screen.account.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.domain.model.account.Account

@Composable
fun AccountContent(
    accounts: List<Account>,
    totalBalance: Long,
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
                modifier = modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun AccountListContent(
    accounts: List<Account>,
    totalBalance: Long,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                account = accounts[index],
                modifier = Modifier
                    .clickable {}
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun AccountEmptyListContent(
    totalBalance: Long,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AccountOverview(totalBalance = totalBalance)
        Text(
            text = stringResource(R.string.your_list_account),
            modifier = Modifier.padding(top = 24.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Kamu belum memiliki akun",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}