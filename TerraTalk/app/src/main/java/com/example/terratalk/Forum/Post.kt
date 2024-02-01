package com.example.terratalk.Forum


data class Post(
    val userName: String? = "",
    val title: String? = "",
    val content: String = "",
    var postId: String = "",
    val postTag: String = "",
    val postLikes: Int = 0,
    val postComments: Int = 0
)


