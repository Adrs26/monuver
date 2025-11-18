package com.android.monuver.feature.account.domain.repository

import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.domain.model.TransactionState
import kotlinx.coroutines.flow.Flow

internal interface AccountRepository {

    suspend fun createAccount(accountState: AccountState, transactionState: TransactionState)

    fun getAccountById(accountId: Int): Flow<AccountState?>

    fun getAllAccounts(): Flow<List<AccountState>>

    fun getTotalAccountBalance(): Flow<Long?>

    suspend fun updateAccountStatus(accountId: Int, isActive: Boolean)

    suspend fun updateAccount(accountState: AccountState)
}