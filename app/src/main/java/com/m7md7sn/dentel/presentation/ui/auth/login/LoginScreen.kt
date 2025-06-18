package com.m7md7sn.dentel.presentation.ui.auth.login

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseUser
import com.m7md7sn.dentel.presentation.common.components.FullDentelHeader
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.ui.auth.login.components.LoginContent
import com.m7md7sn.dentel.utils.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Main login screen composable that handles the user authentication UI flow
 */
@Composable
fun LoginScreen(
    onLoginSuccess: (FirebaseUser) -> Unit,
    onNavigateToSignup: () -> Unit,
    onNavigateToPasswordReset: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Collect snackbar messages
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { event ->
            event.getContentIfNotHandled()?.let { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }

    // Handle login result
    LaunchedEffect(uiState.loginResult) {
        when (uiState.loginResult) {
            is Result.Success -> {
                onLoginSuccess((uiState.loginResult as Result.Success).data)
                viewModel.resetLoginResult()
            }
            is Result.Error -> {
                viewModel.resetLoginResult()
            }
            else -> {}
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

                // Login form content
                LoginContent(
                    modifier = Modifier.weight(0.7f),
                    email = uiState.email,
                    onEmailValueChange = viewModel::onEmailChange,
                    password = uiState.password,
                    onPasswordValueChange = viewModel::onPasswordChange,
                    onLoginClick = viewModel::login,
                    onForgetPasswordClick = onNavigateToPasswordReset,
                    onCreateAccountClick = onNavigateToSignup,
                    isLoading = uiState.isLoading,
                    isEmailError = uiState.emailError != null,
                    emailErrorMessage = uiState.emailError,
                    isPasswordError = uiState.passwordError != null,
                    passwordErrorMessage = uiState.passwordError,
                    isPasswordVisible = uiState.isPasswordVisible,
                    onTogglePasswordVisibility = viewModel::togglePasswordVisibility
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
