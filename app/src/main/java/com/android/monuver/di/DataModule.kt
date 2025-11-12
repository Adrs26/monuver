package com.android.monuver.di

import com.android.monuver.data.datastore.UserPreferences
import com.android.monuver.data.manager.DataManagerImpl
import com.android.monuver.data.repository.AccountRepositoryImpl
import com.android.monuver.data.repository.BillRepositoryImpl
import com.android.monuver.data.repository.BudgetRepositoryImpl
import com.android.monuver.data.repository.FinanceRepositoryImpl
import com.android.monuver.data.repository.SavingRepositoryImpl
import com.android.monuver.data.repository.TransactionRepositoryImpl
import com.android.monuver.domain.manager.DataManager
import com.android.monuver.domain.repository.AccountRepository
import com.android.monuver.domain.repository.BillRepository
import com.android.monuver.domain.repository.BudgetRepository
import com.android.monuver.domain.repository.FinanceRepository
import com.android.monuver.domain.repository.SavingRepository
import com.android.monuver.domain.repository.TransactionRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::TransactionRepositoryImpl){ bind<TransactionRepository>()}
    singleOf(::AccountRepositoryImpl){ bind<AccountRepository>()}
    singleOf(::FinanceRepositoryImpl){ bind<FinanceRepository>()}
    singleOf(::BudgetRepositoryImpl){ bind<BudgetRepository>()}
    singleOf(::BillRepositoryImpl){ bind<BillRepository>()}
    singleOf(::SavingRepositoryImpl){ bind<SavingRepository>() }
    singleOf(::DataManagerImpl){ bind<DataManager>() }

    singleOf(::UserPreferences)
}