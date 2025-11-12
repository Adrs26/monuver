package com.android.monuver.ui.feature.screen.saving.deposit

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
import com.android.monuver.domain.model.DepositWithdrawState
import com.android.monuver.ui.feature.components.CommonAppBar
import com.android.monuver.ui.feature.screen.saving.deposit.components.DepositContent
import com.android.monuver.ui.feature.screen.saving.deposit.components.DepositContentActions
import com.android.monuver.ui.feature.utils.isCreateDepositTransactionSuccess
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
fun DepositScreen(
    depositUiState: DepositUiState,
    depositActions: DepositActions
) {
    var depositDate by rememberSaveable { mutableStateOf("") }
    var depositAmount by rememberSaveable { mutableLongStateOf(0L) }
    var depositAmountFormat by remember {
        mutableStateOf(TextFieldValue(NumberHelper.formatToRupiah(depositAmount)))
    }

    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    val depositState = DepositWithdrawState(
        date = depositDate,
        amount = depositAmount,
        accountId = depositUiState.account.first,
        accountName = depositUiState.account.second,
        savingId = depositUiState.saving.first,
        savingName = depositUiState.saving.second
    )

    val depositContentActions = object : DepositContentActions {
        override fun onDateClick() {
            calendarState.show()
        }

        override fun onAmountChange(amountFormat: TextFieldValue) {
            val cleanInput = amountFormat.text.replace(Regex("\\D"), "")
            depositAmount = try {
                cleanInput.toLong()
            } catch (_: NumberFormatException) { 0L }

            val formattedText = NumberHelper.formatToRupiah(depositAmount)
            val newCursorPosition = formattedText.length

            depositAmountFormat = TextFieldValue(
                text = formattedText,
                selection = TextRange(newCursorPosition)
            )
        }

        override fun onNavigateToAccount() {
            depositActions.onNavigateToAccount()
        }

        override fun onAddNewDeposit(depositState: DepositWithdrawState) {
            depositActions.onAddNewDeposit(depositState)
        }
    }

    LaunchedEffect(depositUiState.addResult) {
        depositUiState.addResult?.let { result ->
            result.showToast(context)
            if (result.isCreateDepositTransactionSuccess()) depositActions.onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.add_save_balance),
                onNavigateBack = depositActions::onNavigateBack
            )
        }
    ) { innerPadding ->
        DepositContent(
            depositState = depositState,
            depositAmountFormat = depositAmountFormat,
            depositActions = depositContentActions,
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
                depositDate = selectedDate.toString()
            }
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}

data class DepositUiState(
    val account: Pair<Int, String>,
    val saving: Pair<Long, String>,
    val addResult: DatabaseResultState?
)

interface DepositActions {
    fun onNavigateBack()
    fun onNavigateToAccount()
    fun onAddNewDeposit(depositState: DepositWithdrawState)
}