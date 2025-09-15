package com.android.monu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.monu.data.local.entity.room.SavingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingDao {

    @Query("SELECT * FROM saving WHERE isActive = 1 ORDER BY targetDate ASC")
    fun getAllActiveSavings(): Flow<List<SavingEntity>>

    @Query("SELECT * FROM saving WHERE isActive = 0 ORDER BY targetDate DESC")
    fun getAllInactiveSavings(): Flow<List<SavingEntity>>

    @Query("SELECT SUM(currentAmount) FROM saving WHERE isActive = 1")
    fun getTotalSavingCurrentAmount(): Flow<Long?>

    @Query("SELECT * FROM saving WHERE id = :savingId LIMIT 1")
    fun getSavingById(savingId: Long): Flow<SavingEntity?>

    @Query("SELECT currentAmount FROM saving WHERE id = :savingId")
    suspend fun getSavingBalance(savingId: Long): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewSaving(saving: SavingEntity)

    @Query("""
        UPDATE saving
        SET currentAmount = currentAmount + :delta 
        WHERE id = :savingId
    """)
    suspend fun increaseSavingCurrentAmount(savingId: Long, delta: Long)

    @Query("""
        UPDATE saving
        SET currentAmount = currentAmount - :delta 
        WHERE id = :saveId
    """)
    suspend fun decreaseSavingCurrentAmount(saveId: Long, delta: Long)

    @Update
    suspend fun updateSaving(savingEntity: SavingEntity)

    @Query("UPDATE saving SET isActive = 0 WHERE id = :savingId")
    suspend fun updateSavingStatusToInactiveById(savingId: Long)

    @Query("DELETE FROM saving WHERE id = :savingId")
    suspend fun deleteSavingById(savingId: Long)
}