package com.example.terratalk.models

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.terratalk.models.User
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID


data class Post(
    val userName: String? = "",
    var title: String? = "",
    var content: String = "",
    var postId: String = UUID.randomUUID().toString(),
    val postTag: String = "",
    val postLikes: Int = 0,
    val postComments: Int = 0
)


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
}

*/