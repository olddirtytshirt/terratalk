package com.example.terratalk.models

//data classes to handle search nearby api calls and responses
//the variables need to be the SAME NAME as the api result fields

//request data classes
data class LocationRestriction(
    val circle: Circle
)

data class NearbyPlacesRequest(
    val includedTypes: List<String>,
    val maxResultCount: Int,
    val locationRestriction: LocationRestriction
)

//response data classes
data class Location(
    val latitude: Double,
    val longitude: Double
)

data class Circle(
    val center: Location,
    val radius: Double
)


data class NearbyPlacesResponse(
    val places: List<Place>
)

data class Place(
    val name: String,
    val types: List<String>,
    val formattedAddress: String,
    val displayName: DisplayName,
    val location: Location ? = null
)

data class DisplayName(
    val text: String,
    val languageCode: String
)
