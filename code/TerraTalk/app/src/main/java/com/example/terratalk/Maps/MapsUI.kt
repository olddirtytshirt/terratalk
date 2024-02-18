package com.example.terratalk.Maps


import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import com.example.terratalk.models.Place
import com.google.android.gms.maps.model.BitmapDescriptorFactory



@Composable
fun MapsPage(
    viewModel: MapsViewModel,
    navController: NavController,
    askPermissions: () -> Unit
) {
    //ask for location permissions
    askPermissions()

    val mapsState = viewModel.state.value
    val lastKnownLocation = mapsState.lastKnownLocation

    //Log.d("lastKnownLocation", lastKnownLocation.toString())

    Scaffold(
        topBar = {
            PageBar("//maps")
        },
        bottomBar = {
            BottomNavigation(navController)
        }
    ) { innerPadding ->
        // Check if location permission is granted
        if (lastKnownLocation != null) {
            // Set user location
            val userLocation = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)

            // Set places list
            val places = mapsState.places
            //Log.d("Maps Places", places.toString())
            val mapProperties = MapProperties(
                isMyLocationEnabled = true,
            )

            // Configure maps location to user location
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
                    Button(onClick = { viewModel.performSearchNearby("vegetarian_restaurant") }) {
                        Text("Vegetarian Restaurants")
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Button(onClick = { viewModel.performSearchNearby("park") }) {
                        Text("Park")
                    }
                }

                Map(
                    properties = mapProperties,
                    cameraPositionState = cameraPositionState,
                    places = places
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //display a message or UI element informing the user about disabled location tracking
                Text(
                    text = "Location tracking is disabled or permission denied. Please enable precise location permissions to use this feature.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}



//set up and configure map
@Composable
fun Map(
    properties: MapProperties,
    cameraPositionState: CameraPositionState,
    places: List<Place>,
) {
    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp)),
        properties = properties,
        cameraPositionState = cameraPositionState,
    ) {
        //iterate through each place object and place a market at each place location
        places.forEach { place ->
            place.location?.let { location ->
                //depending on the type of place type, use different marker colours
                val icon = if (place.types.contains("park")) {
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                } else if (place.types.contains("restaurant")) {
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                } else {
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                }
                //set location, title - place name, snipper - place address, icon
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            location.latitude,
                            location.longitude
                        )
                    ),
                    title = place.displayName.text,
                    snippet = place.formattedAddress,
                    icon = icon
                )
            }
        }
    }
}