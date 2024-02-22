package com.example.terratalk.Maps

import android.annotation.SuppressLint
import android.content.pm.PackageManager.*
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.terratalk.BuildConfig.MAPS_API_KEY
import com.example.terratalk.models.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.example.terratalk.models.Maps
import com.example.terratalk.models.Circle
import com.example.terratalk.models.GeocodingResponse
import com.example.terratalk.models.LocationRestriction
import com.example.terratalk.models.NearbyPlacesRequest
import com.example.terratalk.models.NearbyPlacesResponse

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/*
CODE REFERENCES:
getDeviceLocation() permission implementation
https://github.com/mitchtabian/Google-Maps-Compose/blob/master/app/src/main/java/com/codingwithmitch/composegooglemaps/MapViewModel.kt

*/


class MapsViewModel(): ViewModel() {

    //initialise Maps UI states (maps model)
    val state: MutableState<Maps> = mutableStateOf(
        Maps(
            lastKnownLocation = null,
            places = emptyList()
        )
    )

    //function that calls and fetches google maps nearby search api()
    //parameters passed - a string with the place type we are looking for
    fun performSearchNearby(query: String) {
        val latitude = state.value.lastKnownLocation?.latitude ?: 0.0
        val longitude = state.value.lastKnownLocation?.longitude ?: 0.0

        //define
        val retrofit = Retrofit.Builder()
            .baseUrl("https://places.googleapis.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //request we send to api
        val request = NearbyPlacesRequest(
            //place types we want to search for
            includedTypes = listOf(query),
            //maximum number of places to be returned
            maxResultCount = 10,
            //set location restriction to user location
            locationRestriction = LocationRestriction(
                circle = Circle(
                    center = Location(
                        latitude = latitude,
                        longitude = longitude
                    ),
                    //search for places in a 3000m radius from the location
                    radius = 3000.0
                )
            )
        )


        val service = retrofit.create(PlacesApiService::class.java)

        //set up api call
        val call: Call<NearbyPlacesResponse> = service.searchNearby(request)

        //make api call and get response
        call.enqueue(object : Callback<NearbyPlacesResponse> {
            override fun onResponse(call: Call<NearbyPlacesResponse>, response: Response<NearbyPlacesResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let { nearbyPlacesResponse ->
                        val updatedMaps = state.value.copy(places = nearbyPlacesResponse.places)
                        state.value = updatedMaps
                    }
                    //make call to geocoding api to convert formattedAddress to coordiantes
                    responseBody?.places?.forEach { place ->
                        //format the place id for the api
                        val formattedPlaceId = place.name.substringAfter('/')
                        //call convertPlaceIdToCoordinates for each place's formatted address
                        convertPlaceIdToCoordinates(formattedPlaceId) { latitude, longitude ->
                            //update the location for the current place
                            val updatedPlaces = state.value.places.map { updatedPlace ->
                                if (updatedPlace.name == place.name) {
                                    updatedPlace.copy(location = Location(latitude, longitude))
                                } else {
                                    updatedPlace
                                }
                            }

                            //set Maps UI state `places` to nearbyPlacesResponse from the api
                            //this is for MapsUI so we can get places from the UI using viewModel.state
                            state.value = state.value.copy(places = updatedPlaces)
                        }


                    }
                    //to check responsebody in logcat
                    Log.d("success", responseBody.toString())
                } else {
                    //to check errors in logcat
                    Log.d("fail", response.toString())
                }
            }

            override fun onFailure(call: Call<NearbyPlacesResponse>, t: Throwable) {
                //to check errors in logcat
                Log.d("fail2", t.toString())
            }
        })
    }


    //function that calls and fetches geocoding api to convert address to coordinates
    //parameters passed:
    //place id of the place we want to convert
    //callback to the caller who called this func
    fun convertPlaceIdToCoordinates(placeId: String, callback: (Double, Double) -> Unit) {
        //secret
        val apiKey = MAPS_API_KEY

        //create a Retrofit service instance
        val service = RetrofitClient.instance.create(GeocodingService::class.java)

        //call the geocodePlaceId function with the place id and api key
        service.geocode(placeId, apiKey).enqueue(object : Callback<GeocodingResponse> {
            override fun onResponse(call: Call<GeocodingResponse>, response: Response<GeocodingResponse>) {
                if (response.isSuccessful) {
                    val geocodingResponse = response.body()
                    //extract coordinates from the response
                    val latitude = geocodingResponse?.results?.getOrNull(0)?.geometry?.location?.lat
                    val longitude = geocodingResponse?.results?.getOrNull(0)?.geometry?.location?.lng

                    //handle latitude and longitude values
                    if (latitude != null && longitude != null) {
                        //to check coordinates in logcat
                        //val coordinates = "Latitude: $latitude, Longitude: $longitude"
                        //Log.d("Coordinates", coordinates)

                        // Pass the coordinates to the callback
                        callback(latitude, longitude)
                    } else {
                        //null coordinates
                        Log.d("Coordinates null", "Coordinates not found")
                    }
                } else {
                    //to check errors
                    Log.d("Geocoding Error",  response.toString())
                }
            }

            override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {
                //to check errors
                Log.e("Geocoding Error", "Failed to execute geocoding request", t)
            }
        })
    }



    //code from CODE REFERENCE mentioned above
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
}
