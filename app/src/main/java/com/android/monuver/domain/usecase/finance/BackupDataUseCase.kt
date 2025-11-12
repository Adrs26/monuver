package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.manager.DataManager
import com.android.monuver.domain.model.AccountState
import com.android.monuver.domain.model.BillState
import com.android.monuver.domain.model.BudgetState
import com.android.monuver.domain.model.SavingState
import com.android.monuver.domain.model.TransactionState
import com.android.monuver.domain.repository.AccountRepository
import com.android.monuver.domain.repository.BillRepository
import com.android.monuver.domain.repository.BudgetRepository
import com.android.monuver.domain.repository.SavingRepository
import com.android.monuver.domain.repository.TransactionRepository

class BackupDataUseCase(
    private val dataManager: DataManager,
    private val accountRepository: AccountRepository,
    private val billRepository: BillRepository,
    private val budgetRepository: BudgetRepository,
    private val savingRepository: SavingRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(): DatabaseResultState {
        return try {
            val accounts = accountRepository.getAllAccountsSuspend()
            val bills = billRepository.getAllBills()
            val budgets = budgetRepository.getAllBudgets()
            val savings = savingRepository.getAllSavings()
            val transactions = transactionRepository.getAllTransactions()

            val backupData = BackupData(
                accounts = accounts,
                bills = bills,
                budgets = budgets,
                savings = savings,
                transactions = transactions,
            )

            dataManager.backupData(backupData)
            DatabaseResultState.BackupDataSuccess
        } catch (_: Exception) {
            DatabaseResultState.BackupDataFailed
        }
    }
}

data class BackupData(
    val accounts: List<AccountState>,
    val bills: List<BillState>,
    val budgets: List<BudgetState>,
    val savings: List<SavingState>,
    val transactions: List<TransactionState>
)