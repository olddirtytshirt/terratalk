package com.example.terratalk.Forum

import com.google.firebase.auth.FirebaseUser


data class User (
    var username: String = "",
    var bio: String = "",
    var email: String = "",
    var userid: String = "",
    var image: String = "",
    var cover: String = "",
    var token: String = "",
    val status: String = "",
){

constructor(firebaseUser: FirebaseUser): this(
    username = firebaseUser.displayName ?: "",
    email = firebaseUser.email ?: "",
    userid = firebaseUser.uid,
)
}