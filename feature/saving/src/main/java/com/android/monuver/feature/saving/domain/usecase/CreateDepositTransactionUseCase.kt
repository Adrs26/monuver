package com.android.monuver.feature.saving.domain.usecase

import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.domain.repository.CoreRepository
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.TransactionChildCategory
import com.android.monuver.core.domain.util.TransactionParentCategory
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.feature.saving.domain.model.DepositWithdrawState
import com.android.monuver.feature.saving.domain.repository.SavingRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class CreateDepositTransactionUseCase(
    private val coreRepository: CoreRepository,
    private val savingRepository: SavingRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(depositState: DepositWithdrawState): DatabaseResultState {
        when {
            depositState.date.isEmpty() -> return DatabaseResultState.EmptyDepositDate
            depositState.amount == 0L -> return DatabaseResultState.EmptyDepositAmount
            depositState.accountId == 0 -> return DatabaseResultState.EmptyDepositAccount
        }

        val accountBalance = coreRepository.getAccountBalance(depositState.accountId)
        if (accountBalance == null || accountBalance < depositState.amount) {
            return DatabaseResultState.InsufficientAccountBalance
        }

        val (month, year) = DateHelper.getMonthAndYear(depositState.date)
        val transactionState = TransactionState(
            title = "Setoran Tabungan",
            type = TransactionType.TRANSFER,
            parentCategory = TransactionParentCategory.TRANSFER,
            childCategory = TransactionChildCategory.SAVINGS_IN,
            date = depositState.date,
            month = month,
            year = year,
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            amount = depositState.amount,
            sourceId = depositState.accountId,
            sourceName = depositState.accountName,
            destinationId = depositState.savingId.toInt(),
            destinationName = depositState.savingName,
            saveId = depositState.savingId,
            isLocked = true,
            isSpecialCase = true
        )

        savingRepository.createDepositTransaction(
            savingId = depositState.savingId,
            transactionState = transactionState
        )
        return DatabaseResultState.CreateDepositTransactionSuccess
    }
}