package com.m7md7sn.dentel.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.m7md7sn.dentel.presentation.ui.auth.emailverification.EmailVerificationScreen
import com.m7md7sn.dentel.presentation.ui.auth.login.LoginScreen
import com.m7md7sn.dentel.presentation.ui.auth.passwordreset.PasswordResetScreen
import com.m7md7sn.dentel.presentation.ui.auth.signup.SignUpScreen
import com.m7md7sn.dentel.presentation.ui.splash.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login_screen")
    object SignUp : Screen("signup_screen")
    object EmailVerification : Screen("email_verification_screen")
    object PasswordReset : Screen("password_reset_screen")
    object Home : Screen("home")
}

@Composable
fun DentelApp() {
    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.EmailVerification.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignup = {
                    navController.navigate(Screen.SignUp.route)
                },
                onNavigateToPasswordReset = {
                    navController.navigate(Screen.PasswordReset.route)
                }
            )
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignupSuccess = {
                    navController.navigate(Screen.EmailVerification.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
        composable(Screen.EmailVerification.route) {
            EmailVerificationScreen(
                onEmailVerified = {
                    navController.navigate(Screen.Home.route) { // TODO: Navigate to Home screen
                        popUpTo(Screen.EmailVerification.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
        composable(Screen.PasswordReset.route) {
            PasswordResetScreen(
                onPasswordResetSent = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.PasswordReset.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
        composable(Screen.Home.route) {
            // Home screen will be implemented later
        }
    }
} 