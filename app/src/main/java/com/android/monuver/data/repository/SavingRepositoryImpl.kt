package com.android.monuver.data.repository

import com.android.monuver.data.local.dao.SavingDao
import com.android.monuver.data.mapper.toDomain
import com.android.monuver.data.mapper.toEntity
import com.android.monuver.domain.model.SavingState
import com.android.monuver.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavingRepositoryImpl(
    private val savingDao: SavingDao
) : SavingRepository {

    override fun getAllActiveSavings(): Flow<List<SavingState>> {
        return savingDao.getAllActiveSavings().map { savings ->
            savings.map { it.toDomain() }
        }
    }

    override fun getAllInactiveSavings(): Flow<List<SavingState>> {
        return savingDao.getAllInactiveSavings().map { savings ->
            savings.map { it.toDomain() }
        }
    }

    override fun getTotalSavingCurrentAmount(): Flow<Long?> {
        return savingDao.getTotalSavingCurrentAmount()
    }

    override fun getSavingById(savingId: Long): Flow<SavingState?> {
        return savingDao.getSavingById(savingId).map { it?.toDomain() }
    }

    override suspend fun getSavingBalance(savingId: Long): Long? {
        return savingDao.getSavingBalance(savingId)
    }

    override suspend fun createNewSaving(savingState: SavingState) {
        savingDao.createNewSaving(savingState.toEntity())
    }

    override suspend fun deleteSavingById(savingId: Long) {
        savingDao.deleteSavingById(savingId)
    }

    override suspend fun getAllSavings(): List<SavingState> {
        return savingDao.getAllSavings().map { it.toDomain() }
    }
}