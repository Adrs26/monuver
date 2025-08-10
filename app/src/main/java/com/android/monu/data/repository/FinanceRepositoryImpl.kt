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
import com.android.monu.domain.usecase.finance.UpdateExpenseTransactionUseCase.Companion.DIFFERENT_BUDGETING
import com.android.monu.domain.usecase.finance.UpdateExpenseTransactionUseCase.Companion.NO_BUDGETING
import com.android.monu.domain.usecase.finance.UpdateExpenseTransactionUseCase.Companion.NO_NEW_BUDGETING
import com.android.monu.domain.usecase.finance.UpdateExpenseTransactionUseCase.Companion.NO_OLD_BUDGETING
import com.android.monu.domain.usecase.finance.UpdateExpenseTransactionUseCase.Companion.SAME_BUDGETING
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
        startAmount: Long
    ): Int {
        return database.withTransaction {
            val rowUpdated = transactionDao.updateTransaction(
                TransactionMapper.transactionDomainToEntityForUpdate(transaction)
            )
            val difference = transaction.amount - startAmount
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
        budgetingStatus: String
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
                NO_OLD_BUDGETING -> budgetingDao.increaseBudgetingUsedAmount(
                    transaction.parentCategory,
                    transaction.date,
                    transaction.amount
                )
                NO_NEW_BUDGETING -> budgetingDao.decreaseBudgetingUsedAmount(
                    initialParentCategory,
                    initialDate,
                    initialAmount
                )
                NO_BUDGETING -> {}
                SAME_BUDGETING -> {
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
                DIFFERENT_BUDGETING -> {
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
        startAmount: Long
    ): Int {
        return database.withTransaction {
            val rowUpdated = transactionDao.updateTransaction(
                TransactionMapper.transactionDomainToEntityForUpdate(transaction)
            )
            val difference = transaction.amount - startAmount
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