package com.m7md7sn.dentel.presentation.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingsHeader()
            SettingsList()
        }
    }
}

@Composable
fun SettingsList(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SettingsItem(
            text = stringResource(R.string.account),
            onCardClicked = {},
            icon = Icons.Outlined.AccountCircle,
        )
        SettingsItem(
            text = stringResource(R.string.notifications),
            onCardClicked = {},
            icon = Icons.Outlined.Notifications,
        )
        SettingsItem(
            text = stringResource(R.string.language),
            onCardClicked = {},
            icon = Icons.Outlined.Language,
        )
        SettingsItem(
            text = stringResource(R.string.support),
            onCardClicked = {},
            icon = Icons.Outlined.SupportAgent,
        )
        SettingsItem(
            text = stringResource(R.string.logout),
            onCardClicked = {},
            icon = Icons.Outlined.Logout,
        )
    }
}

@Composable
fun SettingsItem(
    text: String,
    icon: ImageVector,
    onCardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(containerColor = DentelDarkPurple.copy(alpha = 0.1f)),
        onClick = onCardClicked
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector =  icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = DentelDarkPurple
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Right,
                )
            )
        }
    }
}

@Composable
fun SettingsHeader(
    modifier: Modifier = Modifier
) {
    Surface(
        color = DentelDarkPurple,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(
            bottomStart = 32.dp,
            bottomEnd = 32.dp,
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = null,
            )
            Text(
                text = stringResource(R.string.settings),
                style = TextStyle(
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.22.sp,
                )
            )
            Spacer(Modifier.width(12.dp))
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreviewEn() {
    DentelTheme {
        SettingsScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun SettingsScreenPreviewAr() {
    DentelTheme {
        SettingsScreen()
    }
}