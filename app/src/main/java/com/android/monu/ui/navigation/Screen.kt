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

@Serializable object BudgetDetail
@Serializable data class MainBudgetDetail(val id: Long = 0)
@Serializable data class EditBudget(val id: Long = 0)

@Serializable object AddBudget
@Serializable object MainAddBudget
@Serializable object AddBudgetCategory

@Serializable object InactiveBudget
@Serializable object MainInactiveBudget

@Serializable object AnalyticsCategoryTransaction
@Serializable data class MainAnalyticsCategoryTransaction(
    val category: Int = 0,
    val month: Int = 0,
    val year: Int = 0
)

@Serializable object Bill
@Serializable object MainBill
@Serializable object AddBill
@Serializable data class BillDetail(val id: Long = 0)

@Serializable object PayBill
@Serializable data class MainPayBill(val id: Long = 0)
@Serializable object PayBillCategory
@Serializable object PayBillSource