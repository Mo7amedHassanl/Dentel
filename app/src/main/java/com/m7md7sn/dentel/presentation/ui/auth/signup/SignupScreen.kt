package com.m7md7sn.dentel.presentation.ui.auth.signup

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.m7md7sn.dentel.presentation.common.components.MinimizedDentelHeader
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.auth.signup.components.SignUpContent
import com.m7md7sn.dentel.utils.Result
import kotlinx.coroutines.launch

/**
 * Main signup screen composable that handles the user registration UI flow
 */
@Composable
fun SignUpScreen(
    onSignupSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Handle signup result
    LaunchedEffect(uiState.signupResult) {
        when (uiState.signupResult) {
            is Result.Success -> {
                onSignupSuccess()
                viewModel.resetSignupResult()
            }
            is Result.Error -> {
                val errorMessage = (uiState.signupResult as Result.Error).message
                scope.launch { snackbarHostState.showSnackbar(errorMessage) }
                viewModel.resetSignupResult()
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
                MinimizedDentelHeader(
                    modifier = Modifier.weight(0.15f)
                )

                // Signup form content
                SignUpContent(
                    modifier = Modifier.weight(0.85f),
                    email = uiState.email,
                    onEmailValueChange = viewModel::onEmailChange,
                    username = uiState.username,
                    onUsernameValueChange = viewModel::onUsernameChange,
                    password = uiState.password,
                    onPasswordValueChange = viewModel::onPasswordChange,
                    confirmPassword = uiState.confirmPassword,
                    onConfirmPasswordValueChange = viewModel::onConfirmPasswordChange,
                    onSignupClick = viewModel::signup,
                    onLoginClick = onNavigateToLogin,
                    isLoading = uiState.isLoading,
                    isEmailError = uiState.emailError != null,
                    emailErrorMessage = uiState.emailError,
                    isPasswordError = uiState.passwordError != null,
                    passwordErrorMessage = uiState.passwordError,
                    isConfirmPasswordError = uiState.confirmPasswordError != null,
                    confirmPasswordErrorMessage = uiState.confirmPasswordError,
                    isPasswordVisible = uiState.isPasswordVisible,
                    onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
                    isConfirmPasswordVisible = uiState.isConfirmPasswordVisible,
                    onToggleConfirmPasswordVisibility = viewModel::toggleConfirmPasswordVisibility,
                    isUsernameError = uiState.usernameError != null,
                    usernameErrorMessage = uiState.usernameError
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
private fun SignUpScreenPreviewEn() {
    DentelTheme {
        SignUpScreen(onSignupSuccess = {}, onNavigateToLogin = {})
    }
}

@Preview(locale = "ar")
@Composable
private fun SignUpScreenPreviewAr() {
    DentelTheme {
        SignUpScreen(onSignupSuccess = {}, onNavigateToLogin = {})
    }
}