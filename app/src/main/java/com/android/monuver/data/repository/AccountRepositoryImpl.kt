package com.android.monuver.data.repository

import com.android.monuver.data.local.dao.AccountDao
import com.android.monuver.data.mapper.toDomain
import com.android.monuver.domain.model.AccountState
import com.android.monuver.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountRepositoryImpl(
    private val accountDao: AccountDao
) : AccountRepository {

    override fun getAllAccounts(): Flow<List<AccountState>> {
        return accountDao.getAllAccounts().map { accounts ->
            accounts.map { it.toDomain() }
        }
    }

    override fun getActiveAccounts(): Flow<List<AccountState>> {
        return accountDao.getActiveAccounts().map { accounts ->
            accounts.map { it.toDomain() }
        }
    }

    override fun getTotalAccountBalance(): Flow<Long?> {
        return accountDao.getTotalAccountBalance()
    }

    override fun getActiveAccountBalance(): Flow<Long?> {
        return accountDao.getActiveAccountBalance()
    }

    override fun getAccountById(accountId: Int): Flow<AccountState?> {
        return accountDao.getAccountById(accountId).map { it?.toDomain() }
    }

    override suspend fun getAccountBalance(accountId: Int): Long? {
        return accountDao.getAccountBalance(accountId)
    }

    override suspend fun getAllAccountsSuspend(): List<AccountState> {
        return accountDao.getAllAccountsSuspend().map { it.toDomain() }
    }
}