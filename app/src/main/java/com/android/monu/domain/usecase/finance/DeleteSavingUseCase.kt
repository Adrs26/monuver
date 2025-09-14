package com.android.monu.domain.usecase.finance

import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.domain.repository.SavingRepository
import com.android.monu.domain.repository.TransactionRepository
import com.android.monu.ui.feature.utils.TransactionChildCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteSavingUseCase(
    private val financeRepository: FinanceRepository,
    private val transactionRepository: TransactionRepository,
    private val savingRepository: SavingRepository
) {
    operator fun invoke(savingId: Long): Flow<DeleteSavingState> = flow {
        emit(DeleteSavingState.Loading)

        val transactions = transactionRepository.getTransactionsBySavingIdSuspend(savingId)
        val total = transactions.size
        var deletedCount = 0

        try {
            for (transaction in transactions) {
                val accountId = if (transaction.childCategory == TransactionChildCategory.SAVINGS_IN)
                    transaction.sourceId else transaction.destinationId

                val relatedSavingId = if (transaction.childCategory == TransactionChildCategory.SAVINGS_IN)
                    transaction.destinationId else transaction.sourceId

                financeRepository.deleteSavingTransaction(
                    transactionId = transaction.id,
                    category = transaction.childCategory,
                    accountId = accountId ?: 0,
                    savingId = (relatedSavingId ?: 0L).toLong(),
                    amount = transaction.amount
                )
                deletedCount++
                emit(DeleteSavingState.Progress(deletedCount, total))
            }

            savingRepository.deleteSavingById(savingId)
            emit(DeleteSavingState.Success)
        } catch (e: Exception) {
            emit(DeleteSavingState.Error(e))
        }
    }
}

sealed class DeleteSavingState {
    object Loading : DeleteSavingState()
    data class Progress(val current: Int, val total: Int) : DeleteSavingState()
    object Success : DeleteSavingState()
    data class Error(val throwable: Throwable) : DeleteSavingState()
}
