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
import com.android.monu.domain.model.account.Account
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.account.editAccount.components.EditAccountContent
import com.android.monu.ui.feature.screen.account.editAccount.components.EditAccountContentActions
import com.android.monu.ui.feature.screen.account.editAccount.components.EditAccountContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.showMessageWithToast

@Composable
fun EditAccountScreen(
    account: Account,
    editResult: DatabaseResultMessage?,
    accountActions: EditAccountActions
) {
    var accountName by rememberSaveable { mutableStateOf(account.name) }

    val context = LocalContext.current

    val editAccountContentState = EditAccountContentState(
        id = account.id,
        name = accountName,
        type = account.type,
        balance = account.balance
    )

    val editAccountContentActions = object : EditAccountContentActions {
        override fun onNameChange(name: String) {
            accountName = name
        }

        override fun onNavigateToType() {
            accountActions.onNavigateToType()
        }

        override fun onEditAccount(accountState: EditAccountContentState) {
            accountActions.onEditAccount(accountState)
        }
    }

    LaunchedEffect(editResult) {
        editResult?.let { result ->
            context.getString(result.message).showMessageWithToast(context)
            if (result == DatabaseResultMessage.UpdateAccountSuccess) {
                accountActions.onNavigateBack()
            }
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
            accountState = editAccountContentState,
            accountActions = editAccountContentActions,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

interface EditAccountActions {
    fun onNavigateBack()
    fun onNavigateToType()
    fun onEditAccount(accountState: EditAccountContentState)
}