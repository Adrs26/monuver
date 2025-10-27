package com.android.monu.ui.feature.screen.account.editAccount

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.monu.R
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.AccountState
import com.android.monu.domain.model.EditAccountState
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.account.editAccount.components.EditAccountContent
import com.android.monu.ui.feature.screen.account.editAccount.components.EditAccountContentActions
import com.android.monu.ui.feature.utils.isUpdateAccountSuccess
import com.android.monu.ui.feature.utils.showToast

@Composable
fun EditAccountScreen(
    accountState: AccountState,
    accountActions: EditAccountActions,
    editResult: DatabaseResultState?,
) {
    var accountName by rememberSaveable { mutableStateOf(accountState.name) }

    val context = LocalContext.current

    val editAccountState = EditAccountState(
        id = accountState.id,
        name = accountName,
        type = accountState.type,
        balance = accountState.balance
    )

    val editAccountContentActions = object : EditAccountContentActions {
        override fun onNameChange(name: String) {
            accountName = name
        }

        override fun onNavigateToType() {
            accountActions.onNavigateToType()
        }

        override fun onEditAccount(accountState: EditAccountState) {
            accountActions.onEditAccount(accountState)
        }
    }

    LaunchedEffect(editResult) {
        editResult?.let { result ->
            result.showToast(context)
            if (result.isUpdateAccountSuccess()) accountActions.onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.edit_account),
                onNavigateBack = accountActions::onNavigateBack
            )
        }
    ) { innerPadding ->
        EditAccountContent(
            accountState = editAccountState,
            accountActions = editAccountContentActions,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

interface EditAccountActions {
    fun onNavigateBack()
    fun onNavigateToType()
    fun onEditAccount(accountState: EditAccountState)
}