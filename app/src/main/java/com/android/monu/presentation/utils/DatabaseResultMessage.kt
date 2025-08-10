package com.android.monu.presentation.utils

import com.android.monu.R

sealed class DatabaseResultMessage {
    object Loading : DatabaseResultMessage()
    object CreateTransactionSuccess : DatabaseResultMessage()
    object EmptyTransactionTitle : DatabaseResultMessage()
    object EmptyTransactionDate : DatabaseResultMessage()
    object EmptyTransactionCategory : DatabaseResultMessage()
    object EmptyTransactionAmount : DatabaseResultMessage()
    object EmptyTransactionSource : DatabaseResultMessage()
    object InsufficientAccountBalance : DatabaseResultMessage()
    object ActiveBudgetingWithCategoryAlreadyExists : DatabaseResultMessage()
    object CurrentBudgetAmountExceedsMaximumLimit : DatabaseResultMessage()
}

fun mapResultMessageToStringResource(message: DatabaseResultMessage): Int {
    return when (message) {
        DatabaseResultMessage.Loading -> R.string.loading
        DatabaseResultMessage.CreateTransactionSuccess -> R.string.transaction_successfully_saved
        DatabaseResultMessage.EmptyTransactionTitle -> R.string.empty_transaction_title
        DatabaseResultMessage.EmptyTransactionDate -> R.string.empty_transaction_date
        DatabaseResultMessage.EmptyTransactionCategory -> R.string.empty_transaction_category
        DatabaseResultMessage.EmptyTransactionAmount -> R.string.empty_transaction_amount
        DatabaseResultMessage.EmptyTransactionSource -> R.string.empty_transaction_source
        DatabaseResultMessage.InsufficientAccountBalance -> R.string.insufficient_account_balance
        DatabaseResultMessage.ActiveBudgetingWithCategoryAlreadyExists -> R.string.active_budgeting_with_category_already_exists
        DatabaseResultMessage.CurrentBudgetAmountExceedsMaximumLimit -> R.string.current_budget_amount_exceeds_maximum_limit
    }
}