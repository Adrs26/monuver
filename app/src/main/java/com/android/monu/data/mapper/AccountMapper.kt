package com.android.monu.data.mapper

import com.android.monu.data.local.entity.room.AccountEntity
import com.android.monu.domain.model.account.Account

object AccountMapper {
    fun accountEntityToDomain(
        accountEntity: AccountEntity
    ): Account {
        return Account(
            id = accountEntity.id,
            name = accountEntity.name,
            type = accountEntity.type,
            balance = accountEntity.balance
        )
    }

    fun accountDomainToEntity(
        account: Account
    ): AccountEntity {
        return AccountEntity(
            name = account.name,
            type = account.type,
            balance = account.balance
        )
    }
}