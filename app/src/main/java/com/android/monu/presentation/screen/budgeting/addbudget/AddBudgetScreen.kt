package com.android.monu.presentation.screen.budgeting.addbudget

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.android.monu.R
import com.android.monu.presentation.components.CommonAppBar
import com.android.monu.presentation.screen.budgeting.addbudget.components.AddBudgetContent
import com.android.monu.presentation.screen.budgeting.addbudget.components.AddBudgetContentActions
import com.android.monu.presentation.screen.budgeting.addbudget.components.AddBudgetContentState
import com.android.monu.presentation.utils.DatabaseResultMessage
import com.android.monu.presentation.utils.DateHelper
import com.android.monu.presentation.utils.NumberFormatHelper
import com.android.monu.presentation.utils.showMessageWithToast
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBudgetScreen(
    category: Int,
    addResult: DatabaseResultMessage?,
    budgetActions: AddBudgetActions
) {
    var budgetMaxAmount by rememberSaveable { mutableLongStateOf(0L) }
    var budgetMaxAmountFormat by remember {
        mutableStateOf(TextFieldValue(text = NumberFormatHelper.formatToRupiah(budgetMaxAmount)))
    }
    var budgetPeriod by rememberSaveable { mutableIntStateOf(1) }
    var budgetStartDate by rememberSaveable {
        mutableStateOf(DateHelper.getFirstDayOfCurrentMonth())
    }
    var budgetEndDate by rememberSaveable {
        mutableStateOf(DateHelper.getLastDayOfCurrentMonth())
    }
    var isBudgetOverflowAllowed by rememberSaveable { mutableStateOf(false) }
    var isBudgetAutoUpdate by rememberSaveable { mutableStateOf(false) }

    var activeField by rememberSaveable { mutableStateOf<CalendarField?>(null) }
    val calendarState = rememberSheetState()
    val context = LocalContext.current

    val addBudgetContentState = AddBudgetContentState(
        category = category,
        maxAmount = budgetMaxAmount,
        maxAmountFormat = budgetMaxAmountFormat,
        period = budgetPeriod,
        startDate = budgetStartDate,
        endDate = budgetEndDate,
        isOverflowAllowed = isBudgetOverflowAllowed,
        isAutoUpdate = isBudgetAutoUpdate
    )

    val addBudgetContentActions = object : AddBudgetContentActions {
        override fun onNavigateToCategory() {
            budgetActions.onNavigateToCategory()
        }

        override fun onAmountChange(maxAmountFormat: TextFieldValue) {
            val cleanInput = maxAmountFormat.text.replace(Regex("\\D"), "")
            budgetMaxAmount = try {
                cleanInput.toLong()
            } catch (_: NumberFormatException) { 0L }

            val formattedText = NumberFormatHelper.formatToRupiah(budgetMaxAmount)
            val newCursorPosition = formattedText.length

            budgetMaxAmountFormat = TextFieldValue(
                text = formattedText,
                selection = TextRange(newCursorPosition)
            )
        }

        override fun onPeriodChange(period: Int) {
            budgetPeriod = period
            val previousStartDate = budgetStartDate
            budgetStartDate = getBudgetStartDate(period, previousStartDate)
            val previousEndDate = budgetEndDate
            budgetEndDate = getBudgetEndDate(period, previousEndDate)
            if (period == 3) isBudgetAutoUpdate = false
        }

        override fun onStartDateClick() {
            calendarState.show()
            activeField = CalendarField.START
        }

        override fun onEndDateClick() {
            calendarState.show()
            activeField = CalendarField.END
        }

        override fun onOverflowAllowedChange(isOverflowAllowed: Boolean) {
            isBudgetOverflowAllowed = isOverflowAllowed
        }

        override fun onAutoUpdateChange(isAutoUpdate: Boolean) {
            isBudgetAutoUpdate = isAutoUpdate
        }

        override fun onAddNewBudget(budgetState: AddBudgetContentState) {
            budgetActions.onAddNewBudget(budgetState)
        }
    }

    LaunchedEffect(addResult) {
        addResult?.let { result ->
            context.getString(result.message).showMessageWithToast(context)
            if (result == DatabaseResultMessage.CreateBudgetSuccess) {
                budgetActions.onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.add_budgeting),
                onNavigateBack = { budgetActions.onNavigateBack() }
            )
        }
    ) { innerPadding ->
        AddBudgetContent(
            budgetState = addBudgetContentState,
            budgetActions = addBudgetContentActions,
            modifier = Modifier.padding(innerPadding)
        )
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            val inputDate = LocalDate.parse(selectedDate.toString(), DateTimeFormatter.ISO_LOCAL_DATE)
            val today = LocalDate.now()
            val isAfterToday = inputDate.isAfter(today)

            when (activeField) {
                CalendarField.START -> {
                    if (isAfterToday) {
                        context.getString(R.string.you_can_not_select_future_start_date)
                            .showMessageWithToast(context)
                    } else {
                        budgetStartDate = selectedDate.toString()
                    }
                }
                CalendarField.END -> {
                    val startDate = LocalDate.parse(budgetStartDate, DateTimeFormatter.ISO_LOCAL_DATE)
                    val isBeforeStartDate = inputDate.isBefore(startDate)

                    if (isBeforeStartDate) {
                        context.getString(R.string.end_date_must_be_same_or_after_start_date)
                            .showMessageWithToast(context)
                    } else {
                        budgetEndDate = selectedDate.toString()
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

private fun getBudgetStartDate(budgetPeriod: Int, previousDate: String): String {
    return when (budgetPeriod) {
        1 -> DateHelper.getFirstDayOfCurrentMonth()
        2 -> DateHelper.getFirstDayOfCurrentWeek()
        else -> previousDate
    }
}

private fun getBudgetEndDate(budgetPeriod: Int, previousDate: String): String {
    return when (budgetPeriod) {
        1 -> DateHelper.getLastDayOfCurrentMonth()
        2 -> DateHelper.getLastDayOfCurrentWeek()
        else -> previousDate
    }
}

enum class CalendarField { START, END }

interface AddBudgetActions {
    fun onNavigateBack()
    fun onNavigateToCategory()
    fun onAddNewBudget(budgetState: AddBudgetContentState)
}