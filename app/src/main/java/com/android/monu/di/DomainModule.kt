package com.android.monu.di

import com.android.monu.domain.usecase.account.GetAllAccountsUseCase
import com.android.monu.domain.usecase.account.GetTotalAccountBalanceUseCase
import com.android.monu.domain.usecase.bill.CreateBillUseCase
import com.android.monu.domain.usecase.bill.DeleteBillUseCase
import com.android.monu.domain.usecase.bill.GetBillByIdUseCase
import com.android.monu.domain.usecase.bill.GetDueBillsUseCase
import com.android.monu.domain.usecase.bill.GetPaidBillsUseCase
import com.android.monu.domain.usecase.bill.GetPendingBillsUseCase
import com.android.monu.domain.usecase.bill.UpdateBillUseCase
import com.android.monu.domain.usecase.budget.CreateBudgetUseCase
import com.android.monu.domain.usecase.budget.DeleteBudgetUseCase
import com.android.monu.domain.usecase.budget.GetAllActiveBudgetsUseCase
import com.android.monu.domain.usecase.budget.GetAllInactiveBudgetsUseCase
import com.android.monu.domain.usecase.budget.GetBudgetByIdUseCase
import com.android.monu.domain.usecase.budget.GetBudgetSummaryUseCase
import com.android.monu.domain.usecase.budget.HandleExpiredBudgetUseCase
import com.android.monu.domain.usecase.budget.UpdateBudgetUseCase
import com.android.monu.domain.usecase.finance.CreateAccountUseCase
import com.android.monu.domain.usecase.finance.CreateDepositTransactionUseCase
import com.android.monu.domain.usecase.finance.CreateExpenseTransactionUseCase
import com.android.monu.domain.usecase.finance.CreateIncomeTransactionUseCase
import com.android.monu.domain.usecase.finance.CreateTransferTransactionUseCase
import com.android.monu.domain.usecase.finance.CreateWithdrawTransactionUseCase
import com.android.monu.domain.usecase.finance.DeleteExpenseTransactionUseCase
import com.android.monu.domain.usecase.finance.DeleteIncomeTransactionUseCase
import com.android.monu.domain.usecase.finance.DeleteSaveUseCase
import com.android.monu.domain.usecase.finance.PayBillUseCase
import com.android.monu.domain.usecase.finance.UpdateExpenseTransactionUseCase
import com.android.monu.domain.usecase.finance.UpdateIncomeTransactionUseCase
import com.android.monu.domain.usecase.finance.UpdateSaveUseCase
import com.android.monu.domain.usecase.save.CreateSaveUseCase
import com.android.monu.domain.usecase.save.GetAllSavesUseCase
import com.android.monu.domain.usecase.save.GetSaveByIdUseCase
import com.android.monu.domain.usecase.save.GetTotalSaveCurrentAmountUseCase
import com.android.monu.domain.usecase.transaction.GetAllTransactionsByCategoryUseCase
import com.android.monu.domain.usecase.transaction.GetAllTransactionsBySaveIdUseCase
import com.android.monu.domain.usecase.transaction.GetAllTransactionsUseCase
import com.android.monu.domain.usecase.transaction.GetDistinctTransactionYearsUseCase
import com.android.monu.domain.usecase.transaction.GetRecentTransactionsUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionBalanceSummaryUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionByIdUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionCategorySummaryUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionSummaryUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionsByCategoryAndDateRangeUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::GetAllAccountsUseCase)
    factoryOf(::GetTotalAccountBalanceUseCase)

    factoryOf(::GetRecentTransactionsUseCase)
    factoryOf(::GetAllTransactionsUseCase)
    factoryOf(::GetAllTransactionsByCategoryUseCase)
    factoryOf(::GetAllTransactionsBySaveIdUseCase)
    factoryOf(::GetTransactionByIdUseCase)
    factoryOf(::GetTransactionsByCategoryAndDateRangeUseCase)
    factoryOf(::GetDistinctTransactionYearsUseCase)
    factoryOf(::GetTransactionBalanceSummaryUseCase)
    factoryOf(::GetTransactionCategorySummaryUseCase)
    factoryOf(::GetTransactionSummaryUseCase)

    factoryOf(::CreateAccountUseCase)
    factoryOf(::CreateIncomeTransactionUseCase)
    factoryOf(::CreateExpenseTransactionUseCase)
    factoryOf(::CreateTransferTransactionUseCase)
    factoryOf(::CreateDepositTransactionUseCase)
    factoryOf(::CreateWithdrawTransactionUseCase)
    factoryOf(::DeleteIncomeTransactionUseCase)
    factoryOf(::DeleteExpenseTransactionUseCase)
    factoryOf(::DeleteSaveUseCase)
    factoryOf(::PayBillUseCase)
    factoryOf(::UpdateIncomeTransactionUseCase)
    factoryOf(::UpdateExpenseTransactionUseCase)

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
    factoryOf(::UpdateBillUseCase)

    factoryOf(::CreateSaveUseCase)
    factoryOf(::GetAllSavesUseCase)
    factoryOf(::GetSaveByIdUseCase)
    factoryOf(::GetTotalSaveCurrentAmountUseCase)
    factoryOf(::UpdateSaveUseCase)
}