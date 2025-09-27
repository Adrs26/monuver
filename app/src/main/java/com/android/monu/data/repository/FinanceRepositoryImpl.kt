package com.android.monu.data.repository

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.room.withTransaction
import com.android.monu.data.local.MonuDatabase
import com.android.monu.data.local.dao.AccountDao
import com.android.monu.data.local.dao.BillDao
import com.android.monu.data.local.dao.BudgetDao
import com.android.monu.data.local.dao.SavingDao
import com.android.monu.data.local.dao.TransactionDao
import com.android.monu.data.mapper.AccountMapper
import com.android.monu.data.mapper.BillMapper
import com.android.monu.data.mapper.BudgetMapper
import com.android.monu.data.mapper.SavingMapper
import com.android.monu.data.mapper.TransactionMapper
import com.android.monu.domain.model.account.Account
import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.model.saving.Saving
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.domain.usecase.finance.BackupData
import com.android.monu.domain.usecase.finance.BudgetStatus
import com.android.monu.ui.feature.utils.TransactionChildCategory
import com.google.gson.Gson
import java.io.File
import java.io.IOException
import kotlin.math.absoluteValue

class FinanceRepositoryImpl(
    private val database: MonuDatabase,
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao,
    private val budgetDao: BudgetDao,
    private val billDao: BillDao,
    private val savingDao: SavingDao,
    private val context: Context
) : FinanceRepository {

    override suspend fun createAccount(account: Account, transaction: Transaction): Long {
        return database.withTransaction {
            val accountId = accountDao.createNewAccount(
                AccountMapper.accountDomainToEntity(account)
            )
            val transactionWithAccountId = transaction.copy(sourceId = accountId.toInt())
            transactionDao.createNewTransaction(
                TransactionMapper.transactionDomainToEntity(transactionWithAccountId)
            )
            accountId
        }
    }

    override suspend fun updateAccountStatus(accountId: Int, isActive: Boolean) {
        return database.withTransaction {
            accountDao.updateAccountStatus(accountId, isActive)
            transactionDao.updateTransactionLockStatusByAccountId(accountId, !isActive)
        }
    }

    override suspend fun updateAccount(account: Account) {
        return database.withTransaction {
            accountDao.updateAccount(AccountMapper.accountDomainToEntityForUpdate(account))
            transactionDao.updateAccountNameOnCommonTransaction(account.id, account.name)
            transactionDao.updateAccountNameOnTransferTransaction(account.id, account.name)
            transactionDao.updateAccountNameOnDepositTransaction(account.id, account.name)
            transactionDao.updateAccountNameOnWithdrawTransaction(account.id, account.name)
        }
    }

    override suspend fun createIncomeTransaction(transaction: Transaction): Long {
        return database.withTransaction {
            val transactionId = transactionDao.createNewTransaction(
                TransactionMapper.transactionDomainToEntity(transaction)
            )
            accountDao.increaseAccountBalance(transaction.sourceId, transaction.amount)
            transactionId
        }
    }

    override suspend fun createExpenseTransaction(transaction: Transaction): Long {
        return database.withTransaction {
            val transactionId = transactionDao.createNewTransaction(
                TransactionMapper.transactionDomainToEntity(transaction)
            )
            accountDao.decreaseAccountBalance(transaction.sourceId, transaction.amount)
            budgetDao.increaseBudgetUsedAmount(transaction.parentCategory, transaction.date, transaction.amount)
            transactionId
        }
    }

    override suspend fun createTransferTransaction(transaction: Transaction): Long {
        return database.withTransaction {
            val transactionId = transactionDao.createNewTransaction(
                TransactionMapper.transactionDomainToEntity(transaction)
            )
            accountDao.decreaseAccountBalance(transaction.sourceId, transaction.amount)
            accountDao.increaseAccountBalance(transaction.destinationId ?: 0, transaction.amount)
            transactionId
        }
    }

    override suspend fun createDepositTransaction(savingId: Long, transaction: Transaction) {
        return database.withTransaction {
            savingDao.increaseSavingCurrentAmount(savingId, transaction.amount)
            accountDao.decreaseAccountBalance(transaction.sourceId, transaction.amount)
            transactionDao.createNewTransaction(
                TransactionMapper.transactionDomainToEntity(transaction)
            )
        }
    }

    override suspend fun createWithdrawTransaction(
        savingId: Long,
        transaction: Transaction
    ) {
        return database.withTransaction {
            savingDao.decreaseSavingCurrentAmount(savingId, transaction.amount)
            accountDao.increaseAccountBalance(transaction.destinationId ?: 0, transaction.amount)
            transactionDao.createNewTransaction(
                TransactionMapper.transactionDomainToEntity(transaction)
            )
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
        transaction: Transaction,
        initialAmount: Long
    ): Int {
        return database.withTransaction {
            val rowUpdated = transactionDao.updateTransaction(
                TransactionMapper.transactionDomainToEntityForUpdate(transaction)
            )
            val difference = transaction.amount - initialAmount
            if (difference != 0L) {
                if (difference > 0) {
                    accountDao.increaseAccountBalance(transaction.sourceId, difference)
                } else {
                    accountDao.decreaseAccountBalance(transaction.sourceId, difference.absoluteValue)
                }
            }
            rowUpdated
        }
    }

    override suspend fun updateExpenseTransaction(
        transaction: Transaction,
        initialParentCategory: Int,
        initialDate: String,
        initialAmount: Long,
        budgetStatus: BudgetStatus
    ): Int {
        return database.withTransaction {
            val rowUpdated = transactionDao.updateTransaction(
                TransactionMapper.transactionDomainToEntityForUpdate(transaction)
            )
            val difference = transaction.amount - initialAmount
            if (difference != 0L) {
                if (difference > 0) {
                    accountDao.decreaseAccountBalance(transaction.sourceId, difference)
                } else {
                    accountDao.increaseAccountBalance(transaction.sourceId, difference.absoluteValue)
                }
            }

            when(budgetStatus) {
                BudgetStatus.NoOldBudget -> budgetDao.increaseBudgetUsedAmount(
                    transaction.parentCategory,
                    transaction.date,
                    transaction.amount
                )
                BudgetStatus.NoNewBudget -> budgetDao.decreaseBudgetUsedAmount(
                    initialParentCategory,
                    initialDate,
                    initialAmount
                )
                BudgetStatus.NoBudget -> {}
                BudgetStatus.SameBudget -> {
                    if (difference != 0L) {
                        if (difference > 0) {
                            budgetDao.increaseBudgetUsedAmount(
                                transaction.parentCategory,
                                transaction.date,
                                difference
                            )
                        } else {
                            budgetDao.decreaseBudgetUsedAmount(
                                transaction.parentCategory,
                                transaction.date,
                                difference.absoluteValue
                            )
                        }
                    }
                }
                BudgetStatus.DifferentBudget -> {
                    budgetDao.decreaseBudgetUsedAmount(
                        initialParentCategory,
                        initialDate,
                        initialAmount
                    )
                    budgetDao.increaseBudgetUsedAmount(
                        transaction.parentCategory,
                        transaction.date,
                        transaction.amount
                    )
                }
            }

            rowUpdated
        }
    }

    override suspend fun processBillPayment(
        billId: Long,
        billPaidDate: String,
        transaction: Transaction,
        isRecurring: Boolean,
        bill: Bill
    ) {
        database.withTransaction {
            billDao.updateBillPaidStatusById(billId, billPaidDate, true)
            transactionDao.createNewTransaction(TransactionMapper.transactionDomainToEntity(transaction))
            accountDao.decreaseAccountBalance(transaction.sourceId, transaction.amount)
            budgetDao.increaseBudgetUsedAmount(transaction.parentCategory, transaction.date, transaction.amount)

            if (isRecurring) {
                billDao.createNewBill(BillMapper.billDomainToEntity(bill))
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

    override suspend fun updateSaving(saving: Saving) {
        database.withTransaction {
            savingDao.updateSaving(SavingMapper.savingDomainToEntityForUpdate(saving))
            transactionDao.updateSavingTitleOnDepositTransaction(saving.id, saving.title)
            transactionDao.updateSavingTitleOnWithdrawTransaction(saving.id, saving.title)
        }
    }

    override suspend fun completeSaving(transaction: Transaction, savingId: Long) {
        database.withTransaction {
            transactionDao.createNewTransaction(TransactionMapper.transactionDomainToEntity(transaction))
            savingDao.updateSavingStatusToInactiveById(savingId)
        }
    }

    override suspend fun backupAllData(fileName: String) {
        val backupData = BackupData(
            accounts = accountDao.getAllAccountsSuspend().map { AccountMapper.accountEntityToDomain(it) },
            bills = billDao.getAllBillsSuspend().map { BillMapper.billEntityToDomain(it) },
            budgets = budgetDao.getAllBudgetsSuspend().map { BudgetMapper.budgetEntityToDomain(it) },
            savings = savingDao.getAllSavingsSuspend().map { SavingMapper.savingEntityToDomain(it) },
            transactions = transactionDao.getAllTransactionsSuspend().map {
                TransactionMapper.transactionEntityToDomain(it)
            }
        )
        val backupDataJson = Gson().toJson(backupData)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/json")
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                ?: throw IOException("Failed insert to MediaStore")

            resolver.openOutputStream(uri)?.use { it.write(backupDataJson.toByteArray()) }
        } else {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)
            file.writeText(backupDataJson)
        }
    }

    override suspend fun restoreAllData(uri: Uri) {
        val backupDataJson = context.contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() }
        val backupData = Gson().fromJson(backupDataJson, BackupData::class.java)

        database.withTransaction {
            accountDao.deleteAllAccounts()
            transactionDao.deleteAllTransactions()
            budgetDao.deleteAllBudgets()
            billDao.deleteAllBills()
            savingDao.deleteAllSavings()
            accountDao.insertAllAccounts(
                backupData.accounts.map { AccountMapper.accountDomainToEntityForUpdate(it) }
            )
            billDao.insertAllBills(
                backupData.bills.map { BillMapper.billDomainToEntityForUpdate(it) }
            )
            budgetDao.insertAllBudgets(
                backupData.budgets.map { BudgetMapper.budgetDomainToEntityForUpdate(it) }
            )
            savingDao.insertAllSavings(
                backupData.savings.map { SavingMapper.savingDomainToEntityForUpdate(it) }
            )
            transactionDao.insertAllTransactions(
                backupData.transactions.map { TransactionMapper.transactionDomainToEntityForUpdate(it) }
            )
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