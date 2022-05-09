package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent

val empty = Post(
    id = 0L,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    numLikes = 999,
    numShares = 9_999_995,
    numViews = 23_195
)


class PostViewModel() : ViewModel() {
    private val repository: PostRepository = InMemoryPostRepository()
    val data by repository::data
    val editedPost = MutableLiveData<Post?>(null)
    var tempText: String = ""
    var editingNow: Boolean = false

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostScreen = SingleLiveEvent<Unit>()
    val showExternalVideo = SingleLiveEvent<Post>()

    fun onLikeClicked(post: Post) = repository.like(post.id)

    fun onRemovePost(post: Post) = repository.remove(post.id)

    fun onShareClicked(post: Post) {
        sharePostContent.value = post.content
        repository.share(post.id)
    }

    fun updateEdited(content: String) {
        editedPost.value?.let {
            val text = content.trim()
            if (it.content == text)
                return

            editedPost.value = it.copy(content = text)
        }
    }

    fun savePost() {
        editedPost.value?.let {
            repository.save(it)
        }
        editedPost.value = empty
    }

    fun editPost(post: Post) {
        editedPost.value = post
    }


    fun onCreatePostClicked() {
        navigateToPostScreen.call()
    }

    fun onSaveButtonClicked(postContent: String) {
        if (postContent.isBlank()) return

        val post = editedPost.value?.copy(content = postContent) ?: Post(
            id = 0L,
            author = "Me",
            content = postContent,
            published = "Today"
        )
        repository.save(post)

        editedPost.value = null
    }

    fun onShowVideo(post: Post) {
        showExternalVideo.value = post
    }


}