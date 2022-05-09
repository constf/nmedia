package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostDetailActivityBinding
import ru.netology.nmedia.databinding.PostDetailsBinding

class PostDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val incomingIntent = intent ?: return
        if (incomingIntent.action != Intent.ACTION_SEND) return

        val textToEdit = incomingIntent.getStringExtra(Intent.EXTRA_TEXT) ?: ""

        binding.editText.setText(textToEdit)

        binding.editText.requestFocus()
        binding.okFab.setOnClickListener {
            val resultIntent = Intent()
            val outText = binding.editText.text
            if (outText.isNullOrBlank()){
                setResult(Activity.RESULT_CANCELED, resultIntent)
            } else {
                val content = outText.toString()
                resultIntent.putExtra(RESULT_KEY, content)
                setResult(Activity.RESULT_OK, resultIntent)
            }
            finish()
        }
    }

    object ResultContract : ActivityResultContract<String, String?>(){
        override fun createIntent(context: Context, input: String): Intent {
            val newIntent = Intent(context, PostDetailActivity::class.java).apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, input)
                type = "text/plain"
            }

            return newIntent
        }

        override fun parseResult(resultCode: Int, intent: Intent?) =
            if (resultCode == Activity.RESULT_OK) {
                intent?.getStringExtra(RESULT_KEY)
            } else null

    }

    private companion object {
        private const val RESULT_KEY = "postNewContent"
    }

}