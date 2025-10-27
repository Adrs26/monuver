package com.android.monu.ui.feature.screen.settings.export

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.monu.R
import com.android.monu.domain.common.ExportStatusState
import com.android.monu.domain.model.ExportState
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.settings.components.FirstActionConfirmation
import com.android.monu.ui.feature.screen.settings.export.components.ExportBottomBar
import com.android.monu.ui.feature.screen.settings.export.components.ExportContent
import com.android.monu.ui.feature.screen.settings.export.components.ExportContentActions
import com.android.monu.ui.feature.screen.settings.export.components.ExportProgressDialog
import com.android.monu.ui.feature.utils.showMessageWithToast
import com.android.monu.ui.feature.utils.showToast
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ExportScreen(
    exportProgress: ExportStatusState,
    isFirstExport: Boolean,
    onSetFirstExportToFalse: () -> Unit,
    onNavigateBack: () -> Unit,
    onExportData: (ExportState) -> Unit
) {
    var reportTitle by remember { mutableStateOf("") }
    var reportUsername by remember { mutableStateOf("") }
    var reportStartDate by remember { mutableStateOf("") }
    var reportEndDate by remember { mutableStateOf("") }
    var transactionSortType by remember { mutableIntStateOf(1) }
    var isIncomeExpenseTransactionGrouped by remember { mutableStateOf(false) }
    var isTransferTransactionIncluded by remember { mutableStateOf(false) }
    var activeField by rememberSaveable { mutableStateOf<CalendarField?>(null) }

    var showFirstExportDialog by remember { mutableStateOf(false) }
    var showExportProgressDialog by remember { mutableStateOf(false) }

    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    val exportState = ExportState(
        title = reportTitle,
        username = reportUsername,
        startDate = reportStartDate,
        endDate = reportEndDate,
        sortType = transactionSortType,
        isIncomeExpenseGrouped = isIncomeExpenseTransactionGrouped,
        isTransferIncluded = isTransferTransactionIncluded
    )

    val exportContentActions = object : ExportContentActions {
        override fun onTitleChange(title: String) {
            reportTitle = title
        }

        override fun onUserNameChange(username: String) {
            reportUsername = username
        }

        override fun onStartDateClick() {
            calendarState.show()
            activeField = CalendarField.START
        }

        override fun onEndDateClick() {
            if (reportStartDate.isEmpty()) {
                context.getString(R.string.select_start_date_first)
                    .showMessageWithToast(context)

            } else {
                calendarState.show()
                activeField = CalendarField.END
            }
        }

        override fun onSortTypeChange(sortType: Int) {
            transactionSortType = sortType
        }

        override fun onIncomeExpenseGroupedChange(isIncomeExpenseGrouped: Boolean) {
            isIncomeExpenseTransactionGrouped = isIncomeExpenseGrouped
        }

        override fun onTransferIncludedChange(isTransferIncluded: Boolean) {
            isTransferTransactionIncluded = isTransferIncluded
        }
    }

    val storagePermissionState = rememberPermissionState(
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
        onPermissionResult = { isGranted ->
            if (isGranted) {
                onExportData(exportState)
            } else {
                context.getString(R.string.write_permission_storage_is_required_to_export_data)
                    .showMessageWithToast(context)
            }
        }
    )

    LaunchedEffect(exportProgress) {
        when (exportProgress) {
            is ExportStatusState.Idle -> {}
            is ExportStatusState.Progress -> showExportProgressDialog = true
            is ExportStatusState.Success -> {
                showExportProgressDialog = false
                onNavigateBack()
                context.getString(R.string.data_successfully_exported).showMessageWithToast(context)
            }
            is ExportStatusState.Error -> exportProgress.error.showToast(context)
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.export_data),
                onNavigateBack = onNavigateBack
            )
        },
        bottomBar = {
            ExportBottomBar {
                if (isFirstExport) {
                    showFirstExportDialog = true
                } else {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                        !storagePermissionState.status.isGranted) {
                        storagePermissionState.launchPermissionRequest()
                    } else {
                        onExportData(exportState)
                    }
                }
            }
        }
    ) { innerPadding ->
        ExportContent(
            exportState = exportState,
            exportActions = exportContentActions,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            val inputDate = LocalDate.parse(selectedDate.toString(), DateTimeFormatter.ISO_LOCAL_DATE)
            val today = LocalDate.now()
            val isAfterToday = inputDate.isAfter(today)

            when (activeField) {
                CalendarField.START -> {
                    var isAfterEndDate = false
                    if (reportStartDate.isNotEmpty()) {
                        val endDate = LocalDate.parse(reportStartDate, DateTimeFormatter.ISO_LOCAL_DATE)
                        isAfterEndDate = inputDate.isAfter(endDate)
                    }

                    if (isAfterToday) {
                        context.getString(R.string.you_can_not_select_future_start_period)
                            .showMessageWithToast(context)
                    } else if (isAfterEndDate) {
                        context.getString(R.string.start_period_can_not_after_end_period)
                            .showMessageWithToast(context)
                    } else {
                        reportStartDate = selectedDate.toString()
                    }
                }
                CalendarField.END -> {
                    val startDate = LocalDate.parse(reportStartDate, DateTimeFormatter.ISO_LOCAL_DATE)
                    val isBeforeStartDate = inputDate.isBefore(startDate)

                    if (isBeforeStartDate) {
                        context.getString(R.string.end_period_must_be_same_or_after_start_period)
                            .showMessageWithToast(context)
                    } else {
                        reportEndDate = selectedDate.toString()
                    }
                }
                null -> {}
            }
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )

    if (showFirstExportDialog) {
        FirstActionConfirmation(
            text = stringResource(R.string.first_export_confirmation),
            onDismissRequest = {
                showFirstExportDialog = false
                onSetFirstExportToFalse()
            },
            onConfirmRequest = {
                showFirstExportDialog = false
                onSetFirstExportToFalse()
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                    !storagePermissionState.status.isGranted) {
                    storagePermissionState.launchPermissionRequest()
                } else {
                    onExportData(exportState)
                }
            }
        )
    }

    if (showExportProgressDialog) {
        ExportProgressDialog(
            onDismissRequest = { showExportProgressDialog = false }
        )
    }
}

enum class CalendarField { START, END }