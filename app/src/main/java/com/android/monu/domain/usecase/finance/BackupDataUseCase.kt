package com.android.monu.domain.usecase.finance

import com.android.monu.domain.manager.DataManager
import com.android.monu.domain.model.account.Account
import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.model.budget.Budget
import com.android.monu.domain.model.saving.Saving
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.BillRepository
import com.android.monu.domain.repository.BudgetRepository
import com.android.monu.domain.repository.SavingRepository
import com.android.monu.domain.repository.TransactionRepository
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BackupDataUseCase(
    private val dataManager: DataManager,
    private val accountRepository: AccountRepository,
    private val billRepository: BillRepository,
    private val budgetRepository: BudgetRepository,
    private val savingRepository: SavingRepository,
    private val transactionRepository: TransactionRepository,
) {
    suspend operator fun invoke(): DatabaseResultMessage = withContext(Dispatchers.IO) {
        try {
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
            DatabaseResultMessage.BackupDataSuccess
        } catch (_: Exception) {
            DatabaseResultMessage.BackupDataFailed
        }
    }
}

data class BackupData(
    val accounts: List<Account>,
    val bills: List<Bill>,
    val budgets: List<Budget>,
    val savings: List<Saving>,
    val transactions: List<Transaction>
)