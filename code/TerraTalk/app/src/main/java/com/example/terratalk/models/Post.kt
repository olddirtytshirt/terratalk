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
