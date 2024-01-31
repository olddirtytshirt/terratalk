package com.example.terratalk.Forum


class Post(userName: String? = "",
           val userEmail: String? = "",
           val content: String = "",
           val postId: String = "",
           val postTag: String = "",
           val postLikes: Int = 0,
           val postComments: Int = 0)