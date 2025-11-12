package com.android.monuver.di

import com.android.monuver.domain.common.AppDispatchers
import com.android.monuver.domain.common.CoroutineDispatchers
import com.android.monuver.domain.usecase.account.GetAccountByIdUseCase
import com.android.monuver.domain.usecase.account.GetActiveAccountBalanceUseCase
import com.android.monuver.domain.usecase.account.GetActiveAccountsUseCase
import com.android.monuver.domain.usecase.account.GetAllAccountsUseCase
import com.android.monuver.domain.usecase.account.GetTotalAccountBalanceUseCase
import com.android.monuver.domain.usecase.bill.CreateBillUseCase
import com.android.monuver.domain.usecase.bill.DeleteBillUseCase
import com.android.monuver.domain.usecase.bill.GetBillByIdUseCase
import com.android.monuver.domain.usecase.bill.GetDueBillsUseCase
import com.android.monuver.domain.usecase.bill.GetPaidBillsUseCase
import com.android.monuver.domain.usecase.bill.GetPendingBillsUseCase
import com.android.monuver.domain.usecase.bill.GetUnpaidBillsUseCase
import com.android.monuver.domain.usecase.bill.UpdateBillUseCase
import com.android.monuver.domain.usecase.budget.CreateBudgetUseCase
import com.android.monuver.domain.usecase.budget.DeleteBudgetUseCase
import com.android.monuver.domain.usecase.budget.GetAllActiveBudgetsUseCase
import com.android.monuver.domain.usecase.budget.GetAllInactiveBudgetsUseCase
import com.android.monuver.domain.usecase.budget.GetBudgetByIdUseCase
import com.android.monuver.domain.usecase.budget.GetBudgetSummaryUseCase
import com.android.monuver.domain.usecase.budget.HandleExpiredBudgetUseCase
import com.android.monuver.domain.usecase.budget.UpdateBudgetUseCase
import com.android.monuver.domain.usecase.finance.BackupDataUseCase
import com.android.monuver.domain.usecase.finance.CancelBillPaymentUseCase
import com.android.monuver.domain.usecase.finance.CheckAppVersionUseCase
import com.android.monuver.domain.usecase.finance.CompleteSavingUseCase
import com.android.monuver.domain.usecase.finance.CreateAccountUseCase
import com.android.monuver.domain.usecase.finance.CreateDepositTransactionUseCase
import com.android.monuver.domain.usecase.finance.CreateExpenseTransactionUseCase
import com.android.monuver.domain.usecase.finance.CreateIncomeTransactionUseCase
import com.android.monuver.domain.usecase.finance.CreateTransferTransactionUseCase
import com.android.monuver.domain.usecase.finance.CreateWithdrawTransactionUseCase
import com.android.monuver.domain.usecase.finance.DeleteAllDataUseCase
import com.android.monuver.domain.usecase.finance.DeleteExpenseTransactionUseCase
import com.android.monuver.domain.usecase.finance.DeleteIncomeTransactionUseCase
import com.android.monuver.domain.usecase.finance.DeleteSavingUseCase
import com.android.monuver.domain.usecase.finance.ExportDataToPdfUseCase
import com.android.monuver.domain.usecase.finance.ProcessBillPaymentUseCase
import com.android.monuver.domain.usecase.finance.RestoreDataUseCase
import com.android.monuver.domain.usecase.finance.UpdateAccountStatusUseCase
import com.android.monuver.domain.usecase.finance.UpdateAccountUseCase
import com.android.monuver.domain.usecase.finance.UpdateExpenseTransactionUseCase
import com.android.monuver.domain.usecase.finance.UpdateIncomeTransactionUseCase
import com.android.monuver.domain.usecase.finance.UpdateSavingUseCase
import com.android.monuver.domain.usecase.saving.CreateSavingUseCase
import com.android.monuver.domain.usecase.saving.GetAllActiveSavingsUseCase
import com.android.monuver.domain.usecase.saving.GetAllInactiveSavingsUseCase
import com.android.monuver.domain.usecase.saving.GetSavingByIdUseCase
import com.android.monuver.domain.usecase.saving.GetTotalSavingCurrentAmountUseCase
import com.android.monuver.domain.usecase.transaction.GetAllTransactionsByCategoryUseCase
import com.android.monuver.domain.usecase.transaction.GetAllTransactionsBySavingIdUseCase
import com.android.monuver.domain.usecase.transaction.GetAllTransactionsUseCase
import com.android.monuver.domain.usecase.transaction.GetDistinctTransactionYearsUseCase
import com.android.monuver.domain.usecase.transaction.GetRecentTransactionsUseCase
import com.android.monuver.domain.usecase.transaction.GetTransactionBalanceSummaryUseCase
import com.android.monuver.domain.usecase.transaction.GetTransactionByIdUseCase
import com.android.monuver.domain.usecase.transaction.GetTransactionCategorySummaryUseCase
import com.android.monuver.domain.usecase.transaction.GetTransactionSummaryUseCase
import com.android.monuver.domain.usecase.transaction.GetTransactionsByCategoryAndDateRangeUseCase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::AppDispatchers) { bind<CoroutineDispatchers>() }

    factoryOf(::GetAccountByIdUseCase)
    factoryOf(::GetActiveAccountBalanceUseCase)
    factoryOf(::GetActiveAccountsUseCase)
    factoryOf(::GetAllAccountsUseCase)
    factoryOf(::GetTotalAccountBalanceUseCase)

    factoryOf(::GetRecentTransactionsUseCase)
    factoryOf(::GetAllTransactionsUseCase)
    factoryOf(::GetAllTransactionsByCategoryUseCase)
    factoryOf(::GetAllTransactionsBySavingIdUseCase)
    factoryOf(::GetTransactionByIdUseCase)
    factoryOf(::GetTransactionsByCategoryAndDateRangeUseCase)
    factoryOf(::GetDistinctTransactionYearsUseCase)
    factoryOf(::GetTransactionBalanceSummaryUseCase)
    factoryOf(::GetTransactionCategorySummaryUseCase)
    factoryOf(::GetTransactionSummaryUseCase)

    factoryOf(::BackupDataUseCase)
    factoryOf(::CancelBillPaymentUseCase)
    factoryOf(::CheckAppVersionUseCase)
    factoryOf(::CompleteSavingUseCase)
    factoryOf(::CreateAccountUseCase)
    factoryOf(::CreateIncomeTransactionUseCase)
    factoryOf(::CreateExpenseTransactionUseCase)
    factoryOf(::CreateTransferTransactionUseCase)
    factoryOf(::CreateDepositTransactionUseCase)
    factoryOf(::CreateWithdrawTransactionUseCase)
    factoryOf(::DeleteAllDataUseCase)
    factoryOf(::DeleteIncomeTransactionUseCase)
    factoryOf(::DeleteExpenseTransactionUseCase)
    factoryOf(::DeleteSavingUseCase)
    factoryOf(::ExportDataToPdfUseCase)
    factoryOf(::ProcessBillPaymentUseCase)
    factoryOf(::RestoreDataUseCase)
    factoryOf(::UpdateAccountStatusUseCase)
    factoryOf(::UpdateAccountUseCase)
    factoryOf(::UpdateIncomeTransactionUseCase)
    factoryOf(::UpdateExpenseTransactionUseCase)
    factoryOf(::UpdateSavingUseCase)

    factoryOf(::CreateBudgetUseCase)
    factoryOf(::DeleteBudgetUseCase)
    factoryOf(::GetAllActiveBudgetsUseCase)
    factoryOf(::GetAllInactiveBudgetsUseCase)
    factoryOf(::GetBudgetByIdUseCase)
    factoryOf(::GetBudgetSummaryUseCase)
    factoryOf(::HandleExpiredBudgetUseCase)
    factoryOf(::UpdateBudgetUseCase)

    factoryOf(::CreateBillUseCase)
    factoryOf(::DeleteBillUseCase)
    factoryOf(::GetBillByIdUseCase)
    factoryOf(::GetPendingBillsUseCase)
    factoryOf(::GetDueBillsUseCase)
    factoryOf(::GetPaidBillsUseCase)
    factoryOf(::GetUnpaidBillsUseCase)
    factoryOf(::UpdateBillUseCase)

    factoryOf(::CreateSavingUseCase)
    factoryOf(::GetAllActiveSavingsUseCase)
    factoryOf(::GetAllInactiveSavingsUseCase)
    factoryOf(::GetSavingByIdUseCase)
    factoryOf(::GetTotalSavingCurrentAmountUseCase)
}