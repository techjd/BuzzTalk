package com.ssip.buzztalk.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ssip.buzztalk.databinding.ActivityCompanyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompanyActivity : AppCompatActivity() {
    lateinit var binding: ActivityCompanyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.NewPost.setOnClickListener {
            Intent(this, postContent::class.java).also {
                it.putExtra("EXTRA_PERSON", 1)
                startActivity(it)
            }
        }

    }
}