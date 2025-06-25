package com.m7md7sn.dentel.presentation.ui.settings

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.ui.settings.components.AccountContent
import com.m7md7sn.dentel.presentation.ui.settings.components.ErrorMessage
import com.m7md7sn.dentel.presentation.ui.settings.components.LanguageContent
import com.m7md7sn.dentel.presentation.ui.settings.components.LoadingIndicator
import com.m7md7sn.dentel.presentation.ui.settings.components.NotificationsContent
import com.m7md7sn.dentel.presentation.ui.settings.components.SettingsHeader
import com.m7md7sn.dentel.presentation.ui.settings.components.SettingsList
import com.m7md7sn.dentel.presentation.ui.settings.components.SupportContent
import kotlinx.coroutines.flow.collectLatest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState

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
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Image picker launcher for profile photo
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setSelectedImageUri(uri)
            viewModel.uploadProfilePicture()
        }
    }

    val showChangePasswordDialog by viewModel.showChangePasswordDialog.collectAsState()
    val showResetPasswordDialog by viewModel.showResetPasswordDialog.collectAsState()
    val changePasswordCurrent by viewModel.changePasswordCurrent.collectAsState()
    val changePasswordNew by viewModel.changePasswordNew.collectAsState()
    val showDeleteAccountDialog by viewModel.showDeleteAccountDialog.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is SettingsViewModel.Event.RecreateActivity -> {
                    (context as? Activity)?.recreate()
                }
                is SettingsViewModel.Event.EmailSendSuccess -> {
                    Toast.makeText(context, "Email sent successfully", Toast.LENGTH_SHORT).show()
                }
                is SettingsViewModel.Event.EmailSendError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Collect snackbar messages from ViewModel
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { message ->
            scope.launch { snackbarHostState.showSnackbar(message) }
        }
    }

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
                    SettingsContent.Account -> AccountContent(
                        profileName = uiState.profileName,
                        profileEmail = uiState.profileEmail,
                        profilePhotoUrl = uiState.profilePhotoUrl,
                        isLoading = uiState.isLoading || uiState.isUploading,
                        uploadProgress = uiState.uploadProgress,
                        onNameChange = { viewModel.onNameChange(it) },
                        onUpdateProfileClick = { viewModel.onUpdateProfileClick() },
                        onPhotoClick = { pickImageLauncher.launch("image/*") },
                        onChangePasswordClick = { viewModel.onChangePasswordClick() },
                        onResetPasswordClick = { viewModel.onResetPasswordClick() },
                        onDeleteAccountClick = { viewModel.onDeleteAccountClick() },
                    )
                    SettingsContent.Notifications -> NotificationsContent()
                    SettingsContent.Language -> {
                        uiState.selectedLanguage?.let {
                            LanguageContent(
                                selectedLanguage = it,
                                onLanguageSelected = { lang -> viewModel.onLanguageSelected(lang) }
                            )
                        }
                    }
                    SettingsContent.Support -> SupportContent(
                        email = uiState.supportEmail,
                        message = uiState.supportMessage,
                        isEmailError = uiState.isEmailError,
                        isMessageError = uiState.isMessageError,
                        isLoading = uiState.isLoading,
                        onEmailChange = { email -> viewModel.updateSupportEmail(email) },
                        onMessageChange = { message -> viewModel.updateSupportMessage(message) },
                        onSendClick = { viewModel.validateAndSendSupportEmail() }
                    )
                    null -> {}
                }
            }
        }
        // Snackbar for showing messages
        SnackbarHost(
            hostState = snackbarHostState,
        )
        // Dialog for Change Password
        if (showChangePasswordDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissDialogs() },
                containerColor = Color.White,
                titleContentColor = DentelDarkPurple,
                textContentColor = DentelDarkPurple,
                title = { Text("Change Password", color = DentelDarkPurple) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = changePasswordCurrent,
                            onValueChange = { viewModel.changePasswordCurrent.value = it },
                            label = { Text("Current Password", color = DentelDarkPurple) },
                            singleLine = true,
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = changePasswordNew,
                            onValueChange = { viewModel.changePasswordNew.value = it },
                            label = { Text("New Password", color = DentelDarkPurple) },
                            singleLine = true,
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.submitChangePassword() }) {
                        Text("Change", color = DentelDarkPurple)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissDialogs() }) {
                        Text("Cancel", color = DentelDarkPurple)
                    }
                }
            )
        }
        // Dialog for Reset Password
        if (showResetPasswordDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissDialogs() },
                containerColor = Color.White,
                titleContentColor = DentelDarkPurple,
                textContentColor = DentelDarkPurple,
                title = { Text("Reset Password", color = DentelDarkPurple) },
                text = { Text("A password reset link will be sent to your email address.", color = DentelDarkPurple) },
                confirmButton = {
                    TextButton(onClick = { viewModel.submitResetPassword() }) {
                        Text("Send Reset Email", color = DentelDarkPurple)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissDialogs() }) {
                        Text("Cancel", color = DentelDarkPurple)
                    }
                }
            )
        }
        // Dialog for Delete Account
        if (showDeleteAccountDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissDialogs() },
                containerColor = Color.White,
                titleContentColor = DentelDarkPurple,
                textContentColor = DentelDarkPurple,
                title = { Text("Delete Account", color = DentelDarkPurple) },
                text = {
                    Column {
                        Text("Are you sure you want to delete your account? This action cannot be undone.", color = DentelDarkPurple)
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = viewModel.deleteAccountPassword.collectAsState().value,
                            onValueChange = { viewModel.deleteAccountPassword.value = it },
                            label = { Text("Password", color = DentelDarkPurple) },
                            singleLine = true,
                            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.submitDeleteAccount() }) {
                        Text("Delete", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissDialogs() }) {
                        Text("Cancel", color = DentelDarkPurple)
                    }
                }
            )
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
