package com.example.terratalk.Forum
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction


data class User (
    var username: String = "",
    var bio: String = "",
    var email: String = "",
    var userid: String = "",
    var profilePic: String = "",
    var cover: String = "",
    var token: String = "",
    var status: String = "",
    val posts: MutableList<Post> = mutableListOf(),
    val comments: MutableList<Comment> = mutableListOf()
){

    constructor(firebaseUser: FirebaseUser): this(
        username = firebaseUser.displayName ?: "",
        email = firebaseUser.email ?: "",
        userid = firebaseUser.uid,
    )

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

    }*/

    fun createPost(username: String, content: String, database: FirebaseDatabase){

        val newPost = Post(username, content)
        val postref = database.getReference("posts")
        val postkey = postref.push().key
        postkey?.let{
            postref.child(postkey).setValue(newPost)
        }

        posts.add(newPost)
        updatePostFirebase(database)
    }

    private fun updatePostFirebase(database: FirebaseDatabase){
        val usersRef = database.getReference("users")
        val userRef = usersRef.child(userid)
        userRef.child("posts").setValue(posts.map{it.postId})
    }

    fun likePost(postId: String,database: FirebaseDatabase,  userId: String ) {
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

    fun commentPost(postId: String, database: FirebaseDatabase,  userId: String, commentContent: String ) {
        val postRef = database.getReference("posts/$postId")


        postRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val post = currentData.getValue(Post::class.java)


                if (post == null || post.postId != postId) {
                    return Transaction.success(currentData)
                }


                post.numComments += 1

                val newcomment = Comment(commentContent, username, userid)
                post.postComments.add(newcomment)
                currentData.value = post
                return Transaction.success(currentData)
            }

            override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
            }
        })
    }}
