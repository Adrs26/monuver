package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.account.Account
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.presentation.screen.account.addaccount.components.AddAccountContentState
import com.android.monu.presentation.utils.DatabaseResultMessage
import com.android.monu.presentation.utils.DateHelper
import com.android.monu.presentation.utils.TransactionChildCategory
import com.android.monu.presentation.utils.TransactionParentCategory
import com.android.monu.presentation.utils.TransactionType
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class CreateAccountUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(accountState: AddAccountContentState): DatabaseResultMessage {
        when {
            accountState.name.isEmpty() -> return DatabaseResultMessage.EmptyAccountName
            accountState.type == 0 -> return DatabaseResultMessage.EmptyAccountType
            accountState.balance == 0L -> return DatabaseResultMessage.EmptyAccountBalance
        }

        val account = Account(
            name = accountState.name,
            type = accountState.type,
            balance = accountState.balance
        )

        val currentDate = LocalDate.now()
        val isoDate = currentDate.format(DateTimeFormatter.ISO_DATE)
        val (month, year) = DateHelper.getMonthAndYear(isoDate)

        val transaction = Transaction(
            title = "Penambahan akun baru ${accountState.name}",
            type = TransactionType.INCOME,
            parentCategory = TransactionParentCategory.OTHER_INCOME,
            childCategory = TransactionChildCategory.OTHER_INCOME,
            date = isoDate,
            month = month,
            year = year,
            timeStamp = System.currentTimeMillis(),
            amount = accountState.balance,
            sourceId = 0,
            sourceName = accountState.name
        )

        repository.createAccount(account = account, transaction = transaction)
        return DatabaseResultMessage.CreateAccountSuccess
    }
}