package com.android.monuver.feature.account.domain.usecase

import com.android.monuver.feature.account.domain.model.AddAccountState
import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.TransactionChildCategory
import com.android.monuver.core.domain.util.TransactionParentCategory
import com.android.monuver.core.domain.util.TransactionType
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class CreateAccountUseCase(
    private val repository: AccountRepository
) {
    @OptIn(ExperimentalTime::class)
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

        val currentDate = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date.toString()
        val (month, year) = DateHelper.getMonthAndYear(currentDate)

        val transactionState = TransactionState(
            title = "Penambahan Akun",
            type = TransactionType.INCOME,
            parentCategory = TransactionParentCategory.OTHER_INCOME,
            childCategory = TransactionChildCategory.OTHER_INCOME,
            date = currentDate,
            month = month,
            year = year,
            timeStamp = Clock.System.now().toEpochMilliseconds(),
            amount = accountState.balance,
            sourceId = 0,
            sourceName = accountState.name,
            isLocked = true,
            isSpecialCase = true
        )

        repository.createAccount(
            account = account,
            transactionState = transactionState
        )
        return DatabaseResultState.CreateAccountSuccess
    }
}