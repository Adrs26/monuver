package com.android.monu.data.repository

import com.android.monu.data.local.dao.SavingDao
import com.android.monu.data.mapper.SavingMapper
import com.android.monu.domain.model.saving.Saving
import com.android.monu.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavingRepositoryImpl(
    private val savingDao: SavingDao
) : SavingRepository {

    override fun getAllActiveSavings(): Flow<List<Saving>> {
        return savingDao.getAllActiveSavings().map { savings ->
            savings.map { saving ->
                SavingMapper.savingEntityToDomain(saving)
            }
        }
    }

    override fun getAllInactiveSavings(): Flow<List<Saving>> {
        return savingDao.getAllInactiveSavings().map { savings ->
            savings.map { saving ->
                SavingMapper.savingEntityToDomain(saving)
            }
        }
    }

    override fun getTotalSavingCurrentAmount(): Flow<Long?> {
        return savingDao.getTotalSavingCurrentAmount()
    }

    override fun getSavingById(savingId: Long): Flow<Saving?> {
        return savingDao.getSavingById(savingId).map { saving ->
            saving?.let { SavingMapper.savingEntityToDomain(it) }
        }
    }

    override suspend fun getSavingBalance(savingId: Long): Long? {
        return savingDao.getSavingBalance(savingId)
    }

    override suspend fun createNewSaving(saving: Saving) {
        savingDao.createNewSaving(SavingMapper.savingDomainToEntity(saving))
    }

    override suspend fun deleteSavingById(savingId: Long) {
        savingDao.deleteSavingById(savingId)
    }

    override suspend fun getAllSavingsSuspend(): List<Saving> {
        return savingDao.getAllSavingsSuspend().map { saving ->
            SavingMapper.savingEntityToDomain(saving)
        }
    }

    override suspend fun insertAllSavings(savings: List<Saving>) {
        savingDao.insertAllSavings(savings.map { saving ->
            SavingMapper.savingDomainToEntity(saving)
        })
    }
}