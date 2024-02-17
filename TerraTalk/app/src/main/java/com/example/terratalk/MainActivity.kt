package com.example.terratalk

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.terratalk.Events.EventViewModel
import com.example.terratalk.Maps.MapsViewModel
import com.example.terratalk.Webscrapping.NewsViewModel
import com.example.terratalk.Profile.ProfileViewModel
import com.example.terratalk.models.StatusTag
import com.example.terratalk.ui.theme.TerraTalkTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


/*
CODE REFERENCES:
location permission implementation
https://github.com/mitchtabian/Google-Maps-Compose/blob/master/app/src/main/java/com/codingwithmitch/composegooglemaps/MapViewModel.kt

*/

class MainActivity : ComponentActivity() {
    //viewModels
    private val newsViewModel by lazy {
        ViewModelProvider(this).get(NewsViewModel::class.java)
    }

    private val  mapsViewModel: MapsViewModel by viewModels()

    private val  profileViewModel: ProfileViewModel by viewModels()

    private val  eventsViewModel: EventViewModel by viewModels()


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                mapsViewModel.getDeviceLocation(fusedLocationProviderClient)
            }

        }

    private fun askPermissions() = when {
        ContextCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED -> {
            mapsViewModel.getDeviceLocation(fusedLocationProviderClient)
        }
        else -> {
            requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.updateStatus(StatusTag.ACTIVE)
    }

    override fun onPause() {
        super.onPause()
        profileViewModel.updateStatus(StatusTag.OFFLINE)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        val myAuth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        val currentUser = myAuth.currentUser

        //keep track is user is logged in already or not
        var loggedIn = false

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (currentUser != null) {
            //user is already logged in
            Log.d("Authentication", "User is already logged in. Email: ${currentUser.email}")
            loggedIn = true

        } else {
            Log.d("Authentication", "User is not logged in")
        }

        MainScope().launch {
            val newsItems1 = newsViewModel.parseIrishTimes()
            newsViewModel.setNewsItems(newsItems1)
            val eventItems = eventsViewModel.eventbriteParse()
            eventsViewModel.setEventItems(eventItems)

        }
        askPermissions()
        setContent {
            TerraTalkTheme {
                Navigation(
                    newsViewModel = newsViewModel,
                    eventViewModel = eventsViewModel ,
                    mapsViewModel = mapsViewModel,
                    profileViewModel = profileViewModel,
                    loggedin = loggedIn
                )
            }
        }
    }
}
