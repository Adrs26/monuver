package com.android.monu.ui.feature.screen.saving.deposit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.domain.model.save.Save
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.components.CommonLottieAnimation
import com.android.monu.ui.feature.screen.saving.components.SaveListItemIcon
import com.android.monu.ui.feature.utils.DateHelper
import com.android.monu.ui.feature.utils.NumberFormatHelper

@Composable
fun DepositDestinationScreen(
    saves: List<Save>,
    onNavigateBack: () -> Unit,
    onDestinationSelect: (Long, String) -> Unit
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.choose_saving),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        if (saves.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                items(
                    count = saves.size,
                    key = { index -> saves[index].id }
                ) { index ->
                    SaveListItem(
                        save = saves[index],
                        modifier = Modifier.clickable {
                            onDestinationSelect(saves[index].id, saves[index].title)
                            onNavigateBack()
                        }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CommonLottieAnimation(lottieAnimation = R.raw.empty)
            }
        }
    }
}

@Composable
fun SaveListItem(
    save: Save,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SaveListItemIcon(
            currentAmount = save.currentAmount,
            targetAmount = save.targetAmount
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = save.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = DateHelper.formatDateToReadable(save.targetDate),
                modifier = Modifier.padding(top = 4.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = NumberFormatHelper.formatToRupiah(save.currentAmount),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 13.sp
                    )
                )
                VerticalDivider(
                    modifier = Modifier
                        .height(12.dp)
                        .padding(bottom = 2.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                Text(
                    text = NumberFormatHelper.formatToRupiah(save.targetAmount),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}