package com.android.monu.ui.feature.screen.saving.editSaving

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
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.EditSavingState
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.saving.editSaving.components.EditSavingContent
import com.android.monu.ui.feature.screen.saving.editSaving.components.EditSavingContentActions
import com.android.monu.ui.feature.utils.isUpdateSavingSuccess
import com.android.monu.ui.feature.utils.showToast
import com.android.monu.utils.NumberHelper
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSavingScreen(
    savingUiState: EditSavingUiState,
    onNavigateBack: () -> Unit,
    onEditSaving: (EditSavingState) -> Unit
) {
    var savingTitle by rememberSaveable { mutableStateOf(savingUiState.title) }
    var savingTargetDate by rememberSaveable { mutableStateOf(savingUiState.targetDate) }
    var savingTargetAmount by rememberSaveable { mutableLongStateOf(savingUiState.targetAmount) }
    var savingTargetAmountFormat by remember {
        mutableStateOf(TextFieldValue(NumberHelper.formatToRupiah(savingTargetAmount)))
    }

    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    val editSavingContentState = EditSavingState(
        id = savingUiState.id,
        title = savingTitle,
        targetDate = savingTargetDate,
        currentAmount = savingUiState.currentAmount,
        targetAmount = savingTargetAmount
    )

    val editSavingContentActions = object : EditSavingContentActions {
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

            val formattedText = NumberHelper.formatToRupiah(savingTargetAmount)
            val newCursorPosition = formattedText.length

            savingTargetAmountFormat = TextFieldValue(
                text = formattedText,
                selection = TextRange(newCursorPosition)
            )
        }

        override fun onEditSaving(savingState: EditSavingState) {
            onEditSaving(savingState)
        }
    }

    LaunchedEffect(savingUiState.editResult) {
        savingUiState.editResult?.let { result ->
            result.showToast(context)
            if (result.isUpdateSavingSuccess()) onNavigateBack()
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
        EditSavingContent(
            savingState = editSavingContentState,
            savingTargetAmountFormat = savingTargetAmountFormat,
            savingActions = editSavingContentActions,
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

data class EditSavingUiState(
    val id: Long,
    val title: String,
    val targetDate: String,
    val currentAmount: Long,
    val targetAmount: Long,
    val editResult: DatabaseResultState?
)