package com.m7md7sn.dentel.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.ui.auth.emailverification.EmailVerificationScreen
import com.m7md7sn.dentel.presentation.ui.auth.login.LoginScreen
import com.m7md7sn.dentel.presentation.ui.auth.passwordreset.PasswordResetScreen
import com.m7md7sn.dentel.presentation.ui.auth.pictureupload.PictureUploadScreen
import com.m7md7sn.dentel.presentation.ui.auth.signup.SignUpScreen
import com.m7md7sn.dentel.presentation.ui.home.HomeScreen
import com.m7md7sn.dentel.presentation.ui.splash.SplashScreen
import com.m7md7sn.dentel.presentation.ui.section.SectionScreen
import com.m7md7sn.dentel.presentation.ui.profile.ProfileScreen
import com.m7md7sn.dentel.presentation.ui.section.TopicType
import com.m7md7sn.dentel.presentation.ui.settings.SettingsScreen
import com.m7md7sn.dentel.presentation.ui.settings.SettingsViewModel
import androidx.compose.ui.graphics.vector.ImageVector
import com.m7md7sn.dentel.presentation.ui.favorites.FavoritesScreen
import com.m7md7sn.dentel.presentation.ui.favorites.FavoritesViewModel
import com.m7md7sn.dentel.presentation.ui.favorites.NavigationState
import com.m7md7sn.dentel.presentation.ui.notifications.NotificationsDialog
import androidx.compose.material3.CircularProgressIndicator
import com.m7md7sn.dentel.presentation.ui.article.ArticleViewModel
import com.m7md7sn.dentel.presentation.ui.home.HomeViewModel
import com.m7md7sn.dentel.presentation.ui.video.VideoViewModel

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
    object Favorites : Screen("favorites/{typeOrdinal}")
}

@Composable
fun DentelApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Simplified state for notifications dialog - just track visibility
    val (showNotificationsDialog, setShowNotificationsDialog) = androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(false)
    }

    val showBottomBar = when (currentRoute) {
        Screen.Home.route, Screen.Profile.route, Screen.Settings.route -> true
        else -> false
    }

    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsUiState by settingsViewModel.uiState.collectAsState()

    val topBarTitle: String = when (currentRoute) {
        Screen.Settings.route -> settingsUiState.currentContent?.let { stringResource(id = it.titleResId) }
            ?: stringResource(id = R.string.settings)

        else -> ""
    }

    val topBarIcon: ImageVector? = when (currentRoute) {
        Screen.Settings.route -> settingsUiState.currentContent?.icon
        else -> null
    }

    val topBarShowBackButton: Boolean = when (currentRoute) {
        Screen.Settings.route -> settingsUiState.currentContent != null
        Screen.Splash.route, Screen.Login.route, Screen.SignUp.route, Screen.EmailVerification.route, Screen.PasswordReset.route, Screen.PictureUpload.route -> false
        else -> navController.previousBackStackEntry != null
    }

    val topBarOnBackClick: () -> Unit = when (currentRoute) {
        Screen.Settings.route -> {
            { settingsViewModel.clearSettingsContent() }
        }

        Screen.Splash.route, Screen.Login.route, Screen.SignUp.route, Screen.EmailVerification.route, Screen.PasswordReset.route, Screen.PictureUpload.route -> {
            {}
        }

        else -> {
            { navController.popBackStack() }
        }
    }

    val showTopBar = when (currentRoute) {
        Screen.Splash.route, Screen.Login.route, Screen.SignUp.route, Screen.EmailVerification.route, Screen.PasswordReset.route, Screen.PictureUpload.route -> false
        else -> !showBottomBar
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            if (showTopBar) {
                DentelTopBar(
                    navController = navController,
                    title = topBarTitle,
                    icon = topBarIcon,
                    showBackButton = topBarShowBackButton,
                    onBackClick = topBarOnBackClick,
                    onNotificationClick = {
                        setShowNotificationsDialog(true)
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                DentelBottomBar(
                    currentRoute = currentRoute,
                    onTabSelected = { screen ->
                        if (screen == BottomNavScreen.Notifications) {
                            setShowNotificationsDialog(true)
                        } else if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    ) {
        // Show the notifications dialog if state is true
        if (showNotificationsDialog) {
            NotificationsDialog(
                visible = true,
                onDismiss = { setShowNotificationsDialog(false) }
            )
        }

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
                    },
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.SignUp.route) { inclusive = true }
                        }
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
                val homeViewModel: HomeViewModel = hiltViewModel()
                val navigationState by homeViewModel.navigationState.collectAsState()

                // Handle navigation from home to content screens
                LaunchedEffect(navigationState) {
                    when (val state = navigationState) {
                        is NavigationState.NavigateToContent -> {
                            navController.currentBackStackEntry?.savedStateHandle?.set("topic", state.topic)
                            when (state.topic.type) {
                                TopicType.Video -> navController.navigate(Screen.Video.route)
                                TopicType.Article -> navController.navigate(Screen.Article.route)
                            }
                            homeViewModel.resetNavigationState()
                        }
                        else -> {}
                    }
                }

                HomeScreen(
                    onSectionClick = { sectionId ->
                        navController.navigate("section/$sectionId")
                    },
                    onNavigateToVideo = { topic ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("topic", topic)
                        navController.navigate(Screen.Video.route)
                    },
                    onNavigateToArticle = { topic ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("topic", topic)
                        navController.navigate(Screen.Article.route)
                    }
                )
            }
            composable(Screen.Profile.route) {
                val favoritesViewModel: FavoritesViewModel = hiltViewModel()
                val navigationState by favoritesViewModel.navigationState.collectAsState()

                // Handle navigation from favorites to content screens
                LaunchedEffect(navigationState) {
                    when (val state = navigationState) {
                        is NavigationState.NavigateToContent -> {
                            navController.currentBackStackEntry?.savedStateHandle?.set("topic", state.topic)
                            when (state.topic.type) {
                                TopicType.Video -> navController.navigate(Screen.Video.route)
                                TopicType.Article -> navController.navigate(Screen.Article.route)
                            }
                            favoritesViewModel.resetNavigationState()
                        }
                        else -> {}
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    ProfileScreen(
                        onNavigateToFavorites = { typeOrdinal ->
                            navController.navigate("favorites/$typeOrdinal")
                        },
                        onFavoriteItemClick = { favoriteItem ->
                            favoritesViewModel.onFavoriteItemClicked(favoriteItem)
                        },
                        navigationState = navigationState
                    )
                }
            }
            composable(Screen.Settings.route) {
                val settingsViewModel: SettingsViewModel = hiltViewModel()
                val uiState by settingsViewModel.uiState.collectAsState()

                LaunchedEffect(uiState.isLoggedOut) {
                    if (uiState.isLoggedOut) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                }
                SettingsScreen(viewModel = settingsViewModel)
            }
            composable(Screen.Section.route) { backStackEntry ->
                val sectionId =
                    backStackEntry.arguments?.getString("sectionId") ?: return@composable
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
                val topic =
                    navController.previousBackStackEntry?.savedStateHandle?.get<com.m7md7sn.dentel.presentation.ui.section.Topic>(
                        "topic"
                    )
                val videoViewModel: VideoViewModel = hiltViewModel()
                val navigationState by videoViewModel.navigationState.collectAsState()

                // Handle navigation from video screen to other content screens
                LaunchedEffect(navigationState) {
                    when (val state = navigationState) {
                        is NavigationState.NavigateToContent -> {
                            navController.currentBackStackEntry?.savedStateHandle?.set("topic", state.topic)
                            when (state.topic.type) {
                                TopicType.Video -> {
                                    if (navController.currentDestination?.route == Screen.Video.route) {
                                        // If already on video screen, use popUpTo to avoid stack buildup
                                        navController.navigate(Screen.Video.route) {
                                            popUpTo(Screen.Video.route) { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate(Screen.Video.route)
                                    }
                                }
                                TopicType.Article -> navController.navigate(Screen.Article.route)
                            }
                            videoViewModel.resetNavigationState()
                        }
                        else -> {}
                    }
                }

                com.m7md7sn.dentel.presentation.ui.video.VideoScreen(
                    topic = topic,
                    onNavigateToVideo = { videoTopic ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("topic", videoTopic)
                        navController.navigate(Screen.Video.route) {
                            popUpTo(Screen.Video.route) { inclusive = true }
                        }
                    },
                    onNavigateToArticle = { articleTopic ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("topic", articleTopic)
                        navController.navigate(Screen.Article.route)
                    }
                )
            }
            composable(Screen.Article.route) {
                val topic =
                    navController.previousBackStackEntry?.savedStateHandle?.get<com.m7md7sn.dentel.presentation.ui.section.Topic>(
                        "topic"
                    )
                val articleViewModel: ArticleViewModel = hiltViewModel()
                val navigationState by articleViewModel.navigationState.collectAsState()

                // Handle navigation from article screen to other content screens
                LaunchedEffect(navigationState) {
                    when (val state = navigationState) {
                        is NavigationState.NavigateToContent -> {
                            navController.currentBackStackEntry?.savedStateHandle?.set("topic", state.topic)
                            when (state.topic.type) {
                                TopicType.Video -> navController.navigate(Screen.Video.route)
                                TopicType.Article -> {
                                    if (navController.currentDestination?.route == Screen.Article.route) {
                                        // If already on article screen, use popUpTo to avoid stack buildup
                                        navController.navigate(Screen.Article.route) {
                                            popUpTo(Screen.Article.route) { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate(Screen.Article.route)
                                    }
                                }
                            }
                            articleViewModel.resetNavigationState()
                        }
                        else -> {}
                    }
                }

                com.m7md7sn.dentel.presentation.ui.article.ArticleScreen(
                    topic = topic,
                    onNavigateToVideo = { videoTopic ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("topic", videoTopic)
                        navController.navigate(Screen.Video.route)
                    },
                    onNavigateToArticle = { articleTopic ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("topic", articleTopic)
                        navController.navigate(Screen.Article.route) {
                            popUpTo(Screen.Article.route) { inclusive = true }
                        }
                    }
                )
            }
            // Add Favorites screen composable
            composable(Screen.Favorites.route) {
                val typeOrdinal =
                    navBackStackEntry?.arguments?.getString("typeOrdinal")?.toIntOrNull() ?: 0

                val favoritesViewModel: FavoritesViewModel = hiltViewModel()
                val uiState by favoritesViewModel.uiState.collectAsState()
                val navigationState by favoritesViewModel.navigationState.collectAsState()

                // Handle navigation from favorites to content screens
                LaunchedEffect(navigationState) {
                    when (val state = navigationState) {
                        is NavigationState.NavigateToContent -> {
                            navController.currentBackStackEntry?.savedStateHandle?.set("topic", state.topic)
                            when (state.topic.type) {
                                TopicType.Video -> navController.navigate(Screen.Video.route)
                                TopicType.Article -> navController.navigate(Screen.Article.route)
                            }
                            favoritesViewModel.resetNavigationState()
                        }
                        else -> {}
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    FavoritesScreen(
                        selectedTypeOrdinal = typeOrdinal,
                        onFavoriteClick = { favoriteItem ->
                            favoritesViewModel.onFavoriteItemClicked(favoriteItem)
                        }
                    )

                    // Show loading indicator when fetching topic
                    if (navigationState is NavigationState.Loading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0x80000000)), // Semi-transparent background
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF05B3EF),
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
