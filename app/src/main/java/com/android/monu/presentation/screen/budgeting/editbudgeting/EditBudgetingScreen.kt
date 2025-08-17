package com.android.monu.presentation.screen.budgeting.editbudgeting

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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.android.monu.R
import com.android.monu.presentation.components.CommonAppBar
import com.android.monu.presentation.screen.budgeting.addbudgeting.CalendarField
import com.android.monu.presentation.screen.budgeting.editbudgeting.components.EditBudgetingContent
import com.android.monu.presentation.screen.budgeting.editbudgeting.components.EditBudgetingContentActions
import com.android.monu.presentation.screen.budgeting.editbudgeting.components.EditBudgetingContentState
import com.android.monu.presentation.utils.DatabaseResultMessage
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
fun EditBudgetingScreen(
    budgetingState: EditBudgetingState,
    onNavigateBack: () -> Unit,
    onEditBudgeting: (EditBudgetingContentState) -> Unit
) {
    var budgetingMaxAmount by rememberSaveable { mutableLongStateOf(budgetingState.maxAmount) }
    var budgetingMaxAmountFormat by remember {
        mutableStateOf(TextFieldValue(text = NumberFormatHelper.formatToRupiah(budgetingMaxAmount)))
    }
    var budgetingStartDate by rememberSaveable { mutableStateOf(budgetingState.startDate) }
    var budgetingEndDate by rememberSaveable { mutableStateOf(budgetingState.endDate) }
    var isBudgetingOverflowAllowed by rememberSaveable { mutableStateOf(budgetingState.isOverflowAllowed) }
    var isBudgetingAutoUpdate by rememberSaveable { mutableStateOf(budgetingState.isAutoUpdate) }

    var activeField by rememberSaveable { mutableStateOf<CalendarField?>(null) }
    val calendarState = rememberSheetState()
    val context = LocalContext.current

    val editBudgetingContentState = EditBudgetingContentState(
        id = budgetingState.id,
        category = budgetingState.category,
        maxAmount = budgetingMaxAmount,
        maxAmountFormat = budgetingMaxAmountFormat,
        period = budgetingState.period,
        startDate = budgetingStartDate,
        endDate = budgetingEndDate,
        isOverflowAllowed = isBudgetingOverflowAllowed,
        isAutoUpdate = isBudgetingAutoUpdate
    )

    val editBudgetingContentActions = object : EditBudgetingContentActions {
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

        override fun onEditBudgeting(budgetingState: EditBudgetingContentState) {
            onEditBudgeting(budgetingState)
        }
    }

    LaunchedEffect(budgetingState.editResult) {
        budgetingState.editResult?.let { result ->
            context.getString(result.message).showMessageWithToast(context)
            if (result == DatabaseResultMessage.UpdateBudgetingSuccess) {
                onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = "Edit Budgeting",
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        EditBudgetingContent(
            budgetingState = editBudgetingContentState,
            budgetingActions = editBudgetingContentActions,
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
                        budgetingStartDate = selectedDate.toString()
                    }
                }
                CalendarField.END -> {
                    val startDate = LocalDate.parse(budgetingStartDate, DateTimeFormatter.ISO_LOCAL_DATE)
                    val isBeforeStartDate = inputDate.isBefore(startDate)

                    if (isBeforeStartDate) {
                        context.getString(R.string.end_date_must_be_same_or_after_start_date)
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

data class EditBudgetingState(
    val id: Long,
    val category: Int,
    val maxAmount: Long,
    val period: Int,
    val startDate: String,
    val endDate: String,
    val isOverflowAllowed: Boolean,
    val isAutoUpdate: Boolean,
    val editResult: DatabaseResultMessage?
)