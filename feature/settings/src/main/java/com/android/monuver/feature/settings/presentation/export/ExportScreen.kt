package com.android.monuver.feature.settings.presentation.export

import android.Manifest
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.PrimaryActionButton
import com.android.monuver.core.presentation.components.StaticTextInputField
import com.android.monuver.core.presentation.components.TextDateInputField
import com.android.monuver.core.presentation.components.TextInputField
import com.android.monuver.core.presentation.components.TextWithSwitch
import com.android.monuver.core.presentation.util.showMessageWithToast
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.feature.settings.R
import com.android.monuver.feature.settings.domain.common.ExportStatusState
import com.android.monuver.feature.settings.domain.model.ExportState
import com.android.monuver.feature.settings.presentation.components.SettingsFirstActionConfirmationDialog
import com.android.monuver.feature.settings.presentation.export.components.ExportProgressDialog
import com.android.monuver.feature.settings.presentation.export.components.ExportSortTypeRadioGroupField
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalPermissionsApi::class, ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
internal fun ExportScreen(
    exportStatus: ExportStatusState,
    isFirstExport: Boolean,
    onNavigateBack: () -> Unit,
    onSetFirstExportToFalse: () -> Unit,
    onExportData: (ExportState) -> Unit
) {
    val context = LocalContext.current

    var title by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var startDate by rememberSaveable { mutableStateOf("") }
    var endDate by rememberSaveable { mutableStateOf("") }
    var sortType by rememberSaveable { mutableIntStateOf(1) }
    var isTransactionGrouped by rememberSaveable { mutableStateOf(false) }
    var isTransferIncluded by rememberSaveable { mutableStateOf(false) }
    var activeField by rememberSaveable { mutableStateOf<CalendarField?>(null) }

    var showFirstExportDialog by remember { mutableStateOf(false) }
    var showExportProgressDialog by remember { mutableStateOf(false) }

    val calendarState = rememberUseCaseState()

    val storagePermissionState = rememberPermissionState(
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
        onPermissionResult = { isGranted ->
            if (isGranted) {
                onExportData(
                    ExportState(
                        title = title,
                        username = username,
                        startDate = startDate,
                        endDate = endDate,
                        sortType = sortType,
                        isTransactionGrouped = isTransactionGrouped,
                        isTransferIncluded = isTransferIncluded
                    )
                )
            } else {
                context.getString(R.string.write_permission_storage_is_required_to_export_data)
                    .showMessageWithToast(context)
            }
        }
    )

    LaunchedEffect(exportStatus) {
        when (exportStatus) {
            is ExportStatusState.Idle -> {}
            is ExportStatusState.Progress -> showExportProgressDialog = true
            is ExportStatusState.Success -> {
                showExportProgressDialog = false
                onNavigateBack()
                context.getString(R.string.data_successfully_exported).showMessageWithToast(context)
            }
            is ExportStatusState.Error -> exportStatus.error.showToast(context)
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
            PrimaryActionButton(
                text = stringResource(R.string.export),
                onClick = {
                    if (isFirstExport) {
                        showFirstExportDialog = true
                    } else {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                            !storagePermissionState.status.isGranted) {
                            storagePermissionState.launchPermissionRequest()
                        } else {
                            onExportData(
                                ExportState(
                                    title = title,
                                    username = username,
                                    startDate = startDate,
                                    endDate = endDate,
                                    sortType = sortType,
                                    isTransactionGrouped = isTransactionGrouped,
                                    isTransferIncluded = isTransferIncluded
                                )
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TextInputField(
                title = stringResource(R.string.report_title),
                value = title,
                onValueChange = { title = it },
                placeholderText = stringResource(R.string.enter_report_title),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            TextInputField(
                title = stringResource(R.string.report_username),
                value = username,
                onValueChange = { username = it },
                placeholderText = stringResource(R.string.enter_report_username),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            TextDateInputField(
                title = stringResource(R.string.report_start_period),
                value = DateHelper.formatToReadable(startDate),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_report_start_period),
                isEnable = true,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            calendarState.show()
                            activeField = CalendarField.START
                        }
                    )
                    .padding(horizontal = 16.dp)
            )
            TextDateInputField(
                title = stringResource(R.string.report_end_period),
                value = DateHelper.formatToReadable(endDate),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_report_end_period),
                isEnable = true,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            if (startDate.isEmpty()) {
                                context.getString(R.string.select_start_date_period_first)
                                    .showMessageWithToast(context)
                            } else {
                                calendarState.show()
                                activeField = CalendarField.END
                            }
                        }
                    )
                    .padding(horizontal = 16.dp)
            )
            StaticTextInputField(
                title = stringResource(R.string.report_format),
                value = "PDF",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            ExportSortTypeRadioGroupField(
                selectedSortType = sortType,
                onSortTypeSelect = { sortType = it },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                TextWithSwitch(
                    text = stringResource(R.string.group_income_expense),
                    checked = isTransactionGrouped,
                    onCheckedChange = { isTransactionGrouped = it },
                    isEnable = true
                )
                TextWithSwitch(
                    text = stringResource(R.string.include_transfer_transaction),
                    checked = isTransferIncluded,
                    onCheckedChange = { isTransferIncluded = it },
                    isEnable = true
                )
            }
        }
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            val inputDate = LocalDate.parse(selectedDate.toString())
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

            when (activeField) {
                CalendarField.START -> {
                    var isAfterEndDate = false
                    if (startDate.isNotEmpty()) {
                        val endDate = LocalDate.parse(startDate)
                        isAfterEndDate = inputDate > endDate
                    }

                    if (inputDate > today) {
                        context.getString(R.string.you_can_not_select_future_start_period)
                            .showMessageWithToast(context)
                    } else if (isAfterEndDate) {
                        context.getString(R.string.start_period_can_not_after_end_period)
                            .showMessageWithToast(context)
                    } else {
                        startDate = selectedDate.toString()
                    }
                }
                CalendarField.END -> {
                    val startDate = LocalDate.parse(startDate)

                    if (inputDate < startDate) {
                        context.getString(R.string.end_period_must_be_same_or_after_start_period)
                            .showMessageWithToast(context)
                    } else {
                        endDate = selectedDate.toString()
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
        SettingsFirstActionConfirmationDialog(
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
                    onExportData(
                        ExportState(
                            title = title,
                            username = username,
                            startDate = startDate,
                            endDate = endDate,
                            sortType = sortType,
                            isTransactionGrouped = isTransactionGrouped,
                            isTransferIncluded = isTransferIncluded
                        )
                    )
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