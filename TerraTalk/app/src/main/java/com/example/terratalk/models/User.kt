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
    val userId: String = "",
    var profilePic: String = "",
    var bio: String = "",
    var cover: String? = null,
    var token: String? = null,
    var status: StatusTag? = StatusTag.ACTIVE,
    val postsCreated: MutableList<Post> = mutableListOf(),
    val eventsSaved: MutableList<Events> = mutableListOf(),

    ) {

    constructor(firebaseUser: FirebaseUser) : this(
        username = firebaseUser.displayName ?: "",
        email = firebaseUser.email ?: "",
        userId = firebaseUser.uid,
    )

    // Method to add events to the user's saved events list

    /* code below causes app build errors, need to revise

       need to set these in a viewModel with setValue() i think, like we set newsItems()

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


     */

}
