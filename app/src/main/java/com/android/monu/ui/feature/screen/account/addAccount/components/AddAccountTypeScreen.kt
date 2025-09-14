package com.android.monu.ui.feature.screen.account.addAccount.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.ui.feature.components.CategoryIcon
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.utils.DataProvider
import com.android.monu.ui.feature.utils.DatabaseCodeMapper

@Composable
fun AddAccountTypeScreen(
    onNavigateBack: () -> Unit,
    onTypeSelect: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.choose_account_type),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            DataProvider.getAccountTypes().forEach { accountType ->
                AccountTypeListItem(
                    accountType = accountType,
                    modifier = Modifier
                        .clickable {
                            onTypeSelect(accountType)
                            onNavigateBack()
                        }
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun AccountTypeListItem(
    accountType: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIcon(
                icon = DatabaseCodeMapper.toAccountTypeIcon(accountType),
                backgroundColor = DatabaseCodeMapper.toAccountTypeColor(accountType),
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = stringResource(DatabaseCodeMapper.toAccountType(accountType)),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 13.sp
                )
            )
        }
    }
}