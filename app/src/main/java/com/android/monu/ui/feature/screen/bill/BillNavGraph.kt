package com.android.monu.ui.feature.screen.bill

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.android.monu.ui.feature.screen.bill.addbill.AddBillScreen
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.navigation.AddBill
import com.android.monu.ui.navigation.Bill
import com.android.monu.ui.navigation.MainBill

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
            BillScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToAddBill = { navController.navigate(AddBill) }
            )
        }
        composable<AddBill>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            AddBillScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}