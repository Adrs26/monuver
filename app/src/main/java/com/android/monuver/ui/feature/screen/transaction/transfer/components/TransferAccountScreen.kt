package com.android.monuver.ui.feature.screen.transaction.transfer.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.R
import com.android.monuver.domain.model.AccountState
import com.android.monuver.ui.feature.components.CommonAppBar
import com.android.monuver.ui.feature.components.AccountListItem
import com.android.monuver.utils.SelectAccountType
import com.android.monuver.ui.feature.utils.debouncedClickable

@Composable
fun TransferAccountScreen(
    selectAccountType: Int,
    accounts: List<AccountState>,
    selectedAccounts: List<Int>,
    onNavigateBack: () -> Unit,
    onAccountSelect: (Int, String) -> Unit,
    onNavigateToAddAccount: () -> Unit
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = if (selectAccountType == SelectAccountType.SOURCE)
                    stringResource(R.string.choose_source_account) else
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
                    val isAccountSelected = accounts[index].id in selectedAccounts

                    AccountListItem(
                        accountState = accounts[index],
                        modifier = if (!isAccountSelected) {
                            Modifier
                                .debouncedClickable {
                                    onAccountSelect(accounts[index].id, accounts[index].name)
                                    onNavigateBack()
                                }
                                .padding(horizontal = 16.dp, vertical = 2.dp)
                        } else {
                            Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 16.dp, vertical = 2.dp)
                        },
                        containerColor = if (!isAccountSelected) {
                            MaterialTheme.colorScheme.background
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.you_do_not_have_account),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    OutlinedButton(
                        onClick = onNavigateToAddAccount,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant)
                    ) {
                        Text(
                            text = stringResource(R.string.add_new_account),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }
    }
}