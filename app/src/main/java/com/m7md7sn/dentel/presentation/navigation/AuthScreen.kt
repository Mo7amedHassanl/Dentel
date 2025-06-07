package com.m7md7sn.dentel.presentation.navigation

sealed class AuthScreen(val route: String) {
    data object Login : AuthScreen("login_screen")
    data object Signup : AuthScreen("signup_screen")
    data object EmailVerification : AuthScreen("email_verification_screen")
    data object PasswordReset : AuthScreen("password_reset_screen")
} 