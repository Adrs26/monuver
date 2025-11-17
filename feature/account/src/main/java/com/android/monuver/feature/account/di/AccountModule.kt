package com.android.monuver.feature.account.di

import com.android.monuver.feature.account.data.repository.AccountRepositoryImpl
import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.feature.account.domain.usecase.CreateAccountUseCase
import com.android.monuver.feature.account.domain.usecase.GetAccountByIdUseCase
import com.android.monuver.feature.account.domain.usecase.GetAllAccountsUseCase
import com.android.monuver.feature.account.domain.usecase.GetTotalAccountBalanceUseCase
import com.android.monuver.feature.account.domain.usecase.UpdateAccountStatusUseCase
import com.android.monuver.feature.account.domain.usecase.UpdateAccountUseCase
import com.android.monuver.feature.account.presentation.AccountViewModel
import com.android.monuver.feature.account.presentation.accountDetail.AccountDetailViewModel
import com.android.monuver.feature.account.presentation.addAccount.AddAccountViewModel
import com.android.monuver.feature.account.presentation.editAccount.EditAccountViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val accountModule = module {
    singleOf(::AccountRepositoryImpl) { bind<AccountRepository>()}

    factoryOf(::CreateAccountUseCase)
    factoryOf(::GetAccountByIdUseCase)
    factoryOf(::GetAllAccountsUseCase)
    factoryOf(::GetTotalAccountBalanceUseCase)
    factoryOf(::UpdateAccountStatusUseCase)
    factoryOf(::UpdateAccountUseCase)

    viewModelOf(::AccountViewModel)
    viewModelOf(::AccountDetailViewModel)
    viewModelOf(::AddAccountViewModel)
    viewModelOf(::EditAccountViewModel)
}