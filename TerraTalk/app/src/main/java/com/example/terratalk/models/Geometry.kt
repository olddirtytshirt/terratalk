package com.example.terratalk.models

//data classes to handle geocoding api response
//the variables need to be the SAME NAME as the api result fields
data class GeocodingResponse(
    val results: List<GeocodingResult>,
    val status: String
)

data class GeocodingResult(
    val geometry: Geometry
)


data class Geometry(
    val location: LocationGeometry,
)

data class LocationGeometry(
    val lat: Double,
    val lng: Double
)
