package com.m7md7sn.dentel.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.m7md7sn.dentel.presentation.ui.auth.emailverification.EmailVerificationScreen
import com.m7md7sn.dentel.presentation.ui.auth.login.LoginScreen
import com.m7md7sn.dentel.presentation.ui.auth.passwordreset.PasswordResetScreen
import com.m7md7sn.dentel.presentation.ui.auth.pictureupload.PictureUploadScreen
import com.m7md7sn.dentel.presentation.ui.auth.signup.SignUpScreen
import com.m7md7sn.dentel.presentation.ui.home.HomeScreen
import com.m7md7sn.dentel.presentation.ui.splash.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login_screen")
    object SignUp : Screen("signup_screen")
    object EmailVerification : Screen("email_verification_screen")
    object PasswordReset : Screen("password_reset_screen")
    object PictureUpload : Screen("picture_upload_screen")
    object Home : Screen("home")
}

@Composable
fun DentelApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        ) {
        NavHost(
            navController = navController, startDestination = Screen.Splash.route,
            modifier = Modifier.padding(it)
        ) {
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
                    onLoginSuccess = { user ->
                        if (user.isEmailVerified) {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Screen.EmailVerification.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
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
                        navController.navigate(Screen.PictureUpload.route) {
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
            composable(Screen.PictureUpload.route) {
                PictureUploadScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.PictureUpload.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(

                )
            }
        }
    }

} 