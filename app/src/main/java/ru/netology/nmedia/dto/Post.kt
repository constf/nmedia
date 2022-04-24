package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,
    val numLikes: Int = 0,
    val numShares: Int = 0,
    val numViews: Int = 0
)
