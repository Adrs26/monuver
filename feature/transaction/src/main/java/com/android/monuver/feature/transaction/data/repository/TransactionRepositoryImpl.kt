package com.android.monuver.feature.transaction.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.room.withTransaction
import com.android.monuver.core.data.database.MonuverDatabase
import com.android.monuver.core.data.database.dao.AccountDao
import com.android.monuver.core.data.database.dao.BudgetDao
import com.android.monuver.core.data.database.dao.TransactionDao
import com.android.monuver.core.data.mapper.toDomain
import com.android.monuver.core.data.mapper.toEntity
import com.android.monuver.core.data.mapper.toEntityForUpdate
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.transaction.domain.common.BudgetStatusState
import com.android.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.absoluteValue

internal class TransactionRepositoryImpl(
    private val database: MonuverDatabase,
    private val accountDao: AccountDao,
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override suspend fun createIncomeTransaction(transactionState: TransactionState) {
        database.withTransaction {
            transactionDao.createNewTransaction(transactionState.toEntity())
            accountDao.increaseAccountBalance(
                accountId = transactionState.sourceId,
                delta = transactionState.amount
            )
        }
    }

    override suspend fun createExpenseTransaction(transactionState: TransactionState) {
        database.withTransaction {
            transactionDao.createNewTransaction(transactionState.toEntity())
            accountDao.decreaseAccountBalance(
                accountId = transactionState.sourceId,
                delta = transactionState.amount
            )
            budgetDao.increaseBudgetUsedAmount(
                category = transactionState.parentCategory,
                date = transactionState.date,
                delta = transactionState.amount
            )
        }
    }

    override suspend fun createTransferTransaction(transactionState: TransactionState) {
        database.withTransaction {
            transactionDao.createNewTransaction(transactionState.toEntity())
            accountDao.decreaseAccountBalance(
                accountId = transactionState.sourceId,
                delta = transactionState.amount
            )
            accountDao.increaseAccountBalance(
                accountId = transactionState.destinationId ?: 0,
                delta = transactionState.amount
            )
        }
    }

    override suspend fun getBudgetUsagePercentage(category: Int): Float {
        return budgetDao.getBudgetUsagePercentage(category)
    }

    override suspend fun deleteIncomeTransaction(transactionId: Long, sourceId: Int, amount: Long) {
        database.withTransaction {
            transactionDao.deleteTransactionById(transactionId)
            accountDao.decreaseAccountBalance(
                accountId = sourceId,
                delta = amount
            )
        }
    }

    override suspend fun deleteExpenseTransaction(
        transactionId: Long,
        parentCategory: Int,
        date: String,
        sourceId: Int,
        amount: Long
    ) {
        database.withTransaction {
            transactionDao.deleteTransactionById(transactionId)
            accountDao.increaseAccountBalance(
                accountId = sourceId,
                delta = amount
            )
            budgetDao.decreaseBudgetUsedAmount(
                category = parentCategory,
                date = date,
                delta = amount
            )
        }
    }

    override suspend fun updateIncomeTransaction(
        transactionState: TransactionState,
        initialAmount: Long
    ) {
        database.withTransaction {
            transactionDao.updateTransaction(transactionState.toEntityForUpdate())
            val difference = transactionState.amount - initialAmount
            if (difference != 0L) {
                if (difference > 0) {
                    accountDao.increaseAccountBalance(
                        accountId = transactionState.sourceId,
                        delta = difference
                    )
                } else {
                    accountDao.decreaseAccountBalance(
                        accountId = transactionState.sourceId,
                        delta = difference.absoluteValue
                    )
                }
            }
        }
    }

    override suspend fun updateExpenseTransaction(
        transactionState: TransactionState,
        initialParentCategory: Int,
        initialDate: String,
        initialAmount: Long,
        budgetStatus: BudgetStatusState
    ) {
        database.withTransaction {
            transactionDao.updateTransaction(transactionState.toEntityForUpdate())
            val difference = transactionState.amount - initialAmount
            if (difference != 0L) {
                if (difference > 0) {
                    accountDao.decreaseAccountBalance(
                        accountId = transactionState.sourceId,
                        delta = difference
                    )
                } else {
                    accountDao.increaseAccountBalance(
                        accountId = transactionState.sourceId,
                        delta = difference.absoluteValue
                    )
                }
            }

            when(budgetStatus) {
                BudgetStatusState.NoOldBudget -> budgetDao.increaseBudgetUsedAmount(
                    category = transactionState.parentCategory,
                    date = transactionState.date,
                    delta = transactionState.amount
                )
                BudgetStatusState.NoNewBudget -> budgetDao.decreaseBudgetUsedAmount(
                    category = initialParentCategory,
                    date = initialDate,
                    delta = initialAmount
                )
                BudgetStatusState.NoBudget -> {}
                BudgetStatusState.SameBudget -> {
                    if (difference != 0L) {
                        if (difference > 0) {
                            budgetDao.increaseBudgetUsedAmount(
                                category = transactionState.parentCategory,
                                date = transactionState.date,
                                delta = difference
                            )
                        } else {
                            budgetDao.decreaseBudgetUsedAmount(
                                category = transactionState.parentCategory,
                                date = transactionState.date,
                                delta = difference.absoluteValue
                            )
                        }
                    }
                }
                BudgetStatusState.DifferentBudget -> {
                    budgetDao.decreaseBudgetUsedAmount(
                        category = initialParentCategory,
                        date = initialDate,
                        delta = initialAmount
                    )
                    budgetDao.increaseBudgetUsedAmount(
                        category = transactionState.parentCategory,
                        date = transactionState.date,
                        delta = transactionState.amount
                    )
                }
            }
        }
    }

    override fun getAllTransactions(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ): Flow<PagingData<TransactionState>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                transactionDao.getAllTransactions(
                    query = query,
                    type = type,
                    month = month,
                    year = year
                )
            }
        ).flow
            .map { pagingData ->
                pagingData.map { it.toDomain() }
            }.cachedIn(scope)
    }

    override fun getTransactionById(transactionId: Long): Flow<TransactionState?> {
        return transactionDao.getTransactionById(transactionId).map { transaction ->
            transaction?.toDomain()
        }
    }
}