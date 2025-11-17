package com.android.monuver.feature.budgeting.presentation.addBudget

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
import androidx.compose.runtime.mutableIntStateOf
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
import com.android.monuver.feature.budgeting.presentation.components.BudgetTextSwitchField
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.CycleFilterField
import com.android.monuver.core.presentation.components.TextAmountInputField
import com.android.monuver.core.presentation.components.TextDateInputField
import com.android.monuver.core.presentation.components.TextInputField
import com.android.monuver.core.presentation.util.DatabaseCodeMapper
import com.android.monuver.core.presentation.util.isCreateBudgetSuccess
import com.android.monuver.core.presentation.util.showMessageWithToast
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.core.presentation.util.toRupiahFieldValue
import com.android.monuver.core.domain.util.Cycle
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.feature.budgeting.R
import com.android.monuver.feature.budgeting.domain.model.AddBudgetState
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
internal fun AddBudgetScreen(
    category: Int,
    result: DatabaseResultState?,
    onNavigateBack: () -> Unit,
    onNavigateToCategory: () -> Unit,
    onAddNewBudget: (AddBudgetState) -> Unit
) {
    val context = LocalContext.current

    var maxAmount by rememberSaveable { mutableLongStateOf(0L) }
    var formattedMaxAmount by remember { mutableStateOf(TextFieldValue(maxAmount.toRupiah())) }
    var cycle by rememberSaveable { mutableIntStateOf(Cycle.MONTHLY) }
    var startDate by rememberSaveable { mutableStateOf(DateHelper.getFirstDayOfCurrentMonth()) }
    var endDate by rememberSaveable { mutableStateOf(DateHelper.getLastDayOfCurrentMonth()) }
    var isOverflowAllowed by rememberSaveable { mutableStateOf(false) }
    var isAutoUpdate by rememberSaveable { mutableStateOf(false) }
    var activeField by rememberSaveable { mutableStateOf<CalendarField?>(null) }

    val calendarState = rememberUseCaseState()

    LaunchedEffect(result) {
        result?.let { result ->
            result.showToast(context)
            if (result.isCreateBudgetSuccess()) onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.add_budgeting),
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
                title = stringResource(R.string.category),
                value = if (category == 0) "" else
                    stringResource(DatabaseCodeMapper.toParentCategoryTitle(category)),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_transaction_category),
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onNavigateToCategory
                    )
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                isEnable = false
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
            CycleFilterField(
                cycles = listOf(Cycle.MONTHLY, Cycle.WEEKLY, Cycle.CUSTOM),
                selectedCycle = cycle,
                onCycleChange = { cycle = it },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            TextDateInputField(
                title = stringResource(R.string.start_date),
                value = DateHelper.formatToReadable(startDate),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_budgeting_start_date),
                isEnable = cycle == Cycle.CUSTOM,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            if (cycle == Cycle.CUSTOM) {
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
                isEnable = cycle == Cycle.CUSTOM,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            if (cycle == Cycle.CUSTOM) {
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
                budgetCycle = cycle,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    onAddNewBudget(
                        AddBudgetState(
                            category = category,
                            maxAmount = maxAmount,
                            cycle = cycle,
                            startDate = getBudgetStartDate(cycle, startDate),
                            endDate = getBudgetEndDate(cycle, endDate),
                            isOverflowAllowed = isOverflowAllowed,
                            isAutoUpdate = isAutoUpdate
                        )
                    )
                },
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

private fun getBudgetStartDate(budgetCycle: Int, previousDate: String): String {
    return when (budgetCycle) {
        Cycle.MONTHLY -> DateHelper.getFirstDayOfCurrentMonth()
        Cycle.WEEKLY -> DateHelper.getFirstDayOfCurrentWeek()
        else -> previousDate
    }
}

private fun getBudgetEndDate(budgetCycle: Int, previousDate: String): String {
    return when (budgetCycle) {
        Cycle.MONTHLY -> DateHelper.getLastDayOfCurrentMonth()
        Cycle.WEEKLY -> DateHelper.getLastDayOfCurrentWeek()
        else -> previousDate
    }
}

enum class CalendarField { START, END }