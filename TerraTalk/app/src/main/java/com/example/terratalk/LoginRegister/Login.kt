package com.example.terratalk.LoginRegister

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.terratalk.Forum.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


val auth: FirebaseAuth = FirebaseAuth.getInstance()

fun loginUser(email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Login successful
                val user = auth.currentUser
            } else {
                // Login failed
                Log.w("Login", "signInWithEmail:failure", task.exception)
            }
        }
}

fun updateProfile(user: User, username: String){
    val userId = user.userid
    SetUsername(userId, username)
}


fun SetUsername(userId: String, username: String){

    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")
    val userRef = usersRef.child(userId)

    userRef.child("username").setValue(username)

}

fun registerUser(email: String, password: String, username: String, context: Context) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Registration successful
                val user = auth.currentUser
                if(user != null){
                    val newUser = User(user)
                    updateProfile(newUser, username)
                    sendEmailVerification(context)
                }

            } else {
                // Registration failed
                Log.w("Registration", "createUserWithEmail:failure", task.exception)
            }
        }
}

fun logOut(){
    auth.signOut()
}

fun sendEmailVerification(context: Context) {
    auth.currentUser?.sendEmailVerification()
        ?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Verification email sent", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("Verification", "sendEmailVerification:failure", task.exception)
                Toast.makeText(context, "Failed to send verification email", Toast.LENGTH_SHORT).show()
            }}}


