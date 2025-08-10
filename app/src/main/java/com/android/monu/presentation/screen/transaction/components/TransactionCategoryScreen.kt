package com.android.monu.presentation.screen.transaction.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.presentation.components.CategoryIcon
import com.android.monu.presentation.components.CommonAppBar
import com.android.monu.presentation.utils.DataProvider
import com.android.monu.presentation.utils.DatabaseCodeMapper
import com.android.monu.presentation.utils.TransactionType

@Composable
fun TransactionCategoryScreen(
    transactionType: Int,
    onNavigateBack: () -> Unit,
    onCategorySelect: (Int, Int) -> Unit
) {
    val transactionCategory = if (transactionType == TransactionType.INCOME)
        DataProvider.getIncomeCategories() else DataProvider.getExpenseCategories()

    Scaffold(
        topBar = {
            CommonAppBar(
                title = if (transactionType == TransactionType.INCOME)
                    stringResource(R.string.choose_income_category) else
                        stringResource(R.string.choose_expense_category),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            transactionCategory.forEach { (parentCategory, childCategories) ->
                CategorySection(
                    transactionType = transactionType,
                    parentCategory = parentCategory,
                    childCategories = childCategories,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    onNavigateBack = onNavigateBack,
                    onCategorySelect = onCategorySelect
                )
            }
        }
    }
}

@Composable
fun CategorySection(
    transactionType: Int,
    parentCategory: Int,
    childCategories: List<Int>,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onCategorySelect: (Int, Int) -> Unit
) {
    val spacerWeight = when {
        childCategories.size % 4 == 1 -> 3f
        childCategories.size % 4 == 2 -> 2f
        childCategories.size % 4 == 3 -> 1f
        else -> 0f
    }

    Card(
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(16.dp)
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(DatabaseCodeMapper.toParentCategoryTitle(parentCategory)),
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp)
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                maxItemsInEachRow = 4
            ) {
                childCategories.forEach { childCategory ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                if (transactionType == TransactionType.INCOME) {
                                    onCategorySelect(childCategory, childCategory)
                                } else {
                                    onCategorySelect(parentCategory, childCategory)
                                }
                                onNavigateBack()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CategoryIcon(
                            icon = DatabaseCodeMapper.toChildCategoryIcon(childCategory),
                            backgroundColor = DatabaseCodeMapper.toParentCategoryIconBackground(
                                if (transactionType == TransactionType.INCOME) childCategory else
                                    parentCategory
                            ),
                            modifier = modifier.size(42.dp)
                        )
                        Text(
                            text = stringResource(DatabaseCodeMapper.toChildCategoryTitle(childCategory)),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
                if (spacerWeight > 0) {
                    Spacer(modifier = Modifier.weight(spacerWeight))
                }
            }
        }
    }
}