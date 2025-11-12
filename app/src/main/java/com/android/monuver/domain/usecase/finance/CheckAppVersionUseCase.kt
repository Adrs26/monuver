package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.CheckAppVersionStatusState
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