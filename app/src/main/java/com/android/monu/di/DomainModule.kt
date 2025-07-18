package com.android.monu.di

import com.android.monu.domain.usecase.account.GetAllAccountsUseCase
import com.android.monu.domain.usecase.account.GetTotalAccountBalanceUseCase
import com.android.monu.domain.usecase.finance.CreateAccountWithInitialTransactionUseCase
import com.android.monu.domain.usecase.finance.CreateTransactionAndAdjustAccountBalanceUseCase
import com.android.monu.domain.usecase.finance.DeleteTransactionAndAdjustAccountBalanceUseCase
import com.android.monu.domain.usecase.transaction.GetAllTransactionsUseCase
import com.android.monu.domain.usecase.transaction.GetAvailableTransactionYearsUseCase
import com.android.monu.domain.usecase.transaction.GetMonthlyTransactionOverviewsUseCase
import com.android.monu.domain.usecase.transaction.GetRecentTransactionsUseCase
import com.android.monu.domain.usecase.transaction.GetTotalTransactionAmountUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionAmountOverviewUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionByIdUseCase
import com.android.monu.domain.usecase.transaction.UpdateTransactionUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetTotalTransactionAmountUseCase(get()) }
    factory { GetRecentTransactionsUseCase(get()) }
    factory { GetAllTransactionsUseCase(get()) }
    factory { GetTransactionByIdUseCase(get()) }
    factory { GetAvailableTransactionYearsUseCase(get()) }
    factory { GetTransactionAmountOverviewUseCase(get()) }
    factory { GetMonthlyTransactionOverviewsUseCase(get()) }
    factory { UpdateTransactionUseCase(get()) }

    factory { GetAllAccountsUseCase(get()) }
    factory { GetTotalAccountBalanceUseCase(get()) }

    factory { CreateAccountWithInitialTransactionUseCase(get()) }
    factory { CreateTransactionAndAdjustAccountBalanceUseCase(get()) }
    factory { DeleteTransactionAndAdjustAccountBalanceUseCase(get()) }
}