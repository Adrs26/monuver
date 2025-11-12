package com.android.monuver.domain.manager

import com.android.monuver.domain.model.TransactionState
import com.android.monuver.domain.usecase.finance.BackupData

interface DataManager {
    suspend fun backupData(data: BackupData)

    suspend fun restoreData(stringUri: String): BackupData

    suspend fun exportDataToPdf(
        reportName: String,
        username: String,
        startDate: String,
        endDate: String,
        commonTransactions: List<TransactionState>,
        transferTransactions: List<TransactionState>,
        totalIncome: Long,
        totalExpense: Long,
    )
}