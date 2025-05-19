package com.android.monu.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Transactions : Screen("transactions")
    data object Reports : Screen("reports")
    data object Analytics : Screen("analytics")
    data object Settings : Screen("settings")
    data object AddIncome : Screen("add_income")
    data object AddExpense : Screen("add_expense")
    data object EditTransaction : Screen("edit_transaction/{transactionId}") {
        fun createRoute(transactionId: Long) = "edit_transaction/$transactionId"
    }
    data object ReportDetail : Screen("report_detail")
}