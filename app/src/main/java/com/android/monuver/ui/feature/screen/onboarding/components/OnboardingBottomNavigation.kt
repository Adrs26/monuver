package com.android.monuver.ui.feature.screen.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monuver.R

@Composable
fun OnboardingBottomNavigation(
    pagerState: PagerState,
    onNext: () -> Unit,
    onBack: () -> Unit,
    totalPages: Int,
    modifier: Modifier = Modifier
) {
    val currentPage = pagerState.currentPage
    val isLastPage = currentPage == totalPages - 1

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onBack,
            modifier = Modifier.weight(0.5f),
            enabled = currentPage > 0
        ) {
            Text(
                text = stringResource(R.string.back),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = if (currentPage > 0) MaterialTheme.colorScheme.onSurfaceVariant else
                        MaterialTheme.colorScheme.background
                )
            )
        }
        PageIndicator(
            totalPages = totalPages,
            currentPage = currentPage,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
        TextButton(
            onClick = onNext,
            modifier = Modifier.weight(0.5f)
        ) {
            Text(
                text = if (isLastPage) stringResource(R.string.start) else stringResource(R.string.next),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
fun PageIndicator(
    totalPages: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalPages) { index ->
            val color = if (index == currentPage) MaterialTheme.colorScheme.primary else
                MaterialTheme.colorScheme.surfaceVariant

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(color)
            )
        }
    }
}