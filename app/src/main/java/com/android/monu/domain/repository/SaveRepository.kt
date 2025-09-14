package com.android.monu.domain.repository

import com.android.monu.domain.model.save.Save
import kotlinx.coroutines.flow.Flow

interface SaveRepository {

    fun getAllSaves(): Flow<List<Save>>

    fun getTotalSaveCurrentAmount(): Flow<Long?>

    fun getSaveById(saveId: Long): Flow<Save?>

    suspend fun getSaveBalance(saveId: Long): Long?

    suspend fun createNewSave(save: Save)

    suspend fun deleteSaveById(saveId: Long)
}