package com.android.monu.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.interFontFamily

@Composable
fun OutlinedActionButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 13.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Blue
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OutlinedActionButtonPreview() {
    OutlinedActionButton(
        text = "Outlined",
        onClick = {}
    )
}