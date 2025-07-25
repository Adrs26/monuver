package com.android.monu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.monu.data.local.entity.room.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM account ORDER BY balance DESC")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT SUM(balance) FROM account")
    fun getTotalAccountBalance(): Flow<Long?>

    @Query("SELECT balance FROM account WHERE id = :accountId")
    suspend fun getAccountBalance(accountId: Int): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNewAccount(account: AccountEntity): Long

    @Query("UPDATE account SET balance = balance + :delta WHERE id = :accountId")
    suspend fun increaseAccountBalance(accountId: Int, delta: Long)

    @Query("UPDATE account SET balance = balance - :delta WHERE id = :accountId")
    suspend fun decreaseAccountBalance(accountId: Int, delta: Long)
}