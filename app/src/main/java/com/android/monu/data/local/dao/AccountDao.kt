package com.android.monu.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.android.monu.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM account ORDER BY balance DESC")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT SUM(balance) FROM account")
    fun getTotalAccountBalance(): Flow<Long?>

    @Query("SELECT balance FROM account WHERE id = :accountId")
    suspend fun getAccountBalance(accountId: Int): Long?
}