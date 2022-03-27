package com.example.blogapp.data.model

import java.sql.Timestamp
import java.util.*

data class Post(
    val profile_picture: String = "",
    val profile_name: String = "",
    val post_timestamp: Date? = null, //No anda con timestamp
    val post_image: String = ""

)