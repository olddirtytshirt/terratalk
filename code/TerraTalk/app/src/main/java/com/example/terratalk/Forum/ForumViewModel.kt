package com.example.terratalk.Forum

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

    private val currentUser = FirebaseAuth.getInstance().currentUser
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
            posts = emptyList()
        )
    )

    val displayName = currentUser?.displayName

    fun createPost(title: String, content: String, selectedOption: String){
        if (displayName != null) {
            //use the displayName
            stateUser.value.username = displayName
            statePost.value.title = title
            statePost.value.content = content
            statePost.value.postTag = selectedOption
            //Log.d("UserProfile", "Display name is ${state.value.username}")
        } else {
            //cry
            Log.w("UserProfile", "Display name is null")
        }

        if (currentUser != null) {
            if (title.isNotEmpty() && content.isNotEmpty()) {
                val newPost = Post(stateUser.value.username, title, statePost.value.content, statePost.value.postTag)
                val postref = database.reference.child("posts")
                val postkey = postref.push().key
                Log.d("postKey", postkey ?: "null")
                postkey?.let {
                    postref.child(postkey).setValue(newPost)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("PostCreation", "Post created successfully with key: $postkey")
                            } else {
                                Log.e("PostCreation", "Failed to create post", task.exception)
                            }
                        }
                }
                stateUser.value.postsCreated.add(newPost)
                allPosts.add(newPost)
                updateUserPostsinDatabase(postkey!!, newPost)
            }
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


    fun User.commentPost(postId: String, userId: String, commentContent: String) {
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