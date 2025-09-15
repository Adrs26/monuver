package com.android.monu.ui.feature.screen.saving.addSaving

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
import com.android.monu.ui.feature.screen.saving.addSaving.components.AddSavingContent
import com.android.monu.ui.feature.screen.saving.addSaving.components.AddSavingContentActions
import com.android.monu.ui.feature.screen.saving.addSaving.components.AddSavingContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.NumberFormatHelper
import com.android.monu.ui.feature.utils.showMessageWithToast
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSavingScreen(
    addResult: DatabaseResultMessage?,
    onNavigateBack: () -> Unit,
    onAddNewSaving: (AddSavingContentState) -> Unit
) {
    var savingTitle by rememberSaveable { mutableStateOf("") }
    var savingTargetDate by rememberSaveable { mutableStateOf("") }
    var savingTargetAmount by rememberSaveable { mutableLongStateOf(0L) }
    var savingTargetAmountFormat by remember {
        mutableStateOf(TextFieldValue(NumberFormatHelper.formatToRupiah(savingTargetAmount)))
    }

    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    val addSavingContentState = AddSavingContentState(
        title = savingTitle,
        targetDate = savingTargetDate,
        targetAmount = savingTargetAmount,
        targetAmountFormat = savingTargetAmountFormat
    )

    val addSavingContentActions = object : AddSavingContentActions {
        override fun onTitleChange(title: String) {
            savingTitle = title
        }

        override fun onDateClick() {
            calendarState.show()
        }

        override fun onTargetAmountChange(targetAmountFormat: TextFieldValue) {
            val cleanInput = targetAmountFormat.text.replace(Regex("\\D"), "")
            savingTargetAmount = try {
                cleanInput.toLong()
            } catch (_: NumberFormatException) { 0L }

            val formattedText = NumberFormatHelper.formatToRupiah(savingTargetAmount)
            val newCursorPosition = formattedText.length

            savingTargetAmountFormat = TextFieldValue(
                text = formattedText,
                selection = TextRange(newCursorPosition)
            )
        }

        override fun onAddNewSaving(savingState: AddSavingContentState) {
            onAddNewSaving(savingState)
        }
    }

    LaunchedEffect(addResult) {
        addResult?.let { result ->
            context.getString(result.message).showMessageWithToast(context)
            if (result == DatabaseResultMessage.CreateSavingSuccess) {
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
        AddSavingContent(
            savingState = addSavingContentState,
            savingActions = addSavingContentActions,
            modifier = Modifier.padding(innerPadding)
        )
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            savingTargetDate = selectedDate.toString()
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}