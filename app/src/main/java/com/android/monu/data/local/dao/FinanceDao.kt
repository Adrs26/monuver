package com.android.monu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.android.monu.data.local.entity.AccountEntity
import com.android.monu.data.local.entity.TransactionEntity
import com.android.monu.presentation.utils.TransactionChildCategory
import com.android.monu.presentation.utils.TransactionType

@Dao
interface FinanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewTransaction(transaction: TransactionEntity): Long

    @Query("DELETE FROM `transaction` WHERE id = :id")
    suspend fun deleteTransactionById(id: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewAccount(account: AccountEntity): Long

    @Query("UPDATE account SET balance = balance + :delta WHERE id = :accountId")
    suspend fun increaseAccountBalance(accountId: Int, delta: Long)

    @Query("UPDATE account SET balance = balance - :delta WHERE id = :accountId")
    suspend fun decreaseAccountBalance(accountId: Int, delta: Long)

    @Transaction
    suspend fun createAccountWithInitialTransaction(
        account: AccountEntity,
        transaction: TransactionEntity
    ): Long {
        val accountId = createNewAccount(account)
        val transactionWithId = transaction.copy(sourceId = accountId.toInt())
        createNewTransaction(transactionWithId)
        return accountId
    }

    @Transaction
    suspend fun createTransactionAndAdjustAccountBalance(
        transaction: TransactionEntity
    ): Long {
        val transactionId = createNewTransaction(transaction)

        when {
            transaction.type == TransactionType.INCOME -> {
                increaseAccountBalance(transaction.sourceId, transaction.amount)
            }
            transaction.type == TransactionType.EXPENSE -> {
                decreaseAccountBalance(transaction.sourceId, transaction.amount)
            }
            transaction.type == TransactionType.TRANSFER &&
                    transaction.childCategory == TransactionChildCategory.TRANSFER_ACCOUNT -> {
                decreaseAccountBalance(transaction.sourceId, transaction.amount)
                increaseAccountBalance(transaction.destinationId ?: 0, transaction.amount)
            }
        }

        return transactionId
    }

    @Transaction
    suspend fun deleteTransactionAndAdjustAccountBalance(
        transactionId: Long,
        transaction: TransactionEntity
    ): Int {
        val rowDeleted = deleteTransactionById(transactionId)

        when {
            transaction.type == TransactionType.INCOME -> {
                decreaseAccountBalance(transaction.sourceId, transaction.amount)
            }
            transaction.type == TransactionType.EXPENSE -> {
                increaseAccountBalance(transaction.sourceId, transaction.amount)
            }
            transaction.type == TransactionType.TRANSFER &&
                    transaction.childCategory == TransactionChildCategory.TRANSFER_ACCOUNT -> {
                increaseAccountBalance(transaction.sourceId, transaction.amount)
                decreaseAccountBalance(transaction.destinationId ?: 0, transaction.amount)
            }
        }

        return rowDeleted
    }
}