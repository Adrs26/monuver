package com.android.monuver.ui.feature.screen.budgeting.editBudget

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
import com.android.monuver.R
import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.EditBudgetState
import com.android.monuver.ui.feature.components.CommonAppBar
import com.android.monuver.ui.feature.screen.budgeting.addBudget.CalendarField
import com.android.monuver.ui.feature.screen.budgeting.editBudget.components.EditBudgetContentActions
import com.android.monuver.ui.feature.screen.budgeting.editBudget.components.EditBudgetingContent
import com.android.monuver.ui.feature.utils.isUpdateBudgetSuccess
import com.android.monuver.ui.feature.utils.showMessageWithToast
import com.android.monuver.ui.feature.utils.showToast
import com.android.monuver.utils.NumberHelper
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBudgetScreen(
    budgetUiState: EditBudgetUiState,
    onNavigateBack: () -> Unit,
    onEditBudget: (EditBudgetState) -> Unit
) {
    var budgetMaxAmount by rememberSaveable { mutableLongStateOf(budgetUiState.maxAmount) }
    var budgetMaxAmountFormat by remember {
        mutableStateOf(TextFieldValue(text = NumberHelper.formatToRupiah(budgetMaxAmount)))
    }
    var budgetStartDate by rememberSaveable { mutableStateOf(budgetUiState.startDate) }
    var budgetEndDate by rememberSaveable { mutableStateOf(budgetUiState.endDate) }
    var isBudgetOverflowAllowed by rememberSaveable { mutableStateOf(budgetUiState.isOverflowAllowed) }
    var isBudgetAutoUpdate by rememberSaveable { mutableStateOf(budgetUiState.isAutoUpdate) }

    var activeField by rememberSaveable { mutableStateOf<CalendarField?>(null) }
    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    val editBudgetState = EditBudgetState(
        id = budgetUiState.id,
        category = budgetUiState.category,
        maxAmount = budgetMaxAmount,
        cycle = budgetUiState.cycle,
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

            val formattedText = NumberHelper.formatToRupiah(budgetMaxAmount)
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

        override fun onEditBudget(budgetState: EditBudgetState) {
            onEditBudget(budgetState)
        }
    }

    LaunchedEffect(budgetUiState.editResult) {
        budgetUiState.editResult?.let { result ->
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
        }
    ) { innerPadding ->
        EditBudgetingContent(
            budgetState = editBudgetState,
            budgetMaxAmountFormat = budgetMaxAmountFormat,
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
                    val endDate = LocalDate.parse(budgetStartDate, DateTimeFormatter.ISO_LOCAL_DATE)
                    val isAfterEndDate = inputDate.isAfter(endDate)

                    if (isAfterToday) {
                        context.getString(R.string.you_can_not_select_future_start_date)
                            .showMessageWithToast(context)
                    } else if (isAfterEndDate) {
                        context.getString(R.string.start_date_can_not_after_end_date)
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

data class EditBudgetUiState(
    val id: Long,
    val category: Int,
    val maxAmount: Long,
    val cycle: Int,
    val startDate: String,
    val endDate: String,
    val isOverflowAllowed: Boolean,
    val isAutoUpdate: Boolean,
    val editResult: DatabaseResultState?
)