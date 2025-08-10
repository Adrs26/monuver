package com.android.monu.di

import com.android.monu.domain.usecase.account.GetAllAccountsUseCase
import com.android.monu.domain.usecase.account.GetTotalAccountBalanceUseCase
import com.android.monu.domain.usecase.budgeting.CreateBudgetingUseCase
import com.android.monu.domain.usecase.budgeting.GetAllBudgetsUseCase
import com.android.monu.domain.usecase.budgeting.GetBudgetingSummaryUseCase
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
import com.android.monu.domain.usecase.transaction.GetAllTransactionsUseCase
import com.android.monu.domain.usecase.transaction.GetDistinctTransactionYearsUseCase
import com.android.monu.domain.usecase.transaction.GetRecentTransactionsUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionBalanceSummaryUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionByIdUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionCategorySummaryUseCase
import com.android.monu.domain.usecase.transaction.GetTransactionSummaryUseCase
import org.koin.dsl.module

val domainModule = module {

    factory { GetAllAccountsUseCase(get()) }
    factory { GetTotalAccountBalanceUseCase(get()) }

    factory { GetRecentTransactionsUseCase(get()) }
    factory { GetAllTransactionsUseCase(get()) }
    factory { GetTransactionByIdUseCase(get()) }
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

    factory { GetAllBudgetsUseCase(get()) }
    factory { GetBudgetingSummaryUseCase(get()) }
    factory { CreateBudgetingUseCase(get(), get()) }
}