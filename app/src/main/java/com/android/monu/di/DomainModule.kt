package com.android.monu.di

import com.android.monu.domain.usecase.DeleteTransactionByIdUseCase
import com.android.monu.domain.usecase.GetAllTransactionsUseCase
import com.android.monu.domain.usecase.GetAvailableTransactionYearsUseCase
import com.android.monu.domain.usecase.GetAverageTransactionAmountUseCase
import com.android.monu.domain.usecase.GetMonthlyTransactionOverviewUseCase
import com.android.monu.domain.usecase.GetMostExpenseTransactionCategoryAmountByYear
import com.android.monu.domain.usecase.GetRecentTransactionUseCase
import com.android.monu.domain.usecase.GetTotalTransactionAmountUseCase
import com.android.monu.domain.usecase.GetTransactionByIdUseCase
import com.android.monu.domain.usecase.GetTransactionMonthlyAmountUseCase
import com.android.monu.domain.usecase.InsertTransactionUseCase
import com.android.monu.domain.usecase.UpdateTransactionUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetTotalTransactionAmountUseCase(get()) }
    factory { GetRecentTransactionUseCase(get()) }
    factory { GetAllTransactionsUseCase(get()) }
    factory { GetTransactionByIdUseCase(get()) }
    factory { GetAvailableTransactionYearsUseCase(get()) }
    factory { GetTransactionMonthlyAmountUseCase(get()) }
    factory { GetAverageTransactionAmountUseCase(get()) }
    factory { GetMonthlyTransactionOverviewUseCase(get()) }
    factory { GetMostExpenseTransactionCategoryAmountByYear(get()) }
    factory { InsertTransactionUseCase(get()) }
    factory { UpdateTransactionUseCase(get()) }
    factory { DeleteTransactionByIdUseCase(get()) }
}