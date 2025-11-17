package com.android.monuver.feature.home.data.repository

import com.android.monuver.core.data.database.dao.AccountDao
import com.android.monuver.core.data.database.dao.TransactionDao
import com.android.monuver.core.data.mapper.toDomain
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class HomeRepositoryImpl(
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao
) : HomeRepository {

    override fun getActiveAccountBalance(): Flow<Long?> {
        return accountDao.getActiveAccountBalance()
    }

    override fun getRecentTransactions(): Flow<List<TransactionState>> {
        return transactionDao.getRecentTransactions().map { transactions ->
            transactions.map { it.toDomain() }
        }
    }
}