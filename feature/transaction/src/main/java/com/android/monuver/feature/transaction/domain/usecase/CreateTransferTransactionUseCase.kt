package com.android.monuver.feature.transaction.domain.usecase

import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.domain.repository.CoreRepository
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.TransactionChildCategory
import com.android.monuver.core.domain.util.TransactionParentCategory
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.feature.transaction.domain.model.TransferState
import com.android.monuver.feature.transaction.domain.repository.TransactionRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class CreateTransferTransactionUseCase(
    private val coreRepository: CoreRepository,
    private val transactionRepository: TransactionRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(transferState: TransferState): DatabaseResultState {
        when {
            transferState.sourceId == 0 -> return DatabaseResultState.EmptyTransactionSource
            transferState.destinationId == 0 -> return DatabaseResultState.EmptyTransactionDestination
            transferState.date.isEmpty() -> return DatabaseResultState.EmptyTransactionDate
            transferState.amount == 0L -> return DatabaseResultState.EmptyTransactionAmount
        }

        val (month, year) = DateHelper.getMonthAndYear(transferState.date)
        val transactionState = TransactionState(
            title = "Transfer Saldo",
            type = TransactionType.TRANSFER,
            parentCategory = TransactionParentCategory.TRANSFER,
            childCategory = TransactionChildCategory.TRANSFER_ACCOUNT,
            date = transferState.date,
            month = month,
            year = year,
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            amount = transferState.amount,
            sourceId = transferState.sourceId,
            sourceName = transferState.sourceName,
            destinationId = transferState.destinationId,
            destinationName = transferState.destinationName,
            isLocked = true,
            isSpecialCase = true
        )

        val accountBalance = coreRepository.getAccountBalance(transactionState.sourceId)

        if (accountBalance == null || accountBalance < transactionState.amount) {
            return DatabaseResultState.InsufficientAccountBalance
        }

        transactionRepository.createTransferTransaction(transactionState)
        return DatabaseResultState.CreateTransactionSuccess
    }
}