package com.android.monu.domain.repository

import com.android.monu.domain.model.account.Account
import com.android.monu.domain.model.transaction.Transaction

interface FinanceRepository {

    suspend fun createAccount(account: Account, transaction: Transaction): Long

    suspend fun createIncomeTransaction(transaction: Transaction): Long
    suspend fun createExpenseTransaction(transaction: Transaction): Long
    suspend fun createTransferTransaction(transaction: Transaction): Long

    suspend fun deleteIncomeTransaction(id: Long, sourceId: Int, amount: Long): Int
    suspend fun deleteExpenseTransaction(id: Long, sourceId: Int, amount: Long): Int
    suspend fun deleteTransferTransaction(id: Long, sourceId: Int, destinationId: Int, amount: Long): Int
}