package com.android.monu.ui.feature.screen.saving.savingDetail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monu.R

@Composable
fun SavingDetailBottomBar(
    onCompleteSaving: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceVariant,
            thickness = 1.dp
        )
        Button(
            onClick = onCompleteSaving,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.settled),
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium
             )
        }
    }
}