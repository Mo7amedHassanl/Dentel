package com.m7md7sn.dentel.presentation.ui.auth.passwordreset.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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

/**
 * Main content component for the password reset screen
 */
@Composable
fun PasswordResetContent(
    email: String,
    onEmailValueChange: (String) -> Unit,
    onSendPasswordResetClick: () -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean,
    isEmailError: Boolean,
    emailErrorMessage: String?,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Title
            Text(
                text = stringResource(R.string.password_reset),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.7.sp,
                )
            )

            Spacer(Modifier.height(14.dp))

            // Instruction text
            Text(
                text = stringResource(R.string.password_reset_note),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF796C94),
                    textAlign = TextAlign.Center,
                )
            )

            Spacer(Modifier.height(16.dp))

            // Email input field
            EmailInputField(
                email = email,
                onEmailValueChange = onEmailValueChange,
                onSendPasswordResetClick = onSendPasswordResetClick,
                isEmailError = isEmailError,
                emailErrorMessage = emailErrorMessage
            )

            Spacer(Modifier.height(60.dp))

            // Confirm button
            CommonLargeButton(
                text = stringResource(R.string.confirm),
                onClick = onSendPasswordResetClick,
                isLoading = isLoading
            )

            Spacer(Modifier.height(40.dp))
            Spacer(Modifier.height(16.dp))

            // Login option
            LoginOption(onLoginClick = onLoginClick)
        }
    }
}

/**
 * Login option component
 */
@Composable
fun LoginOption(onLoginClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
