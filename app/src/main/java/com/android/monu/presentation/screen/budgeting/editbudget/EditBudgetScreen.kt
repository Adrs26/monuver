package com.android.monu.presentation.screen.budgeting.editbudget

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
import com.android.monu.presentation.screen.budgeting.addbudget.CalendarField
import com.android.monu.presentation.screen.budgeting.editbudget.components.EditBudgetContentActions
import com.android.monu.presentation.screen.budgeting.editbudget.components.EditBudgetContentState
import com.android.monu.presentation.screen.budgeting.editbudget.components.EditBudgetingContent
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
fun EditBudgetScreen(
    budgetState: EditBudgetState,
    onNavigateBack: () -> Unit,
    onEditBudget: (EditBudgetContentState) -> Unit
) {
    var budgetMaxAmount by rememberSaveable { mutableLongStateOf(budgetState.maxAmount) }
    var budgetMaxAmountFormat by remember {
        mutableStateOf(TextFieldValue(text = NumberFormatHelper.formatToRupiah(budgetMaxAmount)))
    }
    var budgetStartDate by rememberSaveable { mutableStateOf(budgetState.startDate) }
    var budgetEndDate by rememberSaveable { mutableStateOf(budgetState.endDate) }
    var isBudgetOverflowAllowed by rememberSaveable { mutableStateOf(budgetState.isOverflowAllowed) }
    var isBudgetAutoUpdate by rememberSaveable { mutableStateOf(budgetState.isAutoUpdate) }

    var activeField by rememberSaveable { mutableStateOf<CalendarField?>(null) }
    val calendarState = rememberSheetState()
    val context = LocalContext.current

    val editBudgetContentState = EditBudgetContentState(
        id = budgetState.id,
        category = budgetState.category,
        maxAmount = budgetMaxAmount,
        maxAmountFormat = budgetMaxAmountFormat,
        period = budgetState.period,
        startDate = budgetStartDate,
        endDate = budgetEndDate,
        isOverflowAllowed = isBudgetOverflowAllowed,
        isAutoUpdate = isBudgetAutoUpdate
    )

    val editBudgetContentActions = object : EditBudgetContentActions {
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

        override fun onEditBudget(budgetState: EditBudgetContentState) {
            onEditBudget(budgetState)
        }
    }

    LaunchedEffect(budgetState.editResult) {
        budgetState.editResult?.let { result ->
            context.getString(result.message).showMessageWithToast(context)
            if (result == DatabaseResultMessage.UpdateBudgetSuccess) {
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
            budgetState = editBudgetContentState,
            budgetActions = editBudgetContentActions,
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

data class EditBudgetState(
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