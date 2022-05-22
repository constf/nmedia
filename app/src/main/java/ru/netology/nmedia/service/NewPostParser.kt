package ru.netology.nmedia.service

import com.google.gson.annotations.SerializedName

data class NewPostParser(
    @SerializedName("userId")
    val userId: Long,

    @SerializedName("userName")
    val userName: String,

    @SerializedName("postId")
    val postId: Long,

    @SerializedName("postContent")
    val postContent: String
)