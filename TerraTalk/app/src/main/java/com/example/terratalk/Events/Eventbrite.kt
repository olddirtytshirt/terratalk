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

suspend fun Eventbrite(): List<Triple<String, String, String>> {
    val url = "https://www.eventbrite.ie/d/ireland--dublin/environmental/"

    val doc: Document = withContext(Dispatchers.IO) {
        Jsoup.connect(url).get()
    }

    val parentContainer = doc.select(".search-main-content__events-list-item.search-main-content__events-list-item__mobile")

    val eventItems = mutableListOf<Triple<String, String, String>>()

    for (container in parentContainer) {
        val titleElement = container.selectFirst("h2.Typography_root__487rx.Typography_body-lg__487rx.event-card__clamp-line--two.Typography_align-match-parent__487rx")
        val title = titleElement?.text() ?: "No title found"
        val link = "https://www.eventbrite.ie" + container.selectFirst("a.search-main-content__events-list-item.search-main-content__events-list-item-mobile")?.attr("href") ?: "No link found"
        val imageUrl = container.selectFirst("img.event-card-image")?.attr("src") ?: "No image found"
        eventItems.add(Triple(title, link, imageUrl))
    }
    return eventItems
}