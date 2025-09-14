package com.android.monu.data.mapper

import com.android.monu.data.local.entity.room.SavingEntity
import com.android.monu.domain.model.saving.Saving

object SavingMapper {
    fun savingEntityToDomain(
        savingEntity: SavingEntity
    ): Saving {
        return Saving(
            id = savingEntity.id,
            title = savingEntity.title,
            targetDate = savingEntity.targetDate,
            targetAmount = savingEntity.targetAmount,
            currentAmount = savingEntity.currentAmount,
            isActive = savingEntity.isActive
        )
    }

    fun savingDomainToEntity(
        saving: Saving
    ): SavingEntity {
        return SavingEntity(
            title = saving.title,
            targetDate = saving.targetDate,
            targetAmount = saving.targetAmount,
            currentAmount = saving.currentAmount,
            isActive = saving.isActive
        )
    }

    fun savingDomainToEntityForUpdate(
        saving: Saving
    ): SavingEntity {
        return SavingEntity(
            id = saving.id,
            title = saving.title,
            targetDate = saving.targetDate,
            targetAmount = saving.targetAmount,
            currentAmount = saving.currentAmount,
            isActive = saving.isActive
        )
    }
}