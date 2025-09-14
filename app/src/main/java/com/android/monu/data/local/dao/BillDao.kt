package com.android.monu.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.monu.data.local.entity.room.BillEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {

    @Query("SELECT * FROM bill WHERE dueDate > date('now') AND isPaid = 0 ORDER BY dueDate ASC, timeStamp DESC")
    fun getPendingBills(): Flow<List<BillEntity>>

    @Query("SELECT * FROM bill WHERE dueDate <= date('now') AND isPaid = 0 ORDER BY dueDate DESC, timeStamp DESC")
    fun getDueBills(): Flow<List<BillEntity>>

    @Query("SELECT * FROM bill WHERE isPaid = 1 ORDER BY paidDate DESC, timeStamp DESC")
    fun getPaidBills(): PagingSource<Int, BillEntity>

    @Query("SELECT * FROM bill WHERE id = :id")
    fun getBillById(id: Long): Flow<BillEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewBill(bill: BillEntity)

    @Query("UPDATE bill SET isPaid = 1, paidDate = :paidDate WHERE id = :id")
    suspend fun payBillById(id: Long, paidDate: String)

    @Query("DELETE FROM bill WHERE id = :id")
    suspend fun deleteBillById(id: Long)

    @Update
    suspend fun updateBill(bill: BillEntity)
}