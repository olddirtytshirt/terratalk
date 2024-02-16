package com.example.terratalk.Profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.terratalk.models.User
import com.example.terratalk.ui.BottomNavigation
import com.example.terratalk.ui.PageBar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun ProfilePage(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser

    /*
    var bio by remember { mutableStateOf(user.bio ?: "") }
    var profilePic by remember { mutableStateOf(user.profilePic ?: "") }
    var cover by remember { mutableStateOf(user.cover ?: "") }
    var token by remember { mutableStateOf(user.token ?: "") }
    var status by remember { mutableStateOf(user.status ?: "") }


     */
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

            //Display User data (will do later)

        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /*
            TextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio") }
            )


            TextField(
                value = profilePic,
                onValueChange = { profilePic = it },
                label = { Text("Profile Picture URL") }
            )


            TextField(
                value = cover,
                onValueChange = { cover = it },
                label = { Text("Cover URL") }
            )


            TextField(
                value = token,
                onValueChange = { token = it },
                label = { Text("Token") }
            )


            TextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Status") }
            )


            Button(
                onClick = {
                    user.apply {
                        /* causes build errors

                        setBio(bio)
                        setprofilePic(profilePic)
                        setCover(cover)
                        setToken(token)
                        setStatus(status)

                         */
                    }

                    //viewModel.updateUserProfile(user)
                }
            )
            {
             */
                Text("Save Changes")
            }
        }
    }


