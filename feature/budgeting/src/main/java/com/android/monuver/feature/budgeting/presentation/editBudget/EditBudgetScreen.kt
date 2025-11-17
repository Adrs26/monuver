package com.android.monuver.feature.budgeting.presentation.editBudget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.BudgetState
import com.android.monuver.core.domain.util.Cycle
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.PrimaryActionButton
import com.android.monuver.core.presentation.components.StaticTextInputField
import com.android.monuver.core.presentation.components.TextAmountInputField
import com.android.monuver.core.presentation.components.TextDateInputField
import com.android.monuver.core.presentation.util.DatabaseCodeMapper
import com.android.monuver.core.presentation.util.isUpdateBudgetSuccess
import com.android.monuver.core.presentation.util.showMessageWithToast
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.core.presentation.util.toRupiahFieldValue
import com.android.monuver.feature.budgeting.R
import com.android.monuver.feature.budgeting.domain.model.EditBudgetState
import com.android.monuver.feature.budgeting.presentation.addBudget.CalendarField
import com.android.monuver.feature.budgeting.presentation.components.BudgetTextSwitchField
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
internal fun EditBudgetScreen(
    budgetState: BudgetState,
    result: DatabaseResultState?,
    onNavigateBack: () -> Unit,
    onEditBudget: (EditBudgetState) -> Unit
) {
    val context = LocalContext.current

    var maxAmount by rememberSaveable { mutableLongStateOf(budgetState.maxAmount) }
    var formattedMaxAmount by remember { mutableStateOf(TextFieldValue(maxAmount.toRupiah())) }
    var startDate by rememberSaveable { mutableStateOf(budgetState.startDate) }
    var endDate by rememberSaveable { mutableStateOf(budgetState.endDate) }
    var isOverflowAllowed by rememberSaveable { mutableStateOf(budgetState.isOverflowAllowed) }
    var isAutoUpdate by rememberSaveable { mutableStateOf(budgetState.isAutoUpdate) }
    var activeField by rememberSaveable { mutableStateOf<CalendarField?>(null) }

    val calendarState = rememberUseCaseState()

    LaunchedEffect(result) {
        result?.let { result ->
            result.showToast(context)
            if (result.isUpdateBudgetSuccess()) onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.edit_budget),
                onNavigateBack = onNavigateBack
            )
        },
        bottomBar = {
            PrimaryActionButton(
                text = stringResource(R.string.save),
                onClick = {
                    onEditBudget(
                        EditBudgetState(
                            id = budgetState.id,
                            category = budgetState.category,
                            maxAmount = maxAmount,
                            cycle = budgetState.cycle,
                            startDate = startDate,
                            endDate = endDate,
                            isOverflowAllowed = isOverflowAllowed,
                            isAutoUpdate = isAutoUpdate
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
            StaticTextInputField(
                title = stringResource(R.string.category),
                value = stringResource(DatabaseCodeMapper.toParentCategoryTitle(budgetState.category)),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            TextAmountInputField(
                title = stringResource(R.string.maximum_amount),
                value = formattedMaxAmount,
                onValueChange = {
                    maxAmount = it.toRupiahFieldValue().first
                    formattedMaxAmount = it.toRupiahFieldValue().second
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            TextDateInputField(
                title = stringResource(R.string.start_date),
                value = DateHelper.formatToReadable(startDate),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_budgeting_start_date),
                isEnable = budgetState.cycle == Cycle.CUSTOM,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            if (budgetState.cycle == Cycle.CUSTOM) {
                                calendarState.show()
                                activeField = CalendarField.START
                            }
                        }
                    )
                    .padding(horizontal = 16.dp)
            )
            TextDateInputField(
                title = stringResource(R.string.end_date),
                value = DateHelper.formatToReadable(endDate),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_budgeting_end_date),
                isEnable = budgetState.cycle == Cycle.CUSTOM,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            if (budgetState.cycle == Cycle.CUSTOM) {
                                calendarState.show()
                                activeField = CalendarField.END
                            }
                        }
                    )
                    .padding(horizontal = 16.dp)
            )
            BudgetTextSwitchField(
                isOverflowAllowed = isOverflowAllowed,
                onOverflowAllowedChange = { isOverflowAllowed = it },
                isAutoUpdate = isAutoUpdate,
                onAutoUpdateChange = { isAutoUpdate = it },
                budgetCycle = budgetState.cycle,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            val inputDate = LocalDate.parse(selectedDate.toString())
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

            when (activeField) {
                CalendarField.START -> {
                    val endDate = LocalDate.parse(startDate)
                    val isAfterEndDate = inputDate > endDate

                    if (inputDate > today) {
                        context.getString(R.string.you_can_not_select_future_start_date)
                            .showMessageWithToast(context)
                    } else if (isAfterEndDate) {
                        context.getString(R.string.start_date_can_not_after_end_date)
                            .showMessageWithToast(context)
                    } else {
                        startDate = selectedDate.toString()
                    }
                }
                CalendarField.END -> {
                    val startDate = LocalDate.parse(startDate)
                    val isBeforeStartDate = inputDate < startDate

                    if (isBeforeStartDate) {
                        context.getString(R.string.end_date_must_be_same_or_after_start_date)
                            .showMessageWithToast(context)
                    } else {
                        endDate = selectedDate.toString()
                    }
                }
                null -> {}
            }
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}