package com.android.monuver.feature.account.presentation.addAccount

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
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.PrimaryActionButton
import com.android.monuver.core.presentation.components.TextAmountInputField
import com.android.monuver.core.presentation.components.TextInputField
import com.android.monuver.core.presentation.util.DatabaseCodeMapper
import com.android.monuver.core.presentation.util.isCreateAccountSuccess
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.core.presentation.util.toRupiahFieldValue
import com.android.monuver.feature.account.R
import com.android.monuver.feature.account.domain.model.AddAccountState
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
internal fun AddAccountScreen(
    accountType: Int,
    result: DatabaseResultState?,
    onNavigateBack: () -> Unit,
    onNavigateToType: () -> Unit,
    onAddAccount: (AddAccountState) -> Unit
) {
    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf("") }
    var balance by rememberSaveable { mutableLongStateOf(0L) }
    var formattedBalance by remember { mutableStateOf(TextFieldValue(balance.toRupiah())) }

    LaunchedEffect(result) {
        result?.let { result ->
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
        },
        bottomBar = {
            PrimaryActionButton(
                text = stringResource(R.string.add),
                onClick = {
                    onAddAccount(
                        AddAccountState(
                            name = name,
                            type = accountType,
                            balance = balance
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
                    balance = it.toRupiahFieldValue().first
                    formattedBalance = it.toRupiahFieldValue().second
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}