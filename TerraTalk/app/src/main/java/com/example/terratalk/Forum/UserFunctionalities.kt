package com.example.terratalk.Forum

import com.example.terratalk.models.Comment
import com.example.terratalk.models.Events
import com.example.terratalk.models.Post
import com.example.terratalk.models.PostTag
import com.example.terratalk.models.User
import com.example.terratalk.models.allPosts
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction

fun User.createPost(username: String, content: String, database: FirebaseDatabase, title: String){

    val newPost = Post(username, content)
    val postref = database.getReference("posts")
    val postkey = postref.push().key
    postkey?.let{
        postref.child(postkey).setValue(newPost)
    }

    postsCreated.add(newPost)
    allPosts.add(newPost)
    updatePostsinFirebase(database)
}

fun User.updatePostsinFirebase(database: FirebaseDatabase){
    val usersRef = database.getReference("users")
    val userRef = usersRef.child(userId)
    userRef.child("posts").setValue(postsCreated)
}

fun User.likePost(postId: String,database: FirebaseDatabase,  userId: String ) {
    val postRef = database.getReference("posts/$postId")


    postRef.runTransaction(object : Transaction.Handler {
        override fun doTransaction(currentData: MutableData): Transaction.Result {
            val post = currentData.getValue(Post::class.java)


            if (post == null || post.postId != postId) {
                return Transaction.success(currentData)
            }


            post.postLikes += 1

            currentData.value = post
            return Transaction.success(currentData)
        }

        override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
        }
    })
}

fun User.commentPost(postId: String, database: FirebaseDatabase,  userId: String, commentContent: String ) {
    val postRef = database.getReference("posts/$postId")


    postRef.runTransaction(object : Transaction.Handler {
        override fun doTransaction(currentData: MutableData): Transaction.Result {
            val post = currentData.getValue(Post::class.java)


            if (post == null || post.postId != postId) {
                return Transaction.success(currentData)
            }


            post.numComments += 1

            val newcomment = Comment(commentContent, username, userId)
            post.postComments.add(newcomment)
            currentData.value = post
            return Transaction.success(currentData)
        }

        override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
        }
    })
}


fun Conversion(post: Post): Events{
    return Events(
        title = post.title?: "",
        date = "",
        location = "",
        eventId = post.postId

    )
}

fun PosttoEvent(allPosts: MutableList<Post>, listofUserEvents: MutableList<Events>){
    for (post in allPosts){
        if (post.postLikes > 100 && post.postTag == PostTag.EVENT){
            listofUserEvents.add(Conversion(post))

        }
    }
}

