package com.android.monuver.feature.saving.domain.repository

import com.android.monuver.core.domain.model.SavingState
import com.android.monuver.core.domain.model.TransactionState
import kotlinx.coroutines.flow.Flow

internal interface SavingRepository {

    fun getAllActiveSavings(): Flow<List<SavingState>>

    fun getAllInactiveSavings(): Flow<List<SavingState>>

    fun getTotalSavingCurrentAmount(): Flow<Long?>

    fun getSavingById(savingId: Long): Flow<SavingState?>

    suspend fun getSavingBalance(savingId: Long): Long?

    suspend fun createNewSaving(savingState: SavingState)

    suspend fun deleteSavingById(savingId: Long)

    suspend fun createDepositTransaction(savingId: Long, transactionState: TransactionState)

    suspend fun createWithdrawTransaction(savingId: Long, transactionState: TransactionState)

    suspend fun deleteSavingTransaction(
        transactionId: Long,
        category: Int,
        accountId: Int,
        savingId: Long,
        amount: Long
    )

    suspend fun updateSaving(savingState: SavingState)

    suspend fun completeSaving(transactionState: TransactionState, savingId: Long)

    fun getTransactionsBySavingId(savingId: Long): Flow<List<TransactionState>>

    suspend fun getTransactionsBySavingIdSuspend(savingId: Long): List<TransactionState>
}