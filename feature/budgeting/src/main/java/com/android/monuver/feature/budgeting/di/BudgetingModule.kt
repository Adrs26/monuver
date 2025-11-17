package com.android.monuver.feature.budgeting.di

import com.android.monuver.feature.budgeting.data.repository.BudgetRepositoryImpl
import com.android.monuver.feature.budgeting.domain.repository.BudgetRepository
import com.android.monuver.feature.budgeting.domain.usecase.CreateBudgetUseCase
import com.android.monuver.feature.budgeting.domain.usecase.DeleteBudgetUseCase
import com.android.monuver.feature.budgeting.domain.usecase.GetAllActiveBudgetsUseCase
import com.android.monuver.feature.budgeting.domain.usecase.GetAllInactiveBudgetsUseCase
import com.android.monuver.feature.budgeting.domain.usecase.GetBudgetByIdUseCase
import com.android.monuver.feature.budgeting.domain.usecase.GetTransactionsByCategoryAndDateRangeUseCase
import com.android.monuver.feature.budgeting.domain.usecase.UpdateBudgetUseCase
import com.android.monuver.feature.budgeting.presentation.BudgetingViewModel
import com.android.monuver.feature.budgeting.presentation.addBudget.AddBudgetViewModel
import com.android.monuver.feature.budgeting.presentation.budgetDetail.BudgetDetailViewModel
import com.android.monuver.feature.budgeting.presentation.editBudget.EditBudgetViewModel
import com.android.monuver.feature.budgeting.presentation.inactiveBudget.InactiveBudgetViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val budgetingModule = module {
    singleOf(::BudgetRepositoryImpl) { bind<BudgetRepository>() }

    factoryOf(::CreateBudgetUseCase)
    factoryOf(::DeleteBudgetUseCase)
    factoryOf(::GetAllActiveBudgetsUseCase)
    factoryOf(::GetAllInactiveBudgetsUseCase)
    factoryOf(::GetBudgetByIdUseCase)
    factoryOf(::GetTransactionsByCategoryAndDateRangeUseCase)
    factoryOf(::UpdateBudgetUseCase)

    viewModelOf(::BudgetingViewModel)
    viewModelOf(::AddBudgetViewModel)
    viewModelOf(::BudgetDetailViewModel)
    viewModelOf(::EditBudgetViewModel)
    viewModelOf(::InactiveBudgetViewModel)
}