package com.m7md7sn.dentel.presentation.ui.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.ui.graphics.vector.ImageVector
import com.m7md7sn.dentel.R

sealed class SettingsContent(
    @StringRes val titleResId: Int,
    val icon: ImageVector?
) {
    object Account : SettingsContent(R.string.account, Icons.Outlined.AccountCircle)
    object Notifications : SettingsContent(R.string.notifications, Icons.Outlined.Notifications)
    object Language : SettingsContent(R.string.language, Icons.Outlined.Language)
    object Support : SettingsContent(R.string.support, Icons.Outlined.SupportAgent)
}

data class SettingsUiState(
    val isLoggedOut: Boolean = false,
    val currentContent: SettingsContent? = null
)

