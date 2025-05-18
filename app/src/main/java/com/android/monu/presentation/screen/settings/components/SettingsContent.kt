package com.android.monu.presentation.screen.settings.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        AccountPreferenceMenuSection(modifier = Modifier.padding(bottom = 16.dp))
        PrivacySecurityMenuSection(modifier = Modifier.padding(bottom = 16.dp))
        AboutMenuSection(modifier = Modifier.padding(bottom = 16.dp))
    }
}

@Composable
fun AccountPreferenceMenuSection(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(width = 1.dp, color = SoftGrey, shape = RoundedCornerShape(16.dp)),
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
                text = stringResource(R.string.account_preference),
                modifier = Modifier.padding(bottom = 16.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
            SettingsMenu(
                icon = R.drawable.ic_account_circle,
                title = stringResource(R.string.change_username)
            )
            SettingsMenu(
                icon = R.drawable.ic_language,
                title = stringResource(R.string.change_language)
            )
        }
    }
}

@Composable
fun PrivacySecurityMenuSection(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(width = 1.dp, color = SoftGrey, shape = RoundedCornerShape(16.dp)),
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
                text = stringResource(R.string.privacy_security),
                modifier = Modifier.padding(bottom = 16.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
            SettingsMenu(
                icon = R.drawable.ic_lock,
                title = stringResource(R.string.enable_security_code)
            )
            SettingsMenu(
                icon = R.drawable.ic_fingerprint,
                title = stringResource(R.string.enable_fingerprint)
            )
            SettingsMenu(
                icon = R.drawable.ic_warning,
                title = stringResource(R.string.reset_all_transactions_data)
            )
        }
    }
}

@Composable
fun AboutMenuSection(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(width = 1.dp, color = SoftGrey, shape = RoundedCornerShape(16.dp)),
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
                text = stringResource(R.string.about),
                modifier = Modifier.padding(bottom = 16.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
            SettingsMenu(
                icon = R.drawable.ic_info,
                title = stringResource(R.string.about_application)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_smartphone),
                    contentDescription = null,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = stringResource(R.string.version),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Text(
                    text = "v1.0",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                )
            }
        }
    }
}

@Composable
fun SettingsMenu(
    icon: Int,
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )
        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = null
        )
    }
}