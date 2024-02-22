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

//file where we define app navigation and screen routes

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
    //Log.d("loggedIn", loggedIn.toString())

    NavHost(navController = navController, startDestination = determineStartDestination(loggedIn)) {
        //sign in screen
        composable(route = Screen.SignInPreview.route) {
            SignInPreview(navController = navController)
        }
        //register screen
        composable(route = Screen.RegisterPreview.route) {
            RegisterPreview(navController = navController)
        }
        //news page screen
        composable(route = Screen.NewsPage.route) {
            NewsPage(newsViewModel, navController = navController)

        }
        //events page screen
        composable(route = Screen.EventsPage.route) {
            EventsPage(eventViewModel, navController = navController)
        }
        //maps page screen
        composable(route = Screen.MapsPage.route) {

            MapsPage(mapsViewModel, navController = navController, askPermissions)
        }
        //forum page screen
        composable(route = Screen.ForumPage.route) {
            ForumPage(forumViewModel, navController = navController)
        }
        //add post screen
        composable(route = Screen.AddPost.route) {
            AddPost(forumViewModel, navController = navController)
        }
        //single post page, proceeded by the post id to indicate which post
        composable(
            route = "${Screen.PostPage.route}/{postId}",
            arguments = listOf(navArgument("postId") {
                type = NavType.StringType
            })
        ) {
            PostPage(forumViewModel, navController = navController, postId = forumViewModel.stateForum.value.postId)
        }
        //profile page screen
        composable(route = Screen.ProfilePage.route) {
            ProfilePage(profileViewModel, eventViewModel, navController = navController)
        }


    }
}

//function that determines which screen to display first based on user authentication state
//if not logged in, display sign in page
//if logged in, show news page
private fun determineStartDestination(isLoggedIn: Boolean): String {
    return if (isLoggedIn) {
        Screen.NewsPage.route
    } else {
        Screen.SignInPreview.route
    }
}
