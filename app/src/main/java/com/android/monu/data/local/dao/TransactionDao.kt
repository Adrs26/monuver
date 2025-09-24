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

    @Query("SELECT * FROM `transaction` WHERE id = :transactionId LIMIT 1")
    fun getTransactionById(transactionId: Long): Flow<TransactionEntity?>

    @Query("""
        SELECT * 
        FROM `transaction` 
        WHERE saveId = :savingId
        ORDER BY date DESC, timeStamp DESC
    """)
    fun getTransactionsBySavingId(savingId: Long): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * 
        FROM `transaction`
        WHERE parentCategory = :category AND date BETWEEN :startDate AND :endDate
        ORDER BY date DESC, timeStamp DESC
    """)
    fun getTransactionsByParentCategoryAndDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * 
        FROM `transaction`
        WHERE parentCategory = :category AND month = :month AND year = :year
        ORDER BY date DESC, timeStamp DESC
    """)
    fun getTransactionsByParentCategoryAndMonthAndYear(
        category: Int,
        month: Int,
        year: Int
    ): Flow<List<TransactionEntity>>

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

    @Query("DELETE FROM `transaction` WHERE id = :transactionId")
    suspend fun deleteTransactionById(transactionId: Long): Int

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity): Int

    @Query("""
        SELECT IFNULL(SUM(amount), 0) 
        FROM `transaction`
        WHERE parentCategory = :category AND date BETWEEN :startDate AND :endDate
    """)
    suspend fun getTotalTransactionAmountInDateRange(
        category: Int,
        startDate: String,
        endDate: String
    ): Long

    @Query("""
        UPDATE `transaction`
        SET sourceName = :accountName
        WHERE sourceId = :accountId AND (type = 1001 OR type = 1002)
    """)
    suspend fun updateAccountNameOnCommonTransaction(accountId: Int, accountName: String)

    @Query("""
        UPDATE `transaction`
        SET 
            sourceName = CASE WHEN sourceId = :accountId THEN :accountName ELSE sourceName END,
            destinationName = CASE WHEN destinationId = :accountId THEN :accountName ELSE destinationName END
        WHERE (sourceId = :accountId OR destinationId = :accountId) AND type = 1003 AND childCategory = 1003
    """)
    suspend fun updateAccountNameOnTransferTransaction(accountId: Int, accountName: String)

    @Query("""
        UPDATE `transaction`
        SET sourceName = :accountName
        WHERE sourceId = :accountId AND type = 1003 AND childCategory = 1004
    """)
    suspend fun updateAccountNameOnDepositTransaction(accountId: Int, accountName: String)

    @Query("""
        UPDATE `transaction`
        SET destinationName = :accountName
        WHERE destinationId = :accountId AND type = 1003 AND childCategory = 1005
    """)
    suspend fun updateAccountNameOnWithdrawTransaction(accountId: Int, accountName: String)

    @Query("""
        UPDATE `transaction`
        SET destinationName = :saveTitle
        WHERE saveId = :savingId AND type = 1003 AND childCategory = 1004
    """)
    suspend fun updateSavingTitleOnDepositTransaction(savingId: Long, saveTitle: String)

    @Query("""
        UPDATE `transaction`
        SET sourceName = :saveTitle
        WHERE saveId = :savingId AND type = 1003 AND childCategory = 1005
    """)
    suspend fun updateSavingTitleOnWithdrawTransaction(savingId: Long, saveTitle: String)

    @Query("""
        SELECT * 
        FROM `transaction` 
        WHERE saveId = :savingId
        ORDER BY date DESC, timeStamp DESC
    """)
    suspend fun getTransactionsBySavingIdSuspend(savingId: Long): List<TransactionEntity>

    @Query("""
        UPDATE `transaction`
        SET isLocked = :isLocked
        WHERE (type = 1001 OR type = 1002) AND isSpecialCase = 0 AND sourceId = :accountId
    """)
    suspend fun updateTransactionLockStatusByAccountId(accountId: Int, isLocked: Boolean)

    @Query("SELECT * FROM `transaction` WHERE billId = :billId LIMIT 1")
    suspend fun getTransactionIdByBillId(billId: Long): TransactionEntity?

    @Query("DELETE FROM `transaction`")
    suspend fun deleteAllTransactions()

    @Query("SELECT * FROM `transaction`")
    suspend fun getAllTransactionsSuspend(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTransactions(transactions: List<TransactionEntity>)

}