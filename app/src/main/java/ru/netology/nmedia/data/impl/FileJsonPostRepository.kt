package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.R
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post
import kotlin.properties.Delegates

class FileJsonPostRepository(private var application: Application) : PostRepository {

    private val prefs = application.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private var postId: Long by Delegates.observable(prefs.getLong(COUNTER_KEY, 1L)) {_, _, newValue ->
        prefs.edit { putLong(COUNTER_KEY, newValue)}
    }

    private val gson = Gson()
    private val typeTokenPost = TypeToken.getParameterized(List::class.java, Post::class.java).type
    // private val typeTokenLong = TypeToken.getParameterized(Long::class.java).type

    private var posts: List<Post>
        get() = checkNotNull(data.value) {
            "List of posts may not be null!"
        }
        set(value){
            application.openFileOutput(
                JSON_FILE_NAME, Context.MODE_PRIVATE
            ).bufferedWriter().use {
                it.write(gson.toJson(value))
            }
            data.value = value
        }

    override val data: MutableLiveData<List<Post>>

    init {
        val file = application.filesDir.resolve(JSON_FILE_NAME)
        val posts: List<Post> = if (file.exists()) {
                val fileInput = application.openFileInput(JSON_FILE_NAME)
                val reader = fileInput.bufferedReader()
                reader.use {
                    gson.fromJson(it, typeTokenPost)
                }
            } else {
                postId = 1L
                emptyList()
            }
        data = MutableLiveData(posts)
    }


    override fun like(id: Long) {
        posts = posts.map {
            if (it.id != id) it
            else {
                val liked = it.likedByMe
                it.copy(
                    likedByMe = !it.likedByMe,
                    numLikes = it.numLikes + if (liked) -1 else 1
                )
            }
        }
        data.value = posts
    }


    override fun share(id: Long) {
        posts = posts.map {
            if (it.id != id) it
            else it.copy(numShares = it.numShares + 1)
        }
        data.value = posts
    }

    override fun view(id: Long) {
        posts = posts.map {
            if (it.id != id) it
            else it.copy(numViews = it.numViews + 1)
        }
        data.value = posts
    }

    override fun remove(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == 0L){ // Если это новый пост
            posts = posts + post.copy(
                id = postId++,
                author = "Konstantin",
                published = "May 8, 2022",
                likedByMe = false,
                numLikes = 999,
                numShares = 9_999_999,
                numViews = 9_195
            )
            data.value = posts
            return
        }

        // Редактирование поста
        posts = posts.map {
            if (it.id != post.id) it else post.copy(content = post.content)
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