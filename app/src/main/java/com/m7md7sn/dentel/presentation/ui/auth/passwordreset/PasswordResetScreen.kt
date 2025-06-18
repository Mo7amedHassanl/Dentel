package com.m7md7sn.dentel.presentation.ui.auth.passwordreset

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentel.presentation.common.components.FullDentelHeader
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.auth.passwordreset.components.PasswordResetContent
import com.m7md7sn.dentel.utils.Event
import kotlinx.coroutines.launch

/**
 * Main password reset screen composable that handles the password reset UI flow
 */
@Composable
fun PasswordResetScreen(
    onPasswordResetSent: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PasswordResetViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Collect snackbar messages
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { event: Event<String> ->
            event.getContentIfNotHandled()?.let { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                    if (message == "Password reset email sent!") {
                        onPasswordResetSent()
                    }
                }
            }
        }
    }

    // Handle reset result
    LaunchedEffect(uiState.passwordResetResult) {
        if (uiState.passwordResetResult != null) {
            viewModel.resetPasswordResetResult()
        }
    }

    // Main UI
    Surface(
        modifier = modifier.fillMaxSize(),
        color = DentelDarkPurple
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App header (logo)
                FullDentelHeader(
                    modifier = Modifier.weight(0.35f)
                )

                // Password reset content
                PasswordResetContent(
                    modifier = Modifier.weight(0.65f),
                    email = uiState.email,
                    onEmailValueChange = viewModel::onEmailChange,
                    onSendPasswordResetClick = viewModel::sendPasswordResetEmail,
                    onLoginClick = onNavigateToLogin,
                    isLoading = uiState.isLoading,
                    isEmailError = uiState.emailError != null,
                    emailErrorMessage = uiState.emailError
                )
            }

            // Snackbar for showing messages
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Preview
@Composable
private fun PasswordResetScreenPreviewEn() {
    DentelTheme {
        PasswordResetScreen(onPasswordResetSent = {}, onNavigateToLogin = {})
    }
}

@Preview(locale = "ar")
@Composable
private fun PasswordResetScreenPreviewAr() {
    DentelTheme {
        PasswordResetScreen(onPasswordResetSent = {}, onNavigateToLogin = {})
    }
}