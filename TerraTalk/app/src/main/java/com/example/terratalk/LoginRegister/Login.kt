package com.example.terratalk.LoginRegister

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase


val auth: FirebaseAuth = FirebaseAuth.getInstance()

fun loginUser(email: String, password: String, username: String) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Login successful
                val user = auth.currentUser
                SetUsername(username)
            } else {
                // Login failed
                Log.w("Login", "signInWithEmail:failure", task.exception)
            }
        }
}


fun SetUsername(username: String){

    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    if (userId != null) {
        val userRef = usersRef.child(userId)
        userRef.child("username").setValue(username)
    }

}

fun registerUser(email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Registration successful
                val user = auth.currentUser
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

