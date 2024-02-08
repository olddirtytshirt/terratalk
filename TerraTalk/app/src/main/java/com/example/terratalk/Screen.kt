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
}

//used for bottom navigation, implemented in ui/utilsUI.kt
sealed class BottomNavItem(
    var title: String,
    var icon: ImageVector
) {
    object NewsPage :
        BottomNavItem(
            "news",
            Icons.Filled.Newspaper
        )

    object EventsPage :
        BottomNavItem(
            "events",
            Icons.Filled.Event
        )

    object MapsPage :
        BottomNavItem(
            "maps",
            Icons.Filled.Map
        )

    object ForumPage :
        BottomNavItem(
            "forum",
            Icons.Filled.Forum
        )
    object ProfilePage :
        BottomNavItem(
            "profile",
            Icons.Filled.Person
        )
}