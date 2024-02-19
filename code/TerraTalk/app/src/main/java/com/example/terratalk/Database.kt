package com.example.terratalk

import android.util.Log
import com.example.terratalk.models.User
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class ReadandWrite {

    private lateinit var database: DatabaseReference


    //intialise Database Reference
    private fun initialiseDb() {
        database = Firebase.database.reference
    }

    fun writeNewUser(username: String, email: String, userId: String) {
        initialiseDb()
        val user = User(username, email)

        //add to "users" branch in database with userId as key
        database.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                Log.d("Database", "User added successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Database", "Error adding user", e)
            }
    }
}
