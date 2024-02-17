package com.example.terratalk.models

import android.location.Location
import com.google.android.libraries.places.api.model.Place

data class Maps(
    val lastKnownLocation: Location?,
    val places: List<Place>
)
