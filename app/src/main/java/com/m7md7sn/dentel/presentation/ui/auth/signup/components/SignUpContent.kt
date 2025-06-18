package com.m7md7sn.dentel.presentation.ui.auth.signup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.common.components.CommonLargeButton
import com.m7md7sn.dentel.presentation.ui.auth.login.components.SocialLoginButtons

/**
 * Main content of the signup screen including all form elements
 */
@Composable
fun SignUpContent(
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
    usernameErrorMessage: String?,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
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
            SocialLoginButtons()
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
