package com.m7md7sn.dentel.presentation.ui.auth.login.components

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
import androidx.compose.ui.platform.LocalFocusManager
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

/**
 * Main content of the login screen including all form elements
 */
@Composable
fun LoginContent(
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
    onTogglePasswordVisibility: () -> Unit,
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
            ForgetPasswordButton(
                onClick = onForgetPasswordClick
            )
            Spacer(Modifier.height(12.dp))
            CommonLargeButton(
                text = stringResource(R.string.join),
                onClick = onLoginClick,
                isLoading = isLoading
            )
            Spacer(Modifier.height(28.dp))
            SocialLoginButtons()
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
