package com.android.monu.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Transaction

@Serializable
object Report

@Serializable
object Analytics

@Serializable
object Settings

@Serializable
object AddIncome

@Serializable
object AddExpense

@Serializable
data class EditTransaction(val id: Long)

@Serializable
object ReportDetail

@Serializable
object Budgeting