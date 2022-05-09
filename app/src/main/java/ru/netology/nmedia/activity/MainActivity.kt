package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<PostViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel::onLikeClicked, viewModel::onShareClicked, viewModel::onRemovePost, viewModel::editPost, viewModel::onShowVideo)
        binding.postsList.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }


        binding.fabCreatePost.setOnClickListener {
            viewModel.onCreatePostClicked()
        }


        viewModel.sharePostContent.observe(this) { postContent ->
            val intentShare = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val showChoose = Intent.createChooser(intentShare, getString(R.string.chooser_share_post_string))
            startActivity(intentShare)
        }

        viewModel.showExternalVideo.observe(this) { postContent ->
            val intentVideo = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=${postContent.ytVideo}")
            )
            startActivity(intentVideo)
        }

        val postContentActLauncher = registerForActivityResult(
            PostDetailActivity.ResultContract
        ){ postContent->
            postContent ?: return@registerForActivityResult
            viewModel.onSaveButtonClicked(postContent)
        }

        viewModel.navigateToPostScreen.observe(this) {
            postContentActLauncher.launch("")
        }

        viewModel.editedPost.observe(this) { post -> // отслеживаем когда поступает пост на редактирование
            post ?: return@observe // пусто, уходим
            if (post.id == 0L) return@observe // это новый пост, уходим
            postContentActLauncher.launch(post.content)
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
