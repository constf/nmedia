package ru.netology.nmedia.dto

import ru.netology.nmedia.R

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,
    val ytVideo: String? = null,
    val numLikes: Int = 0,
    val numShares: Int = 0,
    val numViews: Int = 0
)
