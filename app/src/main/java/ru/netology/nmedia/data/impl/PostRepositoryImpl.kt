package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.*
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.toEntity
import ru.netology.nmedia.dao.toModel
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post
import kotlin.properties.Delegates

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {

    private var posts: List<Post> = emptyList()
                get() = checkNotNull(data.value){"Data value should not be null!"}

    override val data: LiveData<List<Post>> = dao.getAll().asLiveData().map { postList ->
        postList.map { it.toModel() }
    }



    override fun like(id: Long) {
        dao.likeById(id)
    }


    override fun share(id: Long) {
        dao.shareById(id)
    }

    override fun view(id: Long) {
        dao.viewById(id)
    }

    override fun remove(id: Long) {
        dao.removeById(id)
    }

    override fun save(post: Post) {
        dao.insert(post.toEntity())
    }

    override fun get(id: Long): Post? {
        val post = posts.find { it.id == id}
        return post
    }

    private companion object {
        const val POSTS_KEY = "posts"
        const val COUNTER_KEY = "counter"
        const val JSON_FILE_NAME = "posts.json"
    }


}