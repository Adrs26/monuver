package com.android.monu.ui.feature.screen.account.addAccount

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.android.monu.R
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.AddAccountState
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.account.addAccount.components.AddAccountContent
import com.android.monu.ui.feature.screen.account.addAccount.components.AddAccountContentActions
import com.android.monu.ui.feature.utils.isCreateAccountSuccess
import com.android.monu.ui.feature.utils.showToast
import com.android.monu.utils.NumberHelper

@Composable
fun AddAccountScreen(
    accountType: Int,
    accountActions: AddAccountActions,
    addResult: DatabaseResultState?
) {
    var accountName by rememberSaveable { mutableStateOf("") }
    var accountBalance by rememberSaveable { mutableLongStateOf(0L) }
    var accountBalanceFormat by remember {
        mutableStateOf(TextFieldValue(text = NumberHelper.formatToRupiah(accountBalance)))
    }
    val context = LocalContext.current

    val addAccountState = AddAccountState(
        name = accountName,
        type = accountType,
        balance = accountBalance
    )

    val addAccountContentActions = object : AddAccountContentActions {
        override fun onNameChange(name: String) {
            accountName = name
        }

        override fun onNavigateToType() {
            accountActions.onNavigateToType()
        }

        override fun onAmountChange(balanceFormat: TextFieldValue) {
            val cleanInput = balanceFormat.text.replace(Regex("\\D"), "")
            accountBalance = try {
                cleanInput.toLong()
            } catch (_: NumberFormatException) {
                0L
            }

            val formattedText = NumberHelper.formatToRupiah(accountBalance)
            val newCursorPosition = formattedText.length

            accountBalanceFormat = TextFieldValue(
                text = formattedText,
                selection = TextRange(newCursorPosition)
            )
        }

        override fun onAddNewAccount(accountState: AddAccountState) {
            accountActions.onAddNewAccount(accountState)
        }
    }

    LaunchedEffect(addResult) {
        addResult?.let { result ->
            result.showToast(context)
            if (result.isCreateAccountSuccess()) accountActions.onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.add_new_account),
                onNavigateBack = accountActions::onNavigateBack
            )
        }
    ) { innerPadding ->
        AddAccountContent(
            accountState = addAccountState,
            accountBalanceFormat = accountBalanceFormat,
            accountActions = addAccountContentActions,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

interface AddAccountActions {
    fun onNavigateBack()
    fun onNavigateToType()
    fun onAddNewAccount(accountState: AddAccountState)
}