package com.android.monu.ui.feature.screen.account.addaccount

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
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.account.addaccount.components.AddAccountContent
import com.android.monu.ui.feature.screen.account.addaccount.components.AddAccountContentActions
import com.android.monu.ui.feature.screen.account.addaccount.components.AddAccountContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.NumberFormatHelper
import com.android.monu.ui.feature.utils.showMessageWithToast

@Composable
fun AddAccountScreen(
    accountType: Int,
    addResult: DatabaseResultMessage?,
    accountActions: AddAccountActions,
) {
    var accountName by rememberSaveable { mutableStateOf("") }
    var accountBalance by rememberSaveable { mutableLongStateOf(0L) }
    var accountBalanceFormat by remember {
        mutableStateOf(TextFieldValue(text = NumberFormatHelper.formatToRupiah(accountBalance)))
    }
    val context = LocalContext.current

    val addAccountContentState = AddAccountContentState(
        name = accountName,
        type = accountType,
        balance = accountBalance,
        balanceFormat = accountBalanceFormat
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

            val formattedText = NumberFormatHelper.formatToRupiah(accountBalance)
            val newCursorPosition = formattedText.length

            accountBalanceFormat = TextFieldValue(
                text = formattedText,
                selection = TextRange(newCursorPosition)
            )
        }

        override fun onAddNewAccount(accountState: AddAccountContentState) {
            accountActions.onAddNewAccount(accountState)
        }
    }

    LaunchedEffect(addResult) {
        addResult?.let { result ->
            context.getString(result.message).showMessageWithToast(context)
            if (result == DatabaseResultMessage.CreateAccountSuccess) {
                accountActions.onNavigateBack()
            }
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
            accountState = addAccountContentState,
            accountActions = addAccountContentActions,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

interface AddAccountActions {
    fun onNavigateBack()
    fun onNavigateToType()
    fun onAddNewAccount(accountState: AddAccountContentState)
}