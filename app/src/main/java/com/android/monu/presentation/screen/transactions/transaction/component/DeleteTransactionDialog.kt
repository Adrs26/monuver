package com.android.monu.presentation.screen.transactions.transaction.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.monu.R
import com.android.monu.presentation.components.ActionButton
import com.android.monu.presentation.components.OutlinedActionButton
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.interFontFamily

@Composable
fun DeleteTransactionDialog(
    modifier: Modifier = Modifier,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.delete_this_transaction),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedActionButton(
                        text = stringResource(R.string.cancel),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .padding(end = 4.dp),
                        onClick = onDismissRequest
                    )
                    ActionButton(
                        text = stringResource(R.string.delete),
                        color = Blue,
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .padding(start = 4.dp),
                        onClick = {
                            onConfirmClick()
                            onDismissRequest()
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteTransactionDialogPreview() {
    DeleteTransactionDialog(
        onConfirmClick = {},
        onDismissRequest = {}
    )
}