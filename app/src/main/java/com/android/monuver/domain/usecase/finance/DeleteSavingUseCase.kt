package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.DeleteSavingStatusState
import com.android.monuver.domain.repository.FinanceRepository
import com.android.monuver.domain.repository.SavingRepository
import com.android.monuver.domain.repository.TransactionRepository
import com.android.monuver.utils.TransactionChildCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteSavingUseCase(
    private val financeRepository: FinanceRepository,
    private val transactionRepository: TransactionRepository,
    private val savingRepository: SavingRepository
) {
    operator fun invoke(savingId: Long): Flow<DeleteSavingStatusState> = flow {
        emit(DeleteSavingStatusState.Idle)

        val transactions = transactionRepository.getTransactionsBySavingIdSuspend(savingId)
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

                financeRepository.deleteSavingTransaction(
                    transactionId = transaction.id,
                    category = transaction.childCategory,
                    accountId = accountId ?: 0,
                    savingId = (relatedSavingId ?: 0L).toLong(),
                    amount = transaction.amount
                )
                deletedCount++
                emit(DeleteSavingStatusState.Progress(deletedCount, total))
            }

            savingRepository.deleteSavingById(savingId)
            emit(DeleteSavingStatusState.Success)
        } catch (e: Exception) {
            emit(DeleteSavingStatusState.Error(e))
        }
    }
}
