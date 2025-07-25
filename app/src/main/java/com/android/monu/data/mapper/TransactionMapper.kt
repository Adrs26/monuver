package com.android.monu.data.mapper

import com.android.monu.data.local.entity.room.TransactionEntity
import com.android.monu.data.local.entity.projection.TransactionCategorySummaryEntity
import com.android.monu.data.local.entity.projection.TransactionSummaryEntity
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.model.transaction.TransactionCategorySummary
import com.android.monu.domain.model.transaction.TransactionSummary

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

    fun transactionSummaryEntityToDomain(
        transactionSummaryEntity: TransactionSummaryEntity
    ): TransactionSummary {
        return TransactionSummary(
            type = transactionSummaryEntity.type,
            date = transactionSummaryEntity.date,
            amount = transactionSummaryEntity.amount
        )
    }

    fun transactionCategorySummaryEntityToDomain(
        transactionCategorySummaryEntity: TransactionCategorySummaryEntity
    ): TransactionCategorySummary {
        return TransactionCategorySummary(
            parentCategory = transactionCategorySummaryEntity.parentCategory,
            totalAmount = transactionCategorySummaryEntity.totalAmount
        )
    }
}