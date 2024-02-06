package com.example.terratalk.models

import com.google.firebase.auth.FirebaseUser

data class User (
    val username: String = "",
    val email: String? = null,
    val userId: String = "",
    val profilePic: String? = null,
    val bio: String? = null,
    val cover: String? = null,
    val token: String? = null,
    val status: String? = null,
    val posts: MutableList<String> = mutableListOf()
) {

    constructor(firebaseUser: FirebaseUser) : this(
        username = firebaseUser.displayName ?: "",
        email = firebaseUser.email ?: "",
        userId = firebaseUser.uid,
    )
}