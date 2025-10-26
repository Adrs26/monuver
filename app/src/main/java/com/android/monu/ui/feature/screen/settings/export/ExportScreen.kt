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
import com.android.monu.domain.usecase.finance.ExportDataState
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.settings.components.FirstActionConfirmation
import com.android.monu.ui.feature.screen.settings.export.components.ExportBottomBar
import com.android.monu.ui.feature.screen.settings.export.components.ExportContent
import com.android.monu.ui.feature.screen.settings.export.components.ExportContentActions
import com.android.monu.ui.feature.screen.settings.export.components.ExportContentState
import com.android.monu.ui.feature.screen.settings.export.components.ExportProgressDialog
import com.android.monu.ui.feature.utils.showMessageWithToast
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
    exportProgress: ExportDataState,
    isFirstExport: Boolean,
    onSetFirstExportToFalse: () -> Unit,
    onNavigateBack: () -> Unit,
    onExportData: (ExportContentState) -> Unit
) {
    var reportTitle by remember { mutableStateOf("") }
    var reportUsername by remember { mutableStateOf("") }
    var reportStartDate by remember { mutableStateOf("") }
    var reportEndDate by remember { mutableStateOf("") }
    var transactionSortType by remember { mutableIntStateOf(1) }
    var isTransactionSeparate by remember { mutableStateOf(false) }
    var isTransferTransactionIncluded by remember { mutableStateOf(false) }
    var activeField by rememberSaveable { mutableStateOf<CalendarField?>(null) }

    var showFirstExportDialog by remember { mutableStateOf(false) }
    var showExportProgressDialog by remember { mutableStateOf(false) }

    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    LaunchedEffect(exportProgress) {
        when (exportProgress) {
            is ExportDataState.Idle -> {}
            is ExportDataState.Loading -> showExportProgressDialog = true
            is ExportDataState.Success -> {
                showExportProgressDialog = false
                onNavigateBack()
                context.getString(R.string.data_successfully_exported).showMessageWithToast(context)
            }
            is ExportDataState.Error -> {
                context.getString(exportProgress.error.message).showMessageWithToast(context)
            }
        }
    }

    val exportContentState = ExportContentState(
        title = reportTitle,
        username = reportUsername,
        startDate = reportStartDate,
        endDate = reportEndDate,
        sortType = transactionSortType,
        isSeparate = isTransactionSeparate,
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

        override fun onSeparateChange(isSeparate: Boolean) {
            isTransactionSeparate = isSeparate
        }

        override fun onTransferIncludedChange(isTransferIncluded: Boolean) {
            isTransferTransactionIncluded = isTransferIncluded
        }
    }

    val storagePermissionState = rememberPermissionState(
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
        onPermissionResult = { isGranted ->
            if (isGranted) {
                onExportData(exportContentState)
            } else {
                "Izin akses penyimpanan diperlukan untuk melakukan backup data".showMessageWithToast(context)
            }
        }
    )

    Scaffold(
        topBar = {
            CommonAppBar(
                title = "Ekspor data",
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
                        onExportData(exportContentState)
                    }
                }
            }
        }
    ) { innerPadding ->
        ExportContent(
            exportState = exportContentState,
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
                    if (isAfterToday) {
                        context.getString(R.string.you_can_not_select_future_start_date)
                            .showMessageWithToast(context)
                    } else {
                        reportStartDate = selectedDate.toString()
                    }
                }
                CalendarField.END -> {
                    val startDate = LocalDate.parse(reportStartDate, DateTimeFormatter.ISO_LOCAL_DATE)
                    val isBeforeStartDate = inputDate.isBefore(startDate)

                    if (isBeforeStartDate) {
                        context.getString(R.string.end_date_must_be_same_or_after_start_date)
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
                    onExportData(exportContentState)
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