package com.example.terratalk.models

import java.util.UUID


data class Comment(
    var commentContent:String="",
    var username:String="",
    var userid: String = "",
    var commentId: String = UUID.randomUUID().toString(),

)