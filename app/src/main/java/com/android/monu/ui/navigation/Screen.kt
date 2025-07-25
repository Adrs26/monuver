package com.android.monu.ui.navigation

import kotlinx.serialization.Serializable

@Serializable object Main
@Serializable object Home
@Serializable object Transaction
@Serializable object Budgeting
@Serializable object Analytics

@Serializable object Settings
@Serializable object MainSettings

@Serializable object Account
@Serializable object MainAccount
@Serializable object AddAccount
@Serializable object AccountType

@Serializable object DetailTransaction
@Serializable data class MainDetailTransaction(val id: Long = 0)

@Serializable object AddTransaction
@Serializable data class MainAddTransaction(val type: Int = 0)
@Serializable data class TransactionCategory(val type: Int = 0)
@Serializable data class TransactionSource(val amount: Long = 0)

@Serializable object Transfer
@Serializable object MainTransfer
@Serializable data class TransferAccount(val type: Int = 0)