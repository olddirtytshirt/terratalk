package com.example.terratalk.Events

import android.util.Log
import com.example.terratalk.models.User
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser



fun User.saveEvent(eventId: String, database: FirebaseDatabase) {
    val userRef = database.getReference("user/$userId")
    val eventsSavedRef = userRef.child("eventsSaved")

    eventsSavedRef.push().setValue(eventId)

        .addOnSuccessListener {
            Log.d("Event", "Event saved successfully: $eventId")
        }
        .addOnFailureListener { exception ->
            Log.e("Event", "Failed to save event: $eventId", exception)
        }

}

fun User.updateEventsinFirebase(database: FirebaseDatabase){
    val usersRef = database.getReference("users")
    val userRef = usersRef.child(userId)
    userRef.child("eventsSaved").setValue(eventsSaved)
}

fun getCurrentUserFromFirebase(): User? {
    val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    return if (firebaseUser != null) {
        User(
            username = firebaseUser.displayName ?: "",
            email = firebaseUser.email,
            userId = firebaseUser.uid,
        )
    } else {
        null
    }
}
