package com.example.terratalk.models

enum class StatusTag{
    ACTIVE,
    IDLE,
    OFFLINE,
}


data class User(
    var username: String = "",
    var email: String? = "",
    var status: StatusTag? = StatusTag.ACTIVE,
    val postsCreated: MutableList<Post> = mutableListOf(),
    val eventsSaved: MutableList<Events> = mutableListOf(),
    )