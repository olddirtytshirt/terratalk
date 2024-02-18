package com.example.terratalk.Maps


import com.example.terratalk.BuildConfig.MAPS_API_KEY
import com.example.terratalk.models.NearbyPlacesRequest
import com.example.terratalk.models.NearbyPlacesResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

//setting up api call to google maps nearby search(new) endpoint
//searches for places in the surrounding area to user location

//api documentation
//https://developers.google.com/maps/documentation/places/web-service/nearby-search

//define how retrofit makes api calls
interface PlacesApiService {
    @Headers(
        "Content-Type: application/json",
        "X-Goog-Api-Key: $MAPS_API_KEY",
        //define what we want the api to post back
        "X-Goog-FieldMask: places.name,places.displayName,places.formattedAddress,places.types"
    )
    @POST("/v1/places:searchNearby")
    fun searchNearby(@Body request: NearbyPlacesRequest): Call<NearbyPlacesResponse>
}

