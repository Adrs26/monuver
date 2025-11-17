package com.android.monuver.feature.transaction.di

import com.android.monuver.feature.transaction.data.repository.TransactionRepositoryImpl
import com.android.monuver.feature.transaction.domain.repository.TransactionRepository
import com.android.monuver.feature.transaction.domain.usecase.CreateExpenseTransactionUseCase
import com.android.monuver.feature.transaction.domain.usecase.CreateIncomeTransactionUseCase
import com.android.monuver.feature.transaction.domain.usecase.CreateTransferTransactionUseCase
import com.android.monuver.feature.transaction.domain.usecase.DeleteExpenseTransactionUseCase
import com.android.monuver.feature.transaction.domain.usecase.DeleteIncomeTransactionUseCase
import com.android.monuver.feature.transaction.domain.usecase.GetAllTransactionsUseCase
import com.android.monuver.feature.transaction.domain.usecase.GetTransactionByIdUseCase
import com.android.monuver.feature.transaction.domain.usecase.UpdateExpenseTransactionUseCase
import com.android.monuver.feature.transaction.domain.usecase.UpdateIncomeTransactionUseCase
import com.android.monuver.feature.transaction.presentation.TransactionViewModel
import com.android.monuver.feature.transaction.presentation.addTransaction.AddTransactionViewModel
import com.android.monuver.feature.transaction.presentation.editTransaction.EditTransactionViewModel
import com.android.monuver.feature.transaction.presentation.transactionDetail.TransactionDetailViewModel
import com.android.monuver.feature.transaction.presentation.transfer.TransferViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val transactionModule = module {
    singleOf(::TransactionRepositoryImpl) { bind<TransactionRepository>()}

    factoryOf(::CreateExpenseTransactionUseCase)
    factoryOf(::CreateIncomeTransactionUseCase)
    factoryOf(::CreateTransferTransactionUseCase)
    factoryOf(::DeleteExpenseTransactionUseCase)
    factoryOf(::DeleteIncomeTransactionUseCase)
    factoryOf(::GetAllTransactionsUseCase)
    factoryOf(::GetTransactionByIdUseCase)
    factoryOf(::UpdateExpenseTransactionUseCase)
    factoryOf(::UpdateIncomeTransactionUseCase)

    viewModelOf(::TransactionViewModel)
    viewModelOf(::AddTransactionViewModel)
    viewModelOf(::EditTransactionViewModel)
    viewModelOf(::TransactionDetailViewModel)
    viewModelOf(::TransferViewModel)
}