package com.android.monu.di

import com.android.monu.data.repository.AccountRepositoryImpl
import com.android.monu.data.repository.BudgetingRepositoryImpl
import com.android.monu.data.repository.FinanceRepositoryImpl
import com.android.monu.data.repository.TransactionRepositoryImpl
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.BudgetingRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.domain.repository.TransactionRepository
import org.koin.dsl.module

val dataModule = module {
    single<TransactionRepository> { TransactionRepositoryImpl(get()) }
    single<AccountRepository> { AccountRepositoryImpl(get()) }
    single<FinanceRepository> { FinanceRepositoryImpl(get(), get(), get(), get()) }
    single<BudgetingRepository> { BudgetingRepositoryImpl(get()) }
}