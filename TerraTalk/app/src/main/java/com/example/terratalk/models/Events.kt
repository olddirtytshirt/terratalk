package com.example.terratalk.models

import java.util.UUID

data class Events(
    val title: String = "",
    val date: String = "",
    val location: String = "",
    var eventId: String = UUID.randomUUID().toString()

)