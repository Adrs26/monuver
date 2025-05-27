package com.android.monu.presentation.screen.budgeting.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.ui.theme.LightGrey
import com.android.monu.ui.theme.interFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetingAppBar(
    navigateBack: () -> Unit,
    onHistoryClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Budgeting",
                modifier = Modifier.padding(start = 8.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
        },
        navigationIcon = {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        },
        actions = {
            IconButton(
                onClick = onHistoryClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_history),
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = LightGrey)
    )
}

@Preview(showBackground = true)
@Composable
fun BudgetingAppBarPreview() {
    BudgetingAppBar(
        navigateBack = { },
        onHistoryClick = { }
    )
}