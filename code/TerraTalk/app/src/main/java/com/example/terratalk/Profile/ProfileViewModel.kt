package com.example.terratalk.Profile

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.terratalk.LoginRegister.auth
import com.example.terratalk.models.StatusTag
import com.example.terratalk.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileViewModel : ViewModel() {
    //getter for currentUser
    val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    //getter for database
    private val database: FirebaseDatabase
        get() = FirebaseDatabase.getInstance()

    private val userRef: DatabaseReference
        get() = database.reference.child("users").child(currentUser?.uid ?: "")

    val state: MutableState<User> = mutableStateOf(
        User(
            username = currentUser?.displayName ?:"",
            status = StatusTag.ACTIVE,
        )
    )

    fun updateStatus(newStatus: StatusTag) {
        //if currentUser is null, return without updating the status
        if (currentUser == null) return

        //if currentUser is not null, proceed with updating the status
        state.value = state.value.copy(status = newStatus)
        userRef.child("status").setValue(newStatus)
    }


    fun signOut() {
        if (currentUser != null) {
            // Update the status to OFFLINE before signing out
            GlobalScope.launch(Dispatchers.IO) {
                updateStatus(StatusTag.OFFLINE)
                // After the status is updated, sign out the user
                withContext(Dispatchers.Main) {
                    auth.signOut()
                }
            }
        }
    }
}

