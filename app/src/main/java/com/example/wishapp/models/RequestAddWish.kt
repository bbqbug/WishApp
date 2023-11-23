package com.example.wishapp.models

import com.google.gson.annotations.SerializedName

data class RequestAddWish(
    val idUser : String,

    @SerializedName("name")
    val fullname : String,
    val content: String,
)
