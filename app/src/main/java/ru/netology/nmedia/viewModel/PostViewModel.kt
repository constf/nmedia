package ru.netology.nmedia.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class PostViewModel() : ViewModel() {
    private val repository: PostRepository = InMemoryPostRepository()

    val data by repository::data

    fun onLikeClicked(id: Long) = repository.like(id)
    fun onShareClicked(id: Long) = repository.share(id)
}