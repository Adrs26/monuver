package com.android.monuver.feature.settings.domain.manager

import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.settings.domain.model.DataBackup

internal interface DataManager {
    suspend fun backupData(data: DataBackup)

    suspend fun restoreData(stringUri: String): DataBackup

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