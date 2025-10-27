package com.android.monu.domain.manager

import com.android.monu.domain.model.TransactionState
import com.android.monu.domain.usecase.finance.BackupData

interface DataManager {
    fun backupData(data: BackupData)

    fun restoreData(stringUri: String): BackupData

    fun exportDataToPdf(
        reportName: String,
        username: String,
        startDate: String,
        endDate: String,
        commonTransactions: List<TransactionState>,
        transferTransactions: List<TransactionState>,
        totalIncome: Long,
        totalExpense: Long
    )
}