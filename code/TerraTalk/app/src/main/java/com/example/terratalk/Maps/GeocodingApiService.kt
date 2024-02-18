package com.example.terratalk.Maps

import com.example.terratalk.models.GeocodingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//setting up api call to geocode api
//convert google place ids to coordinates

//api documentation
//https://developers.google.com/maps/documentation/geocoding/requests-geocoding#geocoding-lookup

//define how retrofit makes api calls
interface GeocodingService {
    @GET("maps/api/geocode/json")
    fun geocode(
        @Query("place_id") placeId: String,
        @Query("key") apiKey: String
    ): Call<GeocodingResponse>
}


//build the retrofit object
object RetrofitClient {
    private const val BASE_URL = "https://maps.googleapis.com/"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            //convert json response into an object
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}