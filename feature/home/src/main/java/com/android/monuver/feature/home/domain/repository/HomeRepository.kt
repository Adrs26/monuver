package com.android.monuver.feature.home.domain.repository

import com.android.monuver.core.domain.model.TransactionState
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun getActiveAccountBalance(): Flow<Long?>

    fun getRecentTransactions(): Flow<List<TransactionState>>
}