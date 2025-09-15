package com.android.monu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.monu.data.local.entity.room.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM account ORDER BY isActive DESC, balance DESC")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM account WHERE isActive = 1 ORDER BY balance DESC")
    fun getActiveAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT SUM(balance) FROM account")
    fun getTotalAccountBalance(): Flow<Long?>

    @Query("SELECT SUM(balance) FROM account WHERE isActive = 1")
    fun getActiveAccountBalance(): Flow<Long?>

    @Query("SELECT * FROM account WHERE id = :accountId LIMIT 1")
    fun getAccountById(accountId: Int): Flow<AccountEntity?>

    @Query("SELECT balance FROM account WHERE id = :accountId")
    suspend fun getAccountBalance(accountId: Int): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewAccount(account: AccountEntity): Long

    @Query("UPDATE account SET balance = balance + :delta WHERE id = :accountId")
    suspend fun increaseAccountBalance(accountId: Int, delta: Long)

    @Query("UPDATE account SET balance = balance - :delta WHERE id = :accountId")
    suspend fun decreaseAccountBalance(accountId: Int, delta: Long)

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Query("UPDATE account SET isActive = :isActive WHERE id = :accountId")
    suspend fun updateAccountStatus(accountId: Int, isActive: Boolean)
}