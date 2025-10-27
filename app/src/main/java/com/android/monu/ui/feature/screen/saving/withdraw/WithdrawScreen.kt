package com.android.monu.ui.feature.screen.saving.withdraw

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
import com.android.monu.domain.model.DepositWithdrawState
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.saving.withdraw.components.WithdrawContent
import com.android.monu.ui.feature.screen.saving.withdraw.components.WithdrawContentActions
import com.android.monu.ui.feature.utils.isCreateWithdrawTransactionSuccess
import com.android.monu.ui.feature.utils.showMessageWithToast
import com.android.monu.ui.feature.utils.showToast
import com.android.monu.utils.NumberHelper
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawScreen(
    withdrawUiState: WithdrawUiState,
    withdrawActions: WithdrawActions
) {
    var withdrawDate by rememberSaveable { mutableStateOf("") }
    var withdrawAmount by rememberSaveable { mutableLongStateOf(0L) }
    var withdrawAmountFormat by remember {
        mutableStateOf(TextFieldValue(NumberHelper.formatToRupiah(withdrawAmount)))
    }

    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    val withdrawState = DepositWithdrawState(
        date = withdrawDate,
        amount = withdrawAmount,
        accountId = withdrawUiState.account.first,
        accountName = withdrawUiState.account.second,
        savingId = withdrawUiState.saving.first,
        savingName = withdrawUiState.saving.second
    )

    val withdrawContentActions = object : WithdrawContentActions {
        override fun onDateClick() {
            calendarState.show()
        }

        override fun onAmountChange(amountFormat: TextFieldValue) {
            val cleanInput = amountFormat.text.replace(Regex("\\D"), "")
            withdrawAmount = try {
                cleanInput.toLong()
            } catch (_: NumberFormatException) { 0L }

            val formattedText = NumberHelper.formatToRupiah(withdrawAmount)
            val newCursorPosition = formattedText.length

            withdrawAmountFormat = TextFieldValue(
                text = formattedText,
                selection = TextRange(newCursorPosition)
            )
        }

        override fun onNavigateToAccount() {
            withdrawActions.onNavigateToAccount()
        }

        override fun onAddNewWithdraw(withdrawState: DepositWithdrawState) {
            withdrawActions.onAddNewWithdraw(withdrawState)
        }
    }

    LaunchedEffect(withdrawUiState.addResult) {
        withdrawUiState.addResult?.let { result ->
            result.showToast(context)
            if (result.isCreateWithdrawTransactionSuccess()) withdrawActions.onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.withdraw_save_balance),
                onNavigateBack = withdrawActions::onNavigateBack
            )
        }
    ) { innerPadding ->
        WithdrawContent(
            withdrawState = withdrawState,
            withdrawAmountFormat = withdrawAmountFormat,
            withdrawActions = withdrawContentActions,
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
                withdrawDate = selectedDate.toString()
            }
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}

data class WithdrawUiState(
    val account: Pair<Int, String>,
    val saving: Pair<Long, String>,
    val addResult: DatabaseResultState?
)

interface WithdrawActions {
    fun onNavigateBack()
    fun onNavigateToAccount()
    fun onAddNewWithdraw(withdrawState: DepositWithdrawState)
}