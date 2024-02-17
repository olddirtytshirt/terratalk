package com.example.terratalk.Forum

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.terratalk.models.Comment
import com.example.terratalk.models.Events
import com.example.terratalk.models.Post
import com.example.terratalk.models.PostTag
import com.example.terratalk.models.User
import com.example.terratalk.models.allPosts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction

class ForumViewModel : ViewModel() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val database = FirebaseDatabase.getInstance()

    private val title1 = mutableStateOf("")
    val title: State<String> = title1

    private val content1 = mutableStateOf("")
    val content: State<String> = content1

    fun setTitle(newTitle: String) {
        title1.value = newTitle
    }

    fun setContent(newContent: String) {
        content1.value = newContent
    }

    fun createPost(database: FirebaseDatabase){
        val username = currentUser?.username
        val userId = currentUser?.userId
        val content = content1.value
        val title = title1.value
        user.createPost(username, content, title)

    }


    fun User.createPost(username: String, content: String, title: String){

        val newPost = Post(username, content)
        val postref = database.getReference("posts")
        val postkey = postref.push().key
        postkey?.let{
            postref.child(postkey).setValue(newPost)
        }

        postsCreated.add(newPost)
        allPosts.add(newPost)
        updatePostsinFirebase()
    }

    fun User.updatePostsinFirebase(){
        val usersRef = database.getReference("users")
        val userRef = usersRef.child(userId)
        userRef.child("posts").setValue(postsCreated)
    }

    fun User.likePost(postId: String, userId: String ) {
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


    fun User.commentPost(postId: String, userId: String, commentContent: String ) {
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


    fun Conversion(post: Post): Events {
        return Events(
            title = post.title?: "",
            link = "",
            imageUrl = "",
            date = "",
            location = "",

            )
    }

    fun PosttoEvent(allPosts: MutableList<Post>, listofUserEvents: MutableList<Events>){
        for (post in allPosts){
            if (post.postLikes > 100 && post.postTag == PostTag.EVENT){
                listofUserEvents.add(Conversion(post))

            }
        }
    }

}