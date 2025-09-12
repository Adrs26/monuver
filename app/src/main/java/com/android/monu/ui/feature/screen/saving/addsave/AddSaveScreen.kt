package com.android.monu.ui.feature.screen.saving.addsave

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
import com.android.monu.ui.feature.screen.saving.addsave.components.AddSaveContent
import com.android.monu.ui.feature.screen.saving.addsave.components.AddSaveContentActions
import com.android.monu.ui.feature.screen.saving.addsave.components.AddSaveContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.NumberFormatHelper
import com.android.monu.ui.feature.utils.showMessageWithToast
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSaveScreen(
    addResult: DatabaseResultMessage?,
    onNavigateBack: () -> Unit,
    onAddNewSave: (AddSaveContentState) -> Unit
) {
    var saveTitle by rememberSaveable { mutableStateOf("") }
    var saveTargetDate by rememberSaveable { mutableStateOf("") }
    var saveTargetAmount by rememberSaveable { mutableLongStateOf(0L) }
    var saveTargetAmountFormat by remember {
        mutableStateOf(TextFieldValue(NumberFormatHelper.formatToRupiah(saveTargetAmount)))
    }

    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    val addSaveContentState = AddSaveContentState(
        title = saveTitle,
        targetDate = saveTargetDate,
        targetAmount = saveTargetAmount,
        targetAmountFormat = saveTargetAmountFormat
    )

    val addSaveContentActions = object : AddSaveContentActions {
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

        override fun onAddNewSave(saveState: AddSaveContentState) {
            onAddNewSave(saveState)
        }
    }

    LaunchedEffect(addResult) {
        addResult?.let { result ->
            context.getString(result.message).showMessageWithToast(context)
            if (result == DatabaseResultMessage.CreateSaveSuccess) {
                onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.add_save),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        AddSaveContent(
            saveState = addSaveContentState,
            saveActions = addSaveContentActions,
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