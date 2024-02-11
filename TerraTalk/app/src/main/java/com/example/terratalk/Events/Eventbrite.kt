package com.example.terratalk.Events


import android.util.Log
import com.example.terratalk.models.Events
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException


class Eventbrite() {

    private val client = OkHttpClient()
    private val apiKey = "ALKBYY4DQUECEF5GI3"

    suspend fun searchEvents(): List<Events> {
        val request = Request.Builder()
            .url("https://www.eventbriteapi.com/v3/events/search/?q=environmentalism")
            .header("Authorization", "Bearer $apiKey")
            .build()

        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        return if (response.isSuccessful) {
            val responseBody = response.body?.string() ?: ""
            parseEvents(responseBody)
        } else {
            Log.e("Eventbrite", "Error fetching events: ${response.code}")
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