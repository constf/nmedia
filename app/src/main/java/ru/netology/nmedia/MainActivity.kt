package ru.netology.nmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var likesCount: Int = 999
    var shareCount: Int = 9_999_995
    var viewCount: Int = 150_000_000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = getString(R.string.text_netology_motto),
            content = getString(R.string.text_main),
            published = getString(R.string.text_published_date)
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            mainText.text = post.content
            textLikes.text = convertToLiterals(likesCount)
            textShare.text = convertToLiterals(shareCount)
            numberViewed.text = convertToLiterals(viewCount)

            if (post.likedByMe) imageLikes.setImageResource(R.drawable.ic_baseline_favorite_24)

            imageLikes.setOnClickListener {
                if (post.likedByMe)
                    likesCount--
                else
                    likesCount++

                post.likedByMe = !post.likedByMe
                imageLikes.setImageResource(
                    if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                )

                // textLikes.text = likesCount.toString()
                textLikes.text = convertToLiterals(likesCount)
            }

            imageShare.setOnClickListener {
                shareCount++
                textShare.text = shareCount.toString()
                textShare.text = convertToLiterals(shareCount)
            }
        }
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
}
