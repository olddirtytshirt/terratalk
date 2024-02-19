package com.example.terratalk.models

import com.google.firebase.auth.FirebaseUser

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

    ) {

    constructor(firebaseUser: FirebaseUser) : this(
        username = firebaseUser.displayName ?: "",
        email = firebaseUser.email ?: "",
    )
}
