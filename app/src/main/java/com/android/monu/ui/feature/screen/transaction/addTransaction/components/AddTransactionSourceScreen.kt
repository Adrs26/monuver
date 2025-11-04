package com.android.monu.ui.feature.screen.transaction.addTransaction.components

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
import com.android.monu.R
import com.android.monu.domain.model.AccountState
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.account.components.AccountListItem
import com.android.monu.ui.feature.utils.debouncedClickable

@Composable
fun AddTransactionSourceScreen(
    accounts: List<AccountState>,
    onNavigateBack: () -> Unit,
    onSourceSelect: (Int, String) -> Unit,
    onNavigateToAddAccount: () -> Unit,
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.choose_account),
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
                                onSourceSelect(accounts[index].id, accounts[index].name)
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Kamu belum memiliki akun",
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
                            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
                        )
                    }
                }
            }
        }
    }
}