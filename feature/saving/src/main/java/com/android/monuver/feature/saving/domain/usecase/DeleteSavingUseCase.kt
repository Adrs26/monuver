package com.android.monuver.feature.saving.domain.usecase

import com.android.monuver.core.domain.util.TransactionChildCategory
import com.android.monuver.feature.saving.domain.common.DeleteSavingStatusState
import com.android.monuver.feature.saving.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class DeleteSavingUseCase(
    private val repository: SavingRepository
) {
    operator fun invoke(savingId: Long): Flow<DeleteSavingStatusState> = flow {
        emit(DeleteSavingStatusState.Idle)

        val transactions = repository.getTransactionsBySavingIdSuspend(savingId)
        val total = transactions.size
        var deletedCount = 0

        try {
            for (transaction in transactions) {
                val accountId =
                    if (transaction.childCategory == TransactionChildCategory.SAVINGS_IN)
                        transaction.sourceId else transaction.destinationId

                val relatedSavingId =
                    if (transaction.childCategory == TransactionChildCategory.SAVINGS_IN)
                        transaction.destinationId else transaction.sourceId

                repository.deleteSavingTransaction(
                    transactionId = transaction.id,
                    category = transaction.childCategory,
                    accountId = accountId ?: 0,
                    savingId = (relatedSavingId ?: 0L).toLong(),
                    amount = transaction.amount
                )
                deletedCount++
                emit(DeleteSavingStatusState.Progress(deletedCount, total))
            }

            repository.deleteSavingById(savingId)
            emit(DeleteSavingStatusState.Success)
        } catch (e: Exception) {
            emit(DeleteSavingStatusState.Error(e))
        }
    }
}