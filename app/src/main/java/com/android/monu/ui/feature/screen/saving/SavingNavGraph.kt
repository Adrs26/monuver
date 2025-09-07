package com.android.monu.ui.feature.screen.saving

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.android.monu.ui.feature.screen.saving.savedetail.SaveDetailScreen
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.navigation.MainSaving
import com.android.monu.ui.navigation.SaveDetail
import com.android.monu.ui.navigation.Saving

fun NavGraphBuilder.savingNavGraph(
    navController: NavHostController
) {
    navigation<Saving>(startDestination = MainSaving) {
        composable<MainSaving>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            SavingScreen(
                onNavigateBack = navController::navigateUp,
                onNavigateToSaveDetail = { navController.navigate(SaveDetail(0)) }
            )
        }
        composable<SaveDetail>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            SaveDetailScreen(
                onNavigateBack = navController::navigateUp
            )
        }
    }
}