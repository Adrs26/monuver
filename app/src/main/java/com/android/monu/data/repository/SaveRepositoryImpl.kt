package com.android.monu.data.repository

import com.android.monu.data.local.dao.SaveDao
import com.android.monu.data.mapper.SaveMapper
import com.android.monu.domain.model.save.Save
import com.android.monu.domain.repository.SaveRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SaveRepositoryImpl(
    private val saveDao: SaveDao
) : SaveRepository {

    override fun getAllSaves(): Flow<List<Save>> {
        return saveDao.getAllSaves().map { saves ->
            saves.map { save ->
                SaveMapper.saveEntityToDomain(save)
            }
        }
    }

    override fun getTotalSaveCurrentAmount(): Flow<Long?> {
        return saveDao.getTotalSaveCurrentAmount()
    }

    override fun getSaveById(saveId: Long): Flow<Save?> {
        return saveDao.getSaveById(saveId).map { save ->
            save?.let { SaveMapper.saveEntityToDomain(it) }
        }
    }

    override suspend fun createNewSave(save: Save) {
        saveDao.createNewSave(SaveMapper.saveDomainToEntity(save))
    }
}