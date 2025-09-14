package com.android.monu.ui.feature.utils

import com.android.monu.R

sealed class DatabaseResultMessage(val message: Int) {
    object EmptyAccountName : DatabaseResultMessage(R.string.empty_account_name)
    object EmptyAccountType : DatabaseResultMessage(R.string.empty_account_type)
    object EmptyAccountBalance : DatabaseResultMessage(R.string.empty_account_balance)
    object InsufficientAccountBalance : DatabaseResultMessage(R.string.insufficient_account_balance)
    object CreateAccountSuccess : DatabaseResultMessage(R.string.your_account_successfully_created)

    object EmptyTransactionTitle : DatabaseResultMessage(R.string.empty_transaction_title)
    object EmptyTransactionDate : DatabaseResultMessage(R.string.empty_transaction_date)
    object EmptyTransactionCategory : DatabaseResultMessage(R.string.empty_transaction_category)
    object EmptyTransactionAmount : DatabaseResultMessage(R.string.empty_transaction_amount)
    object EmptyTransactionSource : DatabaseResultMessage(R.string.empty_transaction_source)
    object EmptyTransactionDestination : DatabaseResultMessage(R.string.empty_transaction_destination)
    object CreateTransactionSuccess : DatabaseResultMessage(R.string.transaction_successfully_added)
    data class CreateSuccessWithWarningCondition(
        val category: Int,
        val warningCondition: Int
    ) : DatabaseResultMessage(R.string.transaction_successfully_added)
    object UpdateTransactionSuccess : DatabaseResultMessage(R.string.transaction_successfully_saved)

    object EmptyBudgetCategory : DatabaseResultMessage(R.string.empty_budgeting_category)
    object EmptyBudgetMaxAmount : DatabaseResultMessage(R.string.empty_budgeting_max_amount)
    object CreateBudgetSuccess : DatabaseResultMessage(R.string.budgeting_successfully_added)
    object UpdateBudgetSuccess : DatabaseResultMessage(R.string.budgeting_successfully_saved)
    object ActiveBudgetWithCategoryAlreadyExists : DatabaseResultMessage(R.string.active_budgeting_with_category_already_exists)
    object CurrentBudgetAmountExceedsMaximumLimit : DatabaseResultMessage(R.string.current_budget_amount_exceeds_maximum_limit)

    object EmptyBillTitle : DatabaseResultMessage(R.string.empty_bill_title)
    object EmptyBillDate : DatabaseResultMessage(R.string.empty_bill_date)
    object EmptyBillAmount : DatabaseResultMessage(R.string.empty_bill_amount)
    object EmptyBillFixPeriod : DatabaseResultMessage(R.string.empty_bill_fix_period)
    object InvalidBillFixPeriod : DatabaseResultMessage(R.string.invalid_bill_fix_period)
    object CreateBillSuccess : DatabaseResultMessage(R.string.bill_successfully_added)
    object PayBillSuccess : DatabaseResultMessage(R.string.bill_successfully_paid)
    object UpdateBillSuccess : DatabaseResultMessage(R.string.bill_successfully_saved)

    object EmptySaveTitle : DatabaseResultMessage(R.string.empty_save_title)
    object EmptySaveTargetAmount : DatabaseResultMessage(R.string.empty_save_target_amount)
    object EmptySaveTargetDate : DatabaseResultMessage(R.string.empty_save_target_date)
    object CreateSaveSuccess : DatabaseResultMessage(R.string.save_successfully_added)
    object UpdateSaveSuccess : DatabaseResultMessage(R.string.save_successfully_saved)
    object CompleteSaveSuccess : DatabaseResultMessage(R.string.save_successfully_completed)
    object EmptyDepositDate : DatabaseResultMessage(R.string.empty_deposit_withdraw_date)
    object EmptyDepositAmount : DatabaseResultMessage(R.string.empty_deposit_withdraw_amount)
    object EmptyDepositAccount : DatabaseResultMessage(R.string.empty_deposit_withdraw_account)
    object CreateDepositTransactionSuccess : DatabaseResultMessage(R.string.save_amount_successfully_added)
    object EmptyWithdrawDate : DatabaseResultMessage(R.string.empty_deposit_withdraw_date)
    object EmptyWithdrawAmount : DatabaseResultMessage(R.string.empty_deposit_withdraw_amount)
    object EmptyWithdrawAccount : DatabaseResultMessage(R.string.empty_deposit_withdraw_account)
    object CreateWithdrawTransactionSuccess : DatabaseResultMessage(R.string.withdraw_save_amount_successfully)
}