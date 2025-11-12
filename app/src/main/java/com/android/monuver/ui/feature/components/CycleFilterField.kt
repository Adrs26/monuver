package com.android.monuver.ui.feature.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.R
import com.android.monuver.ui.feature.utils.DatabaseCodeMapper

@Composable
fun CycleFilterField(
    cycles: List<Int>,
    selectedCycle: Int,
    onCycleChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.cycle),
            modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = MaterialTheme.shapes.extraSmall
                )
        ) {
            cycles.forEach { cycle ->
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(
                            if (cycle == selectedCycle) MaterialTheme.colorScheme.primary else
                                MaterialTheme.colorScheme.background,
                        )
                        .clickable { onCycleChange(cycle) }
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(DatabaseCodeMapper.toCycle(cycle)),
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (cycle == selectedCycle) MaterialTheme.colorScheme.onPrimary else
                                MaterialTheme.colorScheme.onBackground,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}