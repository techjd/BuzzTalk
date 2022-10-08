package com.ssip.buzztalk.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ssip.buzztalk.databinding.ActivityPostContentBinding

class postContent : AppCompatActivity() {
    lateinit var binding: ActivityPostContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostContentBinding.inflate(layoutInflater)
        val view = binding.root

        val person = intent.getSerializableExtra("EXTRA_PERSON")
        val num = person.toString()

        if (num == "2") {
            binding.cbUniversity.text = "Industry"
        }
        setContentView(view)
    }
}