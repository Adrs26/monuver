package com.android.monu.domain.repository

import com.android.monu.domain.model.account.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    fun getAllAccounts(): Flow<List<Account>>

    fun getTotalAccountBalance(): Flow<Long?>

    suspend fun createNewAccount(account: Account): Result<Long>

    suspend fun increaseAccountBalance(accountId: Int, delta: Long)

    suspend fun decreaseAccountBalance(accountId: Int, delta: Long)
}