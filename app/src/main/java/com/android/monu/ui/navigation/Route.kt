package com.android.monu.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
class Starting {
    @Serializable object Main
}

@Serializable
class Main {
    @Serializable object Home
    @Serializable object Transaction
    @Serializable object Budgeting
    @Serializable object Analytics
}

@Serializable
class Settings {
    @Serializable object Main
}

@Serializable
class Account {
    @Serializable object Main
    @Serializable object Add
    @Serializable object Type
}

@Serializable
class AccountDetail {
    @Serializable data class Main(val accountId: Int = 0)
    @Serializable data class Edit(val accountId: Int = 0)
    @Serializable object Type
}

@Serializable
class TransactionDetail {
    @Serializable data class Main(val transactionId: Long = 0)
    @Serializable data class Edit(val transactionId: Long = 0)
    @Serializable data class Category(val transactionType: Int = 0)
}

@Serializable
class AddTransaction {
    @Serializable data class Main(val transactionType: Int = 0)
    @Serializable data class Category(val transactionType: Int = 0)
    @Serializable object Source
}

@Serializable
class Transfer {
    @Serializable object Main
    @Serializable data class Account(val type: Int = 0)
}

@Serializable
class BudgetDetail {
    @Serializable data class Main(val budgetId: Long = 0)
    @Serializable data class Edit(val budgetId: Long = 0)
}

@Serializable
class AddBudget {
    @Serializable object Main
    @Serializable object Category
}

@Serializable
class InactiveBudget {
    @Serializable object Main
}

@Serializable
class AnalyticsCategoryTransaction {
    @Serializable
    data class Main(
        val category: Int = 0,
        val month: Int = 0,
        val year: Int = 0
    )
}

@Serializable
class Billing {
    @Serializable object Main
    @Serializable object Add
    @Serializable data class Detail(val billId: Long = 0)
    @Serializable data class Edit(val billId: Long = 0)
}

@Serializable
class PayBill {
    @Serializable data class Main(val billId: Long = 0)
    @Serializable object Category
    @Serializable object Source
}

@Serializable
class Saving {
    @Serializable object Main
    @Serializable object Add
    @Serializable data class Detail(val id: Long = 0)
    @Serializable data class Edit(val id: Long = 0)
    @Serializable object Inactive
}

@Serializable
class Deposit {
    @Serializable data class Main(val savingId: Long? = null, val savingName: String? = null)
    @Serializable object Account
}

@Serializable
class Withdraw {
    @Serializable data class Main(val savingId: Long? = null, val savingName: String? = null)
    @Serializable object Account
}