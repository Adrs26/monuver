package com.android.monu.presentation.screen.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.android.monu.domain.model.TransactionConcise
import com.android.monu.presentation.screen.transactions.components.TransactionsBottomSheetContent
import com.android.monu.presentation.screen.transactions.components.TransactionsFilterBar
import com.android.monu.presentation.screen.transactions.components.TransactionsFilterDialog
import com.android.monu.presentation.screen.transactions.components.TransactionsList
import com.android.monu.presentation.screen.transactions.components.TransactionsSearchBar
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.LightGrey
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    transactions: LazyPagingItems<TransactionConcise>,
    searchQuery: String,
    selectedType: Int?,
    selectedYear: Int?,
    selectedMonth: Int?,
    onSearchQueryChange: (String) -> Unit,
    onFilterTypeClick: (Int?) -> Unit,
    onFilterYearMonthApply: (Int?, Int?) -> Unit,
    navigateToAddIncome: () -> Unit,
    navigateToAddExpense: () -> Unit,
    navigateToEditTransaction: () -> Unit
) {
    var showFilterDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                shape = CircleShape,
                containerColor = Blue
            ) {
                Icon(
                    imageVector =  Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGrey)
                .padding(innerPadding)
        ) {
            TransactionsSearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            TransactionsFilterBar(
                selectedType = selectedType,
                selectedYear = selectedYear,
                selectedMonth = selectedMonth,
                modifier = Modifier.padding(start = 16.dp, end = 12.dp, top = 8.dp, bottom = 4.dp),
                onFilterTypeClick = onFilterTypeClick,
                onFilterIconClick = { showFilterDialog = true }
            )
            TransactionsList(
                transactions = transactions,
                navigateToEditTransaction = navigateToEditTransaction
            )
        }
    }

    if (showFilterDialog) {
        TransactionsFilterDialog(
            listYears = listOf(2025, 2024),
            selectedYear = selectedYear,
            selectedMonth = selectedMonth,
            onDismissRequest = { showFilterDialog = false },
            onApplyClick = onFilterYearMonthApply
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            shape = BottomSheetDefaults.ExpandedShape,
            containerColor = Color.White,
            dragHandle = {
                Box(
                    Modifier
                        .padding(vertical = 8.dp)
                        .size(width = 36.dp, height = 2.dp)
                        .background(Color.Gray, RoundedCornerShape(2.dp))
                )
            }
        ) {
            TransactionsBottomSheetContent(
                navigateToAddIncome = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                            navigateToAddIncome()
                        }
                    }
                },
                navigateToAddExpense = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                            navigateToAddExpense()
                        }
                    }
                }
            )
        }
    }
}