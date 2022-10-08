package com.ssip.buzztalk.ui.activities

import android.content.Intent
import android.net.Uri
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
        binding.email.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:adit@gmail.com")
            }
            startActivity(Intent.createChooser(emailIntent, "Send email"))
        }

        binding.visitUs.setOnClickListener {
            val url = "https://www.adit.ac.in/"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        binding.callUs.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9925263412"))
            startActivity(intent)

        }
    }
}