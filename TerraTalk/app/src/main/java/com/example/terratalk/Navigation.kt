package com.example.terratalk

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.terratalk.LoginRegister.RegisterPreview
import com.example.terratalk.LoginRegister.SignInPreview
import com.example.terratalk.Webscrapping.NewsPage
import com.example.terratalk.Webscrapping.NewsViewModel


@Composable
fun Navigation(viewModel: NewsViewModel,
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
            NewsPage(viewModel, navController = navController)
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
