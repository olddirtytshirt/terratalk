package com.example.terratalk.models

import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

data class Events(
    val title: String,
    val link: String,
    val imageUrl: String,
    val date: String,
    val location: String,
)


var listofUserEvents: MutableList<Events> = mutableListOf()

