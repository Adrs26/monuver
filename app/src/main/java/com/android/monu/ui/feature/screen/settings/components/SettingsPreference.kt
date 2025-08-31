package com.android.monu.ui.feature.screen.settings.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.data.datastore.ThemeSetting

@Composable
fun SettingsPreference(
    isNotificationEnabled: Boolean,
    themeSetting: ThemeSetting,
    onNotificationClicked: () -> Unit,
    onThemeClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.preference),
            modifier = Modifier.padding(vertical = 8.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .clickable { onNotificationClicked() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.notification),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 13.sp)
                )
                Switch(
                    checked = isNotificationEnabled,
                    onCheckedChange = {},
                    modifier = Modifier.height(24.dp),
                    thumbContent = if (isNotificationEnabled) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                    enabled = false,
                    colors = SwitchDefaults.colors(
                        disabledUncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                        disabledUncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledUncheckedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledCheckedThumbColor = MaterialTheme.colorScheme.background,
                        disabledCheckedTrackColor = MaterialTheme.colorScheme.primary,
                        disabledCheckedIconColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .clickable { onThemeClicked() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_palette),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.theme),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 13.sp)
                )
                Text(
                    text = when (themeSetting) {
                        ThemeSetting.LIGHT -> stringResource(R.string.light)
                        ThemeSetting.DARK -> stringResource(R.string.dark)
                        ThemeSetting.SYSTEM -> stringResource(R.string.default_system)
                    },
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}