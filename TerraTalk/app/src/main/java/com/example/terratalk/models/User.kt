package com.example.terratalk.models

import com.google.firebase.auth.FirebaseUser

data class User (
    var username: String = "",
    var email: String? = null,
    val userId: String = "",
    var profilePic: String? = null,
    var bio: String? = null,
    var cover: String? = null,
    var token: String? = null,
    var status: String? = null,
    val postsCreated: MutableList<Post> = mutableListOf(),
    val eventsSaved: MutableList<Events> = mutableListOf(),

) {

    constructor(firebaseUser: FirebaseUser) : this(
        username = firebaseUser.displayName ?: "",
        email = firebaseUser.email ?: "",
        userId = firebaseUser.uid,
    )

    // Method to add events to the user's saved events list


    fun setBio(bio: String) {
        this.bio = bio

    }

    fun setprofilePic(profilePic: String) {
        this.profilePic = profilePic

    }

    fun setCover(cover: String) {
        this.cover = cover

    }

    fun setToken(token: String) {
        this.token = token

    }

    fun setStatus(status: String) {
        this.status = status

    }


}
