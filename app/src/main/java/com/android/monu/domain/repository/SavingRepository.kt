package com.android.monu.domain.repository

import com.android.monu.domain.model.saving.Saving
import kotlinx.coroutines.flow.Flow

interface SavingRepository {

    fun getAllActiveSavings(): Flow<List<Saving>>

    fun getAllInactiveSavings(): Flow<List<Saving>>

    fun getTotalSavingCurrentAmount(): Flow<Long?>

    fun getSavingById(savingId: Long): Flow<Saving?>

    suspend fun getSavingBalance(savingId: Long): Long?

    suspend fun createNewSaving(saving: Saving)

    suspend fun deleteSavingById(savingId: Long)

    suspend fun getAllSavingsSuspend(): List<Saving>

    suspend fun insertAllSavings(savings: List<Saving>)
}