package com.android.monu.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily

@Composable
fun AmountInputField(
    title: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .border(width = 1.dp, color = SoftGrey, shape = RoundedCornerShape(16.dp))
                .height(50.dp),
            textStyle = TextStyle(
                fontSize = 13.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            ),
            placeholder = {
                Text(
                    text = placeholderText,
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_rupiah_currency),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
    }
}