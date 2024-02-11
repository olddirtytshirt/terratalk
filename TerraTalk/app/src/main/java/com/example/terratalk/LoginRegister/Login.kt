package com.example.terratalk.LoginRegister

import com.example.terratalk.ReadandWrite
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.example.terratalk.Screen
import java.util.Objects

val auth: FirebaseAuth = FirebaseAuth.getInstance()

fun loginUser(email: String, password: String, context: Context, navController: NavController) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Login successful
                val user = auth.currentUser
                if(user!!.isEmailVerified()) {
                    navController.navigate(Screen.NewsPage.route)
                } else {
                    Toast.makeText(context, "please verify your email", Toast.LENGTH_SHORT).show()
                }

            } else {
                // Login failed
                Log.w("Login", "signInWithEmail:failure", task.exception)
                // display error message
                Toast.makeText(context, Objects.toString(task.exception!!.message), Toast.LENGTH_SHORT).show()

            }
        }
}


fun registerUser(email: String, password: String, username: String, navController: NavController, context: Context) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // get current user
                val user = auth.currentUser
                if(user != null){
                    //initialise Database
                    val rw = ReadandWrite()
                    rw.initialiseDb()
                    //write new user in database
                    rw.writeNewUser(username, email, user.uid)
                    sendEmailVerification(context, navController)
                }

            } else {
                // log error in logcat
                Log.w("Registration", "createUserWithEmail:failure", task.exception)
                // pop up indicating error message
                Toast.makeText(context, Objects.toString(task.exception!!.message), Toast.LENGTH_LONG).show()
            }
        }
}



fun logOut() {
    auth.signOut()
}


fun sendEmailVerification(context: Context, navController: NavController) {
    val user = auth.currentUser!!
    user.sendEmailVerification()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Verification email sent", Toast.LENGTH_SHORT).show()
                //go back to login page to login
                navController.navigate(Screen.SignInPreview.route)

            } else {
                Log.e("Verification", "sendEmailVerification:failure", task.exception)
                Toast.makeText(context, "Failed to send verification email", Toast.LENGTH_SHORT).show()
            }
        }
}
