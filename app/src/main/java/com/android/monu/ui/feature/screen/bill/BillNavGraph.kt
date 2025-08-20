package com.android.monu.ui.feature.screen.bill

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.android.monu.ui.feature.screen.bill.addbill.AddBillScreen
import com.android.monu.ui.feature.screen.bill.addbill.AddBillViewModel
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.navigation.AddBill
import com.android.monu.ui.navigation.Bill
import com.android.monu.ui.navigation.MainBill
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.billNavGraph(
    navController: NavHostController
) {
    navigation<Bill>(startDestination = MainBill) {
        composable<MainBill>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<BillViewModel>()
            val pendingBills by viewModel.pendingBills.collectAsStateWithLifecycle()
            val dueBills by viewModel.dueBills.collectAsStateWithLifecycle()

            BillScreen(
                pendingBills = pendingBills,
                dueBills = dueBills,
                paidBills = emptyList(),
                onNavigateBack = navController::navigateUp,
                onNavigateToAddBill = { navController.navigate(AddBill) }
            )
        }
        composable<AddBill>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<AddBillViewModel>()
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()

            AddBillScreen(
                addResult = addResult,
                onNavigateBack = navController::navigateUp,
                onAddNewBill = viewModel::createNewBill
            )
        }
    }
}