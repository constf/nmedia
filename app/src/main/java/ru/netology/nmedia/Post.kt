package ru.netology.nmedia

data class Post(
    val id: Int,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false,
)
