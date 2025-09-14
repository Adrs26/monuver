package com.android.monu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.monu.data.local.entity.room.SaveEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SaveDao {

    @Query("SELECT * FROM save ORDER BY targetDate ASC")
    fun getAllSaves(): Flow<List<SaveEntity>>

    @Query("SELECT SUM(currentAmount) FROM save")
    fun getTotalSaveCurrentAmount(): Flow<Long?>

    @Query("SELECT * FROM save WHERE id = :saveId")
    fun getSaveById(saveId: Long): Flow<SaveEntity?>

    @Query("SELECT currentAmount FROM save WHERE id = :saveId")
    suspend fun getSaveBalance(saveId: Long): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewSave(save: SaveEntity)

    @Query("""
        UPDATE save
        SET currentAmount = currentAmount + :delta 
        WHERE id = :saveId
    """)
    suspend fun increaseSaveCurrentAmount(saveId: Long, delta: Long)

    @Query("""
        UPDATE save
        SET currentAmount = currentAmount - :delta 
        WHERE id = :saveId
    """)
    suspend fun decreaseSaveCurrentAmount(saveId: Long, delta: Long)

    @Update
    suspend fun updateSave(saveEntity: SaveEntity)

    @Query("DELETE FROM save WHERE id = :saveId")
    suspend fun deleteSaveById(saveId: Long)
}