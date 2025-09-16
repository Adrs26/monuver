package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.account.Account
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.ui.feature.screen.account.addAccount.components.AddAccountContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.DateHelper
import com.android.monu.ui.feature.utils.TransactionChildCategory
import com.android.monu.ui.feature.utils.TransactionParentCategory
import com.android.monu.ui.feature.utils.TransactionType
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
            balance = accountState.balance,
            isActive = true
        )

        val currentDate = LocalDate.now()
        val isoDate = currentDate.format(DateTimeFormatter.ISO_DATE)
        val (month, year) = DateHelper.getMonthAndYear(isoDate)

        val transaction = Transaction(
            title = "Penambahan Akun",
            type = TransactionType.INCOME,
            parentCategory = TransactionParentCategory.OTHER_INCOME,
            childCategory = TransactionChildCategory.OTHER_INCOME,
            date = isoDate,
            month = month,
            year = year,
            timeStamp = System.currentTimeMillis(),
            amount = accountState.balance,
            sourceId = 0,
            sourceName = accountState.name,
            isLocked = true,
            isSpecialCase = true
        )

        repository.createAccount(account, transaction)
        return DatabaseResultMessage.CreateAccountSuccess
    }
}