package com.android.monu.domain.repository

import com.android.monu.domain.model.account.Account
import com.android.monu.domain.model.transaction.Transaction

interface FinanceRepository {

    suspend fun createAccountWithInitialTransaction(account: Account, transaction: Transaction): Long

    suspend fun createTransactionAndAdjustAccountBalance(transaction: Transaction): Long

    suspend fun deleteTransactionAndAdjustAccountBalance(transaction: Transaction): Int
}