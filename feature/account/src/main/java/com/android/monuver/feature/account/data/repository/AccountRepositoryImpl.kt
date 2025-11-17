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
        account: AccountState,
        transactionState: TransactionState
    ) {
        database.withTransaction {
            val accountId = accountDao.createNewAccount(account.toEntity())
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
            accountDao.updateAccountStatus(accountId, isActive)
            transactionDao.updateTransactionLockStatusByAccountId(accountId, !isActive)
        }
    }

    override suspend fun updateAccount(account: AccountState) {
        database.withTransaction {
            accountDao.updateAccount(account.toEntityForUpdate())
            transactionDao.updateAccountNameOnCommonTransaction(account.id, account.name)
            transactionDao.updateAccountNameOnTransferTransaction(account.id, account.name)
            transactionDao.updateAccountNameOnDepositTransaction(account.id, account.name)
            transactionDao.updateAccountNameOnWithdrawTransaction(account.id, account.name)
        }
    }
}