package com.android.monuver.data.local.entity.projection

import com.google.gson.annotations.SerializedName

data class BackupData(
    @SerializedName("accounts") val accounts: List<Account>,
    @SerializedName("bills") val bills: List<Bill>,
    @SerializedName("budgets") val budgets: List<Budget>,
    @SerializedName("savings") val savings: List<Saving>,
    @SerializedName("transactions") val transactions: List<Transaction>
)

data class Account(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: Int,
    @SerializedName("balance") val balance: Long,
    @SerializedName("is_active") val isActive: Boolean
)

data class Bill(
    @SerializedName("id") val id: Long = 0L,
    @SerializedName("parent_id") val parentId: Long = 0L,
    @SerializedName("title") val title: String,
    @SerializedName("due_date") val dueDate: String,
    @SerializedName("paid_date") val paidDate: String?,
    @SerializedName("time_stamp") val timeStamp: Long,
    @SerializedName("amount") val amount: Long,
    @SerializedName("is_recurring") val isRecurring: Boolean,
    @SerializedName("cycle") val cycle: Int?,
    @SerializedName("period") val period: Int?,
    @SerializedName("fix_period") val fixPeriod: Int?,
    @SerializedName("is_paid") val isPaid: Boolean,
    @SerializedName("now_paid_period") val nowPaidPeriod: Int,
    @SerializedName("is_paid_before") val isPaidBefore: Boolean
)

data class Budget(
    @SerializedName("id") val id: Long = 0L,
    @SerializedName("category") val category: Int,
    @SerializedName("cycle") val cycle: Int,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("max_amount") val maxAmount: Long,
    @SerializedName("used_amount") val usedAmount: Long,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("is_overflow_allowed") val isOverflowAllowed: Boolean,
    @SerializedName("is_auto_update") val isAutoUpdate: Boolean
)

data class Saving(
    @SerializedName("id") val id: Long = 0L,
    @SerializedName("title") val title: String,
    @SerializedName("target_date") val targetDate: String,
    @SerializedName("target_amount") val targetAmount: Long,
    @SerializedName("current_amount") val currentAmount: Long,
    @SerializedName("is_active") val isActive: Boolean
)

data class Transaction(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: Int,
    @SerializedName("parent_category") val parentCategory: Int,
    @SerializedName("child_category") val childCategory: Int,
    @SerializedName("date") val date: String,
    @SerializedName("month") val month: Int,
    @SerializedName("year") val year: Int,
    @SerializedName("time_stamp") val timeStamp: Long,
    @SerializedName("amount") val amount: Long,
    @SerializedName("source_id") val sourceId: Int,
    @SerializedName("source_name") val sourceName: String,
    @SerializedName("destination_id") val destinationId: Int? = null,
    @SerializedName("destination_name") val destinationName: String? = null,
    @SerializedName("save_id") val saveId: Long? = null,
    @SerializedName("bill_id") val billId: Long? = null,
    @SerializedName("is_locked") val isLocked: Boolean,
    @SerializedName("is_special_case") val isSpecialCase: Boolean
)


