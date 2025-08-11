package com.android.monu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.monu.data.local.entity.room.BudgetingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetingDao {

    @Query("SELECT * FROM budgeting WHERE isActive = 1")
    fun getAllActiveBudgets(): Flow<List<BudgetingEntity>>

    @Query("""
        SELECT IFNULL(SUM(maxAmount), 0) 
        FROM budgeting
        WHERE isActive = 1
    """)
    fun getTotalBudgetingMaxAmount(): Flow<Long>

    @Query("""
        SELECT IFNULL(SUM(usedAmount), 0) 
        FROM budgeting
        WHERE isActive = 1
    """)
    fun getTotalBudgetingUsedAmount(): Flow<Long>

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM budgeting 
            WHERE category = :category AND isActive = 1
        )
    """)
    suspend fun isBudgetingExists(category: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewBudgeting(budgeting: BudgetingEntity): Long

    @Query("""
        SELECT * FROM budgeting 
        WHERE category = :category AND isActive = 1
        AND :date BETWEEN startDate AND endDate
        LIMIT 1
    """)
    suspend fun getBudgetingForDate(category: Int, date: String): BudgetingEntity?

    @Query("""
        UPDATE budgeting 
        SET usedAmount = usedAmount + :delta 
        WHERE category = :category AND isActive = 1
        AND :date BETWEEN startDate AND endDate
    """)
    suspend fun increaseBudgetingUsedAmount(category: Int, date: String, delta: Long)

    @Query("""
        UPDATE budgeting 
        SET usedAmount = usedAmount - :delta 
        WHERE category = :category AND isActive = 1
        AND :date BETWEEN startDate AND endDate
    """)
    suspend fun decreaseBudgetingUsedAmount(category: Int, date: String, delta: Long)
}