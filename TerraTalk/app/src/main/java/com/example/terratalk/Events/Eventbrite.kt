package com.example.terratalk.Events


import android.util.Log
import com.example.terratalk.models.Events
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

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


    suspend fun Eventbrite(): List<Triple<String, String, String>> {
        val url = "https://www.eventbrite.ie/d/ireland--dublin/environmental/"

        val doc: Document = withContext(Dispatchers.IO) {
            Jsoup.connect(url).get()
        }

        val parentContainer = doc.select(".list-item")

        val eventItems = mutableListOf<Triple<String, String, String>>()

        for (container in parentContainer) {
            val titleElement =
                container.selectFirst("h2.Typography_root__487rx.Typography_body-lg__487rx.event-card__clamp-line--two.Typography_align-match-parent__487rx")
            val title = titleElement?.text() ?: "No title found"
            val link =
                "https://www.eventbrite.ie" + container.selectFirst(".search-main-content__events-list-item search-main-content__events-list-item-mobile a")
                    ?.attr("href") ?: "No link found"
            val imageUrl =
                container.selectFirst("img.event-card-image")?.attr("src") ?: "No image found"
            eventItems.add(Triple(title, link, imageUrl))
        }
        return eventItems
    }}
}