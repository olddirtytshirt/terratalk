package com.example.terratalk

import android.content.pm.SigningInfo
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.terratalk.LoginRegister.RegisterPreview
import com.example.terratalk.LoginRegister.SignInPreview

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.SignInPreview.route) {
        composable(route = Screen.SignInPreview.route) {
            SignInPreview(navController = navController)
        }
        composable(
            route = Screen.RegisterPreview.route
        ) {
            RegisterPreview(navController = navController)
        }
    }
}
