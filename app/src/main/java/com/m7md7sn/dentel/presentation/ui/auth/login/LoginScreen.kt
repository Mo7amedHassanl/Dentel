package com.m7md7sn.dentel.presentation.ui.auth.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.m7md7sn.dentel.presentation.common.components.FullDentelHeader
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.ui.auth.login.components.LoginContent
import com.m7md7sn.dentel.utils.Event
import com.m7md7sn.dentel.utils.Result
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
    val context = LocalContext.current

    // Setup Google Sign-In launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                account.idToken?.let { idToken ->
                    viewModel.signInWithGoogle(idToken)
                } ?: run {
                    scope.launch {
                        snackbarHostState.showSnackbar("Google sign in failed: ID token is null")
                    }
                }
            } catch (e: ApiException) {
                scope.launch {
                    snackbarHostState.showSnackbar("Google sign in failed: ${e.message}")
                }
            }
        }
    }

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
                    onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
                    onGoogleLoginClick = {
                        // Launch Google Sign-In flow
                        googleSignInLauncher.launch(viewModel.getGoogleSignInIntent())
                    },
                    onFacebookLoginClick = {
                        // Facebook login is not supported yet, show message
                        scope.launch {
                            snackbarHostState.showSnackbar("Facebook login coming soon!")
                        }
                    }
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
