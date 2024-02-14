package com.example.terratalk.Events


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.terratalk.models.Events
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class EventViewModel : ViewModel() {

    private val _eventItems = MutableLiveData<List<Events>>()
    val eventItems: LiveData<List<Events>>
        get() = _eventItems

    fun setEventItems(events: List<Events>) {
        _eventItems.value = events
    }

    //func that parses events from Eventbrite using Jsoup
    suspend fun eventbriteParse(): List<Events> {
        val url = "https://www.eventbrite.ie/d/ireland--dublin/environmental/"

        val doc: Document = withContext(Dispatchers.IO) {
            Jsoup.connect(url).get()
        }

        //main article container which contains all necessary info
        val parentContainer = doc.select(".discover-horizontal-event-card")

        //initialise an empty list of type Events -- data class from models
        val events = mutableListOf<Events>()

        for (container in parentContainer) {
            val titleElement = container.selectFirst("h2.Typography_root__487rx.Typography_body-lg__487rx.event-card__clamp-line--two.Typography_align-match-parent__487rx")
            val title = titleElement?.text() ?: "No title found"

            val linkElement = container.selectFirst("a.event-card-link")
            val link = linkElement?.attr("href") ?: "No link found"

            val imageUrl = container.selectFirst("img.event-card-image")?.attr("src") ?: "No image found"

            val locationElement = container.selectFirst(".event-card-details p:nth-of-type(2)")
            val location = locationElement?.text() ?: "No date found"

            val dateElement = container.selectFirst(".Typography_root__487rx.Typography_body-md__487rx.event-card__clamp-line--one.Typography_align-match-parent__487rx")
            val date = dateElement?.text() ?: "No location found"

            events.add(Events(title, link, imageUrl, date, location))
        }

        //Log.d("events", events.toString())
        return events
    }

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val database = FirebaseDatabase.getInstance()


    //func that saves the saved event in the database
    fun saveEvent(eventTitle: String) {
        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = database.getReference("users/$userId")
            val eventsSavedRef = userRef.child("eventsSaved")

            eventsSavedRef.push().setValue(eventTitle)
                .addOnSuccessListener {
                    Log.d("Event", "Event saved successfully: $eventTitle")
                }
                .addOnFailureListener { exception ->
                    Log.e("Event", "Failed to save event: $eventTitle", exception)
                }
        }
    }

    //func that deletes the saved event in the database
    fun removeSaved(eventTitle: String) {
        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = database.getReference("users/$userId")
            val eventsSavedRef = userRef.child("eventsSaved")

            // Query the event to remove from the database
            eventsSavedRef.orderByValue().equalTo(eventTitle)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        // Loop through the snapshot and remove the event from the database
                        for (eventSnapshot in snapshot.children) {
                            eventSnapshot.ref.removeValue()
                                .addOnSuccessListener {
                                    Log.d("Event", "Event removed successfully: $eventTitle")
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("Event", "Failed to remove event: $eventTitle", exception)
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Event", "Failed to query event: $eventTitle", error.toException())
                    }
                })
        }
    }


    //funct that retrieves Saved Events from currentUser
    fun getEventsSavedForCurrentUser(callback: (List<String>?) -> Unit) {
        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = database.getReference("users/$userId")
            val eventsSavedRef = userRef.child("eventsSaved")

            //add a listener for a single value event
            //this means that the listener will trigger once, fetching the current data at the location specified by the reference
            eventsSavedRef.addListenerForSingleValueEvent(object : ValueEventListener {
                //this method is called once when the data is successfully retrieved
                //the snapshot contains the data at the time of the retrieval
                override fun onDataChange(snapshot: DataSnapshot) {
                    val eventsSavedList = mutableListOf<String>()

                    //iterate over each child in the snapshot, which represents individual saved events
                    for (eventSnapshot in snapshot.children) {
                        val eventTitle = eventSnapshot.getValue(String::class.java)

                        //if eventTitle is not null, add to eventsSavedList
                        eventTitle?.let {
                            eventsSavedList.add(it)
                        }
                    }
                    //Log.d("eventsSavedList", eventsSavedList.toString())
                    callback(eventsSavedList)
                }

                override fun onCancelled(error: DatabaseError) {
                    //handle error
                    //return emptyList
                    callback(emptyList())
                }
            })
        } else {
            callback(emptyList())
        }
    }
}
