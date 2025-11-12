package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.AccountState
import com.android.monuver.domain.model.AddAccountState
import com.android.monuver.domain.model.TransactionState
import com.android.monuver.domain.repository.FinanceRepository
import com.android.monuver.utils.DateHelper
import com.android.monuver.utils.TransactionChildCategory
import com.android.monuver.utils.TransactionParentCategory
import com.android.monuver.utils.TransactionType
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class CreateAccountUseCase(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(accountState: AddAccountState): DatabaseResultState {
        when {
            accountState.name.isEmpty() -> return DatabaseResultState.EmptyAccountName
            accountState.type == 0 -> return DatabaseResultState.EmptyAccountType
            accountState.balance == 0L -> return DatabaseResultState.EmptyAccountBalance
        }

        val account = AccountState(
            name = accountState.name,
            type = accountState.type,
            balance = accountState.balance,
            isActive = true
        )

        val currentDate = LocalDate.now()
        val isoDate = currentDate.format(DateTimeFormatter.ISO_DATE)
        val (month, year) = DateHelper.getMonthAndYear(isoDate)

        val transactionState = TransactionState(
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

        repository.createAccount(account, transactionState)
        return DatabaseResultState.CreateAccountSuccess
    }
}