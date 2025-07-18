package com.android.monu.utils

import com.android.monu.domain.model.account.Account
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.presentation.screen.account.addaccount.components.AddAccountContentState
import com.android.monu.presentation.screen.transaction.addtransaction.components.AddTransactionContentState
import com.android.monu.presentation.screen.transaction.transfer.components.TransferContentState

object DataMapper {
    fun accountContentStateToAccount(data: AddAccountContentState): Account {
        return Account(
            id = 0,
            name = data.name,
            type = data.type,
            balance = data.balance
        )
    }

    fun addTransactionContentStateToTransaction(data: AddTransactionContentState): Transaction {
        val (month, year) = DateHelper.getMonthAndYear(data.date)
        return Transaction(
            id = 0,
            title = data.title,
            type = data.type,
            parentCategory = data.parentCategory,
            childCategory = data.childCategory,
            date = data.date,
            month = month,
            year = year,
            timeStamp = System.currentTimeMillis(),
            amount = data.amount,
            sourceId = data.sourceId,
            sourceName = data.sourceName
        )
    }

    fun transferContentStateToTransaction(data: TransferContentState): Transaction {
        val (month, year) = DateHelper.getMonthAndYear(data.date)
        return Transaction(
            id = 0,
            title = "Transfer ke akun ${data.destinationName}",
            type = data.type,
            parentCategory = TransactionParentCategory.TRANSFER,
            childCategory = TransactionChildCategory.TRANSFER_ACCOUNT,
            date = data.date,
            month = month,
            year = year,
            timeStamp = System.currentTimeMillis(),
            amount = data.amount,
            sourceId = data.sourceId,
            sourceName = data.sourceName,
            destinationId = data.destinationId,
            destinationName = data.destinationName
        )
    }
}