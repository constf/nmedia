package ru.netology.nmedia.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostDetailsBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.convertToLiterals
import ru.netology.nmedia.viewModel.PostViewModel

class CardPostDetailsFragment: Fragment() {
    private val viewModel by activityViewModels<PostViewModel>()

    private var _binding: CardPostDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CardPostDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var post: Post? = viewModel.showPost.value

        if (post == null) return

        setFragmentResultListener(PostDetailFragment.REQUEST_CARDEDIT_KEY) { requestKey, bundle ->
            if (requestKey != PostDetailFragment.REQUEST_CARDEDIT_KEY) return@setFragmentResultListener
            val newContent = bundle.getString(PostDetailFragment.RESULT_KEY) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newContent)
            binding.mainText.setText(newContent)
        }

        viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
            val intentShare = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val showChoose = Intent.createChooser(intentShare, getString(R.string.chooser_share_post_string))
            startActivity(intentShare)
        }

        binding.mainText.setText(post.content)
        binding.imageShare.text = "   " + convertToLiterals(post.numShares)
        binding.imageViewed.text = "   " + convertToLiterals(post.numViews)
        binding.imageLikes.text = "   " + convertToLiterals(post.numLikes)
        binding.imageLikes.isChecked = post.likedByMe

        binding.imageLikes.setOnClickListener {
            viewModel.onLikeClicked(post)
            val updated = viewModel.getPost(post.id)
            if (updated == null) return@setOnClickListener
            binding.imageLikes.text = "   " + convertToLiterals(updated.numLikes)
        }

        binding.imageShare.setOnClickListener {
            viewModel.onShareClicked(post)
            val updated = viewModel.getPost(post.id)
            if (updated == null) return@setOnClickListener
            binding.imageShare.text = "   " + convertToLiterals(updated.numShares)
        }

        binding.imageMore.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.post_options)
                setOnMenuItemClickListener{ item ->
                    when(item.itemId){
                        R.id.remove -> {
                            viewModel.onRemovePost(post)
                            parentFragmentManager.popBackStack()
                            true
                        }
                        R.id.edit -> {
                            viewModel.editPost(post)
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }

        if (post.ytVideo == null)
            binding.showVideoGroup.visibility = View.GONE
        else{
            binding.showVideoGroup.visibility = View.VISIBLE
            binding.videoImage.setOnClickListener {
                viewModel.onShowVideo(post)
            }
            binding.playVideo.setOnClickListener {
                viewModel.onShowVideo(post)
            }
        }

        viewModel.editedPost.observe(viewLifecycleOwner) { post -> // отслеживаем когда поступает пост на редактирование
            post ?: return@observe // пусто, уходим
            if (post.id == 0L) return@observe // это новый пост, уходим

            val outBundle: Bundle = Bundle(2).apply {
                putString(PostDetailFragment.KEY_RESULT_TYPE, PostDetailFragment.REQUEST_CARDEDIT_KEY)
                putString(PostDetailFragment.INCOMING_KEY, post.content)
            }

            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.app_fragment_container, PostDetailFragment::class.java, outBundle, null)
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "CardPostDetailsFragment"
    }
}