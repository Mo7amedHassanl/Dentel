package com.m7md7sn.dentel.presentation.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.settings.components.AccountContent
import com.m7md7sn.dentel.presentation.ui.settings.components.ErrorMessage
import com.m7md7sn.dentel.presentation.ui.settings.components.LanguageContent
import com.m7md7sn.dentel.presentation.ui.settings.components.LoadingIndicator
import com.m7md7sn.dentel.presentation.ui.settings.components.NotificationsContent
import com.m7md7sn.dentel.presentation.ui.settings.components.SettingsHeader
import com.m7md7sn.dentel.presentation.ui.settings.components.SettingsList
import com.m7md7sn.dentel.presentation.ui.settings.components.SupportContent

/**
 * Main Settings Screen that orchestrates all the settings components
 * and observes the ViewModel state
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onLoggedOut: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle logout navigation
    if (uiState.isLoggedOut) {
        onLoggedOut()
    }

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
            // Show loading indicator if necessary
            if (uiState.isLoading) {
                LoadingIndicator()
                return@Column
            }

            // Show error message if present
            uiState.errorMessage?.let {
                ErrorMessage(message = it)
                return@Column
            }

            // Show appropriate header based on current content
            if (uiState.currentContent == null) {
                // Main settings screen header
                SettingsHeader()
            } else {
                // Detail screen header with back button
                SettingsHeader(
                    title = stringResource(id = uiState.currentContent!!.titleResId),
                    icon = uiState.currentContent!!.icon,
                    showBackButton = true,
                    onBackClick = { viewModel.clearSettingsContent() }
                )
            }

            Spacer(Modifier.height(16.dp))

            // Show appropriate content based on current state
            if (uiState.currentContent == null) {
                // Main settings list
                SettingsList(
                    onLogoutClick = { viewModel.logout() },
                    onSettingsItemClick = { content ->
                        viewModel.selectSettingsContent(content)
                    }
                )
            } else {
                // Display content based on currentContent selection
                when (uiState.currentContent) {
                    SettingsContent.Account -> AccountContent()
                    SettingsContent.Notifications -> NotificationsContent()
                    SettingsContent.Language -> LanguageContent()
                    SettingsContent.Support -> SupportContent()
                    null -> TODO()
                }
            }
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
