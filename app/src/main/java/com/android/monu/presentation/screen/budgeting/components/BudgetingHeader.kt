package com.android.monu.presentation.screen.budgeting.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.ui.theme.Green
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily

@Composable
fun BudgetingHeader(
    modifier: Modifier = Modifier
) {
    val progress by remember { mutableFloatStateOf(0.5f) }

    Card(
        modifier = modifier
            .border(width = 1.dp, color = SoftGrey, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Sisa saldo budgeting kamu",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
            Row(
                modifier = Modifier.padding(top = 32.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "Rp25 Juta",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                )
                VerticalDivider(
                    modifier = Modifier
                        .height(16.dp)
                        .padding(bottom = 4.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                Text(
                    text = "Rp50 Juta",
                    modifier = Modifier.padding(bottom = 2.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                )
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .height(12.dp),
                color = Green,
                trackColor = Color.LightGray,
                strokeCap = StrokeCap.Square,
                gapSize = 0.dp,
                drawStopIndicator = { null }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetingHeaderPreview() {
    BudgetingHeader()
}