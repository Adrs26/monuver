package com.android.monu.domain.usecase.finance

import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.domain.repository.SaveRepository
import com.android.monu.domain.repository.TransactionRepository
import com.android.monu.ui.feature.utils.TransactionChildCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteSaveUseCase(
    private val financeRepository: FinanceRepository,
    private val transactionRepository: TransactionRepository,
    private val saveRepository: SaveRepository
) {
    operator fun invoke(saveId: Long): Flow<DeleteSaveState> = flow {
        emit(DeleteSaveState.Loading)

        val transactions = transactionRepository.getTransactionsBySaveIdSuspend(saveId)
        val total = transactions.size
        var deletedCount = 0

        try {
            for (transaction in transactions) {
                val accountId = if (transaction.childCategory == TransactionChildCategory.SAVINGS_IN)
                    transaction.sourceId else transaction.destinationId

                val relatedSaveId = if (transaction.childCategory == TransactionChildCategory.SAVINGS_IN)
                    transaction.destinationId else transaction.sourceId

                financeRepository.deleteSaveTransaction(
                    transactionId = transaction.id,
                    category = transaction.childCategory,
                    accountId = accountId ?: 0,
                    saveId = (relatedSaveId ?: 0L).toLong(),
                    amount = transaction.amount
                )
                deletedCount++
                emit(DeleteSaveState.Progress(deletedCount, total))
            }

            saveRepository.deleteSaveById(saveId)
            emit(DeleteSaveState.Success)
        } catch (e: Exception) {
            emit(DeleteSaveState.Error(e))
        }
    }
}

sealed class DeleteSaveState {
    object Loading : DeleteSaveState()
    data class Progress(val current: Int, val total: Int) : DeleteSaveState()
    object Success : DeleteSaveState()
    data class Error(val throwable: Throwable) : DeleteSaveState()
}
