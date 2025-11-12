package com.android.monuver.ui.feature.screen.saving.addSaving.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.android.monuver.R
import com.android.monuver.domain.model.AddSavingState
import com.android.monuver.ui.feature.components.TextAmountInputField
import com.android.monuver.ui.feature.components.TextDateInputField
import com.android.monuver.ui.feature.components.TextInputField
import com.android.monuver.utils.DateHelper

@Composable
fun AddSavingContent(
    savingState: AddSavingState,
    savingTargetAmountFormat: TextFieldValue,
    savingActions: AddSavingContentActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TextInputField(
            title = stringResource(R.string.title),
            value = savingState.title,
            onValueChange = savingActions::onTitleChange,
            placeholderText = stringResource(R.string.enter_save_title),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        TextDateInputField(
            title = stringResource(R.string.target_date),
            value = DateHelper.formatDateToReadable(savingState.targetDate),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_target_date),
            isEnable = true,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { savingActions.onDateClick() }
                )
                .padding(horizontal = 16.dp)
        )
        TextAmountInputField(
            title = stringResource(R.string.target_amount),
            value = savingTargetAmountFormat,
            onValueChange = savingActions::onTargetAmountChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { savingActions.onAddNewSaving(savingState) },
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

interface AddSavingContentActions {
    fun onTitleChange(title: String)
    fun onDateClick()
    fun onTargetAmountChange(targetAmountFormat: TextFieldValue)
    fun onAddNewSaving(savingState: AddSavingState)
}