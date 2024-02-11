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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*


@Composable
fun MapsPage(
    viewModel: MapsViewModel,
    navController: NavController
) {
    val mapsState = viewModel.state.value
    val lastKnownLocation = mapsState.lastKnownLocation
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
                    modifier = Modifier

                )
                Button(
                    text = "car recharge",
                    modifier = Modifier

                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    text = "organic shops",
                    modifier = Modifier

                )
                Button(
                    text = "recycle",
                    modifier = Modifier

                )
                Button(
                    text = "parks",
                    modifier = Modifier

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
    location: Location
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        LatLng(location.latitude, location.longitude),
        15f
    ),
)