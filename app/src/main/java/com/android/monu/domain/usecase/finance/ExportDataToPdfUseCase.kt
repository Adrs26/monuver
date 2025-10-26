package com.android.monu.domain.usecase.finance

import com.android.monu.domain.manager.DataManager
import com.android.monu.domain.repository.TransactionRepository
import com.android.monu.ui.feature.screen.settings.export.components.ExportContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.TransactionType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class ExportDataToPdfUseCase(
    private val dataManager: DataManager,
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(exportState: ExportContentState): Flow<ExportDataState>  {
        if (exportState.title.isEmpty()) return flowOf(ExportDataState.Error(DatabaseResultMessage.EmptyReportTitle))
        if (exportState.username.isEmpty()) return flowOf(ExportDataState.Error(DatabaseResultMessage.EmptyReportUsername))
        if (exportState.startDate.isEmpty()) return flowOf(ExportDataState.Error(DatabaseResultMessage.EmptyReportStartDate))
        if (exportState.endDate.isEmpty()) return flowOf(ExportDataState.Error(DatabaseResultMessage.EmptyReportEndDate))

        return flow {
            emit(ExportDataState.Loading)

            delay(3000)

            val transactions = when {
                exportState.sortType == 1 && exportState.isSeparate -> {
                    transactionRepository.getTransactionsInRangeByDateAscWithType(
                        startDate = exportState.startDate,
                        endDate = exportState.endDate
                    )
                }
                exportState.sortType == 1 && !exportState.isSeparate -> {
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

                emit(ExportDataState.Success)
            } catch (_: Exception) {
                emit(ExportDataState.Error(DatabaseResultMessage.ExportDataFailed))
            }
        }
    }
}

sealed class ExportDataState {
    object Idle : ExportDataState()
    object Loading : ExportDataState()
    object Success : ExportDataState()
    data class Error(val error: DatabaseResultMessage) : ExportDataState()
}