package com.android.monuver.core.domain.usecase

import com.android.monuver.core.domain.common.CheckAppVersionStatusState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CheckAppVersionUseCase {
    operator fun invoke(): Flow<CheckAppVersionStatusState> {
        return flow {
            emit(CheckAppVersionStatusState.Progress)

            delay(3000)

            emit(CheckAppVersionStatusState.Success)
        }
    }
}