package com.android.monuver.feature.account.presentation.editAccount

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
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
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.PrimaryActionButton
import com.android.monuver.core.presentation.components.StaticTextInputField
import com.android.monuver.core.presentation.components.TextInputField
import com.android.monuver.core.presentation.util.DatabaseCodeMapper
import com.android.monuver.core.presentation.util.isUpdateAccountSuccess
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.feature.account.R
import com.android.monuver.feature.account.domain.model.EditAccountState

@Composable
internal fun EditAccountScreen(
    accountState: AccountState,
    result: DatabaseResultState?,
    accountActions: EditAccountActions
) {
    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf(accountState.name) }

    LaunchedEffect(result) {
        result?.let { result ->
            result.showToast(context)
            if (result.isUpdateAccountSuccess()) accountActions.onNavigateBack()
        }
    }

    BackHandler { accountActions.onNavigateBack() }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.edit_account),
                onNavigateBack = accountActions::onNavigateBack
            )
        },
        bottomBar = {
            PrimaryActionButton(
                text = stringResource(R.string.add),
                onClick = {
                    accountActions.onEditAccount(
                        EditAccountState(
                            id = accountState.id,
                            name = name,
                            type = accountState.type,
                            balance = accountState.balance
                        )
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
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
                value = accountState.balance.toRupiah(),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }
}

internal interface EditAccountActions {
    fun onNavigateBack()
    fun onNavigateToType()
    fun onEditAccount(accountState: EditAccountState)
}