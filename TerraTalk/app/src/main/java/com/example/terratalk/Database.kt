package com.example.terratalk

import android.util.Log
import com.example.terratalk.models.User
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class ReadandWrite {

    private lateinit var database: DatabaseReference


    //intialise Database Reference
    fun initialiseDb() {
        database = Firebase.database.reference
    }

    fun writeNewUser(username: String, email: String, userId: String) {
        val user = User(username, email)

        //add to "users" branch in database with userId as key
        database.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                Log.d("Database", "User added successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Database", "Error adding user", e)
            }
    }
}


    /*
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
        val postref = database.getReference("posts")
        val postkey = postref.push().key
        postkey?.let{
            postref.child(postkey).setValue(newPost)
        }

        posts.add(newPost.postId)
        updatePostsinFirebase(database)
    }

    private fun updatePostsinFirebase(database: FirebaseDatabase){
        val usersRef = database.getReference("users")
        val userRef = usersRef.child(userid)
        userRef.child("posts").setValue(posts)
    }

     */