package com.android.monuver.feature.settings.data.repository

import androidx.room.withTransaction
import com.android.monuver.core.data.database.MonuverDatabase
import com.android.monuver.core.data.database.dao.AccountDao
import com.android.monuver.core.data.database.dao.BillDao
import com.android.monuver.core.data.database.dao.BudgetDao
import com.android.monuver.core.data.database.dao.SavingDao
import com.android.monuver.core.data.database.dao.TransactionDao
import com.android.monuver.core.data.database.entity.room.AccountEntity
import com.android.monuver.core.data.database.entity.room.BillEntity
import com.android.monuver.core.data.database.entity.room.BudgetEntity
import com.android.monuver.core.data.database.entity.room.SavingEntity
import com.android.monuver.core.data.database.entity.room.TransactionEntity
import com.android.monuver.core.data.mapper.toDomain
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.domain.model.BillState
import com.android.monuver.core.domain.model.BudgetState
import com.android.monuver.core.domain.model.SavingState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.settings.domain.model.AccountBackup
import com.android.monuver.feature.settings.domain.model.BillBackup
import com.android.monuver.feature.settings.domain.model.BudgetBackup
import com.android.monuver.feature.settings.domain.model.DataBackup
import com.android.monuver.feature.settings.domain.model.SavingBackup
import com.android.monuver.feature.settings.domain.model.TransactionBackup
import com.android.monuver.feature.settings.domain.repository.SettingsRepository

internal class SettingsRepositoryImpl(
    private val database: MonuverDatabase,
    private val accountDao: AccountDao,
    private val billDao: BillDao,
    private val budgetDao: BudgetDao,
    private val savingDao: SavingDao,
    private val transactionDao: TransactionDao
) : SettingsRepository {

    override suspend fun getAllAccounts(): List<AccountState> {
        return accountDao.getAllAccountsSuspend().map { it.toDomain() }
    }

    override suspend fun getAllBills(): List<BillState> {
        return billDao.getAllBills().map { it.toDomain() }
    }

    override suspend fun getAllBudgets(): List<BudgetState> {
        return budgetDao.getAllBudgets().map { it.toDomain() }
    }

    override suspend fun getAllSavings(): List<SavingState> {
        return savingDao.getAllSavings().map { it.toDomain() }
    }

    override suspend fun getAllTransactions(): List<TransactionState> {
        return transactionDao.getAllTransactions().map { it.toDomain() }
    }

    override suspend fun deleteAllApplicationData() {
        database.withTransaction {
            accountDao.deleteAllAccounts()
            transactionDao.deleteAllTransactions()
            budgetDao.deleteAllBudgets()
            billDao.deleteAllBills()
            savingDao.deleteAllSavings()
        }
    }

    override suspend fun getTransactionsInRangeByDateAsc(
        startDate: String,
        endDate: String
    ): List<TransactionState> {
        return transactionDao.getTransactionsInRangeByDateAsc(startDate, endDate).map { it.toDomain() }
    }

    override suspend fun getTransactionsInRangeByDateDesc(
        startDate: String,
        endDate: String
    ): List<TransactionState> {
        return transactionDao.getTransactionsInRangeByDateDesc(startDate, endDate).map { it.toDomain() }
    }

    override suspend fun getTransactionsInRangeByDateAscWithType(
        startDate: String,
        endDate: String
    ): List<TransactionState> {
        return transactionDao.getTransactionsInRangeByDateAscWithType(startDate, endDate).map { it.toDomain() }
    }

    override suspend fun getTransactionsInRangeByDateDescWithType(
        startDate: String,
        endDate: String
    ): List<TransactionState> {
        return transactionDao.getTransactionsInRangeByDateDescWithType(startDate, endDate).map { it.toDomain() }
    }

    override suspend fun getTotalIncomeTransactionInRange(
        startDate: String,
        endDate: String
    ): Long? {
        return transactionDao.getTotalIncomeTransactionsInRange(startDate, endDate)
    }

    override suspend fun getTotalExpenseTransactionInRange(
        startDate: String,
        endDate: String
    ): Long? {
        return transactionDao.getTotalExpenseTransactionsInRange(startDate, endDate)
    }

    override suspend fun restoreAllData(dataBackup: DataBackup) {
        database.withTransaction {
            accountDao.deleteAllAccounts()
            transactionDao.deleteAllTransactions()
            budgetDao.deleteAllBudgets()
            billDao.deleteAllBills()
            savingDao.deleteAllSavings()
            accountDao.insertAllAccounts(dataBackup.accounts.map { it.toEntity() })
            billDao.insertAllBills(dataBackup.bills.map { it.toEntity() })
            budgetDao.insertAllBudgets(dataBackup.budgets.map { it.toEntity() })
            savingDao.insertAllSavings(dataBackup.savings.map { it.toEntity() })
            transactionDao.insertAllTransactions(dataBackup.transactions.map { it.toEntity() })
        }
    }

    private fun AccountBackup.toEntity() = AccountEntity(
        id = id,
        name = name,
        type = type,
        balance = balance,
        isActive = isActive
    )

    private fun BillBackup.toEntity() = BillEntity(
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

    private fun BudgetBackup.toEntity() = BudgetEntity(
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

    private fun SavingBackup.toEntity() = SavingEntity(
        id = id,
        title = title,
        targetDate = targetDate,
        targetAmount = targetAmount,
        currentAmount = currentAmount,
        isActive = isActive
    )

    private fun TransactionBackup.toEntity() = TransactionEntity(
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