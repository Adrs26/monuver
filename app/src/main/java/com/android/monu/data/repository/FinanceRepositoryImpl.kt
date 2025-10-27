package com.android.monu.data.repository

import androidx.room.withTransaction
import com.android.monu.data.local.MonuDatabase
import com.android.monu.data.local.dao.AccountDao
import com.android.monu.data.local.dao.BillDao
import com.android.monu.data.local.dao.BudgetDao
import com.android.monu.data.local.dao.SavingDao
import com.android.monu.data.local.dao.TransactionDao
import com.android.monu.data.mapper.toEntity
import com.android.monu.data.mapper.toEntityForUpdate
import com.android.monu.domain.common.BudgetStatusState
import com.android.monu.domain.model.AccountState
import com.android.monu.domain.model.BillState
import com.android.monu.domain.model.SavingState
import com.android.monu.domain.model.TransactionState
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.domain.usecase.finance.BackupData
import com.android.monu.utils.TransactionChildCategory
import kotlin.math.absoluteValue

class FinanceRepositoryImpl(
    private val database: MonuDatabase,
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao,
    private val budgetDao: BudgetDao,
    private val billDao: BillDao,
    private val savingDao: SavingDao
) : FinanceRepository {

    override suspend fun createAccount(account: AccountState, transactionState: TransactionState): Long {
        return database.withTransaction {
            val accountId = accountDao.createNewAccount(account.toEntity())
            val transactionWithAccountId = transactionState.copy(sourceId = accountId.toInt())
            transactionDao.createNewTransaction(transactionWithAccountId.toEntity())
            accountId
        }
    }

    override suspend fun updateAccountStatus(accountId: Int, isActive: Boolean) {
        return database.withTransaction {
            accountDao.updateAccountStatus(accountId, isActive)
            transactionDao.updateTransactionLockStatusByAccountId(accountId, !isActive)
        }
    }

    override suspend fun updateAccount(account: AccountState) {
        return database.withTransaction {
            accountDao.updateAccount(account.toEntityForUpdate())
            transactionDao.updateAccountNameOnCommonTransaction(account.id, account.name)
            transactionDao.updateAccountNameOnTransferTransaction(account.id, account.name)
            transactionDao.updateAccountNameOnDepositTransaction(account.id, account.name)
            transactionDao.updateAccountNameOnWithdrawTransaction(account.id, account.name)
        }
    }

    override suspend fun createIncomeTransaction(transactionState: TransactionState): Long {
        return database.withTransaction {
            val transactionId = transactionDao.createNewTransaction(transactionState.toEntity())
            accountDao.increaseAccountBalance(transactionState.sourceId, transactionState.amount)
            transactionId
        }
    }

    override suspend fun createExpenseTransaction(transactionState: TransactionState): Long {
        return database.withTransaction {
            val transactionId = transactionDao.createNewTransaction(transactionState.toEntity())
            accountDao.decreaseAccountBalance(transactionState.sourceId, transactionState.amount)
            budgetDao.increaseBudgetUsedAmount(transactionState.parentCategory, transactionState.date, transactionState.amount)
            transactionId
        }
    }

    override suspend fun createTransferTransaction(transactionState: TransactionState): Long {
        return database.withTransaction {
            val transactionId = transactionDao.createNewTransaction(transactionState.toEntity())
            accountDao.decreaseAccountBalance(transactionState.sourceId, transactionState.amount)
            accountDao.increaseAccountBalance(transactionState.destinationId ?: 0, transactionState.amount)
            transactionId
        }
    }

    override suspend fun createDepositTransaction(savingId: Long, transactionState: TransactionState) {
        return database.withTransaction {
            savingDao.increaseSavingCurrentAmount(savingId, transactionState.amount)
            accountDao.decreaseAccountBalance(transactionState.sourceId, transactionState.amount)
            transactionDao.createNewTransaction(transactionState.toEntity())
        }
    }

    override suspend fun createWithdrawTransaction(
        savingId: Long,
        transactionState: TransactionState
    ) {
        return database.withTransaction {
            savingDao.decreaseSavingCurrentAmount(savingId, transactionState.amount)
            accountDao.increaseAccountBalance(transactionState.destinationId ?: 0, transactionState.amount)
            transactionDao.createNewTransaction(transactionState.toEntity())
        }
    }

    override suspend fun deleteIncomeTransaction(transactionId: Long, sourceId: Int, amount: Long): Int {
        return database.withTransaction {
            val rowDeleted = transactionDao.deleteTransactionById(transactionId)
            accountDao.decreaseAccountBalance(sourceId, amount)
            rowDeleted
        }
    }

    override suspend fun deleteExpenseTransaction(
        transactionId: Long,
        parentCategory: Int,
        date: String,
        sourceId: Int,
        amount: Long
    ): Int {
        return database.withTransaction {
            val rowDeleted = transactionDao.deleteTransactionById(transactionId)
            accountDao.increaseAccountBalance(sourceId, amount)
            budgetDao.decreaseBudgetUsedAmount(parentCategory, date, amount)
            rowDeleted
        }
    }

    override suspend fun deleteSavingTransaction(
        transactionId: Long,
        category: Int,
        accountId: Int,
        savingId: Long,
        amount: Long
    ) {
        return database.withTransaction {
            if (category == TransactionChildCategory.SAVINGS_IN) {
                savingDao.decreaseSavingCurrentAmount(savingId, amount)
                accountDao.increaseAccountBalance(accountId, amount)
            } else {
                savingDao.increaseSavingCurrentAmount(savingId, amount)
                accountDao.decreaseAccountBalance(accountId, amount)
            }
            transactionDao.deleteTransactionById(transactionId)
        }
    }

    override suspend fun updateIncomeTransaction(
        transactionState: TransactionState,
        initialAmount: Long
    ): Int {
        return database.withTransaction {
            val rowUpdated = transactionDao.updateTransaction(transactionState.toEntityForUpdate())
            val difference = transactionState.amount - initialAmount
            if (difference != 0L) {
                if (difference > 0) {
                    accountDao.increaseAccountBalance(transactionState.sourceId, difference)
                } else {
                    accountDao.decreaseAccountBalance(transactionState.sourceId, difference.absoluteValue)
                }
            }
            rowUpdated
        }
    }

    override suspend fun updateExpenseTransaction(
        transactionState: TransactionState,
        initialParentCategory: Int,
        initialDate: String,
        initialAmount: Long,
        budgetStatus: BudgetStatusState
    ): Int {
        return database.withTransaction {
            val rowUpdated = transactionDao.updateTransaction(transactionState.toEntityForUpdate())
            val difference = transactionState.amount - initialAmount
            if (difference != 0L) {
                if (difference > 0) {
                    accountDao.decreaseAccountBalance(transactionState.sourceId, difference)
                } else {
                    accountDao.increaseAccountBalance(transactionState.sourceId, difference.absoluteValue)
                }
            }

            when(budgetStatus) {
                BudgetStatusState.NoOldBudget -> budgetDao.increaseBudgetUsedAmount(
                    transactionState.parentCategory,
                    transactionState.date,
                    transactionState.amount
                )
                BudgetStatusState.NoNewBudget -> budgetDao.decreaseBudgetUsedAmount(
                    initialParentCategory,
                    initialDate,
                    initialAmount
                )
                BudgetStatusState.NoBudget -> {}
                BudgetStatusState.SameBudget -> {
                    if (difference != 0L) {
                        if (difference > 0) {
                            budgetDao.increaseBudgetUsedAmount(
                                transactionState.parentCategory,
                                transactionState.date,
                                difference
                            )
                        } else {
                            budgetDao.decreaseBudgetUsedAmount(
                                transactionState.parentCategory,
                                transactionState.date,
                                difference.absoluteValue
                            )
                        }
                    }
                }
                BudgetStatusState.DifferentBudget -> {
                    budgetDao.decreaseBudgetUsedAmount(
                        initialParentCategory,
                        initialDate,
                        initialAmount
                    )
                    budgetDao.increaseBudgetUsedAmount(
                        transactionState.parentCategory,
                        transactionState.date,
                        transactionState.amount
                    )
                }
            }

            rowUpdated
        }
    }

    override suspend fun processBillPayment(
        billId: Long,
        billPaidDate: String,
        transactionState: TransactionState,
        isRecurring: Boolean,
        billState: BillState
    ) {
        database.withTransaction {
            billDao.updateBillPaidStatusById(billId, billPaidDate, true)
            transactionDao.createNewTransaction(transactionState.toEntity())
            accountDao.decreaseAccountBalance(transactionState.sourceId, transactionState.amount)
            budgetDao.increaseBudgetUsedAmount(transactionState.parentCategory, transactionState.date, transactionState.amount)

            if (isRecurring) {
                billDao.createNewBill(billState.toEntity())
            }
        }
    }

    override suspend fun cancelBillPayment(billId: Long) {
        database.withTransaction {
            billDao.updateBillPaidStatusById(billId, null, false)
            val transaction = transactionDao.getTransactionIdByBillId(billId)
            transaction?.let {
                deleteExpenseTransaction(
                    transaction.id,
                    transaction.parentCategory,
                    transaction.date,
                    transaction.sourceId,
                    transaction.amount
                )
            }
        }
    }

    override suspend fun updateSaving(savingState: SavingState) {
        database.withTransaction {
            savingDao.updateSaving(savingState.toEntityForUpdate())
            transactionDao.updateSavingTitleOnDepositTransaction(savingState.id, savingState.title)
            transactionDao.updateSavingTitleOnWithdrawTransaction(savingState.id, savingState.title)
        }
    }

    override suspend fun completeSaving(transactionState: TransactionState, savingId: Long) {
        database.withTransaction {
            transactionDao.createNewTransaction(transactionState.toEntity())
            savingDao.updateSavingStatusToInactiveById(savingId)
        }
    }

    override suspend fun restoreAllData(backupData: BackupData) {
        database.withTransaction {
            accountDao.deleteAllAccounts()
            transactionDao.deleteAllTransactions()
            budgetDao.deleteAllBudgets()
            billDao.deleteAllBills()
            savingDao.deleteAllSavings()
            accountDao.insertAllAccounts(backupData.accounts.map { it.toEntityForUpdate() })
            billDao.insertAllBills(backupData.bills.map { it.toEntityForUpdate() })
            budgetDao.insertAllBudgets(backupData.budgets.map { it.toEntityForUpdate() })
            savingDao.insertAllSavings(backupData.savings.map { it.toEntityForUpdate() })
            transactionDao.insertAllTransactions(backupData.transactions.map { it.toEntityForUpdate() })
        }
    }

    override suspend fun deleteAllData() {
        database.withTransaction {
            accountDao.deleteAllAccounts()
            transactionDao.deleteAllTransactions()
            budgetDao.deleteAllBudgets()
            billDao.deleteAllBills()
            savingDao.deleteAllSavings()
        }
    }
}