package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.account.Account
import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.model.budget.Budget
import com.android.monu.domain.model.saving.Saving
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.BillRepository
import com.android.monu.domain.repository.BudgetRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.domain.repository.SavingRepository
import com.android.monu.domain.repository.TransactionRepository
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BackupDataUseCase(
    private val accountRepository: AccountRepository,
    private val billRepository: BillRepository,
    private val budgetRepository: BudgetRepository,
    private val savingRepository: SavingRepository,
    private val transactionRepository: TransactionRepository,
    private val financeRepository: FinanceRepository
) {
    suspend operator fun invoke(): DatabaseResultMessage = withContext(Dispatchers.IO) {
        try {
            val accounts = accountRepository.getAllAccountsSuspend()
            val bills = billRepository.getAllBillsSuspend()
            val budgets = budgetRepository.getAllBudgetsSuspend()
            val savings = savingRepository.getAllSavingsSuspend()
            val transactions = transactionRepository.getAllTransactionsSuspend()

            val backupData = BackupData(accounts, bills, budgets, savings, transactions)
            val backupDataJson = Gson().toJson(backupData)
            financeRepository.backupAllData("monu_backup_${System.currentTimeMillis()}.json", backupDataJson)

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