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

/**
 * Sealed class representing the different settings content sections
 * Each object represents a specific settings screen with its title and icon
 */
sealed class SettingsContent(
    @StringRes val titleResId: Int,
    val icon: ImageVector?
) {
    object Account : SettingsContent(R.string.account, Icons.Outlined.AccountCircle)
    object Notifications : SettingsContent(R.string.notifications, Icons.Outlined.Notifications)
    object Language : SettingsContent(R.string.language, Icons.Outlined.Language)
    object Support : SettingsContent(R.string.support, Icons.Outlined.SupportAgent)
}

/**
 * Data class representing the UI state for the Settings screen
 * Following the unidirectional data flow pattern with immutable state
 */
data class SettingsUiState(
    // Authentication state
    val isLoggedOut: Boolean = false,

    // Content navigation state
    val currentContent: SettingsContent? = null,

    // Language state
    val selectedLanguage: String? = null,

    // Support form state
    val supportEmail: String = "",
    val supportMessage: String = "",
    val isEmailError: Boolean = false,
    val isMessageError: Boolean = false,

    // Loading state
    val isLoading: Boolean = false,

    // Error handling
    val errorMessage: String? = null
)
