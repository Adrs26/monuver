package com.android.monu.presentation.utils

import com.android.monu.R

sealed class DatabaseResultMessage(val message: Int) {
    object EmptyAccountName : DatabaseResultMessage(R.string.empty_account_name)
    object EmptyAccountType : DatabaseResultMessage(R.string.empty_account_type)
    object EmptyAccountBalance : DatabaseResultMessage(R.string.empty_account_balance)
    object CreateAccountSuccess : DatabaseResultMessage(R.string.your_account_successfully_created)
    object EmptyTransactionTitle : DatabaseResultMessage(R.string.empty_transaction_title)
    object EmptyTransactionDate : DatabaseResultMessage(R.string.empty_transaction_date)
    object EmptyTransactionCategory : DatabaseResultMessage(R.string.empty_transaction_category)
    object EmptyTransactionAmount : DatabaseResultMessage(R.string.empty_transaction_amount)
    object EmptyTransactionSource : DatabaseResultMessage(R.string.empty_transaction_source)
    object EmptyTransactionDestination : DatabaseResultMessage(R.string.empty_transaction_destination)
    object CreateTransactionSuccess : DatabaseResultMessage(R.string.transaction_successfully_added)
    object UpdateTransactionSuccess : DatabaseResultMessage(R.string.transaction_successfully_saved)
    object EmptyBudgetingCategory : DatabaseResultMessage(R.string.empty_budgeting_category)
    object EmptyBudgetingMaxAmount : DatabaseResultMessage(R.string.empty_budgeting_max_amount)
    object CreateBudgetingSuccess : DatabaseResultMessage(R.string.budgeting_successfully_added)
    object UpdateBudgetingSuccess : DatabaseResultMessage(R.string.budgeting_successfully_saved)
    object InsufficientAccountBalance : DatabaseResultMessage(R.string.insufficient_account_balance)
    object ActiveBudgetingWithCategoryAlreadyExists : DatabaseResultMessage(R.string.active_budgeting_with_category_already_exists)
    object CurrentBudgetAmountExceedsMaximumLimit : DatabaseResultMessage(R.string.current_budget_amount_exceeds_maximum_limit)
}