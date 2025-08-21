package com.android.monu.domain.repository

import com.android.monu.domain.model.account.Account
import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.usecase.finance.BudgetStatus

interface FinanceRepository {

    suspend fun createAccount(account: Account, transaction: Transaction): Long

    suspend fun createIncomeTransaction(transaction: Transaction): Long

    suspend fun createExpenseTransaction(transaction: Transaction): Long

    suspend fun createTransferTransaction(transaction: Transaction): Long

    suspend fun deleteIncomeTransaction(id: Long, sourceId: Int, amount: Long): Int

    suspend fun deleteExpenseTransaction(
        id: Long,
        parentCategory: Int,
        date: String,
        sourceId: Int,
        amount: Long
    ): Int

    suspend fun deleteTransferTransaction(
        id: Long,
        sourceId: Int,
        destinationId: Int,
        amount: Long
    ): Int

    suspend fun updateIncomeTransaction(transaction: Transaction, initialAmount: Long): Int

    suspend fun updateExpenseTransaction(
        transaction: Transaction,
        initialParentCategory: Int,
        initialDate: String,
        initialAmount: Long,
        budgetStatus: BudgetStatus
    ): Int

    suspend fun updateTransferTransaction(transaction: Transaction, initialAmount: Long): Int

    suspend fun payBill(
        billId: Long,
        billPaidDate: String,
        transaction: Transaction,
        isRecurring: Boolean,
        bill: Bill
    )
}