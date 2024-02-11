package com.example.terratalk

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String
) {
    object SignInPreview : Screen("SignIn")
    object RegisterPreview : Screen("Register")
    object NewsPage : Screen("NewsPage")
    object EventsPage : Screen("EventsPage")
    object MapsPage : Screen("MapsPage")
    object ForumPage : Screen("ForumPage")
    object ProfilePage : Screen("ProfilePage")
}

//used for bottom navigation, implemented in ui/utilsUI.kt
sealed class BottomNavItem(
    var title: String,
    var icon: ImageVector,
    var route: String
) {
    object NewsPage :
        BottomNavItem(
            title = "news",
            icon = Icons.Filled.Newspaper,
            route = Screen.NewsPage.route
        )

    object EventsPage :
        BottomNavItem(
            title = "events",
            icon = Icons.Filled.Event,
            route = Screen.EventsPage.route
        )

    object MapsPage :
        BottomNavItem(
            title = "maps",
            icon = Icons.Filled.Map,
            route = Screen.MapsPage.route
        )

    object ForumPage :
        BottomNavItem(
            title = "forum",
            icon = Icons.Filled.Forum,
            route = Screen.ForumPage.route

        )

    object ProfilePage :
        BottomNavItem(
            title ="profile",
            icon = Icons.Filled.Person,
            route = Screen.ProfilePage.route
        )

}