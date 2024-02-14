package com.example.terratalk.Profile
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.terratalk.models.User
import com.google.firebase.Firebase
//import com.google.firebase.firestore.FirebaseFirestore


//DONT DELETE JUST DOESNT WORK TOO WELL WILL FIX LATER
/*
class ProfileViewModel : ViewModel() {
    //private val db = Firebase.firestore

    fun updateUserProfile(user: User) {
        db.collection("users").document(user.userId)
            .set(user)
            .addOnSuccessListener {
                Log.d(TAG, "User profile updated successfully")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating user profile", e)
            }
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}
*/