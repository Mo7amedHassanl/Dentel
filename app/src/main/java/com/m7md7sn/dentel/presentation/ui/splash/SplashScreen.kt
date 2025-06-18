package com.m7md7sn.dentel.presentation.ui.splash

import android.app.Activity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelTheme
import com.m7md7sn.dentel.presentation.ui.splash.components.AnimatedLogo
import com.m7md7sn.dentel.presentation.ui.splash.components.NetworkErrorDialog
import kotlinx.coroutines.delay

/**
 * Splash screen that displays the app logo and handles initial navigation logic.
 */
@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    // Animation values
    val contentAlpha = remember { Animatable(1f) }
    val scale = remember { Animatable(0.5f) }

    // Collect UI state
    val uiState by viewModel.uiState.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()

    // Local variables
    var showNoInternetDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? Activity

    // Animation and navigation effect
    LaunchedEffect(key1 = true) {
        // Run logo animation
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )
        delay(1000) // Delay for splash animation

        // Check navigation state after animation completes
        when (viewModel.checkAuthAndConnectivityState()) {
            NavigationState.NavigateToHome -> onNavigateToHome()
            NavigationState.NavigateToLogin -> onNavigateToLogin()
            NavigationState.NoInternet -> showNoInternetDialog = true
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

    // UI content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = DentelDarkPurple)
            .padding(16.dp)
    ) {
        // Animated logo component
        AnimatedLogo(
            scale = scale.value,
            contentAlpha = contentAlpha.value
        )
    }

    // Show network error dialog if needed
    if (showNoInternetDialog) {
        NetworkErrorDialog(
            onReconnect = {
                if (viewModel.isInternetConnected()) {
                    showNoInternetDialog = false
                    if (viewModel.isUserSignedIn()) {
                        onNavigateToHome()
                    } else {
                        onNavigateToLogin()
                    }
                }
            },
            onExit = { activity?.finish() }
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