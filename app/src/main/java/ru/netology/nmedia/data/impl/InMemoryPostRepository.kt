package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {

    private var postId = 1L

    override val data = MutableLiveData<List<Post>>(
        List(20) { index ->
            Post(
                id = postId++,
                "Konstantin",
                "Post ${index+1} created by repository initializer! This post is a test text, just for test purposes.\nIt consists of 2 strings of text in content field.",
                "April 26, 2022",
                ytVideo = if (index % 2 == 0) "QUwPzUCjqO8" else null,
                numLikes = 999,
                numShares = 9_999_995,
                numViews = 23_195 + index
            )
        }
    )


    private val posts
        get() = checkNotNull(data.value) {
            "List of posts may not be null!"
        }


    override fun like(id: Long) {
        val likedPosts = posts.map {
            if (it.id != id) it
            else {
                val liked = it.likedByMe
                it.copy(
                    likedByMe = !it.likedByMe,
                    numLikes = it.numLikes + if (liked) -1 else 1
                )
            }
        }
        data.value = likedPosts
    }


    override fun share(id: Long) {
        val sharedPosts = posts.map {
            if (it.id != id) it
            else it.copy(numShares = it.numShares + 1)
        }
        data.value = sharedPosts
    }


    override fun view(id: Long) {
        val viewedPosts = posts.map {
            if (it.id != id) it
            else it.copy(numViews = it.numViews + 1)
        }
        data.value = viewedPosts
    }

    override fun remove(id: Long) {
        data.value = posts.filter { it.id != id }
    }

    override fun save(post: Post) {
        if (post.id == 0L){ // Если это новый пост
            data.value = posts + post.copy(
                id = postId++,
                author = "Konstantin",
                published = "May 8, 2022",
                likedByMe = false,
                numLikes = 999,
                numShares = 9_999_999,
                numViews = 9_195
            )
            return
        }

        // Редактирование поста
        data.value = posts.map {
            if (it.id != post.id) it else post.copy(content = post.content)
        }
    }

}