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
        return accountDao.getAllAccounts().map { entityList ->
            entityList.map { entity ->
                AccountMapper.accountEntityToDomain(entity)
            }
        }
    }

    override fun getTotalAccountBalance(): Flow<Long?> {
        return accountDao.getTotalAccountBalance()
    }

    override suspend fun createNewAccount(account: Account): Result<Long> {
        return try {
            val result = accountDao.createNewAccount(
                AccountMapper.accountDomainToEntity(account)
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun increaseAccountBalance(accountId: Int, delta: Long) {
        return accountDao.increaseAccountBalance(accountId, delta)
    }

    override suspend fun decreaseAccountBalance(accountId: Int, delta: Long) {
        return accountDao.decreaseAccountBalance(accountId, delta)
    }
}