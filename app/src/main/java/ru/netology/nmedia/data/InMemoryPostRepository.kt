package ru.netology.nmedia.data

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {
    override val data = MutableLiveData<Post>()

    override fun like() {
        val currPost = checkNotNull(data.value) { "Data may not be null!" }
        val wasLiked = currPost.likedByMe
        val likedPost =
            currPost.copy(likedByMe = !currPost.likedByMe, numLikes = currPost.numLikes + if (wasLiked) -1 else 1 )
        data.value = likedPost
    }

    override fun share() {
        val currPost = checkNotNull(data.value) { "Data may not be null!" }
        val sharedPost = currPost.copy(numShares = currPost.numShares + 1)
        data.value = sharedPost
    }

    override fun view() {
        val currPost = checkNotNull(data.value) { "Data may not be null!" }
        val viewedPost = currPost.copy(numViews = currPost.numViews + 1 )
        data.value = viewedPost
    }

    override fun setData(post: Post) {
        data.value = post
    }
}