package ru.netology.nmedia.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.AppActivityBinding

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = AppActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportFragmentManager.findFragmentByTag(PostsFeederFragment.TAG) == null) {
            supportFragmentManager.commit {
                add(R.id.app_fragment_container, PostsFeederFragment(), PostsFeederFragment.TAG)
            }
        }
    }
}
