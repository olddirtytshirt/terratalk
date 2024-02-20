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
import com.example.terratalk.Database.updateStatus
import com.example.terratalk.Events.EventViewModel
import com.example.terratalk.Forum.ForumViewModel
import com.example.terratalk.Maps.MapsViewModel
import com.example.terratalk.Webscrapping.NewsViewModel
import com.example.terratalk.Profile.ProfileViewModel
import com.example.terratalk.UserManager.currentUser
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

    private val  forumViewModel: ForumViewModel by viewModels()

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

    //func that displays the location permission pop-up
    //it is called only when in MapsUI/MapsPage
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

    //when user is in the app, status is set as ACTIVE
    //status is stored in database, it dynamically changes whether user is online or not
    override fun onResume() {
        super.onResume()
        updateStatus(StatusTag.ACTIVE)
    }

    //when user is not currently in the app, status is set as OFFLINE
    //stored in database
    override fun onPause() {
        super.onPause()
        updateStatus(StatusTag.OFFLINE)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        //get instance of Firebase Authentication
        val myAuth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        //get reference of current authenticated user
        //it can either be: authenticated user in specific app session, or null if user is not logged in

        //keep track is user is logged in already or not
        var loggedIn = false

        //for location permission
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (currentUser != null) {
            //user is already logged in
            Log.d("Authentication", "User is already logged in. Email: ${currentUser!!.email}")
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

        setContent {
            TerraTalkTheme {
                Navigation(
                    newsViewModel = newsViewModel,
                    eventViewModel = eventsViewModel ,
                    mapsViewModel = mapsViewModel,
                    profileViewModel = profileViewModel,
                    forumViewModel = forumViewModel,
                    //pass askPermissions() func in Navigation to MapsPage
                    //this function is called in MapsUI/MapsPage
                    askPermissions = this@MainActivity::askPermissions,
                    loggedIn = loggedIn
                )
            }
        }
    }
}
