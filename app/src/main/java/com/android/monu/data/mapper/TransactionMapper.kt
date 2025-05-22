package com.android.monu.data.mapper

import com.android.monu.data.local.entity.TransactionEntity
import com.android.monu.data.local.projection.TransactionConciseProj
import com.android.monu.data.local.projection.TransactionMonthlyAmountProj
import com.android.monu.data.local.projection.TransactionOverviewProj
import com.android.monu.domain.model.Transaction
import com.android.monu.domain.model.TransactionConcise
import com.android.monu.domain.model.TransactionMonthlyAmount
import com.android.monu.domain.model.TransactionOverview

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

    fun transactionEntityToDomain(
        transactionEntity: TransactionEntity
    ): Transaction {
        return Transaction(
            id = transactionEntity.id,
            title = transactionEntity.title,
            type = transactionEntity.type,
            category = transactionEntity.category,
            date = transactionEntity.date,
            month = transactionEntity.month,
            year = transactionEntity.year,
            amount = transactionEntity.amount,
            budgetingId = transactionEntity.budgetingId,
            budgetingTitle = transactionEntity.budgetingTitle
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

    fun transactionDomainToEntityForUpdate(
        transaction: Transaction
    ): TransactionEntity {
        return TransactionEntity(
            id = transaction.id,
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