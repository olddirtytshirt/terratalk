package com.example.terratalk

sealed class Screen(
    val route: String
) {
    object SignInPreview : Screen("SignIn")
    object RegisterPreview : Screen("Register")
    object NewsPage : Screen("NewsPage")
}
