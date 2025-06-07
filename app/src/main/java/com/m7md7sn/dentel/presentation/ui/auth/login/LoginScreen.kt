package com.m7md7sn.dentel.presentation.ui.auth.login

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.focus.FocusManager
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.common.components.CommonLargeButton
import com.m7md7sn.dentel.presentation.common.components.CommonTextField
import com.m7md7sn.dentel.presentation.common.components.FullDentelHeader
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.auth.viewmodels.LoginViewModel
import com.m7md7sn.dentel.utils.Result
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.rememberVectorPainter

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit,
    onNavigateToPasswordReset: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val loginResult by viewModel.loginResult.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(loginResult) {
        when (loginResult) {
            is Result.Success -> {
                onLoginSuccess()
                viewModel.resetLoginResult()
            }
            is Result.Error -> {
                // Display error in Snackbar only if email/password validation passes
                if (viewModel.emailError == null && viewModel.passwordError == null) {
                    val errorMessage = (loginResult as Result.Error).message
                    scope.launch { snackbarHostState.showSnackbar(errorMessage) }
                }
                viewModel.resetLoginResult()
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
                FullDentelHeader(
                    modifier = Modifier.weight(0.3f),
                )
                LoginScreenContent(
                    modifier = Modifier.weight(0.7f),
                    email = viewModel.email,
                    onEmailValueChange = viewModel::onEmailChange,
                    password = viewModel.password,
                    onPasswordValueChange = viewModel::onPasswordChange,
                    onLoginClick = viewModel::login,
                    onForgetPasswordClick = onNavigateToPasswordReset,
                    onCreateAccountClick = onNavigateToSignup,
                    isLoading = loginResult is Result.Loading,
                    isEmailError = viewModel.emailError != null,
                    emailErrorMessage = viewModel.emailError,
                    isPasswordError = viewModel.passwordError != null,
                    passwordErrorMessage = viewModel.passwordError,
                    isPasswordVisible = viewModel.isPasswordVisible,
                    onTogglePasswordVisibility = viewModel::togglePasswordVisibility
                )
            }
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    email: String,
    onEmailValueChange: (String) -> Unit,
    password: String,
    onPasswordValueChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onForgetPasswordClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    isLoading: Boolean,
    isEmailError: Boolean,
    emailErrorMessage: String?,
    isPasswordError: Boolean,
    passwordErrorMessage: String?,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit
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
                text = stringResource(R.string.login),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.7.sp,
                )
            )
            Spacer(Modifier.height(30.dp))
            LoginTextFields(
                email = email,
                onEmailValueChange = onEmailValueChange,
                password = password,
                onPasswordValueChange = onPasswordValueChange,
                onLoginClick = onLoginClick,
                focusManager = LocalFocusManager.current,
                isEmailError = isEmailError,
                emailErrorMessage = emailErrorMessage,
                isPasswordError = isPasswordError,
                passwordErrorMessage = passwordErrorMessage,
                isPasswordVisible = isPasswordVisible,
                onTogglePasswordVisibility = onTogglePasswordVisibility
            )
            Spacer(Modifier.height(12.dp))
            ForgetPasswordTextButton(
                onClick = onForgetPasswordClick
            )
            Spacer(Modifier.height(12.dp))
            CommonLargeButton(
                text = stringResource(R.string.join),
                onClick = onLoginClick,
                isLoading = isLoading
            )
            Spacer(Modifier.height(28.dp))
            FacebookGoogleLoginButtons()
            Spacer(Modifier.height(22.dp))
            Text(
                text = stringResource(R.string.have_no_account),
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
                onClick = onCreateAccountClick,
            ) {
                Text(
                    text = stringResource(R.string.create_new_account),
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
fun FacebookGoogleLoginButtons(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.login_with),
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 20.sp,
                fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                fontWeight = FontWeight(400),
                color = Color(0x99200A4D),
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(

        ) {
            CicularLoginButton(
                icon = R.drawable.ic_google,
                onClick = {}
            )
            Spacer(Modifier.width(22.dp))
            CicularLoginButton(
                icon = R.drawable.ic_facebook,
                onClick = {}
            )

        }
    }
}

@Composable
fun CicularLoginButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .size(64.dp),
        shape = CircleShape,
        border = BorderStroke(1.dp, Color.Gray),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = "Facebook",
                modifier = Modifier.size(40.dp),
            )
        }

    }
}

@Composable
fun ForgetPasswordTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Text(
            text = stringResource(R.string.forget_password),
            style = TextStyle(
                fontSize = 13.sp,
                lineHeight = 20.sp,
                fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                fontWeight = FontWeight(500),
                color = Color(0x99200A4D),
                textAlign = TextAlign.Right,
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun LoginTextFields(
    email: String,
    onEmailValueChange: (String) -> Unit,
    password: String,
    onPasswordValueChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    focusManager: FocusManager,
    isEmailError: Boolean,
    emailErrorMessage: String?,
    isPasswordError: Boolean,
    passwordErrorMessage: String?,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CommonTextField(
            value = email,
            onValueChange = onEmailValueChange,
            label = stringResource(R.string.email),
            trailingIcon = painterResource(R.drawable.ic_user),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = isEmailError,
            errorMessage = emailErrorMessage
        )
        Spacer(Modifier.height(32.dp))
        CommonTextField(
            value = password,
            onValueChange = onPasswordValueChange,
            label = stringResource(R.string.password),
            trailingIcon = rememberVectorPainter(
                image = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onLoginClick()
                }
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = isPasswordError,
            errorMessage = passwordErrorMessage,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onTrailingIconClick = onTogglePasswordVisibility
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreviewEn() {
    DentelTheme {
        LoginScreen(onLoginSuccess = {}, onNavigateToSignup = {}, onNavigateToPasswordReset = {})
    }
}

@Preview(locale = "ar")
@Composable
private fun LoginScreenPreviewAr() {
    DentelTheme {
        LoginScreen(onLoginSuccess = {}, onNavigateToSignup = {}, onNavigateToPasswordReset = {})
    }
}