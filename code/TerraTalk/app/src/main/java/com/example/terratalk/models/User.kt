package com.example.terratalk.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class StatusTag{
    ACTIVE,
    OFFLINE,
}


data class User(
    var username: String = "",
    var email: String? = "",
    val dateCreated: String = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date()),
    var status: StatusTag? = StatusTag.ACTIVE,
    val postsCreated: MutableList<Post> = mutableListOf(),
    val eventsSaved: MutableList<Events> = mutableListOf(),
    )