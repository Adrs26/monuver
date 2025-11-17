package com.android.monuver.feature.settings.domain.repository

import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.domain.model.BillState
import com.android.monuver.core.domain.model.BudgetState
import com.android.monuver.core.domain.model.SavingState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.settings.domain.model.DataBackup

internal interface SettingsRepository {

    suspend fun getAllAccounts(): List<AccountState>

    suspend fun getAllBills(): List<BillState>

    suspend fun getAllBudgets(): List<BudgetState>

    suspend fun getAllSavings(): List<SavingState>

    suspend fun getAllTransactions(): List<TransactionState>

    suspend fun deleteAllApplicationData()

    suspend fun getTransactionsInRangeByDateAsc(
        startDate: String,
        endDate: String
    ): List<TransactionState>

    suspend fun getTransactionsInRangeByDateDesc(
        startDate: String,
        endDate: String
    ): List<TransactionState>

    suspend fun getTransactionsInRangeByDateAscWithType(
        startDate: String,
        endDate: String
    ): List<TransactionState>

    suspend fun getTransactionsInRangeByDateDescWithType(
        startDate: String,
        endDate: String
    ): List<TransactionState>

    suspend fun getTotalIncomeTransactionInRange(
        startDate: String,
        endDate: String
    ): Long?

    suspend fun getTotalExpenseTransactionInRange(
        startDate: String,
        endDate: String
    ): Long?

    suspend fun restoreAllData(dataBackup: DataBackup)
}