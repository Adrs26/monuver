package com.android.monu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.monu.data.local.entity.room.BillEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {

    @Query("SELECT * FROM bill WHERE dueDate > date('now') AND isPaid = 0 ORDER BY dueDate ASC")
    fun getPendingBills(): Flow<List<BillEntity>>

    @Query("SELECT * FROM bill WHERE dueDate <= date('now') AND isPaid = 0 ORDER BY dueDate DESC")
    fun getDueBills(): Flow<List<BillEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewBill(bill: BillEntity)
}