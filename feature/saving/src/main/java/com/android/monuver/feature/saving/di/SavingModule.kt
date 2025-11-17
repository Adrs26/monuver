package com.android.monuver.feature.saving.di

import com.android.monuver.feature.saving.data.repository.SavingRepositoryImpl
import com.android.monuver.feature.saving.domain.repository.SavingRepository
import com.android.monuver.feature.saving.domain.usecase.CompleteSavingUseCase
import com.android.monuver.feature.saving.domain.usecase.CreateDepositTransactionUseCase
import com.android.monuver.feature.saving.domain.usecase.CreateSavingUseCase
import com.android.monuver.feature.saving.domain.usecase.CreateWithdrawTransactionUseCase
import com.android.monuver.feature.saving.domain.usecase.DeleteSavingUseCase
import com.android.monuver.feature.saving.domain.usecase.GetAllActiveSavingsUseCase
import com.android.monuver.feature.saving.domain.usecase.GetAllInactiveSavingsUseCase
import com.android.monuver.feature.saving.domain.usecase.GetAllTransactionsBySavingIdUseCase
import com.android.monuver.feature.saving.domain.usecase.GetSavingByIdUseCase
import com.android.monuver.feature.saving.domain.usecase.GetTotalSavingCurrentAmountUseCase
import com.android.monuver.feature.saving.domain.usecase.UpdateSavingUseCase
import com.android.monuver.feature.saving.presentation.SavingViewModel
import com.android.monuver.feature.saving.presentation.addSaving.AddSavingViewModel
import com.android.monuver.feature.saving.presentation.deposit.DepositViewModel
import com.android.monuver.feature.saving.presentation.editSaving.EditSavingViewModel
import com.android.monuver.feature.saving.presentation.inactiveSaving.InactiveSavingViewModel
import com.android.monuver.feature.saving.presentation.savingDetail.SavingDetailViewModel
import com.android.monuver.feature.saving.presentation.withdraw.WithdrawViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val savingModule = module {
    singleOf(::SavingRepositoryImpl) { bind<SavingRepository>() }

    factoryOf(::CompleteSavingUseCase)
    factoryOf(::CreateDepositTransactionUseCase)
    factoryOf(::CreateSavingUseCase)
    factoryOf(::CreateWithdrawTransactionUseCase)
    factoryOf(::DeleteSavingUseCase)
    factoryOf(::GetAllActiveSavingsUseCase)
    factoryOf(::GetAllInactiveSavingsUseCase)
    factoryOf(::GetAllTransactionsBySavingIdUseCase)
    factoryOf(::GetSavingByIdUseCase)
    factoryOf(::GetTotalSavingCurrentAmountUseCase)
    factoryOf(::UpdateSavingUseCase)

    viewModelOf(::SavingViewModel)
    viewModelOf(::AddSavingViewModel)
    viewModelOf(::DepositViewModel)
    viewModelOf(::EditSavingViewModel)
    viewModelOf(::InactiveSavingViewModel)
    viewModelOf(::SavingDetailViewModel)
    viewModelOf(::WithdrawViewModel)
}