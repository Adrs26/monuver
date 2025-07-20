package com.android.monu.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.monu.data.local.dao.TransactionDao
import com.android.monu.data.mapper.TransactionMapper
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.model.transaction.TransactionDailySummary
import com.android.monu.domain.model.transaction.TransactionMonthlyAmountOverview
import com.android.monu.domain.model.transaction.TransactionParentCategorySummary
import com.android.monu.domain.repository.TransactionRepository
import com.android.monu.presentation.utils.DateHelper
import com.android.monu.presentation.utils.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDate

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override fun getRecentTransactions(): Flow<List<Transaction>> {
        return transactionDao.getRecentTransactions().map { entityList ->
            entityList.map { entity ->
                TransactionMapper.transactionEntityToDomain(entity)
            }
        }
    }

    override fun getAllTransactions(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ): Flow<PagingData<Transaction>> {
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
                pagingData.map { entity ->
                    TransactionMapper.transactionEntityToDomain(entity)
                }
            }
            .cachedIn(scope)
    }

    override fun getTransactionById(transactionId: Long): Flow<Transaction?> {
        return transactionDao.getTransactionById(transactionId).map { entity ->
            TransactionMapper.transactionEntityToDomain(entity ?: return@map null)
        }
    }

    override fun getDistinctTransactionYears(): Flow<List<Int>> {
        return transactionDao.getDistinctTransactionYears()
    }

    override fun getTransactionMonthlyAmountOverview(
        month: Int,
        year: Int
    ): Flow<TransactionMonthlyAmountOverview> {
        val totalIncomeAmount = transactionDao.getTotalMonthlyTransactionAmount(
            TransactionType.INCOME, month, year
        )
        val totalExpenseAmount = transactionDao.getTotalMonthlyTransactionAmount(
            TransactionType.EXPENSE, month, year
        )
        val averageIncomeAmount = transactionDao.getAverageDailyTransactionAmountInMonth(
            TransactionType.INCOME, month, year
        )
        val averageExpenseAmount = transactionDao.getAverageDailyTransactionAmountInMonth(
            TransactionType.EXPENSE, month, year
        )

        return combine(
            totalIncomeAmount,
            totalExpenseAmount,
            averageIncomeAmount,
            averageExpenseAmount
        ) { totalIncome, totalExpense, averageIncome, averageExpense ->
            TransactionMonthlyAmountOverview(
                totalIncomeAmount = totalIncome ?: 0L,
                totalExpenseAmount = totalExpense ?: 0L,
                averageIncomeAmount = averageIncome ?: 0.0,
                averageExpenseAmount = averageExpense ?: 0.0
            )
        }
    }

    override fun getGroupedMonthlyTransactionAmountByParentCategory(
        type: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionParentCategorySummary>> {
        return transactionDao
            .getGroupedMonthlyTransactionAmountByParentCategory(type, month, year)
            .map { list ->
                list.map { (parentCategory, totalAmount) ->
                    TransactionParentCategorySummary(
                        parentCategory = parentCategory,
                        totalAmount = totalAmount
                    )
                }
            }
    }

    override fun getWeeklyTransactionSummaryByDateRange(
        month: Int,
        year: Int,
        week: Int
    ): Flow<List<TransactionDailySummary>> {
        val (startDate, endDate) = DateHelper.getDateRangeForWeek(week, month, year)
        val transactionsFlow = transactionDao.getTransactionsInRange(startDate.toString(), endDate.toString())

        return transactionsFlow.map { transactions ->
            val grouped = transactions.groupBy { LocalDate.parse(it.date) }

            val result = mutableListOf<TransactionDailySummary>()

            var currentDate = startDate
            while (!currentDate.isAfter(endDate)) {
                val currentDateTransactions = grouped[currentDate] ?: emptyList()

                val (income, expense) = currentDateTransactions.fold(0L to 0L) { acc, transaction ->
                    when (transaction.type) {
                        TransactionType.INCOME -> acc.copy(first = acc.first + transaction.amount)
                        TransactionType.EXPENSE -> acc.copy(second = acc.second + transaction.amount)
                        else -> acc
                    }
                }

                result.add(
                    TransactionDailySummary(
                        date = currentDate.toString(),
                        totalIncome = income,
                        totalExpense = expense
                    )
                )

                currentDate = currentDate.plusDays(1)
            }

            result
        }
    }

    override suspend fun createNewTransaction(transaction: Transaction): Result<Long> {
        return try {
            val result = transactionDao.createNewTransaction(
                TransactionMapper.transactionDomainToEntity(transaction)
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTransaction(transaction: Transaction): Result<Int> {
        return try {
            val result = transactionDao.updateTransaction(
                TransactionMapper.transactionDomainToEntityForUpdate(transaction)
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTransactionById(transactionId: Long): Result<Int> {
        return try {
            val result = transactionDao.deleteTransactionById(transactionId)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}