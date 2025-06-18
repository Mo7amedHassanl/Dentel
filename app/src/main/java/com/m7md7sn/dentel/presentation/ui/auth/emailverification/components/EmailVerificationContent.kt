package com.m7md7sn.dentel.presentation.ui.auth.emailverification.components

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
 * Main content component for the Email Verification screen
 */
@Composable
fun EmailVerificationContent(
    userEmail: String,
    onResendClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean,
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
                text = stringResource(R.string.email_verification),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_bold)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.7.sp,
                )
            )

            Spacer(Modifier.height(60.dp))

            // Verification instructions
            VerificationInstructions()

            Spacer(Modifier.height(60.dp))

            // Resend email section
            ResendEmailSection(onResendClick = onResendClick)

            Spacer(Modifier.height(60.dp))

            // Confirm button
            CommonLargeButton(
                text = stringResource(R.string.confirm),
                onClick = onConfirmClick,
                isLoading = isLoading
            )

            Spacer(Modifier.height(80.dp))

            // Login option
            LoginOption(onLoginClick = onLoginClick)
        }
    }
}

/**
 * Verification instructions component
 */
@Composable
fun VerificationInstructions() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.email_verification_note),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                fontWeight = FontWeight(400),
                color = Color(0xFF796C94),
                textAlign = TextAlign.Center,
            )
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.email_verification_note_2),
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
    }
}

/**
 * Resend email section component
 */
@Composable
fun ResendEmailSection(onResendClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.link_not_sent),
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
            modifier = Modifier.fillMaxWidth(),
            onClick = onResendClick
        ) {
            Text(
                text = stringResource(R.string.resent),
                style = TextStyle(
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    fontFamily = FontFamily(Font(R.font.din_next_lt_regular)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF421882),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.33.sp,
                )
            )
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
