package com.example.terratalk.Forum


data class Post(
           val userName: String? = "",
           val content: String = "",
           val postId: String = "",
           val postTag: String = "",
           val postLikes: Int = 0,
           val postComments: Int = 0
)


