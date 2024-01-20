package com.example.terratalk.Forum

import java.io.Serializable

data class Post(
    var userName:String?="",
    var userEmail:String?="",
    var caption:String="",
    var postId:String="",
    var postTag:String="",
    var postLikes:Int=0,
    var postComments:Int=0,


):Serializable