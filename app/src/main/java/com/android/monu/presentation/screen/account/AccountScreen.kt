package com.android.monu.presentation.screen.account

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.android.monu.R
import com.android.monu.domain.model.account.Account
import com.android.monu.presentation.components.CommonAppBar
import com.android.monu.presentation.components.CommonFloatingActionButton
import com.android.monu.presentation.screen.account.components.AccountContent
import com.android.monu.ui.theme.MonuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    accounts: List<Account>,
    totalBalance: Long,
    onNavigateBack: () -> Unit,
    onNavigateToAddAccount: () -> Unit
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.my_account),
                onNavigateBack = onNavigateBack
            )
        },
        floatingActionButton = {
            CommonFloatingActionButton { onNavigateToAddAccount() }
        }
    ) { innerPadding ->
        AccountContent(
            accounts = accounts,
            totalBalance = totalBalance,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun AccountScreenPreview() {
    MonuTheme {
        AccountScreen(
            accounts = emptyList(),
            totalBalance = 0L,
            onNavigateBack = { },
            onNavigateToAddAccount = { }
        )
    }
}