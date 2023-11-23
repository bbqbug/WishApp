package com.example.wishapp.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val _id : String,
    @SerializedName("username")
    val username: String,
    @SerializedName("avatar")
    val avatar : String,
)
