package com.ssip.buzztalk.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssip.buzztalk.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UniversityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_university)
    }
}