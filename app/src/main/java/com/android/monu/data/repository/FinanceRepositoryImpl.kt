package com.android.monu.data.repository

import androidx.room.withTransaction
import com.android.monu.data.local.MonuDatabase
import com.android.monu.data.local.dao.AccountDao
import com.android.monu.data.local.dao.TransactionDao
import com.android.monu.data.mapper.AccountMapper
import com.android.monu.data.mapper.TransactionMapper
import com.android.monu.domain.model.account.Account
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.utils.DateHelper
import com.android.monu.utils.TransactionChildCategory
import com.android.monu.utils.TransactionParentCategory
import com.android.monu.utils.TransactionType
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class FinanceRepositoryImpl(
    private val database: MonuDatabase,
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao
) : FinanceRepository {

    override suspend fun createAccountWithInitialTransaction(
        account: Account
    ): Result<Long> {
        return try {
            val accountId = database.withTransaction {
                val accountId = accountDao.createNewAccount(
                    AccountMapper.accountDomainToEntity(account)
                )

                val currentDate = LocalDate.now()
                val isoDate = currentDate.format(DateTimeFormatter.ISO_DATE)
                val (month, year) = DateHelper.getMonthAndYear(isoDate)

                val initialTransaction = Transaction(
                    id = 0,
                    title = "Penambahan akun baru ${account.name}",
                    type = TransactionType.INCOME,
                    parentCategory = TransactionParentCategory.OTHER_INCOME,
                    childCategory = TransactionChildCategory.OTHER_INCOME,
                    date = isoDate,
                    month = month,
                    year = year,
                    timeStamp = System.currentTimeMillis(),
                    amount = account.balance,
                    sourceId = accountId.toInt(),
                    sourceName = account.name
                )

                transactionDao.createNewTransaction(
                    TransactionMapper.transactionDomainToEntity(initialTransaction)
                )

                accountId
            }

            Result.success(accountId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createTransactionAndAdjustAccountBalance(
        transaction: Transaction
    ): Result<Long> {
        return try {
            val transactionId = database.withTransaction {
                val transactionId = transactionDao.createNewTransaction(
                    TransactionMapper.transactionDomainToEntity(transaction)
                )

                when {
                    transaction.type == TransactionType.INCOME -> {
                        accountDao.increaseAccountBalance(transaction.sourceId, transaction.amount)
                    }
                    transaction.type == TransactionType.EXPENSE -> {
                        accountDao.decreaseAccountBalance(transaction.sourceId, transaction.amount)
                    }
                    transaction.type == TransactionType.TRANSFER &&
                            transaction.childCategory == TransactionChildCategory.TRANSFER_ACCOUNT -> {
                        accountDao.decreaseAccountBalance(
                            transaction.sourceId,
                            transaction.amount
                        )
                        accountDao.increaseAccountBalance(
                            transaction.destinationId ?: 0,
                            transaction.amount
                        )
                    }
                }

                transactionId
            }

            Result.success(transactionId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTransactionAndAdjustAccountBalance(
        transaction: Transaction
    ): Result<Int> {
        return try {
            val rowDeleted = database.withTransaction {
                val rowDeleted = transactionDao.deleteTransactionById(transaction.id)

                when {
                    transaction.type == TransactionType.INCOME -> {
                        accountDao.decreaseAccountBalance(transaction.sourceId, transaction.amount)
                    }
                    transaction.type == TransactionType.EXPENSE -> {
                        accountDao.increaseAccountBalance(transaction.sourceId, transaction.amount)
                    }
                    transaction.type == TransactionType.TRANSFER &&
                            transaction.childCategory == TransactionChildCategory.TRANSFER_ACCOUNT -> {
                        accountDao.increaseAccountBalance(
                            transaction.sourceId,
                            transaction.amount
                        )
                        accountDao.decreaseAccountBalance(
                            transaction.destinationId ?: 0,
                            transaction.amount
                        )
                    }
                }

                rowDeleted
            }

            Result.success(rowDeleted)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}