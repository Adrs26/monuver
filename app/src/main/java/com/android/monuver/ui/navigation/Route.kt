package com.android.monuver.ui.navigation

import kotlinx.serialization.Serializable

@Serializable class Starting {
    @Serializable data object Onboarding
    @Serializable data object CheckAppVersion
    @Serializable data object Main
}

@Serializable class Main {
    @Serializable data object Home
    @Serializable data object Transaction
    @Serializable data object Budgeting
    @Serializable data object Analytics
}

@Serializable class Settings {
    @Serializable data object Main
    @Serializable data object Export
}

@Serializable class Account {
    @Serializable data object Main
    @Serializable data object Add
    @Serializable data object Type
}

@Serializable class AccountDetail {
    @Serializable data class Main(val accountId: Int = 0)
    @Serializable data class Edit(val accountId: Int = 0)
    @Serializable data object Type
}

@Serializable class TransactionDetail {
    @Serializable data class Main(val transactionId: Long = 0)
    @Serializable data class Edit(val transactionId: Long = 0)
    @Serializable data class Category(val transactionType: Int = 0)
}

@Serializable class AddTransaction {
    @Serializable data class Main(val transactionType: Int = 0)
    @Serializable data class Category(val transactionType: Int = 0)
    @Serializable data object Source
}

@Serializable class Transfer {
    @Serializable data object Main
    @Serializable data class Account(val type: Int = 0)
}

@Serializable class BudgetDetail {
    @Serializable data class Main(val budgetId: Long = 0)
    @Serializable data class Edit(val budgetId: Long = 0)
}

@Serializable class AddBudget {
    @Serializable data object Main
    @Serializable data object Category
}

@Serializable class InactiveBudget {
    @Serializable data object Main
}

@Serializable class AnalyticsCategoryTransaction {
    @Serializable data class Main(
        val category: Int = 0,
        val month: Int = 0,
        val year: Int = 0
    )
}

@Serializable class Billing {
    @Serializable data object Main
    @Serializable data object Add
    @Serializable data class Detail(val billId: Long = 0)
    @Serializable data class Edit(val billId: Long = 0)
}

@Serializable class PayBill {
    @Serializable data class Main(val billId: Long = 0)
    @Serializable data object Category
    @Serializable data object Source
}

@Serializable class Saving {
    @Serializable data object Main
    @Serializable data object Add
    @Serializable data class Detail(val id: Long = 0)
    @Serializable data class Edit(val id: Long = 0)
    @Serializable data object Inactive
}

@Serializable class Deposit {
    @Serializable data class Main(val savingId: Long? = null, val savingName: String? = null)
    @Serializable data object Account
}

@Serializable class Withdraw {
    @Serializable data class Main(val savingId: Long? = null, val savingName: String? = null)
    @Serializable data object Account
}