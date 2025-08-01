package com.android.monu.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.monu.data.local.entity.projection.TransactionSummaryEntity
import com.android.monu.data.local.entity.room.TransactionEntity
import com.android.monu.data.local.entity.projection.TransactionCategorySummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("""
        SELECT *
        FROM `transaction`
        ORDER BY date DESC, timeStamp DESC
        LIMIT 3
    """)
    fun getRecentTransactions(): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * 
        FROM `transaction`
        WHERE (:type IS NULL OR type = :type)
            AND (:month IS NULL OR month = :month)
            AND (:year IS NULL OR year = :year)
            AND (title LIKE '%' || :query || '%')
        ORDER BY date DESC, timeStamp DESC
    """)
    fun getAllTransactions(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?
    ): PagingSource<Int, TransactionEntity>

    @Query("SELECT * FROM `transaction` WHERE id = :id")
    fun getTransactionById(id: Long): Flow<TransactionEntity?>

    @Query("SELECT DISTINCT year FROM `transaction` ORDER BY year DESC")
    fun getDistinctTransactionYears(): Flow<List<Int>>

    @Query("""
        SELECT SUM(amount) 
        FROM `transaction`
        WHERE type = :type 
            AND month = :month 
            AND year = :year
    """)
    fun getTotalMonthlyTransactionAmount(type: Int, month: Int, year: Int): Flow<Long?>

    @Query("""
        WITH all_dates AS (
            SELECT DISTINCT DATE(date) as date
            FROM `transaction`
            WHERE strftime('%m', date) = printf('%02d', :month)
                AND strftime('%Y', date) = CAST(:year AS TEXT)
        ),
        daily_totals AS (
            SELECT 
                ad.date,
                COALESCE(SUM(t.amount), 0) as dailyTotal
            FROM all_dates ad
            LEFT JOIN `transaction` t ON DATE(t.date) = ad.date 
                AND t.type = :type AND t.month = :month AND t.year = :year
            GROUP BY ad.date
        )
        SELECT AVG(dailyTotal) FROM daily_totals
    """)
    fun getAverageDailyTransactionAmountInMonth(type: Int, month: Int, year: Int): Flow<Double?>

    @Query("""
        SELECT parentCategory, SUM(amount) AS totalAmount
        FROM `transaction`
        WHERE type = :type AND month = :month AND year = :year
        GROUP BY parentCategory
        ORDER BY totalAmount DESC
    """)
    fun getGroupedMonthlyTransactionAmountByParentCategory(
        type: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionCategorySummaryEntity>>

    @Query("""
        SELECT type, date, amount FROM `transaction`
        WHERE date BETWEEN :startDate AND :endDate
    """)
    fun getTransactionsInRange(startDate: String, endDate: String): Flow<List<TransactionSummaryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewTransaction(transaction: TransactionEntity): Long

    @Query("DELETE FROM `transaction` WHERE id = :id")
    suspend fun deleteTransactionById(id: Long): Int

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity): Int
}