package com.android.monu.data.repository

import com.android.monu.data.local.dao.AccountDao
import com.android.monu.data.mapper.AccountMapper
import com.android.monu.domain.model.account.Account
import com.android.monu.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountRepositoryImpl(
    private val accountDao: AccountDao
) : AccountRepository {

    override fun getAllAccounts(): Flow<List<Account>> {
        return accountDao.getAllAccounts().map { accounts ->
            accounts.map { account ->
                AccountMapper.accountEntityToDomain(account)
            }
        }
    }

    override fun getActiveAccounts(): Flow<List<Account>> {
        return accountDao.getActiveAccounts().map { accounts ->
            accounts.map { account ->
                AccountMapper.accountEntityToDomain(account)
            }
        }
    }

    override fun getTotalAccountBalance(): Flow<Long?> {
        return accountDao.getTotalAccountBalance()
    }

    override fun getActiveAccountBalance(): Flow<Long?> {
        return accountDao.getActiveAccountBalance()
    }

    override fun getAccountById(accountId: Int): Flow<Account?> {
        return accountDao.getAccountById(accountId).map { account ->
            account?.let { account ->
                AccountMapper.accountEntityToDomain(account)
            }
        }
    }

    override suspend fun getAccountBalance(accountId: Int): Long? {
        return accountDao.getAccountBalance(accountId)
    }

    override suspend fun getAllAccountsSuspend(): List<Account> {
        return accountDao.getAllAccountsSuspend().map { account ->
            AccountMapper.accountEntityToDomain(account)
        }
    }

    override suspend fun insertAllAccounts(accounts: List<Account>) {
        accountDao.insertAllAccounts(accounts.map { account ->
            AccountMapper.accountDomainToEntity(account)
        })
    }
}