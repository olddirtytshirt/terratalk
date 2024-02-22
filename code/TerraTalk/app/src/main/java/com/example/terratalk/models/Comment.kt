package com.example.terratalk.models

import java.util.UUID

//comments for posts

data class Comment(
    var commentContent:String="",
    var username:String="",
    var userid: String = "",
    var commentId: String = UUID.randomUUID().toString(),

)