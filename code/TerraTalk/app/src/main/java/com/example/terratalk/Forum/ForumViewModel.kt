package com.example.terratalk.Forum

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.terratalk.models.Comment
import com.example.terratalk.models.Events
import com.example.terratalk.models.Forum
import com.example.terratalk.models.Post
import com.example.terratalk.models.User
import com.example.terratalk.models.allPosts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener

class ForumViewModel : ViewModel() {

    val currentUser = FirebaseAuth.getInstance().currentUser
    private val database = FirebaseDatabase.getInstance()


    //initialise User states
    val stateUser: MutableState<User> = mutableStateOf(
        User(
            username = "",
            postsCreated = mutableListOf()
        )
    )

    //initialise Post states
    val statePost: MutableState<Post> = mutableStateOf(
        Post(
            postTag = ""
            )
    )

    val stateForum: MutableState<Forum> = mutableStateOf(
        Forum(
            posts = emptyList(),
            postId = ""
        )
    )

    val displayName = currentUser?.displayName

    fun createPost(title: String, content: String, selectedOption: String) {
        if (displayName != null && currentUser != null) {
            val newPost = Post(displayName, title, content, selectedOption)
            val postRef = database.reference.child("posts").push() // Generate a unique key for the new post
            val postId = postRef.key // Get the generated postId
            if (postId != null) {
                newPost.postId = postId // Assign the generated postId to the new post
                postRef.setValue(newPost)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("PostCreation", "Post created successfully with id: $postId")
                        } else {
                            Log.e("PostCreation", "Failed to create post", task.exception)
                        }
                    }
                stateUser.value.postsCreated.add(newPost)
                allPosts.add(newPost)
                updateUserPostsinDatabase(postId, newPost)
            }
        } else {
            Log.w("UserProfile", "Display name is null or current user is null")
        }
    }


    fun updateUserPostsinDatabase(postKey: String, newPost: Post) {
        if (currentUser != null) {
            val usersRef = database.getReference("users")
            val userRef = usersRef.child(currentUser.uid)
            userRef.child("posts").child(postKey).setValue(newPost)
        }
    }

    fun fetchPosts() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("posts")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val postList = mutableListOf<Post>()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    post?.let { postList.add(it) }
                }
                val updatedForum = stateForum.value.copy(posts = postList)
                stateForum.value = updatedForum
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
    fun setPostId(postID: String) {
        stateForum.value.postId = postID
    }

    fun commentPost(postId: String, commentContent: String) {
        val postRef = database.getReference("posts/$postId")

        postRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val post = currentData.getValue(Post::class.java)

                if (post == null || post.postId != postId) {
                    return Transaction.success(currentData)
                }

                //increment the number of comments
                post.numComments += 1

                if(currentUser != null && displayName != null) {
                    //create a new comment
                    val newComment = Comment(commentContent, displayName, currentUser.uid)

                    //add the new comment to the existing list of comments
                    post.postComments.add(newComment)
                }


                //update the post in the database
                currentData.value = post

                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                //transaction completion
            }
        })
    }


    fun deleteComment(postId: String, commentId: String) {
        val postRef = database.getReference("posts/$postId")

        postRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val post = currentData.getValue(Post::class.java)

                if (post == null || post.postId != postId) {
                    return Transaction.success(currentData)
                }

                //find the comment by its ID and remove it from the list
                val commentToRemove = post.postComments.find { it.commentId == commentId }
                if (commentToRemove != null) {
                    post.postComments.remove(commentToRemove)
                    //decrement the number of comments
                    post.numComments -=  1
                }

                //update the post in the database
                currentData.value = post

                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                //handle transaction completion
                if (error != null) {
                    //transaction failed, log the error
                    Log.w(TAG, "Transaction failed.", error.toException())
                } else if (!committed) {
                    //transaction not committed, handle accordingly
                    Log.d(TAG, "Transaction not committed.")
                } else {
                    //transaction committed successfully
                    Log.d(TAG, "Transaction committed successfully.")
                }
            }
        })
    }

    fun likePost(postId: String, userId: String) {
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

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
            }
        })
    }

    fun Conversion(post: Post): Events {
        return Events(
            title = post.title ?: "",
            link = "",
            imageUrl = "",
            date = "",
            location = "",

            )
    }

    fun PosttoEvent(allPosts: MutableList<Post>, listofUserEvents: MutableList<Events>) {
        for (post in allPosts) {
            if (post.postLikes > 100 && post.postTag.equals(post.postTag.contains("event"))) {
                listofUserEvents.add(Conversion(post))

            }
        }
    }

}
/*
fun createPost(database: FirebaseDatabase){
    val username = currentUser?.username
    val userId = currentUser?.userId
    val content = content1.value
    val title = title1.value
    user.createPost(username, content, title)

}



fun User.createPost(title: String, content: String,) {
    if (currentUser != null) {
        val userId = currentUser.uid

        val userRef = database.getReference("users/$userId")
        val username = userRef.child("username").orderByValue().toString()
        if (title != "" && content != "") {
            val newPost = Post(username, content)
            val postref = database.getReference("posts")
            val postkey = postref.push().key
            postkey?.let {
                postref.child(postkey).setValue(newPost)
            }
            postsCreated.add(newPost)
            allPosts.add(newPost)
            updatePostsinFirebase()
        }
    }
}


 */