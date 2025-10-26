package com.android.monu.di

import com.android.monu.data.datastore.UserPreference
import com.android.monu.data.manager.DataManagerImpl
import com.android.monu.data.repository.AccountRepositoryImpl
import com.android.monu.data.repository.BillRepositoryImpl
import com.android.monu.data.repository.BudgetRepositoryImpl
import com.android.monu.data.repository.FinanceRepositoryImpl
import com.android.monu.data.repository.SavingRepositoryImpl
import com.android.monu.data.repository.TransactionRepositoryImpl
import com.android.monu.domain.manager.DataManager
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.BillRepository
import com.android.monu.domain.repository.BudgetRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.domain.repository.SavingRepository
import com.android.monu.domain.repository.TransactionRepository
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

    singleOf(::UserPreference)
}