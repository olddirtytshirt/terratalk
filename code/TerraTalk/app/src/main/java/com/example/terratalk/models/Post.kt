package com.example.terratalk.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class Post(
    val username: String? = "",
    var title: String? = "",
    var content: String = "",
    var postTag: String = "",
    var postId: String = UUID.randomUUID().toString(),
    var postLikes: Int = 0,
    val postComments: MutableList<Comment> = mutableListOf(),
    var numComments: Int = 0,
    var timestamp: String = SimpleDateFormat("HH:mm, dd/MM/yy", Locale.getDefault()).format(Date()),

)

val allPosts: MutableList<Post> = mutableListOf()


/*
class PostViewModel : ViewModel(){
    val user = User()

    private val title1 = mutableStateOf("")
    val title: State<String> = title1

    private val content1 = mutableStateOf("")
    val content: State<String> = content1

    fun setTitle(newTitle: String){
        title1.value = newTitle
    }

    fun setContent(newContent: String){
        content1.value = newContent
    }

    fun createPost(database:FirebaseDatabase){
        user.createPost(title1.value, content1.value, database)
    }

    fun commentPost(postId: String, database: FirebaseDatabase, userId: String, commentText: String) {
        user.commentPost(postId, database, userId, commentText)
    }

}

*/