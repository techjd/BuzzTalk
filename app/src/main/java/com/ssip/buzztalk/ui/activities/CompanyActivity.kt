package com.ssip.buzztalk.ui.activities

import android.content.Intent
import android.net.Uri
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
            Intent(this, PostContent::class.java).also {
                it.putExtra("EXTRA_PERSON", 1)
                it.putExtra("CATEGORY", 10)
                startActivity(it)
            }
        }
        binding.email.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:reliance@gmail.com")
            }
            startActivity(Intent.createChooser(emailIntent, "Send email"))
        }

        binding.visitUs.setOnClickListener {
            val url = "https://www.reliancedigital.in/"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        binding.callUs.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9106369512"))
            startActivity(intent)

        }

    }
}