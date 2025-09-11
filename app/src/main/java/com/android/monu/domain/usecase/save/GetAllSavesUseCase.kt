package com.android.monu.domain.usecase.save

import com.android.monu.domain.model.save.Save
import com.android.monu.domain.repository.SaveRepository
import kotlinx.coroutines.flow.Flow

class GetAllSavesUseCase(
    private val repository: SaveRepository
) {
    operator fun invoke(): Flow<List<Save>> {
        return repository.getAllSaves()
    }
}