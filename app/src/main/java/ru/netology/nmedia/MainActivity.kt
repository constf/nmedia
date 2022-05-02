package ru.netology.nmedia

import android.app.Activity
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
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

        val adapter = PostsAdapter(viewModel::onLikeClicked, viewModel::onShareClicked, viewModel::onRemovePost, viewModel::editPost)
        binding.postsList.adapter = adapter

        binding.postAddImage.setOnClickListener {
            with(binding.newContent) {
                if (text.isNullOrBlank()){
                    Toast.makeText(this@MainActivity, "Content can not be empty!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.updateEdited(text.toString())
                viewModel.savePost()

                setText(viewModel.tempText)
                clearFocus()
                AndroidUtils.hideSoftKeyboard(this)

                if (binding.editCancelGroup.isVisible) {
                    binding.editCancelGroup.visibility = View.GONE
                }
                viewModel.editingNow = false
            }
        }

        binding.editCancelSymbol.setOnClickListener {
            binding.editCancelGroup.visibility = View.GONE
            binding.newContent.setText(viewModel.tempText)
            binding.newContent.clearFocus()
            viewModel.setEmptyPostAfterEdit()
            viewModel.editingNow = false
            AndroidUtils.hideSoftKeyboard(binding.newContent)
        }

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }


        viewModel.editedPost.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }

            // Редкатирование поста
            binding.editCancelGroup.visibility = View.VISIBLE
            binding.editCancelText.setText(post.content.toString())

            with(binding.newContent) {
                if (!viewModel.editingNow) {
                    viewModel.tempText = text.toString()
                    viewModel.editingNow = true
                }
                setText(post.content)
                requestFocus()
            }
        }
    }


    object AndroidUtils {
        fun hideSoftKeyboard(view: View) {
            val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun showSoftKeyboard(view: View) {
            val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, 0)
        }
    }
}
