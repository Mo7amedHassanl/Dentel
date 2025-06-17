package com.m7md7sn.dentel.presentation.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            if (uiState.currentContent == null) {
                SettingsHeader()
                Spacer(Modifier.height(16.dp))
                SettingsList(
                    onLogoutClick = { viewModel.logout() },
                    onSettingsItemClick = { content ->
                        viewModel.selectSettingsContent(content)
                    })
            } else {
                SettingsHeader(
                    title = stringResource(id = uiState.currentContent!!.titleResId),
                    icon = uiState.currentContent!!.icon,
                    showBackButton = true,
                    onBackClick = { viewModel.clearSettingsContent() })
                Spacer(Modifier.height(16.dp))
                // Display content based on currentContent
                when (uiState.currentContent) {
                    SettingsContent.Account -> AccountContent()
                    SettingsContent.Notifications -> NotificationsContent()
                    SettingsContent.Language -> LanguageContent()
                    SettingsContent.Support -> SupportContent()
                    else -> { /* Should not happen */
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsList(
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit,
    onSettingsItemClick: (SettingsContent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SettingsItem(
            text = stringResource(R.string.account),
            onCardClicked = { onSettingsItemClick(SettingsContent.Account) },
            icon = Icons.Outlined.AccountCircle,
        )
        SettingsItem(
            text = stringResource(R.string.notifications),
            onCardClicked = { onSettingsItemClick(SettingsContent.Notifications) },
            icon = Icons.Outlined.Notifications,
        )
        SettingsItem(
            text = stringResource(R.string.language),
            onCardClicked = { onSettingsItemClick(SettingsContent.Language) },
            icon = Icons.Outlined.Language,
        )
        SettingsItem(
            text = stringResource(R.string.support),
            onCardClicked = { onSettingsItemClick(SettingsContent.Support) },
            icon = Icons.Outlined.SupportAgent,
        )
        SettingsItem(
            text = stringResource(R.string.logout),
            onCardClicked = onLogoutClick,
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E1FF)),
        onClick = onCardClicked
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
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
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.settings),
    icon: ImageVector? = null,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {}
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
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = null,
                )
            }
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.22.sp,
                ),
                modifier = Modifier.weight(1f)
            )
            if (!showBackButton) {
                Spacer(Modifier.width(12.dp))
            } else {
                Icon(
                    imageVector = icon ?: Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun AccountContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Account Settings Content")
    }
}

@Composable
fun NotificationsContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Notifications Settings Content")
    }
}

@Composable
fun LanguageContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Language Settings Content")
    }
}

@Composable
fun SupportContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Support Settings Content")
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

