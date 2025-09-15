package com.android.monu.domain.repository

import com.android.monu.domain.model.account.Account
import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.model.saving.Saving
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.usecase.finance.BudgetStatus

interface FinanceRepository {

    suspend fun createAccount(account: Account, transaction: Transaction): Long

    suspend fun updateAccountStatus(accountId: Int, isActive: Boolean)

    suspend fun createIncomeTransaction(transaction: Transaction): Long

    suspend fun createExpenseTransaction(transaction: Transaction): Long

    suspend fun createTransferTransaction(transaction: Transaction): Long

    suspend fun createDepositTransaction(savingId: Long, transaction: Transaction)

    suspend fun createWithdrawTransaction(savingId: Long, transaction: Transaction)

    suspend fun deleteIncomeTransaction(transactionId: Long, sourceId: Int, amount: Long): Int

    suspend fun deleteExpenseTransaction(
        transactionId: Long,
        parentCategory: Int,
        date: String,
        sourceId: Int,
        amount: Long
    ): Int

    suspend fun deleteSavingTransaction(
        transactionId: Long,
        category: Int,
        accountId: Int,
        savingId: Long,
        amount: Long
    )

    suspend fun updateIncomeTransaction(transaction: Transaction, initialAmount: Long): Int

    suspend fun updateExpenseTransaction(
        transaction: Transaction,
        initialParentCategory: Int,
        initialDate: String,
        initialAmount: Long,
        budgetStatus: BudgetStatus
    ): Int

    suspend fun processBillPayment(
        billId: Long,
        billPaidDate: String,
        transaction: Transaction,
        isRecurring: Boolean,
        bill: Bill
    )

    suspend fun cancelBillPayment(billId: Long)

    suspend fun updateSaving(saving: Saving)

    suspend fun completeSaving(transaction: Transaction, savingId: Long)
}