package com.android.monu.data.repository

import com.android.monu.data.local.dao.FinanceDao
import com.android.monu.data.mapper.AccountMapper
import com.android.monu.data.mapper.TransactionMapper
import com.android.monu.domain.model.account.Account
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository

class FinanceRepositoryImpl(
    private val financeDao: FinanceDao
) : FinanceRepository {

    override suspend fun createAccountWithInitialTransaction(
        account: Account,
        transaction: Transaction
    ): Long {
        return financeDao.createAccountWithInitialTransaction(
            AccountMapper.accountDomainToEntity(account),
            TransactionMapper.transactionDomainToEntity(transaction)
        )
    }

    override suspend fun createTransactionAndAdjustAccountBalance(
        transaction: Transaction
    ): Long {
        return financeDao.createTransactionAndAdjustAccountBalance(
            TransactionMapper.transactionDomainToEntity(transaction)
        )
    }

    override suspend fun deleteTransactionAndAdjustAccountBalance(
        transaction: Transaction
    ): Int {
        return financeDao.deleteTransactionAndAdjustAccountBalance(
            transaction.id,
            TransactionMapper.transactionDomainToEntity(transaction)
        )
    }
}