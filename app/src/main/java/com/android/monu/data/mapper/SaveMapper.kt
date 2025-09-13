package com.android.monu.data.mapper

import com.android.monu.data.local.entity.room.SaveEntity
import com.android.monu.domain.model.save.Save

object SaveMapper {
    fun saveEntityToDomain(
        saveEntity: SaveEntity
    ): Save {
        return Save(
            id = saveEntity.id,
            title = saveEntity.title,
            targetDate = saveEntity.targetDate,
            targetAmount = saveEntity.targetAmount,
            currentAmount = saveEntity.currentAmount,
            isActive = saveEntity.isActive
        )
    }

    fun saveDomainToEntity(
        save: Save
    ): SaveEntity {
        return SaveEntity(
            title = save.title,
            targetDate = save.targetDate,
            targetAmount = save.targetAmount,
            currentAmount = save.currentAmount,
            isActive = save.isActive
        )
    }

    fun saveDomainToEntityForUpdate(
        save: Save
    ): SaveEntity {
        return SaveEntity(
            id = save.id,
            title = save.title,
            targetDate = save.targetDate,
            targetAmount = save.targetAmount,
            currentAmount = save.currentAmount,
            isActive = save.isActive
        )
    }
}