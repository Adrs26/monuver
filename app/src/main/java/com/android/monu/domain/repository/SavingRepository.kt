package com.android.monu.domain.repository

import com.android.monu.domain.model.SavingState
import kotlinx.coroutines.flow.Flow

interface SavingRepository {

    fun getAllActiveSavings(): Flow<List<SavingState>>

    fun getAllInactiveSavings(): Flow<List<SavingState>>

    fun getTotalSavingCurrentAmount(): Flow<Long?>

    fun getSavingById(savingId: Long): Flow<SavingState?>

    suspend fun getSavingBalance(savingId: Long): Long?

    suspend fun createNewSaving(savingState: SavingState)

    suspend fun deleteSavingById(savingId: Long)

    suspend fun getAllSavings(): List<SavingState>
}