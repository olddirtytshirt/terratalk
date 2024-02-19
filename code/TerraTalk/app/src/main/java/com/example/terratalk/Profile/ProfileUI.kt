package com.example.terratalk.Profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.terratalk.Screen
import com.example.terratalk.ui.BottomNavigation
import com.example.terratalk.ui.PageBar

@Composable
fun ProfilePage(
    viewModel: ProfileViewModel,
    navController: NavController
) {

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
                .padding(horizontal = 20.dp, vertical = 5.dp)
        ) {
            Text(
                text = "hi, " + viewModel.state.value.username,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "check your:",
                style = MaterialTheme.typography.titleSmall
            )


            Button(onClick = {
                viewModel.signOut()
                navController.navigate(Screen.SignInPreview.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }) {
                Text("sign out")
            }
        }
    }
}