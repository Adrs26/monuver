package com.android.monu.data.mapper

import com.android.monu.data.local.entity.TransactionEntity
import com.android.monu.data.local.projection.TransactionMonthlyAmountProj
import com.android.monu.data.local.projection.TransactionOverviewProj
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.model.transaction.TransactionMonthlyAmount
import com.android.monu.domain.model.transaction.TransactionOverview

object TransactionMapper {
    fun transactionEntityToDomain(
        transactionEntity: TransactionEntity
    ): Transaction {
        return Transaction(
            id = transactionEntity.id,
            title = transactionEntity.title,
            type = transactionEntity.type,
            parentCategory = transactionEntity.parentCategory,
            childCategory = transactionEntity.childCategory,
            date = transactionEntity.date,
            month = transactionEntity.month,
            year = transactionEntity.year,
            timeStamp = transactionEntity.timeStamp,
            amount = transactionEntity.amount,
            sourceId = transactionEntity.sourceId,
            sourceName = transactionEntity.sourceName,
            destinationId = transactionEntity.destinationId,
            destinationName = transactionEntity.destinationName,
            saveId = transactionEntity.saveId
        )
    }

    fun transactionDomainToEntity(
        transaction: Transaction
    ): TransactionEntity {
        return TransactionEntity(
            title = transaction.title,
            type = transaction.type,
            parentCategory = transaction.parentCategory,
            childCategory = transaction.childCategory,
            date = transaction.date,
            month = transaction.month,
            year = transaction.year,
            timeStamp = transaction.timeStamp,
            amount = transaction.amount,
            sourceId = transaction.sourceId,
            sourceName = transaction.sourceName,
            destinationId = transaction.destinationId,
            destinationName = transaction.destinationName,
            saveId = transaction.saveId
        )
    }

    fun transactionDomainToEntityForUpdate(
        transaction: Transaction
    ): TransactionEntity {
        return TransactionEntity(
            id = transaction.id,
            title = transaction.title,
            type = transaction.type,
            parentCategory = transaction.parentCategory,
            childCategory = transaction.childCategory,
            date = transaction.date,
            month = transaction.month,
            year = transaction.year,
            timeStamp = transaction.timeStamp,
            amount = transaction.amount,
            sourceId = transaction.sourceId,
            sourceName = transaction.sourceName,
            destinationId = transaction.destinationId,
            destinationName = transaction.destinationName,
            saveId = transaction.saveId
        )
    }

    fun transactionMonthlyAmountEntityToDomain(
        transactionMonthlyAmountProj: TransactionMonthlyAmountProj
    ): TransactionMonthlyAmount {
        return TransactionMonthlyAmount(
            month = transactionMonthlyAmountProj.month,
            totalAmountIncome = transactionMonthlyAmountProj.totalAmountIncome,
            totalAmountExpense = transactionMonthlyAmountProj.totalAmountExpense
        )
    }

    fun transactionOverviewEntityToDomain(
        transactionOverviewProj: TransactionOverviewProj
    ): TransactionOverview {
        return TransactionOverview(
            month = transactionOverviewProj.month,
            year = transactionOverviewProj.year,
            amount = transactionOverviewProj.amount
        )
    }
}