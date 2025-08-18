package com.android.monu.presentation.screen.home.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.presentation.components.DebouncedIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    onNavigateToBill: () -> Unit,
    onNavigateToSave: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )
            )
        },
        actions = {
            HomeIconButton(
                icon = R.drawable.ic_overview,
                onClick = onNavigateToBill
            )
            HomeIconButton(
                icon = R.drawable.ic_savings,
                onClick = onNavigateToSave
            )
            HomeIconButton(
                icon = R.drawable.ic_settings,
                onClick = onNavigateToSettings
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun HomeIconButton(
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DebouncedIconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}