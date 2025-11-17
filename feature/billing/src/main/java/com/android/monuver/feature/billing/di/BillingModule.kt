package com.android.monuver.feature.billing.di

import com.android.monuver.feature.billing.data.repository.BillRepositoryImpl
import com.android.monuver.feature.billing.domain.repository.BillRepository
import com.android.monuver.feature.billing.domain.usecase.CancelBillPaymentUseCase
import com.android.monuver.feature.billing.domain.usecase.CreateBillUseCase
import com.android.monuver.feature.billing.domain.usecase.DeleteBillUseCase
import com.android.monuver.feature.billing.domain.usecase.GetBillByIdUseCase
import com.android.monuver.feature.billing.domain.usecase.GetDueBillsUseCase
import com.android.monuver.feature.billing.domain.usecase.GetPaidBillsUseCase
import com.android.monuver.feature.billing.domain.usecase.GetPendingBillsUseCase
import com.android.monuver.feature.billing.domain.usecase.ProcessBillPaymentUseCase
import com.android.monuver.feature.billing.domain.usecase.UpdateBillUseCase
import com.android.monuver.feature.billing.presentation.BillingViewModel
import com.android.monuver.feature.billing.presentation.addBill.AddBillViewModel
import com.android.monuver.feature.billing.presentation.billDetail.BillDetailViewModel
import com.android.monuver.feature.billing.presentation.editBill.EditBillViewModel
import com.android.monuver.feature.billing.presentation.payBill.PayBillViewModel
import com.android.monuver.feature.billing.worker.ReminderWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val billingModule = module {
    singleOf(::BillRepositoryImpl) { bind<BillRepository>() }

    factoryOf(::CancelBillPaymentUseCase)
    factoryOf(::CreateBillUseCase)
    factoryOf(::DeleteBillUseCase)
    factoryOf(::GetBillByIdUseCase)
    factoryOf(::GetPendingBillsUseCase)
    factoryOf(::GetDueBillsUseCase)
    factoryOf(::GetPaidBillsUseCase)
    factoryOf(::ProcessBillPaymentUseCase)
    factoryOf(::UpdateBillUseCase)

    viewModelOf(::BillingViewModel)
    viewModelOf(::AddBillViewModel)
    viewModelOf(::BillDetailViewModel)
    viewModelOf(::EditBillViewModel)
    viewModelOf(::PayBillViewModel)

    workerOf(::ReminderWorker)
}