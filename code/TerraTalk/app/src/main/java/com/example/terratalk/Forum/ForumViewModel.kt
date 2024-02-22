package com.example.terratalk.Forum

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.terratalk.Database.database
import com.example.terratalk.Database.userRef
import com.example.terratalk.UserManager.currentUser
import com.example.terratalk.UserManager.stateUser
import com.example.terratalk.models.Comment
import com.example.terratalk.models.Forum
import com.example.terratalk.models.Post
import com.example.terratalk.models.allPosts
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener

class ForumViewModel : ViewModel() {

    //initialise Forum states
    val stateForum: MutableState<Forum> = mutableStateOf(
        Forum(
            posts = emptyList(),
            postId = ""
        )
    )


    fun createPost(title: String, content: String, selectedOption: String) {
        //is current user not null (is logged in) & the display name is not null
        if (currentUser != null && currentUser!!.displayName != null) {
            //make new Post object
            val newPost = Post(currentUser!!.displayName, title, content, selectedOption)
            //get a database reference of children with value posts
            val postRef = database.reference.child("posts").push()
            //generate a unique key for the new post
            val postId = postRef.key
            if (postId != null) {
                //assign the generated postId to the new post
                newPost.postId = postId
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
                updateUserPostsinDatabase(postId)
            }
        } else {
            Log.w("UserProfile", "Display name is null or current user is null")
        }
    }


    private fun updateUserPostsinDatabase(postKey: String) {
        if (currentUser!= null) {
            userRef.child("posts").setValue(postKey)
        }
    }

    //fetch posts from database
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

    //set postId
    fun setPostId(postID: String) {
        stateForum.value.postId = postID
    }

    fun commentPost(postId: String, commentContent: String) {
        val postRef = database.getReference("posts/$postId")
        //store the user instance in a local variable

        postRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val post = currentData.getValue(Post::class.java)

                if (post == null || post.postId != postId) {
                    return Transaction.success(currentData)
                }

                //increment the number of comments
                post.numComments += 1

                if (currentUser != null && currentUser!!.displayName != null) {
                    //create a new comment
                    val newComment = Comment(
                        commentContent,
                        currentUser!!.displayName!!,
                        currentUser!!.uid
                    )

                    //add the new comment to the existing list of comments
                    post.postComments.add(newComment)
                } else {
                    Log.w("UserProfile", "Display name is null or current user is null")
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

    //func to deleteComment
    fun deletePost(postId: String) {
        val postRef = database.getReference("posts/$postId")

        postRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val post = currentData.getValue(Post::class.java)

                if (post == null || post.postId != postId) {
                    return Transaction.success(currentData)
                }

                // Since we are deleting the post, we don't need to find and remove a comment
                // We can just remove the post data from the currentData
                currentData.value = null

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

    //func to deleteComment
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
                    post.numComments -= 1
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


    fun likePost(postId: String, postTitle: String) {

        val postRef = database.getReference("posts/$postId")

        //increment the likes for the post
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
                if (error != null) {
                    Log.e("LikePost", "Transaction failed.", error.toException())
                } else if (!committed) {
                    Log.d("LikePost", "Transaction not committed.")
                } else {
                    Log.d("LikePost", "Transaction committed successfully.")
                }
            }
        })

        //add the post to the user's saved posts
        //store the user instance in a local variable
        if (currentUser != null) {

            val userId = currentUser!!.uid

            userRef.child("savedPosts").child(postId).setValue(postTitle)
                .addOnSuccessListener {
                    Log.d("LikePost", "Post saved successfully for user: $userId")
                }
                .addOnFailureListener { e ->
                    Log.e("LikePost", "Failed to save post for user: $userId", e)
                }
        }
    }

    fun unlikePost(postId: String, userId: String) {
        val postRef = database.getReference("posts/$postId")

        //decrement the likes for the post
        postRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val post = currentData.getValue(Post::class.java)

                if (post == null || post.postId != postId) {
                    return Transaction.success(currentData)
                }

                //ensure likes count is not negative
                post.postLikes = if (post.postLikes > 0) post.postLikes - 1 else 0
                currentData.value = post
                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                // Handle transaction completion
                if (error != null) {
                    Log.e("UnlikePost", "Transaction failed.", error.toException())
                } else if (!committed) {
                    Log.d("UnlikePost", "Transaction not committed.")
                } else {
                    Log.d("UnlikePost", "Transaction committed successfully.")
                }
            }
        })

        //remove the post from the user's saved posts
        userRef.child("savedPosts").child(postId).removeValue()
            .addOnSuccessListener {
                Log.d("UnlikePost", "Post unliked and removed from saved posts for user: $userId")
            }
            .addOnFailureListener { e ->
                Log.e(
                    "UnlikePost",
                    "Failed to unlike post and remove from saved posts for user: $userId",
                    e
                )
            }
    }

    fun toggleLikePost(postId: String, postTitle: String) {
        if (currentUser != null) {
            val userId = currentUser!!.uid
            val userRef = database.getReference("users/$userId/savedPosts")

            //check if the post is already in the user's saved posts
            userRef.child(postId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        //post is already liked, so unlike it
                        unlikePost(postId, userId)
                    } else {
                        //post is not liked, so like it
                        likePost(postId, postTitle)

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(
                        "ToggleLike",
                        "Failed to check saved posts for user: $userId",
                        error.toException()
                    )
                }
            })
        } else {
            Log.w("UserProfile", "Display name is null or current user is null")
        }
    }

    fun isPostLiked(postId: String): LiveData<Boolean> {
        val isLiked = MutableLiveData<Boolean>()
        if (currentUser != null) {
            val userId = currentUser!!.uid
            val userRef = database.getReference("users/$userId/savedPosts")

            //check if the post is already in the user's saved posts
            userRef.child(postId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //if the snapshot exists, the post is liked, otherwise it's not
                    isLiked.value = snapshot.exists()
                }

                override fun onCancelled(error: DatabaseError) {
                    //else its not liked
                    isLiked.value = false
                }
            })
        }
        return isLiked
    }

}
