package com.android.monu.di

import com.android.monu.domain.usecase.DeleteTransactionByIdUseCase
import com.android.monu.domain.usecase.GetAllTransactionsUseCase
import com.android.monu.domain.usecase.GetAvailableTransactionYearsUseCase
import com.android.monu.domain.usecase.GetAverageTransactionAmountUseCase
import com.android.monu.domain.usecase.GetMonthlyTransactionOverviewsUseCase
import com.android.monu.domain.usecase.GetMostExpenseTransactionCategoryAmountsByYear
import com.android.monu.domain.usecase.GetRecentTransactionsUseCase
import com.android.monu.domain.usecase.GetTotalTransactionAmountUseCase
import com.android.monu.domain.usecase.GetTransactionByIdUseCase
import com.android.monu.domain.usecase.GetTransactionMonthlyAmountsUseCase
import com.android.monu.domain.usecase.InsertTransactionUseCase
import com.android.monu.domain.usecase.UpdateTransactionUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetTotalTransactionAmountUseCase(get()) }
    factory { GetRecentTransactionsUseCase(get()) }
    factory { GetAllTransactionsUseCase(get()) }
    factory { GetTransactionByIdUseCase(get()) }
    factory { GetAvailableTransactionYearsUseCase(get()) }
    factory { GetTransactionMonthlyAmountsUseCase(get()) }
    factory { GetAverageTransactionAmountUseCase(get()) }
    factory { GetMonthlyTransactionOverviewsUseCase(get()) }
    factory { GetMostExpenseTransactionCategoryAmountsByYear(get()) }
    factory { InsertTransactionUseCase(get()) }
    factory { UpdateTransactionUseCase(get()) }
    factory { DeleteTransactionByIdUseCase(get()) }
}