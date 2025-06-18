package com.m7md7sn.dentel.presentation.ui.settings.components

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
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.ui.settings.SettingsContent

/**
 * Displays a list of all settings items
 */
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

/**
 * Individual settings item card
 */
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
