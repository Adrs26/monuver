package com.android.monu.domain.repository

import com.android.monu.domain.common.BudgetStatusState
import com.android.monu.domain.model.AccountState
import com.android.monu.domain.model.BillState
import com.android.monu.domain.model.SavingState
import com.android.monu.domain.model.TransactionState
import com.android.monu.domain.usecase.finance.BackupData

interface FinanceRepository {

    suspend fun createAccount(account: AccountState, transactionState: TransactionState): Long

    suspend fun updateAccountStatus(accountId: Int, isActive: Boolean)

    suspend fun updateAccount(account: AccountState)

    suspend fun createIncomeTransaction(transactionState: TransactionState): Long

    suspend fun createExpenseTransaction(transactionState: TransactionState): Long

    suspend fun createTransferTransaction(transactionState: TransactionState): Long

    suspend fun createDepositTransaction(savingId: Long, transactionState: TransactionState)

    suspend fun createWithdrawTransaction(savingId: Long, transactionState: TransactionState)

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

    suspend fun updateIncomeTransaction(transactionState: TransactionState, initialAmount: Long): Int

    suspend fun updateExpenseTransaction(
        transactionState: TransactionState,
        initialParentCategory: Int,
        initialDate: String,
        initialAmount: Long,
        budgetStatus: BudgetStatusState
    ): Int

    suspend fun processBillPayment(
        billId: Long,
        billPaidDate: String,
        transactionState: TransactionState,
        isRecurring: Boolean,
        billState: BillState
    )

    suspend fun cancelBillPayment(billId: Long)

    suspend fun updateSaving(savingState: SavingState)

    suspend fun completeSaving(transactionState: TransactionState, savingId: Long)

    suspend fun restoreAllData(backupData: BackupData)

    suspend fun deleteAllData()
}