package ru.netology.nmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostDetailsBinding
import ru.netology.nmedia.dto.Post

typealias onClickCallback = (post: Post) -> Unit

class PostsAdapter(private val onLikeClicker: onClickCallback,
                   private val onShareClicker: onClickCallback,
                   private val onRemoveClicker: onClickCallback,
                   private val onEditClicker: onClickCallback
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
            }
        }

        /*   Obsolete now, with usage of MaterialButton, themes/styles and selector!
        @DrawableRes
        private fun getLikeIcon(liked: Boolean) =
            if (liked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
        */

        private fun convertToLiterals(n: Int): String {
            var s: String
            var num1: Int = n
            var num2: Int = num1

            var i: Int = 0
            while (num1 >= 1000) {
                i++
                num2 = num1
                num1 = num1 / 1000
            }

            s  = num1.toString()

            if (i == 0) return s

            if (num1 < 10) {
                val hundreds = (num2 % 1000) / 100
                if (hundreds > 0) {
                    s += "."
                    s += hundreds.toString()
                }
            }

            s += when(i){
                1 -> "K"
                2 -> "M"
                3 -> "B"
                4 -> "T"
                else -> "G" // Gooogol!
            }

            return s
        }
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
