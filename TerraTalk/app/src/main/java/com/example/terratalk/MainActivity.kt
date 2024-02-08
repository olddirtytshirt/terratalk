package com.example.terratalk

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.terratalk.Webscrapping.NewsViewModel
import com.example.terratalk.Webscrapping.parseIrishTimes
import com.example.terratalk.ui.theme.TerraTalkTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import com.example.terratalk.LoginRegister.logOut

class MainActivity : ComponentActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(NewsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
       val myAuth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        val currentUser = FirebaseAuth.getInstance().currentUser

        //keep track is user is logged in already or not
        var loggedIn = false


        if (currentUser != null) {
            // User is already logged in
            Log.d("Authentication", "User is already logged in. Email: ${currentUser.email}")
            // Perform any additional actions if needed
            loggedIn = true
        } else {
            Log.d("Authentication", "User is not logged in")
        }

        MainScope().launch {
            val newsItems1 = parseIrishTimes()
            viewModel.setNewsItems(newsItems1)

        }

        setContent {
            TerraTalkTheme {
                Navigation(viewModel, loggedIn)
            }
        }
    }
}
