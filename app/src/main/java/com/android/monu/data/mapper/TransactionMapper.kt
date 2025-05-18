package com.android.monu.data.mapper

import com.android.monu.data.local.entity.TransactionEntity
import com.android.monu.data.local.projection.TransactionConciseProj
import com.android.monu.domain.model.Transaction
import com.android.monu.domain.model.TransactionConcise

object TransactionMapper {
    fun transactionConciseEntityToDomain(
        transactionConciseProj: TransactionConciseProj
    ): TransactionConcise {
        return TransactionConcise(
            id = transactionConciseProj.id,
            title = transactionConciseProj.title,
            type = transactionConciseProj.type,
            category = transactionConciseProj.category,
            date = transactionConciseProj.date,
            amount = transactionConciseProj.amount
        )
    }

    fun transactionDomainToEntity(
        transaction: Transaction
    ): TransactionEntity {
        return TransactionEntity(
            title = transaction.title,
            type = transaction.type,
            category = transaction.category,
            date = transaction.date,
            month = transaction.month,
            year = transaction.year,
            amount = transaction.amount,
            budgetingId = transaction.budgetingId,
            budgetingTitle = transaction.budgetingTitle
        )
    }
}