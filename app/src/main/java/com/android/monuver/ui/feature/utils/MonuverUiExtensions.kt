package com.android.monuver.ui.feature.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.android.monuver.R
import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.utils.NumberHelper
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.ParametersHolder

fun String.showMessageWithToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun TextFieldValue.toRupiahFormat(): Pair<Long, TextFieldValue> {
    val cleanInput = text.replace(Regex("\\D"), "")
    val balance = cleanInput.toLongOrNull() ?: 0L

    val formattedText = NumberHelper.formatToRupiah(balance)
    val newCursorPosition = formattedText.length

    val formattedBalance = TextFieldValue(
        text = formattedText,
        selection = TextRange(newCursorPosition)
    )

    return balance to formattedBalance
}

@Composable
fun Modifier.debouncedClickable(
    debounceTime: Long = 700L,
    onClick: () -> Unit
): Modifier {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    return this.then(
        Modifier.clickable {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime >= debounceTime) {
                lastClickTime = currentTime
                onClick()
            }
        }
    )
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController,
    noinline parameters: (() -> ParametersHolder)? = null
): T {
    val navGraphRoute = destination.parent?.route ?: error("No parent route")
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return if (parameters != null) {
        koinViewModel(viewModelStoreOwner = parentEntry, parameters = parameters)
    } else {
        koinViewModel(viewModelStoreOwner = parentEntry)
    }
}

fun DatabaseResultState.showToast(context: Context) {
    when(this) {
        DatabaseResultState.ActivateAccountSuccess -> {
            context.getString(R.string.account_successfully_activated).showMessageWithToast(context)
        }
        DatabaseResultState.CreateAccountSuccess -> {
            context.getString(R.string.your_account_successfully_created).showMessageWithToast(context)
        }
        DatabaseResultState.DeactivateAccountSuccess -> {
            context.getString(R.string.account_successfully_deactivated).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyAccountBalance -> {
            context.getString(R.string.empty_account_balance).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyAccountName -> {
            context.getString(R.string.empty_account_name).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyAccountType -> {
            context.getString(R.string.empty_account_type).showMessageWithToast(context)
        }
        DatabaseResultState.InsufficientAccountBalance -> {
            context.getString(R.string.insufficient_account_balance).showMessageWithToast(context)
        }
        DatabaseResultState.UpdateAccountSuccess -> {
            context.getString(R.string.account_successfully_saved).showMessageWithToast(context)
        }
        DatabaseResultState.ActiveBudgetWithCategoryAlreadyExists -> {
            context.getString(R.string.active_budgeting_with_category_already_exists).showMessageWithToast(context)
        }
        DatabaseResultState.BackupDataFailed -> {
            context.getString(R.string.backup_data_failed).showMessageWithToast(context)
        }
        DatabaseResultState.BackupDataSuccess -> {
            context.getString(R.string.all_data_successfully_backed_up).showMessageWithToast(context)
        }
        DatabaseResultState.CancelBillSuccess -> {
            context.getString(R.string.bill_successfully_cancelled).showMessageWithToast(context)
        }
        DatabaseResultState.CompleteSavingSuccess -> {
            context.getString(R.string.save_successfully_completed).showMessageWithToast(context)
        }
        DatabaseResultState.CreateBillSuccess -> {
            context.getString(R.string.bill_successfully_added).showMessageWithToast(context)
        }
        DatabaseResultState.CreateBudgetSuccess -> {
            context.getString(R.string.budgeting_successfully_added).showMessageWithToast(context)
        }
        DatabaseResultState.CreateDepositTransactionSuccess -> {
            context.getString(R.string.save_amount_successfully_added).showMessageWithToast(context)
        }
        DatabaseResultState.CreateSavingSuccess -> {
            context.getString(R.string.save_successfully_added).showMessageWithToast(context)
        }
        is DatabaseResultState.CreateSuccessWithWarningCondition -> {
            context.getString(R.string.transaction_successfully_added).showMessageWithToast(context)
        }
        DatabaseResultState.CreateTransactionSuccess -> {
            context.getString(R.string.transaction_successfully_added).showMessageWithToast(context)
        }
        DatabaseResultState.CreateWithdrawTransactionSuccess -> {
            context.getString(R.string.withdraw_save_amount_successfully).showMessageWithToast(context)
        }
        DatabaseResultState.CurrentBudgetAmountExceedsMaximumLimit -> {
            context.getString(R.string.current_budget_amount_exceeds_maximum_limit).showMessageWithToast(context)
        }
        DatabaseResultState.DeleteAllDataSuccess -> {
            context.getString(R.string.all_data_successfully_deleted).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyBillAmount -> {
            context.getString(R.string.empty_bill_amount).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyBillDate -> {
            context.getString(R.string.empty_bill_date).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyBillFixPeriod -> {
            context.getString(R.string.empty_bill_fix_period).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyBillTitle -> {
            context.getString(R.string.empty_bill_title).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyBudgetCategory -> {
            context.getString(R.string.empty_budgeting_category).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyBudgetMaxAmount -> {
            context.getString(R.string.empty_budgeting_max_amount).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyDepositAccount -> {
            context.getString(R.string.empty_deposit_withdraw_account).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyDepositAmount -> {
            context.getString(R.string.empty_deposit_withdraw_amount).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyDepositDate -> {
            context.getString(R.string.empty_deposit_withdraw_date).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyReportEndDate -> {
            context.getString(R.string.empty_report_end_date).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyReportStartDate -> {
            context.getString(R.string.empty_report_start_date).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyReportTitle -> {
            context.getString(R.string.empty_report_title).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyReportUsername -> {
            context.getString(R.string.empty_report_username).showMessageWithToast(context)
        }
        DatabaseResultState.EmptySavingAmount -> {
            context.getString(R.string.empty_save_amount).showMessageWithToast(context)
        }
        DatabaseResultState.EmptySavingTargetAmount -> {
            context.getString(R.string.empty_save_target_amount).showMessageWithToast(context)
        }
        DatabaseResultState.EmptySavingTargetDate -> {
            context.getString(R.string.empty_save_target_date).showMessageWithToast(context)
        }
        DatabaseResultState.EmptySavingTitle -> {
            context.getString(R.string.empty_save_title).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyWithdrawAccount -> {
            context.getString(R.string.empty_deposit_withdraw_account).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyWithdrawAmount -> {
            context.getString(R.string.empty_deposit_withdraw_amount).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyWithdrawDate -> {
            context.getString(R.string.empty_deposit_withdraw_date).showMessageWithToast(context)
        }
        DatabaseResultState.ExportDataFailed -> {
            context.getString(R.string.export_data_failed).showMessageWithToast(context)
        }
        DatabaseResultState.InsufficientSavingBalance -> {
            context.getString(R.string.insufficient_saving_balance).showMessageWithToast(context)
        }
        DatabaseResultState.InvalidBillFixPeriod -> {
            context.getString(R.string.invalid_bill_fix_period).showMessageWithToast(context)
        }
        DatabaseResultState.PayBillSuccess -> {
            context.getString(R.string.bill_successfully_paid).showMessageWithToast(context)
        }
        DatabaseResultState.RestoreDataFailed -> {
            context.getString(R.string.restore_data_failed).showMessageWithToast(context)
        }
        DatabaseResultState.RestoreDataSuccess -> {
            context.getString(R.string.all_data_successfully_restored).showMessageWithToast(context)
        }
        DatabaseResultState.UpdateBillSuccess -> {
            context.getString(R.string.bill_successfully_saved).showMessageWithToast(context)
        }
        DatabaseResultState.UpdateBudgetSuccess -> {
            context.getString(R.string.budgeting_successfully_saved).showMessageWithToast(context)
        }
        DatabaseResultState.UpdateSavingSuccess -> {
            context.getString(R.string.save_successfully_saved).showMessageWithToast(context)
        }
        DatabaseResultState.UpdateTransactionSuccess -> {
            context.getString(R.string.transaction_successfully_saved).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyTransactionAmount -> {
            context.getString(R.string.empty_transaction_amount).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyTransactionCategory -> {
            context.getString(R.string.empty_transaction_category).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyTransactionDate -> {
            context.getString(R.string.empty_transaction_date).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyTransactionDestination -> {
            context.getString(R.string.empty_transaction_destination).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyTransactionSource -> {
            context.getString(R.string.empty_transaction_source).showMessageWithToast(context)
        }
        DatabaseResultState.EmptyTransactionTitle -> {
            context.getString(R.string.empty_transaction_title).showMessageWithToast(context)
        }
    }
}

fun DatabaseResultState.isCreateAccountSuccess() = this is DatabaseResultState.CreateAccountSuccess
fun DatabaseResultState.isUpdateAccountSuccess() = this is DatabaseResultState.UpdateAccountSuccess
fun DatabaseResultState.isCreateTransactionSuccess() = this is DatabaseResultState.CreateTransactionSuccess
fun DatabaseResultState.isCreateWithdrawTransactionSuccess() = this is DatabaseResultState.CreateWithdrawTransactionSuccess
fun DatabaseResultState.isCreateDepositTransactionSuccess() = this is DatabaseResultState.CreateDepositTransactionSuccess
fun DatabaseResultState.isUpdateTransactionSuccess() = this is DatabaseResultState.UpdateTransactionSuccess
fun DatabaseResultState.isCreateBillSuccess() = this is DatabaseResultState.CreateBillSuccess
fun DatabaseResultState.isUpdateBillSuccess() = this is DatabaseResultState.UpdateBillSuccess
fun DatabaseResultState.isPayBillSuccess() = this is DatabaseResultState.PayBillSuccess
fun DatabaseResultState.isCreateSavingSuccess() = this is DatabaseResultState.CreateSavingSuccess
fun DatabaseResultState.isUpdateSavingSuccess() = this is DatabaseResultState.UpdateSavingSuccess
fun DatabaseResultState.isCompleteSavingSuccess() = this is DatabaseResultState.CompleteSavingSuccess
fun DatabaseResultState.isCreateBudgetSuccess() = this is DatabaseResultState.CreateBudgetSuccess
fun DatabaseResultState.isUpdateBudgetSuccess() = this is DatabaseResultState.UpdateBudgetSuccess