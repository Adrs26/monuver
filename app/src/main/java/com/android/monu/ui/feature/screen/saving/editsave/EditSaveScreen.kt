package com.android.monu.ui.feature.screen.saving.editsave

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.android.monu.ui.feature.screen.saving.editsave.components.EditSaveContent
import com.android.monu.ui.feature.screen.saving.editsave.components.EditSaveContentActions
import com.android.monu.ui.feature.screen.saving.editsave.components.EditSaveContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.NumberFormatHelper
import com.android.monu.ui.feature.utils.showMessageWithToast
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSaveScreen(
    saveState: EditSaveState,
    onNavigateBack: () -> Unit,
    onEditSave: (EditSaveContentState) -> Unit
) {
    var saveTitle by rememberSaveable { mutableStateOf(saveState.title) }
    var saveTargetDate by rememberSaveable { mutableStateOf(saveState.targetDate) }
    var saveTargetAmount by rememberSaveable { mutableLongStateOf(saveState.targetAmount) }
    var saveTargetAmountFormat by remember {
        mutableStateOf(TextFieldValue(NumberFormatHelper.formatToRupiah(saveTargetAmount)))
    }

    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    val editSaveContentState = EditSaveContentState(
        id = saveState.id,
        title = saveTitle,
        targetDate = saveTargetDate,
        currentAmount = saveState.currentAmount,
        targetAmount = saveTargetAmount,
        targetAmountFormat = saveTargetAmountFormat
    )

    val editSaveContentActions = object : EditSaveContentActions {
        override fun onTitleChange(title: String) {
            saveTitle = title
        }

        override fun onDateClick() {
            calendarState.show()
        }

        override fun onTargetAmountChange(targetAmountFormat: TextFieldValue) {
            val cleanInput = targetAmountFormat.text.replace(Regex("\\D"), "")
            saveTargetAmount = try {
                cleanInput.toLong()
            } catch (_: NumberFormatException) { 0L }

            val formattedText = NumberFormatHelper.formatToRupiah(saveTargetAmount)
            val newCursorPosition = formattedText.length

            saveTargetAmountFormat = TextFieldValue(
                text = formattedText,
                selection = TextRange(newCursorPosition)
            )
        }

        override fun onEditSave(saveState: EditSaveContentState) {
            onEditSave(saveState)
        }
    }

    LaunchedEffect(saveState.editResult) {
        saveState.editResult?.let { result ->
            context.getString(result.message).showMessageWithToast(context)
            if (result == DatabaseResultMessage.UpdateSaveSuccess) {
                onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.edit_save),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        EditSaveContent(
            saveState = editSaveContentState,
            saveActions = editSaveContentActions,
            modifier = Modifier.padding(innerPadding)
        )
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            saveTargetDate = selectedDate.toString()
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}

data class EditSaveState(
    val id: Long,
    val title: String,
    val targetDate: String,
    val currentAmount: Long,
    val targetAmount: Long,
    val editResult: DatabaseResultMessage?
)