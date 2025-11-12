package com.android.monuver.domain.usecase.finance

import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.common.ExportStatusState
import com.android.monuver.domain.manager.DataManager
import com.android.monuver.domain.model.ExportState
import com.android.monuver.domain.repository.TransactionRepository
import com.android.monuver.utils.TransactionType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class ExportDataToPdfUseCase(
    private val dataManager: DataManager,
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(exportState: ExportState): Flow<ExportStatusState>  {
        if (exportState.title.isEmpty()) return flowOf(
            ExportStatusState.Error(DatabaseResultState.EmptyReportTitle)
        )
        if (exportState.username.isEmpty()) return flowOf(
            ExportStatusState.Error(DatabaseResultState.EmptyReportUsername)
        )
        if (exportState.startDate.isEmpty()) return flowOf(
            ExportStatusState.Error(DatabaseResultState.EmptyReportStartDate)
        )
        if (exportState.endDate.isEmpty()) return flowOf(
            ExportStatusState.Error(DatabaseResultState.EmptyReportEndDate)
        )

        return flow {
            emit(ExportStatusState.Progress)

            delay(3000)

            val transactions = when {
                exportState.sortType == 1 && exportState.isIncomeExpenseGrouped -> {
                    transactionRepository.getTransactionsInRangeByDateAscWithType(
                        startDate = exportState.startDate,
                        endDate = exportState.endDate
                    )
                }
                exportState.sortType == 1 && !exportState.isIncomeExpenseGrouped -> {
                    transactionRepository.getTransactionsInRangeByDateAsc(
                        startDate = exportState.startDate,
                        endDate = exportState.endDate
                    )
                }
                exportState.sortType == 2 && exportState.isTransferIncluded -> {
                    transactionRepository.getTransactionsInRangeByDateDescWithType(
                        startDate = exportState.startDate,
                        endDate = exportState.endDate
                    )
                }
                else -> {
                    transactionRepository.getTransactionsInRangeByDateDesc(
                        startDate = exportState.startDate,
                        endDate = exportState.endDate
                    )
                }
            }

            val commonTransactions = transactions.filter {
                it.type == TransactionType.INCOME || it.type == TransactionType.EXPENSE
            }

            val transferTransactions = if (exportState.isTransferIncluded) {
                transactions.filter { it.type == TransactionType.TRANSFER }
            } else {
                emptyList()
            }

            val totalIncome = transactionRepository.getTotalIncomeTransactionInRange(
                startDate = exportState.startDate,
                endDate = exportState.endDate
            )
            val totalExpense = transactionRepository.getTotalExpenseTransactionInRange(
                startDate = exportState.startDate,
                endDate = exportState.endDate
            )

            try {
                dataManager.exportDataToPdf(
                    reportName = exportState.title,
                    username = exportState.username,
                    startDate = exportState.startDate,
                    endDate = exportState.endDate,
                    commonTransactions = commonTransactions,
                    transferTransactions = transferTransactions,
                    totalIncome = totalIncome ?: 0,
                    totalExpense = totalExpense ?: 0
                )

                emit(ExportStatusState.Success)
            } catch (_: Exception) {
                emit(ExportStatusState.Error(DatabaseResultState.ExportDataFailed))
            }
        }
    }
}