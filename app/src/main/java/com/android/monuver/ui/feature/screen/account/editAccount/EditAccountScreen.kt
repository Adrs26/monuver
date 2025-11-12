package com.android.monuver.ui.feature.screen.account.editAccount

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monuver.R
import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.AccountState
import com.android.monuver.domain.model.EditAccountState
import com.android.monuver.ui.feature.components.CommonAppBar
import com.android.monuver.ui.feature.components.StaticTextInputField
import com.android.monuver.ui.feature.components.TextInputField
import com.android.monuver.ui.feature.utils.DatabaseCodeMapper
import com.android.monuver.ui.feature.utils.isUpdateAccountSuccess
import com.android.monuver.ui.feature.utils.showToast
import com.android.monuver.utils.NumberHelper

@Composable
fun EditAccountScreen(
    accountState: AccountState,
    accountActions: EditAccountActions,
    editResult: DatabaseResultState?,
) {
    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf(accountState.name) }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TextInputField(
                title = stringResource(R.string.name),
                value = name,
                onValueChange = { name = it },
                placeholderText = stringResource(R.string.enter_account_name),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            TextInputField(
                title = stringResource(R.string.type),
                value = if (accountState.type == 0) "" else
                    stringResource(DatabaseCodeMapper.toAccountType(accountState.type)),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_account_type),
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = accountActions::onNavigateToType
                    )
                    .padding(horizontal = 16.dp),
                isEnable = false
            )
            StaticTextInputField(
                title = stringResource(R.string.balance),
                value = NumberHelper.formatToRupiah(accountState.balance),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    accountActions.onEditAccount(
                        EditAccountState(
                            id = accountState.id,
                            name = name,
                            type = accountState.type,
                            balance = accountState.balance
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Text(
                    text = stringResource(R.string.save),
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

interface EditAccountActions {
    fun onNavigateBack()
    fun onNavigateToType()
    fun onEditAccount(accountState: EditAccountState)
}