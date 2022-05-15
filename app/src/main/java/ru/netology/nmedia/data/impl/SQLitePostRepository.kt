package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post
import kotlin.properties.Delegates

class SQLitePostRepository(private var application: Application, private val dao: PostDao) : PostRepository {

//    private val prefs = application.getSharedPreferences("repo", Context.MODE_PRIVATE)
//    private var postId: Long by Delegates.observable(prefs.getLong(COUNTER_KEY, 1L)) { _, _, newValue ->
//        prefs.edit { putLong(COUNTER_KEY, newValue)}
//    }

    private var posts: List<Post> = emptyList()
    override val data: MutableLiveData<List<Post>>

    init {
        posts = dao.getAll()
        data = MutableLiveData(posts)
    }


    override fun like(id: Long) {
        dao.likeById(id)
        val likedPost = dao.getById(id)

        posts = posts.map {
            if (it.id != id) it
            else likedPost
        }

        data.value = posts
    }


    override fun share(id: Long) {
        dao.shareById(id)
        val sharedPost = dao.getById(id)

        posts = posts.map {
            if (it.id != id) it
            else sharedPost
        }

        data.value = posts
    }

    override fun view(id: Long) {
        dao.viewById(id)
        val viewedPost = dao.getById(id)

        posts = posts.map {
            if (it.id != id) it
            else viewedPost
        }

        data.value = posts
    }

    override fun remove(id: Long) {
        dao.removeById(id)
        posts = dao.getAll()

        data.value = posts
    }

    override fun save(post: Post) {
        val id = post.id
        val savedPost = dao.save(post)
        posts = if (id == 0L) {
            posts + listOf(savedPost)
        } else {
            posts.map {
                if (it.id != id) it
                else savedPost
            }
        }

        data.value = posts
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