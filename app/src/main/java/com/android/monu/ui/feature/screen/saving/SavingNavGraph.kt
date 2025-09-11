package com.android.monu.ui.feature.screen.saving

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.android.monu.ui.feature.screen.saving.addsave.AddSaveScreen
import com.android.monu.ui.feature.screen.saving.addsave.AddSaveViewModel
import com.android.monu.ui.feature.screen.saving.savedetail.SaveDetailScreen
import com.android.monu.ui.feature.screen.saving.savedetail.SaveDetailViewModel
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.navigation.AddSave
import com.android.monu.ui.navigation.MainSaving
import com.android.monu.ui.navigation.SaveDetail
import com.android.monu.ui.navigation.Saving
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

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
            val viewModel = koinViewModel<SavingViewModel>()
            val totalCurrentAmount by viewModel.totalCurrentAmount.collectAsStateWithLifecycle()
            val saves by viewModel.saves.collectAsStateWithLifecycle()

            val saveActions = object : SaveActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNavigateToAddSave() {
                    navController.navigate(AddSave)
                }

                override fun onNavigateToSaveDetail(saveId: Long) {
                    navController.navigate(SaveDetail(saveId))
                }
            }

            SavingScreen(
                totalCurrentAmount = totalCurrentAmount ?: 0,
                saves = saves,
                saveActions = saveActions
            )
        }
        composable<AddSave>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<AddSaveViewModel>()
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()

            AddSaveScreen(
                addResult = addResult,
                onNavigateBack = navController::navigateUp,
                onAddNewSave = viewModel::createNewSave
            )
        }
        composable<SaveDetail>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<SaveDetailViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val save by viewModel.save.collectAsStateWithLifecycle()

            save?.let { save ->
                SaveDetailScreen(
                    save = save,
                    onNavigateBack = navController::navigateUp
                )
            }
        }
    }
}