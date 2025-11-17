package com.android.monuver.feature.saving.domain.usecase

import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.TransactionChildCategory
import com.android.monuver.core.domain.util.TransactionParentCategory
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.feature.saving.domain.model.DepositWithdrawState
import com.android.monuver.feature.saving.domain.repository.SavingRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class CreateWithdrawTransactionUseCase(
    private val repository: SavingRepository
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(withdrawState: DepositWithdrawState): DatabaseResultState {
        when {
            withdrawState.date.isEmpty() -> return DatabaseResultState.EmptyWithdrawDate
            withdrawState.amount == 0L -> return DatabaseResultState.EmptyWithdrawAmount
            withdrawState.accountId == 0 -> return DatabaseResultState.EmptyWithdrawAccount
        }

        val savingBalance = repository.getSavingBalance(withdrawState.savingId)
        if (savingBalance == null || savingBalance < withdrawState.amount) {
            return DatabaseResultState.InsufficientSavingBalance
        }

        val (month, year) = DateHelper.getMonthAndYear(withdrawState.date)
        val transactionState = TransactionState(
            title = "Penarikan Tabungan",
            type = TransactionType.TRANSFER,
            parentCategory = TransactionParentCategory.TRANSFER,
            childCategory = TransactionChildCategory.SAVINGS_OUT,
            date = withdrawState.date,
            month = month,
            year = year,
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            amount = withdrawState.amount,
            sourceId = withdrawState.savingId.toInt(),
            sourceName = withdrawState.savingName,
            destinationId = withdrawState.accountId,
            destinationName = withdrawState.accountName,
            saveId = withdrawState.savingId,
            isLocked = true,
            isSpecialCase = true
        )

        repository.createWithdrawTransaction(
            savingId = withdrawState.savingId,
            transactionState = transactionState
        )
        return DatabaseResultState.CreateWithdrawTransactionSuccess
    }
}