package com.m7md7sn.dentel.presentation.ui.splash

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.DisposableEffect

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val contentAlpha = remember { Animatable(1f) }
    val scale = remember { Animatable(0.5f) }
    val isOnline by viewModel.isOnline.collectAsState(initial = viewModel.isInternetConnected()) // Collect the flow state
    var showNoInternetDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = (context as? Activity)

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )
        delay(1000) // Delay for splash animation

        if (isOnline) {
            if (viewModel.isUserSignedIn()) {
                onNavigateToHome()
            } else {
                onNavigateToLogin()
            }
        } else {
            showNoInternetDialog = true
        }
    }

    // Re-check connectivity if dialog is shown and comes back online
    LaunchedEffect(isOnline) {
        if (isOnline && showNoInternetDialog) {
            showNoInternetDialog = false
            if (viewModel.isUserSignedIn()) {
                onNavigateToHome()
            } else {
                onNavigateToLogin()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = DentelDarkPurple)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.dentel_logo),
            contentDescription = "Dentel Logo",
            modifier = Modifier
                .graphicsLayer(
                    alpha = contentAlpha.value,
                    scaleX = scale.value,
                    scaleY = scale.value
                )
        )
//        Text(
//            text = "We Make Dentistry Visible",
//            color = Color.White,
//            fontSize = 16.sp,
//            modifier = Modifier
//                .padding(top = 16.dp)
//                .align(Alignment.BottomCenter)
//                .graphicsLayer(
//                    alpha = contentAlpha.value,
//                    scaleX = scale.value,
//                    scaleY = scale.value
//                )
//        )
    }

    if (showNoInternetDialog) {
        AlertDialog(
            onDismissRequest = { /* Dialog is not dismissible without action */ },
            title = { Text("No Internet Connection") },
            text = { Text("Please check your internet connection and try again.") },
            confirmButton = {
                TextButton(onClick = {
                    if (viewModel.isInternetConnected()) { // Re-check immediately on reconnect click
                        showNoInternetDialog = false
                        if (viewModel.isUserSignedIn()) {
                            onNavigateToHome()
                        } else {
                            onNavigateToLogin()
                        }
                    } else {
                        // Optionally, show a toast or message that it's still not connected
                    }
                }) {
                    Text("Reconnect")
                }
            },
            dismissButton = {
                TextButton(onClick = { activity?.finish() }) {
                    Text("Exit")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    DentelTheme {
        SplashScreen(onNavigateToHome = {}, onNavigateToLogin = {})
    }
}