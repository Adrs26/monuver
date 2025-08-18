package com.android.monu.di

import com.android.monu.domain.usecase.account.GetAllAccountsUseCase
import com.android.monu.domain.usecase.account.GetTotalAccountBalanceUseCase
import com.android.monu.domain.usecase.budget.CreateBudgetUseCase
import com.android.monu.domain.usecase.budget.DeleteBudgetUseCase
import com.android.monu.domain.usecase.budget.GetAllActiveBudgetsUseCase
import com.android.monu.domain.usecase.budget.GetAllInactiveBudgetsUseCase
import com.android.monu.domain.usecase.budget.GetBudgetByIdUseCase
import com.android.monu.domain.usecase.budget.GetBudgetSummaryUseCase
import com.android.monu.domain.usecase.budget.HandleExpiredBudgetUseCase
import com.android.monu.domain.usecase.budget.UpdateBudgetUseCase
import com.android.monu.domain.usecase.finance.CreateAccountUseCase
import com.android.monu.domain.usecase.finance.CreateExpenseTransactionUseCase
import com.android.monu.domain.usecase.finance.CreateIncomeTransactionUseCase
import com.android.monu.domain.usecase.finance.CreateTransferTransactionUseCase
import com.android.monu.domain.usecase.finance.DeleteExpenseTransactionUseCase
import com.android.monu.domain.usecase.finance.DeleteIncomeTransactionUseCase
import com.android.monu.domain.usecase.finance.DeleteTransferTransactionUseCase
import com.android.monu.domain.usecase.finance.UpdateExpenseTransactionUseCase
import com.android.monu.domain.usecase.finance.UpdateIncomeTransactionUseCase
import com.android.monu.domain.usecase.finance.UpdateTransferTransactionUseCase
import com.android.monu.domain.usecase.transaction.GetAllTransactionsByCategoryUseCase
import com.android.monu.domain.usecase.transaction.GetAllTransactionsUseCase
import com.android.monu.domain.usecase.transaction.GetDistinctTransactionYearsUseCase
import com.android.monu.domain.usecase.transaction.GetRecentTransactionsUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionBalanceSummaryUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionByIdUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionCategorySummaryUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionSummaryUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionsByCategoryAndDateRangeUseCase
import org.koin.dsl.module

val domainModule = module {

    factory { GetAllAccountsUseCase(get()) }
    factory { GetTotalAccountBalanceUseCase(get()) }

    factory { GetRecentTransactionsUseCase(get()) }
    factory { GetAllTransactionsUseCase(get()) }
    factory { GetAllTransactionsByCategoryUseCase(get()) }
    factory { GetTransactionByIdUseCase(get()) }
    factory { GetTransactionsByCategoryAndDateRangeUseCase(get()) }
    factory { GetDistinctTransactionYearsUseCase(get()) }
    factory { GetTransactionBalanceSummaryUseCase(get()) }
    factory { GetTransactionCategorySummaryUseCase(get()) }
    factory { GetTransactionSummaryUseCase(get()) }

    factory { CreateAccountUseCase(get()) }
    factory { CreateIncomeTransactionUseCase(get()) }
    factory { CreateExpenseTransactionUseCase(get(), get(), get()) }
    factory { CreateTransferTransactionUseCase(get(), get()) }
    factory { DeleteIncomeTransactionUseCase(get()) }
    factory { DeleteExpenseTransactionUseCase(get()) }
    factory { DeleteTransferTransactionUseCase(get()) }
    factory { UpdateIncomeTransactionUseCase(get()) }
    factory { UpdateExpenseTransactionUseCase(get(), get(), get()) }
    factory { UpdateTransferTransactionUseCase(get(), get()) }

    factory { CreateBudgetUseCase(get(), get()) }
    factory { DeleteBudgetUseCase(get()) }
    factory { GetAllActiveBudgetsUseCase(get()) }
    factory { GetAllInactiveBudgetsUseCase(get()) }
    factory { GetBudgetByIdUseCase(get()) }
    factory { GetBudgetSummaryUseCase(get()) }
    factory { HandleExpiredBudgetUseCase(get()) }
    factory { UpdateBudgetUseCase(get(), get()) }
}