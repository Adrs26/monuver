package com.android.monu.domain.model

data class TransactionState(
    val id: Long = 0,
    val title: String,
    val type: Int,
    val parentCategory: Int,
    val childCategory: Int,
    val date: String,
    val month: Int,
    val year: Int,
    val timeStamp: Long,
    val amount: Long,
    val sourceId: Int,
    val sourceName: String,
    val destinationId: Int? = null,
    val destinationName: String? = null,
    val saveId: Long? = null,
    val billId: Long? = null,
    val isLocked: Boolean,
    val isSpecialCase: Boolean
)

data class AddTransactionState(
    val title: String,
    val type: Int,
    val parentCategory: Int,
    val childCategory: Int,
    val date: String,
    val amount: Long,
    val sourceId: Int,
    val sourceName: String
)

data class EditTransactionState(
    val id: Long,
    val title: String,
    val type: Int,
    val parentCategory: Int,
    val childCategory: Int,
    val initialParentCategory: Int,
    val date: String,
    val initialDate: String,
    val amount: Long,
    val initialAmount: Long,
    val sourceId: Int,
    val sourceName: String,
    val isLocked: Boolean
)

data class TransferState(
    val sourceId: Int,
    val sourceName: String,
    val destinationId: Int,
    val destinationName: String,
    val date: String,
    val amount: Long
)

data class TransactionBalanceSummaryState(
    val totalIncomeAmount: Long = 0,
    val totalExpenseAmount: Long = 0,
    val averageIncomeAmount: Double = 0.0,
    val averageExpenseAmount: Double = 0.0
)

data class TransactionCategorySummaryState(
    val parentCategory: Int,
    val totalAmount: Long
)

data class TransactionDailySummaryState(
    val date: String,
    val totalIncome: Long,
    val totalExpense: Long
)

data class TransactionSummaryState(
    val type: Int,
    val date: String,
    val amount: Long
)

data class ExportState(
    val title: String,
    val username: String,
    val startDate: String,
    val endDate: String,
    val sortType: Int,
    val isIncomeExpenseGrouped: Boolean,
    val isTransferIncluded: Boolean
)