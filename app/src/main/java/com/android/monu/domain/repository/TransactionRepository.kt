package com.android.monu.domain.repository

import androidx.paging.PagingData
import com.android.monu.domain.model.Transaction
import com.android.monu.domain.model.TransactionConcise
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun getAllTransactions(
        query: String,
        type: Int?,
        month: Int?,
        year: Int?,
        scope: CoroutineScope
    ): Flow<PagingData<TransactionConcise>>

    suspend fun insertTransaction(transaction: Transaction): Result<Long>
}