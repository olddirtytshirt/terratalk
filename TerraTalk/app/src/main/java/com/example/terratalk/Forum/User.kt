package com.example.terratalk.Forum


import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase


data class User (
    var username: String = "",
    var bio: String = "",
    var email: String = "",
    var userid: String = "",
    var profilePic: String = "",
    var cover: String = "",
    var token: String = "",
    var status: String = "",
    val posts: MutableList<String> = mutableListOf()
){

    constructor(firebaseUser: FirebaseUser): this(
        username = firebaseUser.displayName ?: "",
        email = firebaseUser.email ?: "",
        userid = firebaseUser.uid,
    )

    fun setBio(bio: String){
        this.bio = bio

    }

    fun setprofilePic(profilePic: String){
        this.profilePic = profilePic

    }

    fun setCover(cover: String){
        this.cover = cover

    }

    fun setToken(token: String){
        this.token = token

    }

    fun setStatus(status: String){
        this.status = status

    }

    fun createPost(username: String, content: String, database: FirebaseDatabase){

        val newPost = Post(username, content)

    }
}