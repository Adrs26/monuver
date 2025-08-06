package com.android.monu.presentation.screen.budgeting.addbudgeting

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.android.monu.presentation.components.CommonAppBar
import com.android.monu.presentation.screen.budgeting.addbudgeting.components.AddBudgetingContent
import com.android.monu.presentation.screen.budgeting.addbudgeting.components.AddBudgetingContentActions
import com.android.monu.presentation.screen.budgeting.addbudgeting.components.AddBudgetingContentState
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
fun AddBudgetingScreen(
    category: Int,
    onNavigateBack: () -> Unit,
    onNavigateToCategory: () -> Unit
) {
    var budgetingMaxAmount by rememberSaveable { mutableLongStateOf(0L) }
    var budgetingMaxAmountFormat by remember {
        mutableStateOf(TextFieldValue(text = NumberFormatHelper.formatToRupiah(budgetingMaxAmount)))
    }
    var budgetingPeriod by rememberSaveable { mutableIntStateOf(1) }
    var budgetingStartDate by rememberSaveable {
        mutableStateOf(DateHelper.getFirstDayOfCurrentMonth())
    }
    var budgetingEndDate by rememberSaveable {
        mutableStateOf(DateHelper.getLastDayOfCurrentMonth())
    }
    var isBudgetingOverflowAllowed by rememberSaveable { mutableStateOf(false) }
    var isBudgetingAutoUpdate by rememberSaveable { mutableStateOf(false) }

    var activeField by rememberSaveable { mutableStateOf<CalendarField?>(null) }
    val calendarState = rememberSheetState()
    val context = LocalContext.current

    val addBudgetingContentState = AddBudgetingContentState(
        category = category,
        maxAmount = budgetingMaxAmount,
        maxAmountFormat = budgetingMaxAmountFormat,
        period = budgetingPeriod,
        startDate = budgetingStartDate,
        endDate = budgetingEndDate,
        isOverflowAllowed = isBudgetingOverflowAllowed,
        isAutoUpdate = isBudgetingAutoUpdate
    )

    val addBudgetingContentActions = object : AddBudgetingContentActions {
        override fun onNavigateToCategory() {
            onNavigateToCategory()
        }

        override fun onAmountChange(maxAmountFormat: TextFieldValue) {
            val cleanInput = maxAmountFormat.text.replace(Regex("\\D"), "")
            budgetingMaxAmount = try {
                cleanInput.toLong()
            } catch (_: NumberFormatException) { 0L }

            val formattedText = NumberFormatHelper.formatToRupiah(budgetingMaxAmount)
            val newCursorPosition = formattedText.length

            budgetingMaxAmountFormat = TextFieldValue(
                text = formattedText,
                selection = TextRange(newCursorPosition)
            )
        }

        override fun onPeriodChange(period: Int) {
            budgetingPeriod = period
            val previousStartDate = budgetingStartDate
            budgetingStartDate = getBudgetingStartDate(period, previousStartDate)
            val previousEndDate = budgetingEndDate
            budgetingEndDate = getBudgetingEndDate(period, previousEndDate)
            if (period == 3) isBudgetingAutoUpdate = false
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
            isBudgetingOverflowAllowed = isOverflowAllowed
        }

        override fun onAutoUpdateChange(isAutoUpdate: Boolean) {
            isBudgetingAutoUpdate = isAutoUpdate
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = "Tambah budget",
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        AddBudgetingContent(
            budgetingState = addBudgetingContentState,
            budgetingActions = addBudgetingContentActions,
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
                        "Tanggal mulai tidak bisa di masa depan"
                            .showMessageWithToast(context)
                    } else {
                        budgetingStartDate = selectedDate.toString()
                    }
                }
                CalendarField.END -> {
                    val startDate = LocalDate.parse(budgetingStartDate, DateTimeFormatter.ISO_LOCAL_DATE)
                    val isBeforeStartDate = inputDate.isBefore(startDate)

                    if (isBeforeStartDate) {
                        "Tanggal selesai harus sama atau lebih baru dari tanggal mulai"
                            .showMessageWithToast(context)
                    } else {
                        budgetingEndDate = selectedDate.toString()
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

private fun getBudgetingStartDate(budgetingPeriod: Int, previousDate: String): String {
    return when (budgetingPeriod) {
        1 -> DateHelper.getFirstDayOfCurrentMonth()
        2 -> DateHelper.getFirstDayOfCurrentWeek()
        else -> previousDate
    }
}

private fun getBudgetingEndDate(budgetingPeriod: Int, previousDate: String): String {
    return when (budgetingPeriod) {
        1 -> DateHelper.getLastDayOfCurrentMonth()
        2 -> DateHelper.getLastDayOfCurrentWeek()
        else -> previousDate
    }
}

enum class CalendarField { START, END }