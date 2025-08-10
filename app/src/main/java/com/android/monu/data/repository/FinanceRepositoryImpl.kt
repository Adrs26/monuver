package com.android.monu.data.repository

import androidx.room.withTransaction
import com.android.monu.data.local.MonuDatabase
import com.android.monu.data.local.dao.AccountDao
import com.android.monu.data.local.dao.BudgetingDao
import com.android.monu.data.local.dao.TransactionDao
import com.android.monu.data.mapper.AccountMapper
import com.android.monu.data.mapper.TransactionMapper
import com.android.monu.domain.model.account.Account
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.domain.usecase.finance.BudgetingStatus
import kotlin.math.absoluteValue

class FinanceRepositoryImpl(
    private val database: MonuDatabase,
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao,
    private val budgetingDao: BudgetingDao
) : FinanceRepository {

    override suspend fun createAccount(account: Account, transaction: Transaction): Long {
        return database.withTransaction {
            val accountId = accountDao.createNewAccount(
                AccountMapper.accountDomainToEntity(account)
            )
            val transactionWithAccountId = transaction.copy(sourceId = accountId.toInt())
            transactionDao.createNewTransaction(
                TransactionMapper.transactionDomainToEntity(transactionWithAccountId)
            )
            accountId
        }
    }

    override suspend fun createIncomeTransaction(transaction: Transaction): Long {
        return database.withTransaction {
            val transactionId = transactionDao.createNewTransaction(
                TransactionMapper.transactionDomainToEntity(transaction)
            )
            accountDao.increaseAccountBalance(transaction.sourceId, transaction.amount)
            transactionId
        }
    }

    override suspend fun createExpenseTransaction(transaction: Transaction): Long {
        return database.withTransaction {
            val transactionId = transactionDao.createNewTransaction(
                TransactionMapper.transactionDomainToEntity(transaction)
            )
            accountDao.decreaseAccountBalance(transaction.sourceId, transaction.amount)
            budgetingDao.increaseBudgetingUsedAmount(transaction.parentCategory, transaction.date, transaction.amount)
            transactionId
        }
    }

    override suspend fun createTransferTransaction(transaction: Transaction): Long {
        return database.withTransaction {
            val transactionId = transactionDao.createNewTransaction(
                TransactionMapper.transactionDomainToEntity(transaction)
            )
            accountDao.decreaseAccountBalance(transaction.sourceId, transaction.amount)
            accountDao.increaseAccountBalance(transaction.destinationId ?: 0, transaction.amount)
            transactionId
        }
    }

    override suspend fun deleteIncomeTransaction(id: Long, sourceId: Int, amount: Long): Int {
        return database.withTransaction {
            val rowDeleted = transactionDao.deleteTransactionById(id)
            accountDao.decreaseAccountBalance(sourceId, amount)
            rowDeleted
        }
    }

    override suspend fun deleteExpenseTransaction(
        id: Long,
        parentCategory: Int,
        date: String,
        sourceId: Int,
        amount: Long
    ): Int {
        return database.withTransaction {
            val rowDeleted = transactionDao.deleteTransactionById(id)
            accountDao.increaseAccountBalance(sourceId, amount)
            budgetingDao.decreaseBudgetingUsedAmount(parentCategory, date, amount)
            rowDeleted
        }
    }

    override suspend fun deleteTransferTransaction(
        id: Long,
        sourceId: Int,
        destinationId: Int,
        amount: Long
    ): Int {
        return database.withTransaction {
            val rowDeleted = transactionDao.deleteTransactionById(id)
            accountDao.increaseAccountBalance(sourceId, amount)
            accountDao.decreaseAccountBalance(destinationId, amount)
            rowDeleted
        }
    }

    override suspend fun updateIncomeTransaction(
        transaction: Transaction,
        initialAmount: Long
    ): Int {
        return database.withTransaction {
            val rowUpdated = transactionDao.updateTransaction(
                TransactionMapper.transactionDomainToEntityForUpdate(transaction)
            )
            val difference = transaction.amount - initialAmount
            if (difference != 0L) {
                if (difference > 0) {
                    accountDao.increaseAccountBalance(transaction.sourceId, difference)
                } else {
                    accountDao.decreaseAccountBalance(transaction.sourceId, difference.absoluteValue)
                }
            }
            rowUpdated
        }
    }

    override suspend fun updateExpenseTransaction(
        transaction: Transaction,
        initialParentCategory: Int,
        initialDate: String,
        initialAmount: Long,
        budgetingStatus: BudgetingStatus
    ): Int {
        return database.withTransaction {
            val rowUpdated = transactionDao.updateTransaction(
                TransactionMapper.transactionDomainToEntityForUpdate(transaction)
            )
            val difference = transaction.amount - initialAmount
            if (difference != 0L) {
                if (difference > 0) {
                    accountDao.decreaseAccountBalance(transaction.sourceId, difference)
                } else {
                    accountDao.increaseAccountBalance(transaction.sourceId, difference.absoluteValue)
                }
            }

            when(budgetingStatus) {
                BudgetingStatus.NoOldBudgeting -> budgetingDao.increaseBudgetingUsedAmount(
                    transaction.parentCategory,
                    transaction.date,
                    transaction.amount
                )
                BudgetingStatus.NoNewBudgeting -> budgetingDao.decreaseBudgetingUsedAmount(
                    initialParentCategory,
                    initialDate,
                    initialAmount
                )
                BudgetingStatus.NoBudgeting -> {}
                BudgetingStatus.SameBudgeting -> {
                    if (difference != 0L) {
                        if (difference > 0) {
                            budgetingDao.increaseBudgetingUsedAmount(
                                transaction.parentCategory,
                                transaction.date,
                                difference
                            )
                        } else {
                            budgetingDao.decreaseBudgetingUsedAmount(
                                transaction.parentCategory,
                                transaction.date,
                                difference.absoluteValue
                            )
                        }
                    }
                }
                BudgetingStatus.DifferentBudgeting -> {
                    budgetingDao.decreaseBudgetingUsedAmount(
                        initialParentCategory,
                        initialDate,
                        initialAmount
                    )
                    budgetingDao.increaseBudgetingUsedAmount(
                        transaction.parentCategory,
                        transaction.date,
                        transaction.amount
                    )
                }
            }

            rowUpdated
        }
    }

    override suspend fun updateTransferTransaction(
        transaction: Transaction,
        initialAmount: Long
    ): Int {
        return database.withTransaction {
            val rowUpdated = transactionDao.updateTransaction(
                TransactionMapper.transactionDomainToEntityForUpdate(transaction)
            )
            val difference = transaction.amount - initialAmount
            if (difference != 0L) {
                if (difference > 0) {
                    accountDao.decreaseAccountBalance(transaction.sourceId, difference)
                    accountDao.increaseAccountBalance(transaction.destinationId ?: 0, difference)
                } else {
                    accountDao.increaseAccountBalance(transaction.sourceId, difference.absoluteValue)
                    accountDao.decreaseAccountBalance(transaction.destinationId ?: 0, difference.absoluteValue)
                }
            }
            rowUpdated
        }
    }
}