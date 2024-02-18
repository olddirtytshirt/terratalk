package com.example.terratalk.Profile
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.terratalk.models.StatusTag
import com.example.terratalk.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ProfileViewModel : ViewModel() {

    val state: MutableState<User> = mutableStateOf(
        User(
            username = "",
            email = null,
            userId = "",
            profilePic = "",
            bio = "",
            status = StatusTag.ACTIVE,
            postsCreated = mutableListOf(),
            eventsSaved = mutableListOf(),
        )
    )

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val database = FirebaseDatabase.getInstance().reference.child("users").child(currentUser?.uid ?: "")


    fun updateUsername(newUsername: String){
        state.value = state.value.copy(username = newUsername)
        database.child("username").setValue(newUsername)
    }

    fun updateBio(newBio: String){
        state.value = state.value.copy(bio = newBio)
        database.child("bio").setValue(newBio)
    }

    fun updateStatus(newStatus: StatusTag){
        state.value = state.value.copy(status = newStatus)
        database.child("status").setValue(newStatus)
    }

    

}

