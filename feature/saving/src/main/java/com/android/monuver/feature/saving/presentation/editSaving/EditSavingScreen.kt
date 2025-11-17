package com.android.monuver.feature.saving.presentation.editSaving

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.SavingState
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.StaticTextInputField
import com.android.monuver.core.presentation.components.TextAmountInputField
import com.android.monuver.core.presentation.components.TextDateInputField
import com.android.monuver.core.presentation.components.TextInputField
import com.android.monuver.core.presentation.util.isUpdateSavingSuccess
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.core.presentation.util.toRupiahFieldValue
import com.android.monuver.feature.saving.R
import com.android.monuver.feature.saving.domain.model.EditSavingState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditSavingScreen(
    savingState: SavingState,
    result: DatabaseResultState?,
    onNavigateBack: () -> Unit,
    onEditSaving: (EditSavingState) -> Unit
) {
    val context = LocalContext.current

    var title by rememberSaveable { mutableStateOf(savingState.title) }
    var targetDate by rememberSaveable { mutableStateOf(savingState.targetDate) }
    var targetAmount by rememberSaveable { mutableLongStateOf(savingState.targetAmount) }
    var formattedTargetAmount by remember { mutableStateOf(TextFieldValue(targetAmount.toRupiah())) }

    val calendarState = rememberUseCaseState()

    LaunchedEffect(result) {
        result?.let { result ->
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TextInputField(
                title = stringResource(R.string.title),
                value = title,
                onValueChange = { title = it },
                placeholderText = stringResource(R.string.enter_save_title),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            TextDateInputField(
                title = stringResource(R.string.target_date),
                value = DateHelper.formatToReadable(targetDate),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_target_date),
                isEnable = true,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = calendarState::show
                    )
                    .padding(horizontal = 16.dp)
            )
            StaticTextInputField(
                title = stringResource(R.string.current_amount),
                value = savingState.currentAmount.toRupiah(),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            TextAmountInputField(
                title = stringResource(R.string.target_amount),
                value = formattedTargetAmount,
                onValueChange = {
                    targetAmount = it.toRupiahFieldValue().first
                    formattedTargetAmount = it.toRupiahFieldValue().second
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    onEditSaving(
                        EditSavingState(
                            id = savingState.id,
                            title = title,
                            targetDate = targetDate,
                            currentAmount = savingState.currentAmount,
                            targetAmount = targetAmount
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Text(
                    text = stringResource(R.string.save),
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            targetDate = selectedDate.toString()
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}