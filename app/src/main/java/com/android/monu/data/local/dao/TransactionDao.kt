package com.android.monu.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.monu.data.local.entity.TransactionEntity
import com.android.monu.data.local.projection.TransactionConciseProj

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long
}