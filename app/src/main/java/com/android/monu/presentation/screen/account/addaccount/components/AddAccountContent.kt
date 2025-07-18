package com.android.monu.presentation.screen.account.addaccount.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.presentation.components.TextAmountInputField
import com.android.monu.presentation.components.TextInputField
import com.android.monu.presentation.utils.DatabaseCodeMapper

@Composable
fun AddAccountContent(
    accountState: AddAccountContentState,
    accountActions: AddAccountContentActions,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TextInputField(
            title = "Nama",
            value = accountState.name,
            onValueChange = { accountActions.onNameChange(it) },
            placeholderText = "Masukkan nama akun",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        TextInputField(
            title = "Tipe",
            value = if (accountState.type == 0) "" else
                stringResource(DatabaseCodeMapper.toAccountType(accountState.type)),
            onValueChange = { },
            placeholderText = "Pilih tipe akun",
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { accountActions.onNavigateToType() }
                )
                .padding(horizontal = 16.dp),
            isEnable = false
        )
        TextAmountInputField(
            title = "Nominal awal",
            value = accountState.balanceFormat,
            onValueChange = { accountActions.onAmountChange(it) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Button(
            onClick = { accountActions.onAddNewAccountWithInitialTransaction(accountState) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.add),
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

data class AddAccountContentState(
    val name: String,
    val type: Int,
    val balance: Long,
    val balanceFormat: TextFieldValue
)

interface AddAccountContentActions {
    fun onNameChange(name: String)
    fun onAmountChange(balanceFormat: TextFieldValue)
    fun onNavigateToType()
    fun onAddNewAccountWithInitialTransaction(accountState: AddAccountContentState)
}