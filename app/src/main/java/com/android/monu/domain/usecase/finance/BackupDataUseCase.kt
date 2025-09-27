package com.android.monu.domain.usecase.finance

import com.android.monu.domain.model.account.Account
import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.model.budget.Budget
import com.android.monu.domain.model.saving.Saving
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BackupDataUseCase(
    private val financeRepository: FinanceRepository
) {
    suspend operator fun invoke(): DatabaseResultMessage = withContext(Dispatchers.IO) {
        try {
            financeRepository.backupAllData("monu_backup_${System.currentTimeMillis()}.json")
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