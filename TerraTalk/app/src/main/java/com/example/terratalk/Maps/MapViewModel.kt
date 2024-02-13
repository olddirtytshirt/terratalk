package com.example.terratalk.Maps

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.terratalk.models.Maps
import com.google.android.gms.location.FusedLocationProviderClient

/*
CODE REFERENCES:
getDeviceLocation() permission implementation
https://github.com/mitchtabian/Google-Maps-Compose/blob/master/app/src/main/java/com/codingwithmitch/composegooglemaps/MapViewModel.kt

*/


class MapsViewModel: ViewModel() {

    val state: MutableState<Maps> = mutableStateOf(
        Maps(
            lastKnownLocation = null,
        )
    )

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

