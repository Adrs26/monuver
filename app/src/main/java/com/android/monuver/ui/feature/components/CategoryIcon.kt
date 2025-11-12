package com.android.monuver.ui.feature.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.monuver.ui.theme.SoftWhite

@Composable
fun CategoryIcon(
    icon: Int,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    iconSize: Int = 24
) {
    Box(
        modifier = modifier.background(
            color = backgroundColor,
            shape = MaterialTheme.shapes.extraSmall
        ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(iconSize.dp),
            tint = SoftWhite
        )
    }
}