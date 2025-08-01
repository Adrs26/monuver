package com.android.monu.presentation.screen.budgeting.budgetingdetail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.presentation.components.TransactionListItem
import com.android.monu.presentation.components.TransactionListState
import com.android.monu.presentation.screen.budgeting.budgetingdetail.components.BudgetingDetailAppBar
import com.android.monu.presentation.utils.TransactionChildCategory
import com.android.monu.presentation.utils.TransactionParentCategory
import com.android.monu.presentation.utils.TransactionType
import com.android.monu.ui.theme.Green600
import com.android.monu.ui.theme.MonuTheme

@Composable
fun BudgetingDetailScreen() {
    val dummyTransaction = TransactionListState(
        id = 0,
        title = "Pembayaran tagihan air",
        type = TransactionType.EXPENSE,
        parentCategory = TransactionParentCategory.BILLS_UTILITIES,
        childCategory = TransactionChildCategory.WATER,
        date = "2025-07-29",
        amount = 1000000L,
        sourceName = "BCA"
    )

    Scaffold(
        topBar = {
            BudgetingDetailAppBar(
                title = "Air",
                onNavigateBack = {},
                onEditClick = {},
                onDeleteClick = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Kategori",
                        modifier = Modifier.weight(1.5f),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        text = ": Tagihan & utilitas",
                        modifier = Modifier.weight(2.5f),
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Jangka waktu",
                        modifier = Modifier.weight(1.5f),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        text = ": 1 Sep 2025 - 30 Sep 2025",
                        modifier = Modifier.weight(2.5f),
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Saldo maksimal",
                        modifier = Modifier.weight(1.5f),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        text = ": Rp10.000.000",
                        modifier = Modifier.weight(2.5f),
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LinearProgressIndicator(
                        progress = { 0.5f },
                        modifier = Modifier
                            .weight(0.8f)
                            .clip(CircleShape)
                            .height(10.dp),
                        color = Green600,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeCap = StrokeCap.Square,
                        gapSize = 0.dp,
                        drawStopIndicator = { null }
                    )
                    Text(
                        text = "50%",
                        modifier = Modifier.padding(start = 16.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Terpakai",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Tersisa",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rp5.000.000",
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Rp5.000.000",
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp)
                    )
                }
            }
            Text(
                text = "Riwayat transaksi",
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp, start = 16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            TransactionListItem(
                transactionState = dummyTransaction,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
            TransactionListItem(
                transactionState = dummyTransaction,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
            TransactionListItem(
                transactionState = dummyTransaction,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
            TransactionListItem(
                transactionState = dummyTransaction,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
            TransactionListItem(
                transactionState = dummyTransaction,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetingDetailScreenPreview() {
    MonuTheme {
        BudgetingDetailScreen()
    }
}