package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.databinding.IntentHandlerActivityBinding

class IntentHandlerActivity : AppCompatActivity() {
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val binding = IntentHandlerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // We must have an incoming intent for this Activity to run properly
        val incomingIntent = intent?: return
        if (incomingIntent.action != Intent.ACTION_SEND) return

        // Incoming text must be present
        val text = incomingIntent.getStringExtra(Intent.EXTRA_TEXT)
        if (text.isNullOrBlank()) return

        // Showing what we've just received in incoming Intent
        Snackbar.make(binding.root, text, Snackbar.LENGTH_INDEFINITE)
            .setAction(android.R.string.ok) { finish() } // Standard Android dialog teplate
            .show() // Show snack bar
    }
}