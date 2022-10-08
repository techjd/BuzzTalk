package com.ssip.buzztalk.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.ssip.buzztalk.R
import com.ssip.buzztalk.api.newOpportunities
import com.ssip.buzztalk.databinding.ActivityPostContentBinding
import com.ssip.buzztalk.models.chat.request.messages.To
import com.ssip.buzztalk.utils.Constants
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PostContent : AppCompatActivity() {
    lateinit var binding: ActivityPostContentBinding

    private val postOppoViewModel: PostOppoViewModel by viewModels()

    private var category: Int? = null

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


//        category = intent.getIntExtra("CATEGORY")
        Log.d("MSG", "onCreate: $category")
        binding.postToolBar.topPostAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.postMenu -> {
                    postContent()
//                    findNavController().navigate(R.id.action_homeFragment_to_chatsFragment)
                    true
                }
                else -> false
            }
        }


            postOppoViewModel.postOppoCompany.observe(this) { response ->
                when(response.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(this, "Post Added", Toast.LENGTH_LONG).show()
//                        Snackbar.make(this, "Post Added", 1500)
                        finish()
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        DialogClass(view).showDialog(response.message!!)
                    }
                }
            }

            postOppoViewModel.postOppoUniversity.observe(this) { response ->
                when(response.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(this, "Post Added", Toast.LENGTH_LONG).show()
//                        Snackbar.make(this, "Post Added", 1500)
                        finish()
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        DialogClass(view).showDialog(response.message!!)
                    }
                }
            }


    }

    private fun postContent() {
        val title = binding.PostTitle.editableText.toString().trim()
        val content = binding.editTextTextMultiLine.editableText.toString().trim()
        val choices = getChoices()


        if (category == 10) {
            postOppoViewModel.postOppoCompany(newOpportunities(
                postTitle = title,
                postContent = content,
                to = choices
            ))
        } else {
            postOppoViewModel.postOppoUniversitt(newOpportunities(
                postTitle = title,
                postContent = content,
                to = choices
            ))
        }
    }

    private fun getChoices(): List<String> {
        var choice: MutableList<String> = mutableListOf()
        if (binding.cbProfessional.isChecked) {
            choice.add("${Constants.PROFESSIONAL_USERS}")
        }
        if(binding.cbStudents.isChecked) {
            choice.add("${Constants.STUDENTS}")
        }
        if(binding.cbResearch.isChecked) {
            choice.add("${Constants.RESEARCH_SCHOLARS}")
        }
        if (binding.cbProfessors.isChecked) {
            choice.add("${Constants.PROFESSORS}")
        }

        return choice as List<String>
    }
}