package com.example.terratalk.Events

import com.example.terratalk.models.User
import com.google.firebase.database.FirebaseDatabase


fun User.saveEvent(eventId: String, database: FirebaseDatabase, userId: String ) {
    val userRef = database.getReference("user/$userId")
    val eventsSavedRef = userRef.child("eventsSaved")

    eventsSavedRef.push().setValue(eventId)

}

fun User.updateEventsinFirebase(database: FirebaseDatabase){
    val usersRef = database.getReference("users")
    val userRef = usersRef.child(userId)
    userRef.child("eventsSaved").setValue(eventsSaved)
}