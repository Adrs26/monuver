package com.android.monu.presentation.screen.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.android.monu.R
import com.android.monu.presentation.utils.BudgetWarningCondition
import com.android.monu.presentation.utils.DatabaseCodeMapper

@Composable
fun BudgetWarningDialog(
    budgetCategory: Int,
    budgetWarningCondition: Int,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(getAnimation(budgetWarningCondition))
    )
    val progress by animateLottieCompositionAsState(composition)

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = modifier.padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = getWarningText(budgetWarningCondition, budgetCategory),
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Ya, saya mengerti",
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
                    )
                }
            }
        }
    }
}

private fun getAnimation(budgetWarningCondition: Int): Int {
    return if (budgetWarningCondition == BudgetWarningCondition.LOW_REMAINING_BUDGET) {
        R.raw.low_remaining_budget
    } else {
        R.raw.full_budget
    }
}

@Composable
private fun getWarningText(
    budgetWarningCondition: Int,
    budgetCategory: Int
): String {
    return when (budgetWarningCondition) {
        BudgetWarningCondition.LOW_REMAINING_BUDGET -> {
            "Budget kamu untuk kategori ${stringResource(DatabaseCodeMapper.toParentCategoryTitle(budgetCategory))} hampir habis. Yuk, lebih hemat dalam penggunaannya"
        }
        BudgetWarningCondition.FULL_BUDGET -> {
            "Budget kamu untuk kategori ${stringResource(DatabaseCodeMapper.toParentCategoryTitle(budgetCategory))} sudah habis. Hindari pengeluaran lebih lanjut agar tidak melebihi batas"
        }
        else -> {
            "Pengeluaran kamu sudah melebihi budget pada kategori ${stringResource(DatabaseCodeMapper.toParentCategoryTitle(budgetCategory))}. Coba evaluasi dan atur kembali keuanganmu, ya"
        }
    }
}