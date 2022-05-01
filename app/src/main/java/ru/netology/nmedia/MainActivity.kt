package ru.netology.nmedia

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.PostsAdapter
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<PostViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel::onLikeClicked, viewModel::onShareClicked, viewModel::onRemovePost)
        binding.postsList.adapter = adapter

        binding.postAddImage.setOnClickListener {
            with(binding.newContent) {
                if (text.isNullOrBlank()){
                    Toast.makeText(this@MainActivity, "Content can not be empty!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.updateEdited(text.toString())
                viewModel.savePost()

                setText("")
                clearFocus()
            }
        }

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }
}


