package com.android.monu.ui.feature.screen.saving.addsave.components

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
import com.android.monu.R
import com.android.monu.ui.feature.components.TextAmountInputField
import com.android.monu.ui.feature.components.TextDateInputField
import com.android.monu.ui.feature.components.TextInputField
import com.android.monu.ui.feature.utils.DateHelper

@Composable
fun AddSaveContent(
    saveState: AddSaveContentState,
    saveActions: AddSaveContentActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TextInputField(
            title = stringResource(R.string.title),
            value = saveState.title,
            onValueChange = saveActions::onTitleChange,
            placeholderText = stringResource(R.string.enter_save_title),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        TextDateInputField(
            title = stringResource(R.string.target_date),
            value = DateHelper.formatDateToReadable(saveState.targetDate),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_target_date),
            isEnable = true,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { saveActions.onDateClick() }
                )
                .padding(horizontal = 16.dp)
        )
        TextAmountInputField(
            title = stringResource(R.string.target_amount),
            value = saveState.targetAmountFormat,
            onValueChange = saveActions::onTargetAmountChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { saveActions.onAddNewSave(saveState) },
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

data class AddSaveContentState(
    val title: String,
    val targetDate: String,
    val targetAmount: Long,
    val targetAmountFormat: TextFieldValue
)

interface AddSaveContentActions {
    fun onTitleChange(title: String)
    fun onDateClick()
    fun onTargetAmountChange(targetAmountFormat: TextFieldValue)
    fun onAddNewSave(saveState: AddSaveContentState)
}