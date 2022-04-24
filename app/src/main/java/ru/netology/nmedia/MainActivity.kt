package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {
    var likesCount: Int = 999
    var shareCount: Int = 9_999_995
    var viewCount: Int = 150_000_000

    private val viewModel by viewModels<PostViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = getString(R.string.text_netology_motto),
            content = getString(R.string.text_main),
            published = getString(R.string.text_published_date),
            numLikes = likesCount,
            numShares = shareCount,
            numViews = viewCount
        )

        viewModel.data.observe(this){ post ->
            binding.render(post)
        }

        if (viewModel.data.value == null)
            viewModel.loadPost(post)

        with(binding) {
            imageLikes.setOnClickListener {
                viewModel.onLikeClicked()
            }

            imageShare.setOnClickListener {
                viewModel.onShareClicked()
            }
        }
    }


}

private fun ActivityMainBinding.render(post: Post) {
    author.text = post.author
    published.text = post.published
    mainText.text = post.content
    textLikes.text = convertToLiterals(post.numLikes)
    textShare.text = convertToLiterals(post.numShares)
    numberViewed.text = convertToLiterals(post.numViews)
    imageLikes.setImageResource(
        if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
    )
}

fun convertToLiterals(n: Int): String {
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