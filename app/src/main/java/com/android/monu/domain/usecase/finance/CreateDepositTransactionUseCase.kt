package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.ui.feature.screen.saving.deposit.components.DepositContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.DateHelper
import com.android.monu.ui.feature.utils.TransactionChildCategory
import com.android.monu.ui.feature.utils.TransactionParentCategory
import com.android.monu.ui.feature.utils.TransactionType

class CreateDepositTransactionUseCase(
    private val financeRepository: FinanceRepository,
    private val accountRepository: AccountRepository,
) {
    suspend operator fun invoke(depositState: DepositContentState): DatabaseResultMessage {
        when {
            depositState.fixDestinationId == null && depositState.destinationId == 0L ->
                return DatabaseResultMessage.EmptyAddSaveDestination
            depositState.date.isEmpty() -> return DatabaseResultMessage.EmptyAddSaveDate
            depositState.amount == 0L -> return DatabaseResultMessage.EmptyAddSaveAmount
            depositState.sourceId == 0 -> return DatabaseResultMessage.EmptyAddSaveSource
        }

        val accountBalance = accountRepository.getAccountBalance(depositState.sourceId)
        if (accountBalance == null || accountBalance < depositState.amount) {
            return DatabaseResultMessage.InsufficientAccountBalance
        }

        val destinationId = depositState.fixDestinationId ?: depositState.destinationId
        val destinationName = depositState.fixDestinationName ?: depositState.destinationName
        val (month, year) = DateHelper.getMonthAndYear(depositState.date)
        val transaction = Transaction(
            title = "Penambahan saldo ke tabungan $destinationName",
            type = TransactionType.TRANSFER,
            parentCategory = TransactionParentCategory.TRANSFER,
            childCategory = TransactionChildCategory.SAVINGS_IN,
            date = depositState.date,
            month = month,
            year = year,
            timeStamp = System.currentTimeMillis(),
            amount = depositState.amount,
            sourceId = depositState.sourceId,
            sourceName = depositState.sourceName,
            destinationId = destinationId.toInt(),
            destinationName = destinationName,
            saveId = destinationId,
            isLocked = true
        )

        financeRepository.createDepositTransaction(destinationId, transaction)
        return DatabaseResultMessage.AddSaveAmountSuccess
    }
}