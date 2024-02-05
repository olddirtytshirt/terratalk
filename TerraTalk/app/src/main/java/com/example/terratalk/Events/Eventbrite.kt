package com.example.terratalk.Events

import android.content.ContentValues.TAG
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.UUID


data class Events(
    val title: String = "",
    val date: String = "",
    val location: String = "",
    var eventId: String = UUID.randomUUID().toString()

    )

class Eventbrite(private val apiKey: String) {

    private val client = OkHttpClient()
    //private val apiKey =

    fun searchEvents(): List<Events> {
        val request = Request.Builder()
            .url("https://www.eventbriteapi.com/v3/events/search/?q=environmentalism")
            .header("Authorization", "Bearer $apiKey")
            .build()

        val response = client.newCall(request).execute()

        return if (response.isSuccessful) {
            val responseBody = response.body?.string() ?: ""
            parseEvents(responseBody)
        } else {
            Log.e(TAG, "error in events ",)
            emptyList()
        }
    }


    private fun parseEvents(responseBody: String): List<Events> {

        val events = mutableListOf<Events>()

        val json = JSONObject(responseBody)
        val eventList = json.optJSONArray("events")

        eventList?.let {
            for (i in 0 until it.length()) {
                val eventObject = it.getJSONObject(i)
                val eventName = eventObject.getJSONObject("name").getString("text")
                val eventDate = eventObject.getJSONObject("start").getString("local")

                val event = Events(eventName, eventDate)
                events.add(event)
            }
        }
        return events
    }
}