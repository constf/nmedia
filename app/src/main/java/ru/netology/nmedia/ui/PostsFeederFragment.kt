package ru.netology.nmedia.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentPostsFeederBinding
import ru.netology.nmedia.viewModel.PostViewModel

class PostsFeederFragment : Fragment() {
    private val viewModel by activityViewModels<PostViewModel>()
    private var _binding: FragmentPostsFeederBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        setFragmentResultListener(PostDetailFragment.REQUEST_EDIT_KEY) { requestKey, bundle ->
            if (requestKey != PostDetailFragment.REQUEST_EDIT_KEY) return@setFragmentResultListener
            val newContent = bundle.getString(PostDetailFragment.RESULT_KEY) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newContent)
        }


        viewModel.navigateToPostScreen.observe(this) {
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.app_fragment_container, PostDetailFragment())
            }
        }

        viewModel.editedPost.observe(this) { post -> // отслеживаем когда поступает пост на редактирование
            post ?: return@observe // пусто, уходим
            if (post.id == 0L) return@observe // это новый пост, уходим

            val outBundle: Bundle = Bundle(2).apply {
                putString(PostDetailFragment.KEY_RESULT_TYPE, PostDetailFragment.REQUEST_EDIT_KEY)
                putString(PostDetailFragment.INCOMING_KEY, post.content)
            }

            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.app_fragment_container, PostDetailFragment::class.java, outBundle, null)
            }
        }

        viewModel.showPost.observe(this) { post ->
            if (post == null) return@observe
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.app_fragment_container, CardPostDetailsFragment())
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPostsFeederBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter(
            viewModel::onLikeClicked, viewModel::onShareClicked, viewModel::onRemovePost,
            viewModel::editPost, viewModel::onShowVideo, viewModel::onShowPostCard
        )

        binding.postsList.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        binding.fabCreatePost.setOnClickListener {
            viewModel.onCreatePostClicked()
        }


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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

    companion object {
        const val TAG = "PostFeederFragment"
    }
}
