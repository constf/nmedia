package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostDetailsBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.convertToLiterals

typealias onClickCallback = (post: Post) -> Unit

class PostsAdapter(private val onLikeClicker: onClickCallback,
                   private val onShareClicker: onClickCallback,
                   private val onRemoveClicker: onClickCallback,
                   private val onEditClicker: onClickCallback,
                   private val onVideoClicker: onClickCallback,
                   private val onCardClicker: onClickCallback
                   ) : ListAdapter<Post, PostsAdapter.PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostDetailsBinding.inflate(inflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(private val binding: PostDetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            with(binding) {
                author.text = post.author
                mainText.text = post.content
                published.text = post.published

                imageShare.text = "   " + convertToLiterals(post.numShares)
                imageViewed.text = "   " + convertToLiterals(post.numViews)
                imageLikes.text = "   " + convertToLiterals(post.numLikes)
                // imageLikes.setButtonDrawable(getLikeIcon(post.likedByMe)) - не нужно, задаётся в XML layout'е
                imageLikes.isChecked = post.likedByMe
                imageLikes.setOnClickListener {
                    onLikeClicker(post)
                }
                imageShare.setOnClickListener {
                    onShareClicker(post)
                }
                imageMore.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.post_options)
                        setOnMenuItemClickListener{ item ->
                            when(item.itemId){
                                R.id.remove -> {
                                    onRemoveClicker(post)
                                    true
                                }
                                R.id.edit -> {
                                    onEditClicker(post)
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }

                postCardContainer.setOnClickListener {
                    onCardClicker(post)
                }

                if (post.ytVideo == null)
                    showVideoGroup.visibility = View.GONE
                else{
                    showVideoGroup.visibility = View.VISIBLE
                    videoImage.setOnClickListener {
                        onVideoClicker(post)
                    }
                    playVideo.setOnClickListener {
                        onVideoClicker(post)
                    }
                }
            }
        }

        /*   Obsolete now, with usage of MaterialButton, themes/styles and selector!
        @DrawableRes
        private fun getLikeIcon(liked: Boolean) =
            if (liked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
        */


    }

    private object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

}
