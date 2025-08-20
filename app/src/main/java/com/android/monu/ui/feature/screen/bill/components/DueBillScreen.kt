package com.android.monu.ui.feature.screen.bill.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DueBillScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(2) {
            BillListItem(
                billStatus = 2,
                modifier = Modifier
                    .clickable { }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}