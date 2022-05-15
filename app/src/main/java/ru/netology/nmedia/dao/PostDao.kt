package ru.netology.nmedia.dao

import ru.netology.nmedia.dto.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun removeById(id: Long)
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun viewById(id: Long)
    fun getById(id: Long): Post
}