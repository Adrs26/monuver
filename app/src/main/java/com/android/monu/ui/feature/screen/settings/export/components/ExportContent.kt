package com.android.monu.ui.feature.screen.settings.export.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.ui.feature.components.StaticTextInputField
import com.android.monu.ui.feature.components.TextDateInputField
import com.android.monu.ui.feature.components.TextInputField
import com.android.monu.ui.feature.components.TextWithSwitch
import com.android.monu.ui.feature.utils.DateHelper

@Composable
fun ExportContent(
    exportState: ExportContentState,
    exportActions: ExportContentActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TextInputField(
            title = "Judul laporan",
            value = exportState.title,
            onValueChange = exportActions::onTitleChange,
            placeholderText = "Masukkan judul laporan",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        TextInputField(
            title = "Nama pengguna",
            value = exportState.username,
            onValueChange = exportActions::onUserNameChange,
            placeholderText = "Masukkan nama pengguna",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
        TextDateInputField(
            title = "Periode mulai laporan",
            value = DateHelper.formatDateToReadable(exportState.startDate),
            onValueChange = { },
            placeholderText = "Pilih periode mulai laporan",
            isEnable = true,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = exportActions::onStartDateClick
                )
                .padding(horizontal = 16.dp)
        )
        TextDateInputField(
            title = "Periode akhir laporan",
            value = DateHelper.formatDateToReadable(exportState.endDate),
            onValueChange = { },
            placeholderText = "Pilih periode akhir laporan",
            isEnable = true,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = exportActions::onEndDateClick
                )
                .padding(horizontal = 16.dp)
        )
        StaticTextInputField(
            title = "Format laporan",
            value = "PDF",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        SortDateTypeRadioGroupField(
            selectedSortType = exportState.sortType,
            onSortTypeSelect = exportActions::onSortTypeChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            TextWithSwitch(
                text = "Kelompokkan pemasukan dan pengeluaran",
                checked = exportState.isSeparate,
                onCheckedChange = exportActions::onSeparateChange,
                isEnable = true
            )
            TextWithSwitch(
                text = "Sertakan transaksi transfer",
                checked = exportState.isTransferIncluded,
                onCheckedChange = exportActions::onTransferIncludedChange,
                isEnable = true
            )
        }
    }
}

@Composable
fun SortDateTypeRadioGroupField(
    selectedSortType: Int,
    onSortTypeSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Urutkan mulai dari",
            modifier = Modifier.padding(bottom = 12.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedSortType == 1,
                    onClick = { onSortTypeSelect(1) },
                    modifier = Modifier.size(40.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = "Tanggal terlama",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 13.sp
                    )
                )
            }
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedSortType == 2,
                    onClick = { onSortTypeSelect(2) },
                    modifier = Modifier.size(40.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = "Tanggal terbaru",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 13.sp
                    )
                )
            }
        }
    }
}

data class ExportContentState(
    val title: String,
    val username: String,
    val startDate: String,
    val endDate: String,
    val sortType: Int,
    val isSeparate: Boolean,
    val isTransferIncluded: Boolean
)

interface ExportContentActions {
    fun onTitleChange(title: String)
    fun onUserNameChange(username: String)
    fun onStartDateClick()
    fun onEndDateClick()
    fun onSortTypeChange(sortType: Int)
    fun onSeparateChange(isSeparate: Boolean)
    fun onTransferIncludedChange(isTransferIncluded: Boolean)
}