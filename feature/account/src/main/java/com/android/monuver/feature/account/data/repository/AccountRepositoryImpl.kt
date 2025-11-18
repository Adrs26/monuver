package com.android.monuver.feature.account.data.repository

import androidx.room.withTransaction
import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.core.data.database.MonuverDatabase
import com.android.monuver.core.data.database.dao.AccountDao
import com.android.monuver.core.data.database.dao.TransactionDao
import com.android.monuver.core.data.mapper.toDomain
import com.android.monuver.core.data.mapper.toEntity
import com.android.monuver.core.data.mapper.toEntityForUpdate
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.domain.model.TransactionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AccountRepositoryImpl(
    private val database: MonuverDatabase,
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao
) : AccountRepository {

    override suspend fun createAccount(
        accountState: AccountState,
        transactionState: TransactionState
    ) {
        database.withTransaction {
            val accountId = accountDao.createNewAccount(accountState.toEntity())
            val transactionWithAccountId = transactionState.copy(sourceId = accountId.toInt())
            transactionDao.createNewTransaction(transactionWithAccountId.toEntity())
        }
    }

    override fun getAccountById(accountId: Int): Flow<AccountState?> {
        return accountDao.getAccountById(accountId).map { it?.toDomain() }
    }

    override fun getAllAccounts(): Flow<List<AccountState>> {
        return accountDao.getAllAccounts().map { accounts ->
            accounts.map { it.toDomain() }
        }
    }

    override fun getTotalAccountBalance(): Flow<Long?> {
        return accountDao.getTotalAccountBalance()
    }

    override suspend fun updateAccountStatus(accountId: Int, isActive: Boolean) {
        database.withTransaction {
            accountDao.updateAccountStatus(
                accountId = accountId,
                isActive = isActive
            )
            transactionDao.updateTransactionLockStatusByAccountId(
                accountId = accountId,
                isLocked = !isActive
            )
        }
    }

    override suspend fun updateAccount(accountState: AccountState) {
        database.withTransaction {
            accountDao.updateAccount(accountState.toEntityForUpdate())
            transactionDao.updateAccountNameOnCommonTransaction(
                accountId = accountState.id,
                accountName = accountState.name
            )
            transactionDao.updateAccountNameOnTransferTransaction(
                accountId = accountState.id,
                accountName = accountState.name
            )
            transactionDao.updateAccountNameOnDepositTransaction(
                accountId = accountState.id,
                accountName = accountState.name
            )
            transactionDao.updateAccountNameOnWithdrawTransaction(
                accountId = accountState.id,
                accountName = accountState.name
            )
        }
    }
}