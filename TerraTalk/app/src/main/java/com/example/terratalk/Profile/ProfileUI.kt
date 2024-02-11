package com.example.terratalk.Profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.terratalk.ui.BottomNavigation
import com.example.terratalk.ui.PageBar

@Composable
fun ProfilePage (navController: NavController){
    Scaffold(
        topBar = {
            PageBar("//profile")
        },
        bottomBar = {
            BottomNavigation(navController)
        }
        //content
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {

        }

    }
}