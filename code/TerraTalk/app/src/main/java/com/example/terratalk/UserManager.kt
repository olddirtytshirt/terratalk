package com.example.terratalk

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.terratalk.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

//file to initialise a single source of truth for currentUser


object UserManager {
    private val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    //initialise User states
    val stateUser: MutableState<User> = mutableStateOf(
        User(
            username = "",
            postsCreated = mutableListOf()
        )
    )
}