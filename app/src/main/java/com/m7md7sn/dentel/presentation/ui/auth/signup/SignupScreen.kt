package com.m7md7sn.dentel.presentation.ui.auth.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.common.components.CommonLargeButton
import com.m7md7sn.dentel.presentation.common.components.CommonTextField
import com.m7md7sn.dentel.presentation.common.components.FullDentelHeader
import com.m7md7sn.dentel.presentation.common.components.MinimizedDentelHeader
import com.m7md7sn.dentel.presentation.navigation.AuthScreen
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.auth.login.FacebookGoogleLoginButtons
import com.m7md7sn.dentel.presentation.ui.auth.login.ForgetPasswordTextButton
import com.m7md7sn.dentel.presentation.ui.auth.login.LoginScreen
import com.m7md7sn.dentel.presentation.ui.auth.login.LoginScreenContent
import com.m7md7sn.dentel.presentation.ui.auth.login.LoginTextFields
import com.m7md7sn.dentel.presentation.ui.auth.viewmodels.SignupViewModel
import com.m7md7sn.dentel.utils.Result
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.rememberVectorPainter

@Composable
fun SignUpScreen(
    onSignupSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignupViewModel = hiltViewModel()
) {
    val signupResult by viewModel.signupResult.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(signupResult) {
        when (signupResult) {
            is Result.Success -> {
                onSignupSuccess()
                viewModel.resetSignupResult()
            }
            is Result.Error -> {
                val errorMessage = (signupResult as Result.Error).message
                scope.launch { snackbarHostState.showSnackbar(errorMessage) }
                viewModel.resetSignupResult()
            }
            else -> {}
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = DentelDarkPurple
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MinimizedDentelHeader(
                    modifier = Modifier.weight(0.15f),
                )
                SignUpScreenContent(
                    modifier = Modifier.weight(0.85f),
                    email = viewModel.email,
                    onEmailValueChange = viewModel::onEmailChange,
                    username = viewModel.username,
                    onUsernameValueChange = viewModel::onUsernameChange,
                    password = viewModel.password,
                    onPasswordValueChange = viewModel::onPasswordChange,
                    confirmPassword = viewModel.confirmPassword,
                    onConfirmPasswordValueChange = viewModel::onConfirmPasswordChange,
                    onSignupClick = viewModel::signup,
                    onLoginClick = onNavigateToLogin,
                    isLoading = signupResult is Result.Loading,
                    isEmailError = viewModel.emailError != null,
                    emailErrorMessage = viewModel.emailError,
                    isPasswordError = viewModel.passwordError != null,
                    passwordErrorMessage = viewModel.passwordError,
                    isConfirmPasswordError = viewModel.confirmPasswordError != null,
                    confirmPasswordErrorMessage = viewModel.confirmPasswordError,
                    isPasswordVisible = viewModel.isPasswordVisible,
                    onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
                    isConfirmPasswordVisible = viewModel.isConfirmPasswordVisible,
                    onToggleConfirmPasswordVisibility = viewModel::toggleConfirmPasswordVisibility,
                    isUsernameError = viewModel.usernameError != null,
                    usernameErrorMessage = viewModel.usernameError
                )
            }
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    email: String,
    onEmailValueChange: (String) -> Unit,
    username: String,
    onUsernameValueChange: (String) -> Unit,
    password: String,
    onPasswordValueChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordValueChange: (String) -> Unit,
    onSignupClick: () -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean,
    isEmailError: Boolean,
    emailErrorMessage: String?,
    isPasswordError: Boolean,
    passwordErrorMessage: String?,
    isConfirmPasswordError: Boolean,
    confirmPasswordErrorMessage: String?,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isConfirmPasswordVisible: Boolean,
    onToggleConfirmPasswordVisibility: () -> Unit,
    isUsernameError: Boolean,
    usernameErrorMessage: String?
) {
    Surface(
        color = White,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.sign_up),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.7.sp,
                )
            )
            Spacer(Modifier.height(8.dp))
            SignUpTextFields(
                email = email,
                onEmailValueChange = onEmailValueChange,
                password = password,
                onPasswordValueChange = onPasswordValueChange,
                confirmPassword = confirmPassword,
                onConfirmPasswordValueChange = onConfirmPasswordValueChange,
                username = username,
                onUsernameValueChange = onUsernameValueChange,
                onSignupClick = onSignupClick,
                isEmailError = isEmailError,
                emailErrorMessage = emailErrorMessage,
                isPasswordError = isPasswordError,
                passwordErrorMessage = passwordErrorMessage,
                isConfirmPasswordError = isConfirmPasswordError,
                confirmPasswordErrorMessage = confirmPasswordErrorMessage,
                isPasswordVisible = isPasswordVisible,
                onTogglePasswordVisibility = onTogglePasswordVisibility,
                isConfirmPasswordVisible = isConfirmPasswordVisible,
                onToggleConfirmPasswordVisibility = onToggleConfirmPasswordVisibility,
                isUsernameError = isUsernameError,
                usernameErrorMessage = usernameErrorMessage
            )
            Spacer(Modifier.height(20.dp))
            CommonLargeButton(
                text = stringResource(R.string.register),
                onClick = onSignupClick,
                isLoading = isLoading
            )
            Spacer(Modifier.height(28.dp))
            FacebookGoogleLoginButtons()
            Spacer(Modifier.height(22.dp))
            Text(
                text = stringResource(R.string.have_account),
                style = TextStyle(
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF796C94),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.33.sp,
                )
            )
            TextButton(
                onClick = onLoginClick,
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = TextStyle(
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF421882),
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.33.sp,
                    )
                )
            }
        }
    }
}

@Composable
fun SignUpTextFields(
    username: String,
    onUsernameValueChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordValueChange: (String) -> Unit,
    email: String,
    onEmailValueChange: (String) -> Unit,
    password: String,
    onPasswordValueChange: (String) -> Unit,
    onSignupClick: () -> Unit = {},
    isEmailError: Boolean,
    emailErrorMessage: String?,
    isPasswordError: Boolean,
    passwordErrorMessage: String?,
    isConfirmPasswordError: Boolean,
    confirmPasswordErrorMessage: String?,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isConfirmPasswordVisible: Boolean,
    onToggleConfirmPasswordVisibility: () -> Unit,
    isUsernameError: Boolean,
    usernameErrorMessage: String?
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CommonTextField(
            value = username,
            onValueChange = onUsernameValueChange,
            label = stringResource(R.string.username),
            trailingIcon = painterResource(R.drawable.ic_user),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = isUsernameError,
            errorMessage = usernameErrorMessage
        )
        Spacer(Modifier.height(12.dp))
        CommonTextField(
            value = email,
            onValueChange = onEmailValueChange,
            label = stringResource(R.string.email),
            trailingIcon = painterResource(R.drawable.ic_email),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = isEmailError,
            errorMessage = emailErrorMessage
        )
        Spacer(Modifier.height(12.dp))
        CommonTextField(
            value = password,
            onValueChange = onPasswordValueChange,
            label = stringResource(R.string.password),
            trailingIcon = rememberVectorPainter(
                image = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            ),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = isPasswordError,
            errorMessage = passwordErrorMessage,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onTrailingIconClick = onTogglePasswordVisibility
        )
        Spacer(Modifier.height(12.dp))
        CommonTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordValueChange,
            label = stringResource(R.string.confirm_password),
            trailingIcon = rememberVectorPainter(
                image = if (isConfirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            ),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onSignupClick()
                }
            ),
            isError = isConfirmPasswordError,
            errorMessage = confirmPasswordErrorMessage,
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onTrailingIconClick = onToggleConfirmPasswordVisibility
        )
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