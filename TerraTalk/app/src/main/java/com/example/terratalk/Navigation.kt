package com.example.terratalk

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.terratalk.Events.EventViewModel
import com.example.terratalk.Events.EventsPage
import com.example.terratalk.Forum.ForumPage
import com.example.terratalk.LoginRegister.RegisterPreview
import com.example.terratalk.LoginRegister.SignInPreview
import com.example.terratalk.Maps.MapsPage
import com.example.terratalk.Maps.MapsViewModel
import com.example.terratalk.Profile.ProfilePage
import com.example.terratalk.Webscrapping.NewsPage
import com.example.terratalk.Webscrapping.NewsViewModel


@Composable
fun Navigation(newsViewModel: NewsViewModel,
               eventViewModel: EventViewModel,
               mapsViewModel: MapsViewModel,
               askPermissions: () -> Unit,
               loggedin: Boolean
) {
    val navController = rememberNavController()
    Log.d("loggedIn", loggedin.toString())
    NavHost(navController = navController, startDestination = determineStartDestination(loggedin)) {
        composable(route = Screen.SignInPreview.route) {
            SignInPreview(navController = navController)
        }

        composable(route = Screen.RegisterPreview.route) {
            RegisterPreview(navController = navController)
        }

        composable(route = Screen.NewsPage.route) {
            NewsPage(newsViewModel, navController = navController)
        }

        composable(route = Screen.EventsPage.route) {
            EventsPage(eventViewModel, navController = navController)
        }

        composable(route = Screen.MapsPage.route) {

            MapsPage(mapsViewModel, navController = navController, askPermissions)
        }

        composable(route = Screen.ForumPage.route) {
            ForumPage(navController = navController)
        }

        composable(route = Screen.ProfilePage.route) {
            ProfilePage(navController = navController)
        }

    }

}



private fun determineStartDestination(isLoggedIn: Boolean): String {
    return if (isLoggedIn) {
        Screen.NewsPage.route
    } else {
        Screen.SignInPreview.route
    }
}
