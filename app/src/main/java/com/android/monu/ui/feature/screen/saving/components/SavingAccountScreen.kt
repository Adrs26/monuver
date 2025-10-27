package com.android.monu.ui.feature.screen.saving.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.domain.model.AccountState
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.account.components.AccountListItem
import com.android.monu.utils.TransactionChildCategory
import com.android.monu.ui.feature.utils.debouncedClickable

@Composable
fun SavingAccountScreen(
    category: Int,
    accounts: List<AccountState>,
    onNavigateBack: () -> Unit,
    onAccountSelect: (Int, String) -> Unit
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = if (category == TransactionChildCategory.SAVINGS_IN)
                    stringResource(R.string.choose_funds_source) else
                        stringResource(R.string.choose_destination_account),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        if (accounts.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                items(
                    count = accounts.size,
                    key = { index -> accounts[index].id }
                ) { index ->
                    AccountListItem(
                        accountState = accounts[index],
                        modifier = Modifier
                            .debouncedClickable {
                                onAccountSelect(accounts[index].id, accounts[index].name)
                                onNavigateBack()
                            }
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                    )
                }
            }
        } else {
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
}