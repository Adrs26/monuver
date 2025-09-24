package com.android.monu.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.monu.data.local.entity.room.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budget WHERE isActive = 1 ORDER BY endDate ASC")
    fun getAllActiveBudgets(): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budget WHERE isActive = 0 ORDER BY endDate DESC")
    fun getAllInactiveBudgets(): PagingSource<Int, BudgetEntity>

    @Query("SELECT * FROM budget WHERE id = :budgetId LIMIT 1")
    fun getBudgetById(budgetId: Long): Flow<BudgetEntity?>

    @Query("""
        SELECT IFNULL(SUM(maxAmount), 0) 
        FROM budget
        WHERE isActive = 1
    """)
    fun getTotalBudgetMaxAmount(): Flow<Long>

    @Query("""
        SELECT IFNULL(SUM(usedAmount), 0) 
        FROM budget
        WHERE isActive = 1
    """)
    fun getTotalBudgetUsedAmount(): Flow<Long>

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM budget
            WHERE category = :category AND isActive = 1
        )
    """)
    suspend fun isBudgetExists(category: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewBudget(budget: BudgetEntity): Long

    @Query("""
        SELECT * FROM budget 
        WHERE category = :category AND isActive = 1
        AND :date BETWEEN startDate AND endDate
        LIMIT 1
    """)
    suspend fun getBudgetForDate(category: Int, date: String): BudgetEntity?

    @Query("SELECT (CAST(usedAmount AS FLOAT) / CAST(maxAmount AS FLOAT)) * 100 FROM budget WHERE category = :category AND isActive = 1")
    suspend fun getBudgetUsagePercentage(category: Int): Float

    @Query("""
        UPDATE budget
        SET usedAmount = usedAmount + :delta 
        WHERE category = :category
        AND :date BETWEEN startDate AND endDate
    """)
    suspend fun increaseBudgetUsedAmount(category: Int, date: String, delta: Long)

    @Query("""
        UPDATE budget 
        SET usedAmount = usedAmount - :delta 
        WHERE category = :category
        AND :date BETWEEN startDate AND endDate
    """)
    suspend fun decreaseBudgetUsedAmount(category: Int, date: String, delta: Long)

    @Query("UPDATE budget SET isActive = 0 WHERE category = :category")
    suspend fun updateBudgetStatusToInactive(category: Int)

    @Query("DELETE FROM budget WHERE id = :budgetId")
    suspend fun deleteBudgetById(budgetId: Long)

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Query("DELETE FROM budget")
    suspend fun deleteAllBudgets()

    @Query("SELECT * FROM budget")
    suspend fun getAllBudgetsSuspend(): List<BudgetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBudgets(budgets: List<BudgetEntity>)
}