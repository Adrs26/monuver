package com.android.monuver.onboarding

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.android.monuver.R
import com.android.monuver.onboarding.components.OnboardingBottomNavigation
import com.android.monuver.onboarding.components.OnboardingPageContent
import kotlinx.coroutines.launch

@Composable
internal fun OnboardingScreen(
    onFinishOnboarding: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { OnboardingPage.entries.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            OnboardingBottomNavigation(
                pagerState = pagerState,
                totalPages = OnboardingPage.entries.size,
                onNext = {
                    if (pagerState.currentPage == OnboardingPage.entries.size - 1) {
                        onFinishOnboarding()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                onBack = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding)
        ) { index ->
            OnboardingPageContent(
                page = OnboardingPage.entries[index]
            )
        }
    }
}

enum class OnboardingPage(
    val title: Int,
    val description: Int,
    val animation: Int
) {
    FIRST(
        title = R.string.onboarding_first_title,
        description = R.string.onboarding_first_description,
        animation = R.raw.transaction
    ),
    SECOND(
        title = R.string.onboarding_second_title,
        description = R.string.onboarding_second_description,
        animation = R.raw.budget
    ),
    THIRD(
        title = R.string.onboarding_third_title,
        description = R.string.onboarding_third_description,
        animation = R.raw.chart
    ),
    FOURTH(
        title = R.string.onboarding_fourth_title,
        description = R.string.onboarding_fourth_description,
        animation = R.raw.checklist
    )
}