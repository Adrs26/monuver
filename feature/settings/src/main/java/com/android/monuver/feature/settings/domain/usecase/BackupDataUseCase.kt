package com.android.monuver.feature.settings.domain.usecase

import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.domain.model.BillState
import com.android.monuver.core.domain.model.BudgetState
import com.android.monuver.core.domain.model.SavingState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.settings.domain.manager.DataManager
import com.android.monuver.feature.settings.domain.model.AccountBackup
import com.android.monuver.feature.settings.domain.model.BillBackup
import com.android.monuver.feature.settings.domain.model.BudgetBackup
import com.android.monuver.feature.settings.domain.model.DataBackup
import com.android.monuver.feature.settings.domain.model.SavingBackup
import com.android.monuver.feature.settings.domain.model.TransactionBackup
import com.android.monuver.feature.settings.domain.repository.SettingsRepository

internal class BackupDataUseCase(
    private val dataManager: DataManager,
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(): DatabaseResultState {
        return try {
            val accounts = repository.getAllAccounts().map { it.toBackup() }
            val bills = repository.getAllBills().map { it.toBackup() }
            val budgets = repository.getAllBudgets().map { it.toBackup() }
            val savings = repository.getAllSavings().map { it.toBackup() }
            val transactions = repository.getAllTransactions().map { it.toBackup() }

            val backupData = DataBackup(
                accounts = accounts,
                bills = bills,
                budgets = budgets,
                savings = savings,
                transactions = transactions,
            )

            dataManager.backupData(backupData)
            DatabaseResultState.BackupDataSuccess
        } catch (_: Exception) {
            DatabaseResultState.BackupDataFailed
        }
    }

    private fun AccountState.toBackup() = AccountBackup(
        id = id,
        name = name,
        type = type,
        balance = balance,
        isActive = isActive
    )

    private fun BillState.toBackup() = BillBackup(
        id = id,
        parentId = parentId,
        title = title,
        dueDate = dueDate,
        paidDate = paidDate,
        timeStamp = timeStamp,
        amount = amount,
        isRecurring = isRecurring,
        cycle = cycle,
        period = period,
        fixPeriod = fixPeriod,
        isPaid = isPaid,
        nowPaidPeriod = nowPaidPeriod,
        isPaidBefore = isPaidBefore
    )

    private fun BudgetState.toBackup() = BudgetBackup(
        id = id,
        category = category,
        cycle = cycle,
        startDate = startDate,
        endDate = endDate,
        maxAmount = maxAmount,
        usedAmount = usedAmount,
        isActive = isActive,
        isOverflowAllowed = isOverflowAllowed,
        isAutoUpdate = isAutoUpdate
    )

    private fun SavingState.toBackup() = SavingBackup(
        id = id,
        title = title,
        targetDate = targetDate,
        targetAmount = targetAmount,
        currentAmount = currentAmount,
        isActive = isActive
    )

    private fun TransactionState.toBackup() = TransactionBackup(
        id = id,
        title = title,
        type = type,
        parentCategory = parentCategory,
        childCategory = childCategory,
        date = date,
        month = month,
        year = year,
        timeStamp = timeStamp,
        amount = amount,
        sourceId = sourceId,
        sourceName = sourceName,
        destinationId = destinationId,
        destinationName = destinationName,
        saveId = saveId,
        billId = billId,
        isLocked = isLocked,
        isSpecialCase = isSpecialCase
    )
}