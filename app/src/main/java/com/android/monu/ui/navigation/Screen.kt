package com.android.monu.ui.navigation

import kotlinx.serialization.Serializable

@Serializable object Main
@Serializable object Home
@Serializable object Transaction
@Serializable object Budgeting
@Serializable object Analytics

@Serializable object Settings
@Serializable object MainSettings

@Serializable object Account
@Serializable object MainAccount
@Serializable object AddAccount
@Serializable object AccountType

@Serializable object TransactionDetail
@Serializable data class MainTransactionDetail(val id: Long = 0)
@Serializable data class EditTransaction(val id: Long = 0)
@Serializable data class EditTransactionCategory(val type: Int = 0)
@Serializable data class EditTransfer(val id: Long = 0)

@Serializable object AddTransaction
@Serializable data class MainAddTransaction(val type: Int = 0)
@Serializable data class AddTransactionCategory(val type: Int = 0)
@Serializable object AddTransactionSource

@Serializable object Transfer
@Serializable object MainTransfer
@Serializable data class TransferAccount(val type: Int = 0)

@Serializable object BudgetingDetail
@Serializable data class MainBudgetingDetail(val id: Long = 0)
@Serializable data class EditBudgeting(val id: Long = 0)

@Serializable object AddBudgeting
@Serializable object MainAddBudgeting
@Serializable object AddBudgetingCategory

@Serializable object InactiveBudgeting
@Serializable object MainInactiveBudgeting

@Serializable object AnalyticsCategoryTransaction
@Serializable data class MainAnalyticsCategoryTransaction(
    val category: Int = 0,
    val month: Int = 0,
    val year: Int = 0
)