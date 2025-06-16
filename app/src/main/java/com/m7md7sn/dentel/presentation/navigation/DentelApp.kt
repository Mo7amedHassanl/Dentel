package com.m7md7sn.dentel.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.m7md7sn.dentel.presentation.ui.auth.emailverification.EmailVerificationScreen
import com.m7md7sn.dentel.presentation.ui.auth.login.LoginScreen
import com.m7md7sn.dentel.presentation.ui.auth.passwordreset.PasswordResetScreen
import com.m7md7sn.dentel.presentation.ui.auth.pictureupload.PictureUploadScreen
import com.m7md7sn.dentel.presentation.ui.auth.signup.SignUpScreen
import com.m7md7sn.dentel.presentation.ui.home.HomeScreen
import com.m7md7sn.dentel.presentation.ui.splash.SplashScreen
import com.m7md7sn.dentel.presentation.ui.section.SectionScreen
import com.m7md7sn.dentel.presentation.navigation.DentelBottomBar
import com.m7md7sn.dentel.presentation.navigation.BottomNavScreen
import com.m7md7sn.dentel.presentation.ui.profile.ProfileScreen
import com.m7md7sn.dentel.presentation.ui.section.TopicType
import com.m7md7sn.dentel.presentation.ui.settings.SettingsScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login_screen")
    object SignUp : Screen("signup_screen")
    object EmailVerification : Screen("email_verification_screen")
    object PasswordReset : Screen("password_reset_screen")
    object PictureUpload : Screen("picture_upload_screen")
    object Home : Screen("home")
    object Section : Screen("section/{sectionId}")
    object Video : Screen("video")
    object Article : Screen("article")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Notifications : Screen("notifications")
}

@Composable
fun DentelApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = when (currentRoute) {
        Screen.Home.route, Screen.Profile.route, Screen.Settings.route, Screen.Notifications.route  -> true
        else -> false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        bottomBar = {
            if (showBottomBar) {
                DentelBottomBar(currentRoute = currentRoute) { screen ->
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
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
                    onSectionClick = { sectionId ->
                        navController.navigate("section/$sectionId")
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(Screen.Notifications.route) {
                androidx.compose.material3.Text("Notifications Screen")
            }
            composable(Screen.Section.route) { backStackEntry ->
                val sectionId = backStackEntry.arguments?.getString("sectionId") ?: return@composable
                SectionScreen(
                    sectionId = sectionId,
                    onTopicClick = { topic ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("topic", topic)
                        when (topic.type) {
                            TopicType.Video -> navController.navigate("video")
                            TopicType.Article -> navController.navigate("article")
                        }
                    },

                )
            }
            composable(Screen.Video.route) {
                val topic = navController.previousBackStackEntry?.savedStateHandle?.get<com.m7md7sn.dentel.presentation.ui.section.Topic>("topic")
                com.m7md7sn.dentel.presentation.ui.video.VideoScreen(topic = topic)
            }
            composable(Screen.Article.route) {
                val topic = navController.previousBackStackEntry?.savedStateHandle?.get<com.m7md7sn.dentel.presentation.ui.section.Topic>("topic")
                com.m7md7sn.dentel.presentation.ui.article.ArticleScreen(topic = topic)
            }
        }
    }
} 