package com.example.terratalk.models

data class User (
    val username: String? = null,
    val email: String? = null,
    val userId: String? = null,
    val profilePic: String? = null,
    val bio: String? = null,
    val cover: String? = null,
    val token: String? = null,
    val status: String? = null,
    val posts: MutableList<String> = mutableListOf()
)