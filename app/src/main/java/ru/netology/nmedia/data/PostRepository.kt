package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>

    fun like(id: Long)
    fun share(id: Long)
    fun view(id: Long)
    fun remove(id: Long)
    fun save(post: Post)
}
