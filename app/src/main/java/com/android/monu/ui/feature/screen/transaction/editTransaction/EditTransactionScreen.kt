package com.android.monu.ui.feature.screen.transaction.editTransaction

import androidx.activity.compose.BackHandler
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
import com.android.monu.domain.model.EditTransactionState
import com.android.monu.domain.model.TransactionState
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.transaction.editTransaction.components.EditTransactionContent
import com.android.monu.ui.feature.screen.transaction.editTransaction.components.EditTransactionContentActions
import com.android.monu.ui.feature.utils.isUpdateTransactionSuccess
import com.android.monu.ui.feature.utils.showMessageWithToast
import com.android.monu.ui.feature.utils.showToast
import com.android.monu.utils.NumberHelper
import com.android.monu.utils.TransactionType
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    transactionUiState: EditTransactionUiState,
    transactionActions: EditTransactionActions,
    initialTransactionState: TransactionState
) {
    var transactionTitle by rememberSaveable { mutableStateOf(transactionUiState.title) }
    var transactionDate by rememberSaveable { mutableStateOf(transactionUiState.date) }
    var transactionAmount by rememberSaveable { mutableLongStateOf(transactionUiState.amount) }
    var transactionAmountFormat by remember {
        mutableStateOf(TextFieldValue(NumberHelper.formatToRupiah(transactionAmount)))
    }

    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    val editTransactionContentState = EditTransactionState(
        id = transactionUiState.id,
        title = transactionTitle,
        type = transactionUiState.type,
        parentCategory = transactionUiState.parentCategory,
        childCategory = transactionUiState.childCategory,
        initialParentCategory = initialTransactionState.parentCategory,
        date = transactionDate,
        initialDate = initialTransactionState.date,
        amount = transactionAmount,
        initialAmount = initialTransactionState.amount,
        sourceId = transactionUiState.sourceId,
        sourceName = transactionUiState.sourceName,
        isLocked = transactionUiState.isLocked
    )

    val editTransactionContentActions = object : EditTransactionContentActions {
        override fun onTitleChange(title: String) {
            transactionTitle = title
        }

        override fun onNavigateToCategory(transactionType: Int) {
            if (transactionType == TransactionType.INCOME ||
                transactionType == TransactionType.EXPENSE) {
                transactionActions.onNavigateToCategory(transactionType)
            }
        }

        override fun onDateClick() {
            calendarState.show()
        }

        override fun onAmountChange(amountFormat: TextFieldValue) {
            val cleanInput = amountFormat.text.replace(Regex("\\D"), "")
            transactionAmount = try {
                cleanInput.toLong()
            } catch (_: NumberFormatException) { 0L }

            val formattedText = NumberHelper.formatToRupiah(transactionAmount)
            val newCursorPosition = formattedText.length

            transactionAmountFormat = TextFieldValue(
                text = formattedText,
                selection = TextRange(newCursorPosition)
            )
        }

        override fun onEditTransaction(transactionState: EditTransactionState) {
            transactionActions.onEditTransaction(transactionState)
        }
    }

    LaunchedEffect(transactionUiState.editResult) {
        transactionUiState.editResult?.let { result ->
            result.showToast(context)
            if (result.isUpdateTransactionSuccess()) transactionActions.onNavigateBack()
        }
    }

    BackHandler { transactionActions.onNavigateBack() }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.edit_transaction),
                onNavigateBack =transactionActions::onNavigateBack
            )
        }
    ) { innerPadding ->
        EditTransactionContent(
            transactionState = editTransactionContentState,
            transactionAmountFormat = transactionAmountFormat,
            transactionActions = editTransactionContentActions,
            modifier = Modifier.padding(innerPadding)
        )
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            val inputDate = LocalDate.parse(selectedDate.toString(), DateTimeFormatter.ISO_LOCAL_DATE)
            val today = LocalDate.now()
            val isAfterToday = inputDate.isAfter(today)
            if (isAfterToday) {
                context.getString(R.string.cannot_select_future_date).showMessageWithToast(context)
            } else {
                transactionDate = selectedDate.toString()
            }
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}

data class EditTransactionUiState(
    val id: Long,
    val title: String,
    val type: Int,
    val parentCategory: Int,
    val childCategory: Int,
    val date: String,
    val amount: Long,
    val sourceId: Int,
    val sourceName: String,
    val isLocked: Boolean,
    val editResult: DatabaseResultState?
)

interface EditTransactionActions {
    fun onNavigateBack()
    fun onNavigateToCategory(transactionType: Int)
    fun onEditTransaction(transactionState: EditTransactionState)
}