package com.android.monu.domain.repository

import com.android.monu.domain.model.account.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    fun getAllAccounts(): Flow<List<Account>>

    fun getActiveAccounts(): Flow<List<Account>>

    fun getTotalAccountBalance(): Flow<Long?>

    fun getActiveAccountBalance(): Flow<Long?>

    fun getAccountById(accountId: Int): Flow<Account?>

    suspend fun getAccountBalance(accountId: Int): Long?
}