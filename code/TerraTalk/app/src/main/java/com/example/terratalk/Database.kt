package com.example.terratalk

import android.util.Log
import com.example.terratalk.UserManager.currentUser
import com.example.terratalk.models.StatusTag
import com.example.terratalk.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object Database {

    //getter for database
    val database: FirebaseDatabase
        get() = FirebaseDatabase.getInstance()

    val userRef: DatabaseReference
        get() = database.reference.child("users").child(UserManager.currentUser?.uid ?: "")


    fun writeNewUser(username: String, email: String, userId: String) {
        val user = User(username, email)

        //add to "users" branch in database with userId as key
        database.reference.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                Log.d("Database", "User added successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Database", "Error adding user", e)
            }
    }

    //func that saves the saved event in the database
    fun saveEvent(eventTitle: String) {
        if (currentUser != null) {

            val eventsSavedRef = userRef.child("eventsSaved")

            //check if the event already exists in the database, to avoid duplicates if there is any bug
            eventsSavedRef.orderByValue().equalTo(eventTitle).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        //event already exists, do not save again
                        Log.d("Event", "Event already exists: $eventTitle")
                    } else {
                        //event does not exist, save it
                        eventsSavedRef.push().setValue(eventTitle)
                            .addOnSuccessListener {
                                Log.d("Event", "Event saved successfully: $eventTitle")
                            }
                            .addOnFailureListener { exception ->
                                Log.e("Event", "Failed to save event: $eventTitle", exception)
                            }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Event", "Database error occurred: ${databaseError.message}")
                }
            })
        }
    }

    //func that deletes the saved event in the database
    fun removeSaved(eventTitle: String) {
        if (currentUser != null) {
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
                    callback(null)
                }
            })
        } else {
            callback(null)
        }
    }

    fun updateStatus(newStatus: StatusTag) {
        //if currentUser is null, return without updating the status
        if (currentUser == null) return

        //if currentUser is not null, proceed with updating the status
        UserManager.stateUser.value = UserManager.stateUser.value.copy(status = newStatus)
        userRef.child("status").setValue(newStatus)
    }
}
