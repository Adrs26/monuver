package com.android.monu.domain.repository

import com.android.monu.domain.model.AccountState
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    fun getAllAccounts(): Flow<List<AccountState>>

    fun getActiveAccounts(): Flow<List<AccountState>>

    fun getTotalAccountBalance(): Flow<Long?>

    fun getActiveAccountBalance(): Flow<Long?>

    fun getAccountById(accountId: Int): Flow<AccountState?>

    suspend fun getAccountBalance(accountId: Int): Long?

    suspend fun getAllAccountsSuspend(): List<AccountState>
}