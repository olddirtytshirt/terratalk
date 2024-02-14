package com.example.terratalk.Maps

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import com.google.android.libraries.places.api.model.Place.Field
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.example.terratalk.models.Maps
import com.google.android.gms.maps.model.LatLng



/*
CODE REFERENCES:
getDeviceLocation() permission implementation
https://github.com/mitchtabian/Google-Maps-Compose/blob/master/app/src/main/java/com/codingwithmitch/composegooglemaps/MapViewModel.kt

*/


class MapsViewModel(application: Application): ViewModel() {

    val state: MutableState<Maps> = mutableStateOf(
        Maps(
            lastKnownLocation = null,
        )
    )

    private lateinit var placesClient: PlacesClient
    private val applicationContext = application.applicationContext

    init{
        Places.initialize(applicationContext, "AIzaSyAVKv3GX58qS1jRcVY35wNrfneaS3hi2Tg")
        placesClient = Places.createClient(applicationContext)
    }



    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state.value = state.value.copy(
                        lastKnownLocation = task.result,
                        )
                    //Log.d("lastKnownLocation", task.result.toString())

                }
            }
        } catch (e: SecurityException) {
            Log.e("getDeviceLocation", e.toString())
        }

    }

    suspend fun getNearbyPlaces(placeTypes: List<String>,fusedLocationProviderClient: FusedLocationProviderClient): List<LatLng>{
        val location = fusedLocationProviderClient.lastLocation.await()
        val request = FindCurrentPlaceRequest.builder(listOf(Place.Field.NAME, Place.Field.LAT_LNG))
            .build()

        val nearbylocations = mutableListOf<LatLng>()

        withContext(Dispatchers.IO){
            val response: FindCurrentPlaceResponse = placesClient.findCurrentPlace(request).await()
            if(response?.placeLikelihoods != null){
                for (placeLikelihood in response.placeLikelihoods){
                    val place = placeLikelihood.place
                    if (place.types?.intersect(placeTypes)?.isNotEmpty() == true){
                        val placeLocation = LatLng(place.latLng!!.latitude, place.latLng!!.longitude)
                        nearbylocations.add(placeLocation)
                    }
                }
            }
        }
        return nearbylocations
    }
}

