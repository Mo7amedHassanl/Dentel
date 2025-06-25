package com.m7md7sn.dentel.presentation.ui.auth.emailverification

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
import com.m7md7sn.dentel.presentation.ui.auth.emailverification.components.EmailVerificationContent
import com.m7md7sn.dentel.utils.Event
import com.m7md7sn.dentel.utils.Result
import kotlinx.coroutines.launch

/**
 * Main email verification screen composable that handles the email verification UI flow
 */
@Composable
fun EmailVerificationScreen(
    onEmailVerified: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EmailVerificationViewModel = hiltViewModel()
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
                }
            }
        }
    }

    // Handle verification result
    LaunchedEffect(uiState.verificationResult) {
        when (uiState.verificationResult) {
            is Result.Success -> {
                // Check if this is just the initial email sending success or actual verification
                if (!uiState.isEmailSent) {
                    // This means we're in the initial state, do nothing
                } else {
                    // This indicates a successful verification check, now navigate
                    onEmailVerified()
                    viewModel.resetVerificationResult()
                }
            }
            is Result.Error -> {
                viewModel.resetVerificationResult()
            }
            else -> {} // Handle Loading or null state
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
                    modifier = Modifier.weight(0.3f)
                )

                // Email verification content
                EmailVerificationContent(
                    modifier = Modifier.weight(0.7f),
                    userEmail = uiState.userEmail,
                    onResendClick = viewModel::sendEmailVerification,
                    onConfirmClick = viewModel::checkEmailVerificationStatus,
                    onLoginClick = onNavigateToLogin,
                    isLoading = uiState.isLoading
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
private fun EmailVerificationScreenPreviewEn() {
    DentelTheme {
        EmailVerificationScreen(onEmailVerified = {}, onNavigateToLogin = {})
    }
}

@Preview(locale = "ar")
@Composable
private fun EmailVerificationScreenPreviewAr() {
    DentelTheme {
        EmailVerificationScreen(onEmailVerified = {}, onNavigateToLogin = {})
    }
}