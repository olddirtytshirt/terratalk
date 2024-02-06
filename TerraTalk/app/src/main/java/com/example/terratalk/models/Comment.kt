package com.example.terratalk.models

import java.util.UUID


data class Comment(
    var commentId: String = UUID.randomUUID().toString(),
    var commentContent:String="",
    var userName:String="",
    var userid: String = ""

)