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
import com.m7md7sn.dentel.presentation.ui.section.SectionScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login_screen")
    object SignUp : Screen("signup_screen")
    object EmailVerification : Screen("email_verification_screen")
    object PasswordReset : Screen("password_reset_screen")
    object PictureUpload : Screen("picture_upload_screen")
    object Home : Screen("home")
    object Section : Screen("section/{sectionIndex}")
    object Video : Screen("video?title={title}&subtitle={subtitle}")
    object Article : Screen("article?title={title}&subtitle={subtitle}")
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
                    onSectionClick = { index ->
                        navController.navigate("section/$index")
                    }
                )
            }
            composable(Screen.Section.route) { backStackEntry ->
                val sectionIndex = backStackEntry.arguments?.getString("sectionIndex")?.toIntOrNull()
                val homeViewModel = androidx.hilt.navigation.compose.hiltViewModel<com.m7md7sn.dentel.presentation.ui.home.HomeViewModel>()
                val sections = homeViewModel.uiState.value.sections
                val section = sectionIndex?.let { idx -> sections.getOrNull(idx) }
                if (section != null) {
                    com.m7md7sn.dentel.presentation.ui.section.SectionScreen(
                        section = section,
                        onTopicClick = { topic ->
                            val encodedTitle = java.net.URLEncoder.encode(topic.title, "UTF-8")
                            val encodedSubtitle = java.net.URLEncoder.encode(topic.subtitle, "UTF-8")
                            when (topic.type) {
                                is com.m7md7sn.dentel.presentation.ui.section.TopicType.Video -> navController.navigate("video?title=$encodedTitle&subtitle=$encodedSubtitle")
                                is com.m7md7sn.dentel.presentation.ui.section.TopicType.Article -> navController.navigate("article?title=$encodedTitle&subtitle=$encodedSubtitle")
                            }
                        }
                    )
                }
            }
            composable(Screen.Video.route) { backStackEntry ->
                val title = backStackEntry.arguments?.getString("title") ?: ""
                val subtitle = backStackEntry.arguments?.getString("subtitle") ?: ""
                com.m7md7sn.dentel.presentation.ui.video.VideoScreen(title = title, subtitle = subtitle)
            }
            composable(Screen.Article.route) { backStackEntry ->
                val title = backStackEntry.arguments?.getString("title") ?: ""
                val subtitle = backStackEntry.arguments?.getString("subtitle") ?: ""
                com.m7md7sn.dentel.presentation.ui.article.ArticleScreen(title = title, subtitle = subtitle)
            }
        }
    }

} 