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

    override fun getTotalAccountBalance(): Flow<Long?> {
        return accountDao.getTotalAccountBalance()
    }

    override suspend fun getAccountBalance(accountId: Int): Long? {
        return accountDao.getAccountBalance(accountId)
    }
}