package com.example.terratalk.Forum



abstract class User(
    var name:String="",
    var bio:String="",
    var email:String="",
    var id:String="",
    var image:String="",
    var cover:String="",
    var token: String = "",
    val status: String = ""
)