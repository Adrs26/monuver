package com.android.monu.presentation.screen.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    transactions: LazyPagingItems<TransactionConcise>,
    filterState: FilterStateData,
    filterCallbacks: FilterCallbacks,
    navigateToAddIncome: () -> Unit,
    navigateToAddExpense: () -> Unit,
    navigateToEditTransaction: (Long) -> Unit
) {
    var showFilterDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val transactionFilterData = TransactionFilterData(
        selectedType = filterState.selectedType,
        selectedYear = filterState.selectedYear,
        selectedMonth = filterState.selectedMonth
    )

    LaunchedEffect(
        key1 = filterState.searchQuery,
        key2 = filterState.selectedType,
        key3 = Pair(filterState.selectedYear, filterState.selectedMonth),
    ) {
        delay(50)
        listState.animateScrollToItem(0)
    }

    LaunchedEffect(showFilterDialog) {
        if (showFilterDialog == true) {
            filterCallbacks.onFilterYearMonthClick()
        }
    }

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
                query = filterState.searchQuery,
                onQueryChange = filterCallbacks.onSearchQueryChange,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            TransactionsFilterBar(
                transactionFilterData = transactionFilterData,
                modifier = Modifier.padding(start = 16.dp, end = 12.dp, bottom = 8.dp),
                onFilterTypeClick = filterCallbacks.onFilterTypeClick,
                onFilterIconClick = { showFilterDialog = true }
            )
            TransactionsList(
                transactions = transactions,
                listState = listState,
                navigateToEditTransaction = navigateToEditTransaction
            )
        }
    }

    if (showFilterDialog) {
        TransactionsFilterDialog(
            availableTransactionYears = filterState.availableTransactionYears,
            transactionFilterData = transactionFilterData,
            onApplyClick = filterCallbacks.onFilterYearMonthApply,
            onDismissRequest = { showFilterDialog = false },
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

data class FilterStateData(
    val searchQuery: String,
    val selectedType: Int?,
    val availableTransactionYears: List<Int>,
    val selectedYear: Int?,
    val selectedMonth: Int?
)

data class FilterCallbacks(
    val onSearchQueryChange: (String) -> Unit,
    val onFilterTypeClick: (Int?) -> Unit,
    val onFilterYearMonthClick: () -> Unit,
    val onFilterYearMonthApply: (Int?, Int?) -> Unit
)

data class TransactionFilterData(
    val selectedType: Int?,
    val selectedYear: Int?,
    val selectedMonth: Int?
)