package com.android.monuver.feature.saving.presentation.withdraw

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
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.PrimaryActionButton
import com.android.monuver.core.presentation.components.StaticTextInputField
import com.android.monuver.core.presentation.components.TextAmountInputField
import com.android.monuver.core.presentation.components.TextDateInputField
import com.android.monuver.core.presentation.components.TextInputField
import com.android.monuver.core.presentation.util.isCreateWithdrawTransactionSuccess
import com.android.monuver.core.presentation.util.showMessageWithToast
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.core.presentation.util.toRupiahFieldValue
import com.android.monuver.feature.saving.R
import com.android.monuver.feature.saving.domain.model.DepositWithdrawState
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
internal fun WithdrawScreen(
    account: Pair<Int, String>,
    saving: Pair<Long, String>,
    result: DatabaseResultState?,
    withdrawActions: WithdrawActions
) {
    val context = LocalContext.current

    var date by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableLongStateOf(0L) }
    var formattedAmount by remember { mutableStateOf(TextFieldValue(amount.toRupiah())) }

    val calendarState = rememberUseCaseState()

    LaunchedEffect(result) {
        result?.let { result ->
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
        },
        bottomBar = {
            PrimaryActionButton(
                text = stringResource(R.string.withdraw),
                onClick = {
                    withdrawActions.onAddNewWithdraw(
                        DepositWithdrawState(
                            date = date,
                            amount = amount,
                            accountId = account.first,
                            accountName = account.second,
                            savingId = saving.first,
                            savingName = saving.second
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
                title = stringResource(R.string.saving),
                value = saving.second,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            TextDateInputField(
                title = stringResource(R.string.date),
                value = DateHelper.formatToReadable(date),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_transaction_date),
                isEnable = true,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = calendarState::show
                    )
                    .padding(horizontal = 16.dp)
            )
            TextAmountInputField(
                title = stringResource(R.string.amount),
                value = formattedAmount,
                onValueChange = {
                    amount = it.toRupiahFieldValue().first
                    formattedAmount = it.toRupiahFieldValue().second
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            TextInputField(
                title = stringResource(R.string.destination_account),
                value = account.second,
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_destination_account),
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = withdrawActions::onNavigateToAccount
                    )
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                isEnable = false
            )
        }
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            val inputDate = LocalDate.parse(selectedDate.toString())
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            if (inputDate > today) {
                context.getString(R.string.cannot_select_future_date).showMessageWithToast(context)
            } else {
                date = selectedDate.toString()
            }
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}

internal interface WithdrawActions {
    fun onNavigateBack()
    fun onNavigateToAccount()
    fun onAddNewWithdraw(withdrawState: DepositWithdrawState)
}