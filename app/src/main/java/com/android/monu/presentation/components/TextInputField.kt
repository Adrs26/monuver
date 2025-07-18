package com.android.monu.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R

@Composable
fun TextInputField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier,
    isEnable: Boolean = true,
    isDatePicker: Boolean = false
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
        )
        Box(
            modifier = Modifier.border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 4.dp),
                enabled = isEnable,
                textStyle = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp
                ),
                placeholder = {
                    Text(
                        text = placeholderText,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    )
                },
                trailingIcon = {
                    when {
                        !isEnable && isDatePicker -> {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        !isEnable -> {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_forward),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        else -> {}
                    }
                },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onBackground,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}