package com.example.terratalk.Events


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.terratalk.models.Events
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class EventViewModel : ViewModel() {
    //eventItems1
    // Change the type here to List<Events>
    private val _eventItems = MutableLiveData<List<Events>>()
    val eventItems: LiveData<List<Events>>
        get() = _eventItems

    fun setEventItems(events: List<Events>) {
        _eventItems.value = events
    }

    suspend fun eventbriteParse(database:FirebaseDatabase): List<Events> {
        val url = "https://www.eventbrite.ie/d/ireland--dublin/environmental/"

        val doc: Document = withContext(Dispatchers.IO) {
            Jsoup.connect(url).get()
        }

        val parentContainer = doc.select(".search-main-content__events-list-item.search-main-content__events-list-item__mobile")

        val events = mutableListOf<Events>()

        for (container in parentContainer) {
            val titleElement = container.selectFirst("h2.Typography_root__487rx.Typography_body-lg__487rx.event-card__clamp-line--two.Typography_align-match-parent__487rx")
            val title = titleElement?.text() ?: "No title found"

            val linkElement = container.selectFirst("a.event-card-link")
            val link = linkElement?.attr("href") ?: "No link found"

            val imageUrl = container.selectFirst("img.event-card-image")?.attr("src") ?: "No image found"

            val dateElement = container.selectFirst(".Typography_root__487rx.Typography_body-md-bold__487rx")
            val date = dateElement?.text() ?: "No date found"

            val locationElement = container.selectFirst(".Typography_root__487rx.Typography_body-md__487rx.event-card__clamp-line--one.Typography_align-match-parent__487rx")
            val location = locationElement?.text() ?: "No location found"

            events.add(Events(title, link, imageUrl, date, location, database))
        }

        Log.d("events", events.toString())
        return events
    }
}
