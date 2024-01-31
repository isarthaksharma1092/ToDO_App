package com.isarthaksharma.todo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.isarthaksharma.todo.databinding.ActivitySplashScreenBinding

class splash_Screen : AppCompatActivity() {
    lateinit var binding:ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoPath = "android.resource://" + packageName + "/" + R.raw.todo_splash
        val videoUrl = Uri.parse(videoPath)
        binding.splashScreen.setVideoURI(videoUrl)
        binding.splashScreen.start()

        binding.splashScreen.setOnCompletionListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
