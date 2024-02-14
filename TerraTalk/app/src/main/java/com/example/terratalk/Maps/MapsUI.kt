package com.example.terratalk.Maps


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.terratalk.ui.BottomNavigation
import com.example.terratalk.ui.PageBar
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch


@Composable
fun MapsPage(
    viewModel: MapsViewModel,
    navController: NavController,
    fusedLocationProviderClient: FusedLocationProviderClient
) {
    val mapsState = viewModel.state.value
    val lastKnownLocation = mapsState.lastKnownLocation
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            PageBar("//maps")
        },
        bottomBar = {
            BottomNavigation(navController)
        }
    ) { innerPadding ->
        //set user location
        val userLocation = LatLng(lastKnownLocation!!.latitude, lastKnownLocation.longitude)
        val mapProperties = MapProperties(
            //only enable if user has accepted location permissions.
            isMyLocationEnabled = lastKnownLocation != null,
        )
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(userLocation, 13f)
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    text = "vegan restaurants",
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            viewModel.getNearbyPlaces(
                                listOf("vegan restaurant"),
                                fusedLocationProviderClient
                            )

                        }
                    }

                )
                Button(
                    text = "car recharge",
                    modifier = Modifier.clickable {
                        coroutineScope.launch{
                        viewModel.getNearbyPlaces(listOf("car recharge"), fusedLocationProviderClient)
                    }
                    }

                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    text = "organic shops",
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                        viewModel.getNearbyPlaces(listOf("organic shop"), fusedLocationProviderClient)
                    }
                    }

                )
                Button(
                    text = "recycle",
                    modifier = Modifier.clickable {
                        coroutineScope.launch{
                        viewModel.getNearbyPlaces(listOf("recycle"), fusedLocationProviderClient)
                    }
                    }

                )
                Button(
                    text = "parks",
                    modifier = Modifier.clickable {
                        coroutineScope.launch{
                        viewModel.getNearbyPlaces(listOf("parks"), fusedLocationProviderClient)
                    }
                    }

                )
            }
            Spacer(modifier = Modifier.height(5.dp))

            GoogleMap(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .fillMaxSize(),
                properties = mapProperties,
                cameraPositionState = cameraPositionState
            ) {

            }
        }
    }
}


@Composable
fun Button(
    //onClick: onClick
    text: String,
    modifier: Modifier
) {
    OutlinedButton(onClick = {}) {
        Text(text)
    }
}

private suspend fun CameraPositionState.centerOnLocation(
    location: Location,
    map: GoogleMap
) {
    animate(
        update = CameraUpdateFactory.newLatLngZoom(
            LatLng(location.latitude, location.longitude),
            15f
        ),
    )

    map.addMarker(
        MarkerOptions(

        )
    )
}