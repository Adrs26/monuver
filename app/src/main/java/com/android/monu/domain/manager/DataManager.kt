package com.android.monu.domain.manager

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.usecase.finance.BackupData

interface DataManager {
    fun backupData(backupData: BackupData)

    fun restoreData(stringUri: String): BackupData

    fun exportDataToPdf(
        reportName: String,
        username: String,
        startDate: String,
        endDate: String,
        commonTransactions: List<Transaction>,
        transferTransactions: List<Transaction>,
        totalIncome: Long,
        totalExpense: Long
    )
}