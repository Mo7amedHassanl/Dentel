package com.m7md7sn.dentel.presentation.ui.auth.emailverification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.common.components.CommonLargeButton
import com.m7md7sn.dentel.presentation.common.components.FullDentelHeader
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.utils.Result
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.m7md7sn.dentel.utils.Event
import kotlinx.coroutines.flow.collect

@Composable
fun EmailVerificationScreen(
    onEmailVerified: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EmailVerificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(initial = EmailVerificationUiState())
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { event: Event<String> ->
            event.getContentIfNotHandled()?.let { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }

    LaunchedEffect(uiState.verificationResult) {
        if (uiState.verificationResult is Result.Success && uiState.isEmailSent) {
            // This block handles initial email sent success. For actual verification check,
            // the ViewModel's `checkEmailVerificationStatus` will handle it.
            // We'll navigate only when `checkEmailVerificationStatus` confirms verification.
        } else if (uiState.verificationResult is Result.Success<*>) {
            // This indicates a successful verification check, now navigate
            onEmailVerified()
            viewModel.resetVerificationResult()
        } else if (uiState.verificationResult is Result.Error) {
            viewModel.resetVerificationResult()
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
                EmailVerificationScreenContent(
                    modifier = Modifier.weight(0.7f),
                    userEmail = uiState.userEmail,
                    onResendClick = viewModel::sendEmailVerification,
                    onConfirmClick = viewModel::checkEmailVerificationStatus,
                    onLoginClick = onNavigateToLogin,
                    isLoading = uiState.isLoading
                )
            }
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun EmailVerificationScreenContent(
    modifier: Modifier = Modifier,
    userEmail: String,
    onResendClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean
) {
    Surface(
        color = White,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
            Spacer(Modifier.height(60.dp))
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
                modifier = modifier.fillMaxWidth(),
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
            Spacer(Modifier.height(60.dp))
            CommonLargeButton(
                text = stringResource(R.string.confirm),
                onClick = onConfirmClick,
                isLoading = isLoading
            )
            Spacer(Modifier.height(80.dp))
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