package com.android.monu.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.monu.data.local.entity.TransactionEntity
import com.android.monu.data.local.projection.TransactionCategoryAmountProj
import com.android.monu.data.local.projection.TransactionConciseProj
import com.android.monu.data.local.projection.TransactionMonthlyAmountProj
import com.android.monu.data.local.projection.TransactionOverviewProj
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("""
        SELECT SUM(amount) AS totalAmount
        FROM `transaction`
        WHERE type = :type
    """)
    fun getTotalTransactionAmount(type: Int): Flow<Long?>

    @Query("""
        SELECT id, title, type, category, date, amount 
        FROM `transaction`
        ORDER BY date DESC, timeStamp DESC
        LIMIT 3
    """)
    fun getRecentTransactions(): Flow<List<TransactionConciseProj>>

    @Query("""
        SELECT id, title, type, category, date, amount 
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
    ): PagingSource<Int, TransactionConciseProj>

    @Query("SELECT * FROM `transaction` WHERE id = :id")
    fun getTransactionById(id: Long): Flow<TransactionEntity?>

    @Query("SELECT DISTINCT year FROM `transaction` ORDER BY year DESC")
    fun getAvailableTransactionYears(): Flow<List<Int>>

    @Query("""
        SELECT 
            month,
            SUM(CASE WHEN type = 1 THEN amount ELSE 0 END) AS totalAmountIncome,
            SUM(CASE WHEN type = 2 THEN amount ELSE 0 END) AS totalAmountExpense
        FROM `transaction`
        WHERE year = :year
        GROUP BY month, year
        ORDER BY year, month
    """)
    fun getTransactionMonthlyAmounts(year: Int): Flow<List<TransactionMonthlyAmountProj>>

    @Query("""
        WITH all_dates AS (
            SELECT DISTINCT DATE(date) as date
            FROM `transaction`
        ),
        daily_totals AS (
            SELECT 
                ad.date,
                COALESCE(SUM(t.amount), 0) as dailyTotal
            FROM all_dates ad
            LEFT JOIN `transaction` t ON DATE(t.date) = ad.date AND t.type = :type
            GROUP BY ad.date
        )
        SELECT AVG(dailyTotal) FROM daily_totals
    """)
    fun getAverageTransactionAmountPerDay(type: Int): Flow<Double?>

    @Query("""
        WITH all_months AS (
            SELECT DISTINCT year, month
            FROM `transaction`
        ),
        monthly_totals AS (
            SELECT 
                am.year,
                am.month,
                COALESCE(SUM(t.amount), 0) as monthlyTotal
            FROM all_months am
            LEFT JOIN `transaction` t ON t.year = am.year AND t.month = am.month AND t.type = :type
            GROUP BY am.year, am.month
        )
        SELECT AVG(monthlyTotal) FROM monthly_totals
    """)
    fun getAverageTransactionAmountPerMonth(type: Int): Flow<Double?>

    @Query("""
        WITH all_years AS (
            SELECT DISTINCT year
            FROM `transaction`
        ),
        yearly_totals AS (
            SELECT 
                ay.year,
                COALESCE(SUM(t.amount), 0) as yearlyTotal
            FROM all_years ay
            LEFT JOIN `transaction` t ON t.year = ay.year AND t.type = :type
            GROUP BY ay.year
        )
        SELECT AVG(yearlyTotal) FROM yearly_totals
    """)
    fun getAverageTransactionAmountPerYear(type: Int): Flow<Double?>

    @Query("""
        SELECT 
            month,
            year,
            SUM(amount) AS amount
        FROM `transaction`
        WHERE type = :type AND year = :year
        GROUP BY year, month
        ORDER BY year, month
    """)
    fun getMonthlyTransactionOverviewsByType(type: Int, year: Int): Flow<List<TransactionOverviewProj>>

    @Query("""
        SELECT category, SUM(amount) AS amount 
        FROM `transaction`
        WHERE type = 2 AND year = :year 
        GROUP BY category 
        ORDER BY amount DESC 
        LIMIT 5
    """)
    fun getMostExpenseTransactionCategoryAmountsByYear(year: Int): Flow<List<TransactionCategoryAmountProj>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity): Int

    @Query("DELETE FROM `transaction` WHERE id = :id")
    suspend fun deleteTransactionById(id: Long): Int
}