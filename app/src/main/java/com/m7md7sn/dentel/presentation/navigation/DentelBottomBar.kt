package com.m7md7sn.dentel.presentation.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentel.R
import com.m7md7sn.dentel.presentation.theme.DentelDarkPurple
import com.m7md7sn.dentel.presentation.theme.DentelLightPurple
import com.m7md7sn.dentel.presentation.theme.DentelBrightBlue

sealed class BottomNavScreen(
    val route: String,
    val icon: Int,
    val selectedIcon: Int,
    val contentDescription: String
) {
    object Home : BottomNavScreen(
        route = Screen.Home.route,
        icon = R.drawable.ic_home_navigation,
        selectedIcon = R.drawable.ic_home_navigation_selected,
        contentDescription = "Home"
    )

    object Notifications : BottomNavScreen(
        route = Screen.Notifications.route,
        icon = R.drawable.ic_notification_navigation,
        selectedIcon = R.drawable.ic_notification_navigation_selected,
        contentDescription = "Notifications"
    )

    object Settings : BottomNavScreen(
        route = Screen.Settings.route,
        icon = R.drawable.ic_settings_navigation,
        selectedIcon = R.drawable.ic_settings_navigation_selected,
        contentDescription = "Settings"
    )

    object Profile : BottomNavScreen(
        route = Screen.Profile.route,
        icon = R.drawable.ic_profile_navigation,
        selectedIcon = R.drawable.ic_profile_navigation_selected,
        contentDescription = "Profile"
    )
}

val bottomNavItems = listOf(
    BottomNavScreen.Notifications,
    BottomNavScreen.Settings,
    BottomNavScreen.Profile,
    BottomNavScreen.Home
)

@Composable
fun DentelBottomBar(
    currentRoute: String?,
    onTabSelected: (BottomNavScreen) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.height(80.dp)
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(item) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.contentDescription,

                    )
                },
                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                    indicatorColor = DentelLightPurple,
                    selectedIconColor = DentelDarkPurple,
                    unselectedIconColor = DentelDarkPurple
                ),
                alwaysShowLabel = false
            )
        }
    }
} 