package com.android.monu.presentation.utils

import com.android.monu.R

sealed class DatabaseResultMessage {
    object EmptyAccountName : DatabaseResultMessage()
    object EmptyAccountType : DatabaseResultMessage()
    object EmptyAccountBalance : DatabaseResultMessage()
    object CreateAccountSuccess : DatabaseResultMessage()
    object EmptyTransactionTitle : DatabaseResultMessage()
    object EmptyTransactionDate : DatabaseResultMessage()
    object EmptyTransactionCategory : DatabaseResultMessage()
    object EmptyTransactionAmount : DatabaseResultMessage()
    object EmptyTransactionSource : DatabaseResultMessage()
    object EmptyTransactionDestination : DatabaseResultMessage()
    object CreateTransactionSuccess : DatabaseResultMessage()
    object DeleteTransactionSuccess : DatabaseResultMessage()
    object UpdateTransactionSuccess : DatabaseResultMessage()
    object EmptyBudgetingCategory : DatabaseResultMessage()
    object EmptyBudgetingMaxAmount : DatabaseResultMessage()
    object CreateBudgetingSuccess : DatabaseResultMessage()
    object InsufficientAccountBalance : DatabaseResultMessage()
    object ActiveBudgetingWithCategoryAlreadyExists : DatabaseResultMessage()
    object CurrentBudgetAmountExceedsMaximumLimit : DatabaseResultMessage()
}

fun mapResultMessageToStringResource(message: DatabaseResultMessage): Int {
    return when (message) {
        DatabaseResultMessage.EmptyAccountName -> R.string.empty_account_name
        DatabaseResultMessage.EmptyAccountType -> R.string.empty_account_type
        DatabaseResultMessage.EmptyAccountBalance -> R.string.empty_account_balance
        DatabaseResultMessage.CreateAccountSuccess -> R.string.your_account_successfully_created
        DatabaseResultMessage.EmptyTransactionTitle -> R.string.empty_transaction_title
        DatabaseResultMessage.EmptyTransactionDate -> R.string.empty_transaction_date
        DatabaseResultMessage.EmptyTransactionCategory -> R.string.empty_transaction_category
        DatabaseResultMessage.EmptyTransactionAmount -> R.string.empty_transaction_amount
        DatabaseResultMessage.EmptyTransactionSource -> R.string.empty_transaction_source
        DatabaseResultMessage.EmptyTransactionDestination -> R.string.empty_transaction_destination
        DatabaseResultMessage.CreateTransactionSuccess -> R.string.transaction_successfully_added
        DatabaseResultMessage.DeleteTransactionSuccess -> R.string.transaction_successfully_deleted
        DatabaseResultMessage.UpdateTransactionSuccess -> R.string.transaction_successfully_saved
        DatabaseResultMessage.EmptyBudgetingCategory -> R.string.empty_budgeting_category
        DatabaseResultMessage.EmptyBudgetingMaxAmount -> R.string.empty_budgeting_max_amount
        DatabaseResultMessage.CreateBudgetingSuccess -> R.string.budgeting_successfully_added
        DatabaseResultMessage.InsufficientAccountBalance -> R.string.insufficient_account_balance
        DatabaseResultMessage.ActiveBudgetingWithCategoryAlreadyExists -> R.string.active_budgeting_with_category_already_exists
        DatabaseResultMessage.CurrentBudgetAmountExceedsMaximumLimit -> R.string.current_budget_amount_exceeds_maximum_limit
    }
}