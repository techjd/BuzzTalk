package com.ssip.buzztalk.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ssip.buzztalk.databinding.ActivityUniversityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UniversityActivity : AppCompatActivity() {
    lateinit var binding: ActivityUniversityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUniversityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.newPost.setOnClickListener {
            Intent(this, postContent::class.java).also {
                it.putExtra("EXTRA_PERSON", 2)
                startActivity(it)
            }
        }
    }
}