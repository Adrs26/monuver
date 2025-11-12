package com.android.monuver.ui.feature.screen.account.addAccount

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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.android.monuver.R
import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.AddAccountState
import com.android.monuver.ui.feature.components.CommonAppBar
import com.android.monuver.ui.feature.components.TextAmountInputField
import com.android.monuver.ui.feature.components.TextInputField
import com.android.monuver.ui.feature.utils.DatabaseCodeMapper
import com.android.monuver.ui.feature.utils.isCreateAccountSuccess
import com.android.monuver.ui.feature.utils.showToast
import com.android.monuver.ui.feature.utils.toRupiahFormat
import com.android.monuver.utils.NumberHelper

@Composable
fun AddAccountScreen(
    accountType: Int,
    addResult: DatabaseResultState?,
    onNavigateBack: () -> Unit,
    onNavigateToType: () -> Unit,
    onAddAccount: (AddAccountState) -> Unit
) {
    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf("") }
    var balance by rememberSaveable { mutableLongStateOf(0L) }
    var formattedBalance by remember {
        mutableStateOf(TextFieldValue(NumberHelper.formatToRupiah(balance)))
    }

    LaunchedEffect(addResult) {
        addResult?.let { result ->
            result.showToast(context)
            if (result.isCreateAccountSuccess()) onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.add_new_account),
                onNavigateBack = onNavigateBack
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
                value = if (accountType == 0) "" else
                    stringResource(DatabaseCodeMapper.toAccountType(accountType)),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_account_type),
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onNavigateToType
                    )
                    .padding(horizontal = 16.dp),
                isEnable = false
            )
            TextAmountInputField(
                title = stringResource(R.string.start_balance),
                value = formattedBalance,
                onValueChange = {
                    balance = it.toRupiahFormat().first
                    formattedBalance = it.toRupiahFormat().second
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    onAddAccount(
                        AddAccountState(
                            name = name,
                            type = accountType,
                            balance = balance
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Text(
                    text = stringResource(R.string.add),
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}