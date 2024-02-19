package com.example.terratalk

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.terratalk.Events.EventViewModel
import com.example.terratalk.Events.EventsPage
import com.example.terratalk.Forum.ForumPage
import com.example.terratalk.Forum.ForumViewModel
import com.example.terratalk.LoginRegister.RegisterPreview
import com.example.terratalk.LoginRegister.SignInPreview
import com.example.terratalk.Maps.MapsPage
import com.example.terratalk.Maps.MapsViewModel
import com.example.terratalk.Profile.ProfilePage
import com.example.terratalk.Profile.ProfileViewModel
import com.example.terratalk.Webscrapping.NewsPage
import com.example.terratalk.Webscrapping.NewsViewModel
import com.example.terratalk.Forum.AddPost
import com.example.terratalk.Forum.PostPage


@Composable
fun Navigation(newsViewModel: NewsViewModel,
               eventViewModel: EventViewModel,
               mapsViewModel: MapsViewModel,
               profileViewModel: ProfileViewModel,
               forumViewModel: ForumViewModel,
               askPermissions: () -> Unit,
               loggedIn: Boolean
) {
    val navController = rememberNavController()
    Log.d("loggedIn", loggedIn.toString())
    NavHost(navController = navController, startDestination = determineStartDestination(loggedIn)) {
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
            ForumPage(forumViewModel, navController = navController)
        }

        composable(route = Screen.AddPost.route) {
            AddPost(forumViewModel, navController = navController)
        }

        composable(
            route = "${Screen.PostPage.route}/{postId}",
            arguments = listOf(navArgument("postId") {
                type = NavType.StringType
            })
        ) {
            PostPage(forumViewModel, navController = navController, postId = forumViewModel.stateForum.value.postId)
        }

        composable(route = Screen.ProfilePage.route) {
            ProfilePage(profileViewModel, navController = navController)
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
