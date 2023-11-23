package com.example.wishapp.models

import com.google.gson.annotations.SerializedName

data class RequestUpdateWish (
    val idUser : String,
    val idWish : String,
    @SerializedName("name")
    val fullname : String,
    val content : String,
)
