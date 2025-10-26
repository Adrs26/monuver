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

    @Query("SELECT * FROM bill WHERE id = :billId LIMIT 1")
    fun getBillById(billId: Long): Flow<BillEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewBill(bill: BillEntity): Long

    @Query("UPDATE bill SET parentId = :parentId WHERE id = :id")
    suspend fun updateParentId(id: Long, parentId: Long)

    @Query("UPDATE bill SET isPaid = :isPaid, paidDate = :paidDate, isPaidBefore = 1 WHERE id = :billId")
    suspend fun updateBillPaidStatusById(billId: Long, paidDate: String?, isPaid: Boolean)

    @Query("DELETE FROM bill WHERE id = :billId")
    suspend fun deleteBillById(billId: Long)

    @Update
    suspend fun updateBill(bill: BillEntity)

    @Query("UPDATE bill set period = :period, fixPeriod = :fixPeriod WHERE parentId = :parentId")
    suspend fun updateBillPeriodByParentId(period: Int?, fixPeriod: Int?, parentId: Long)

    @Query("DELETE FROM bill")
    suspend fun deleteAllBills()

    @Query("SELECT * FROM bill")
    suspend fun getAllBills(): List<BillEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBills(bills: List<BillEntity>)
}