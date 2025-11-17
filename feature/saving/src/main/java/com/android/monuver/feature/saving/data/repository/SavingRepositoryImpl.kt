package com.android.monuver.feature.saving.data.repository

import androidx.room.withTransaction
import com.android.monuver.core.data.database.MonuverDatabase
import com.android.monuver.core.data.database.dao.AccountDao
import com.android.monuver.core.data.database.dao.SavingDao
import com.android.monuver.core.data.database.dao.TransactionDao
import com.android.monuver.core.data.mapper.toDomain
import com.android.monuver.core.data.mapper.toEntity
import com.android.monuver.core.data.mapper.toEntityForUpdate
import com.android.monuver.core.domain.model.SavingState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.domain.util.TransactionChildCategory
import com.android.monuver.feature.saving.domain.repository.SavingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SavingRepositoryImpl(
    private val database: MonuverDatabase,
    private val accountDao: AccountDao,
    private val savingDao: SavingDao,
    private val transactionDao: TransactionDao
) : SavingRepository {

    override fun getAllActiveSavings(): Flow<List<SavingState>> {
        return savingDao.getAllActiveSavings().map { savings ->
            savings.map { it.toDomain() }
        }
    }

    override fun getAllInactiveSavings(): Flow<List<SavingState>> {
        return savingDao.getAllInactiveSavings().map { savings ->
            savings.map { it.toDomain() }
        }
    }

    override fun getTotalSavingCurrentAmount(): Flow<Long?> {
        return savingDao.getTotalSavingCurrentAmount()
    }

    override fun getSavingById(savingId: Long): Flow<SavingState?> {
        return savingDao.getSavingById(savingId).map { it?.toDomain() }
    }

    override suspend fun getSavingBalance(savingId: Long): Long? {
        return savingDao.getSavingBalance(savingId)
    }

    override suspend fun createNewSaving(savingState: SavingState) {
        savingDao.createNewSaving(savingState.toEntity())
    }

    override suspend fun deleteSavingById(savingId: Long) {
        savingDao.deleteSavingById(savingId)
    }

    override suspend fun createDepositTransaction(
        savingId: Long,
        transactionState: TransactionState
    ) {
        return database.withTransaction {
            savingDao.increaseSavingCurrentAmount(
                savingId = savingId,
                delta = transactionState.amount
            )
            accountDao.decreaseAccountBalance(
                accountId = transactionState.sourceId,
                delta = transactionState.amount
            )
            transactionDao.createNewTransaction(transactionState.toEntity())
        }
    }

    override suspend fun createWithdrawTransaction(
        savingId: Long,
        transactionState: TransactionState
    ) {
        return database.withTransaction {
            savingDao.decreaseSavingCurrentAmount(
                savingId = savingId,
                delta = transactionState.amount
            )
            accountDao.increaseAccountBalance(
                accountId = transactionState.destinationId ?: 0,
                delta = transactionState.amount
            )
            transactionDao.createNewTransaction(transactionState.toEntity())
        }
    }

    override suspend fun deleteSavingTransaction(
        transactionId: Long,
        category: Int,
        accountId: Int,
        savingId: Long,
        amount: Long
    ) {
        return database.withTransaction {
            if (category == TransactionChildCategory.SAVINGS_IN) {
                savingDao.decreaseSavingCurrentAmount(
                    savingId = savingId,
                    delta = amount
                )
                accountDao.increaseAccountBalance(
                    accountId = accountId,
                    delta = amount
                )
            } else {
                savingDao.increaseSavingCurrentAmount(
                    savingId = savingId,
                    delta = amount
                )
                accountDao.decreaseAccountBalance(
                    accountId = accountId,
                    delta = amount
                )
            }
            transactionDao.deleteTransactionById(transactionId)
        }
    }

    override suspend fun updateSaving(savingState: SavingState) {
        database.withTransaction {
            savingDao.updateSaving(savingState.toEntityForUpdate())
            transactionDao.updateSavingTitleOnDepositTransaction(
                savingId = savingState.id,
                savingTitle = savingState.title
            )
            transactionDao.updateSavingTitleOnWithdrawTransaction(
                savingId = savingState.id,
                savingTitle = savingState.title
            )
        }
    }

    override suspend fun completeSaving(transactionState: TransactionState, savingId: Long) {
        database.withTransaction {
            transactionDao.createNewTransaction(transactionState.toEntity())
            savingDao.updateSavingStatusToInactiveById(savingId)
        }
    }

    override fun getTransactionsBySavingId(savingId: Long): Flow<List<TransactionState>> {
        return transactionDao.getTransactionsBySavingId(savingId).map { transactions ->
            transactions.map { it.toDomain() }
        }
    }

    override suspend fun getTransactionsBySavingIdSuspend(savingId: Long): List<TransactionState> {
        return transactionDao.getTransactionsBySavingIdSuspend(savingId).map { it.toDomain() }
    }
}