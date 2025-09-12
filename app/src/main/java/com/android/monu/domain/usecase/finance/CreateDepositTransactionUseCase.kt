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
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(depositState: DepositContentState): DatabaseResultMessage {
        when {
            depositState.fixSaveId == null && depositState.saveId == 0L ->
                return DatabaseResultMessage.EmptyDepositSave
            depositState.date.isEmpty() -> return DatabaseResultMessage.EmptyDepositDate
            depositState.amount == 0L -> return DatabaseResultMessage.EmptyDepositAmount
            depositState.accountId == 0 -> return DatabaseResultMessage.EmptyDepositAccount
        }

        val accountBalance = accountRepository.getAccountBalance(depositState.accountId)
        if (accountBalance == null || accountBalance < depositState.amount) {
            return DatabaseResultMessage.InsufficientAccountBalance
        }

        val saveId = depositState.fixSaveId ?: depositState.saveId
        val saveName = depositState.fixSaveName ?: depositState.saveName
        val (month, year) = DateHelper.getMonthAndYear(depositState.date)
        val transaction = Transaction(
            title = "Penambahan saldo ke tabungan $saveName",
            type = TransactionType.TRANSFER,
            parentCategory = TransactionParentCategory.TRANSFER,
            childCategory = TransactionChildCategory.SAVINGS_IN,
            date = depositState.date,
            month = month,
            year = year,
            timeStamp = System.currentTimeMillis(),
            amount = depositState.amount,
            sourceId = depositState.accountId,
            sourceName = depositState.accountName,
            destinationId = saveId.toInt(),
            destinationName = saveName,
            saveId = saveId,
            isLocked = true
        )

        financeRepository.createDepositTransaction(saveId, transaction)
        return DatabaseResultMessage.CreateDepositTransactionSuccess
    }
}