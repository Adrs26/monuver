package com.android.monu.di

import com.android.monu.domain.usecase.account.GetAllAccountsUseCase
import com.android.monu.domain.usecase.account.GetTotalAccountBalanceUseCase
import com.android.monu.domain.usecase.finance.CreateAccountWithInitialTransactionUseCase
import com.android.monu.domain.usecase.finance.CreateTransactionAndAdjustAccountBalanceUseCase
import com.android.monu.domain.usecase.finance.DeleteTransactionAndAdjustAccountBalanceUseCase
import com.android.monu.domain.usecase.transaction.GetAllTransactionsUseCase
import com.android.monu.domain.usecase.transaction.GetDistinctTransactionYearsUseCase
import com.android.monu.domain.usecase.transaction.GetGroupedMonthlyTransactionAmountByParentCategoryUseCase
import com.android.monu.domain.usecase.transaction.GetRecentTransactionsUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionMonthlyAmountOverviewUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionByIdUseCase
import com.android.monu.domain.usecase.transaction.GetWeeklyTransactionSummaryByDateRangeUseCase
import com.android.monu.domain.usecase.transaction.UpdateTransactionUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetRecentTransactionsUseCase(get()) }
    factory { GetAllTransactionsUseCase(get()) }
    factory { GetTransactionByIdUseCase(get()) }
    factory { GetDistinctTransactionYearsUseCase(get()) }
    factory { GetTransactionMonthlyAmountOverviewUseCase(get()) }
    factory { GetGroupedMonthlyTransactionAmountByParentCategoryUseCase(get()) }
    factory { GetWeeklyTransactionSummaryByDateRangeUseCase(get()) }
    factory { UpdateTransactionUseCase(get()) }

    factory { GetAllAccountsUseCase(get()) }
    factory { GetTotalAccountBalanceUseCase(get()) }

    factory { CreateAccountWithInitialTransactionUseCase(get()) }
    factory { CreateTransactionAndAdjustAccountBalanceUseCase(get()) }
    factory { DeleteTransactionAndAdjustAccountBalanceUseCase(get()) }
}