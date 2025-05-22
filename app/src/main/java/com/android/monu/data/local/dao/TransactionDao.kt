package com.android.monu.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.monu.data.local.entity.TransactionEntity
import com.android.monu.data.local.projection.TransactionConciseProj
import com.android.monu.data.local.projection.TransactionMonthlyAmountProj
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("""
        SELECT id, title, type, category, date, amount 
        FROM `transaction`
        WHERE (:type IS NULL OR type = :type)
            AND (:month IS NULL OR month = :month)
            AND (:year IS NULL OR year = :year)
            AND (title LIKE '%' || :query || '%')
        ORDER BY date DESC
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
    fun getTransactionMonthlyAmount(year: Int): Flow<List<TransactionMonthlyAmountProj>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity): Int

    @Query("DELETE FROM `transaction` WHERE id = :id")
    suspend fun deleteTransactionById(id: Long): Int
}