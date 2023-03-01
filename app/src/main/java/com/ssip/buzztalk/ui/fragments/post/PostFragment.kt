package com.ssip.buzztalk.ui.fragments.post

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.linkedin.android.spyglass.mentions.MentionSpan
import com.linkedin.android.spyglass.mentions.MentionsEditable
import com.linkedin.android.spyglass.suggestions.SuggestionsResult
import com.linkedin.android.spyglass.tokenization.QueryToken
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizer
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizerConfig
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentPostBinding
import com.ssip.buzztalk.models.customdata.choices
import com.ssip.buzztalk.models.post.request.PostBody
import com.ssip.buzztalk.models.usernames.UserNameForSearch
import com.ssip.buzztalk.utils.DialogClass
import com.ssip.buzztalk.utils.Status
import com.ssip.buzztalk.utils.Status.ERROR
import com.ssip.buzztalk.utils.Status.LOADING
import com.ssip.buzztalk.utils.Status.SUCCESS
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part
import okhttp3.RequestBody.Companion.asRequestBody

@AndroidEntryPoint
class PostFragment : Fragment(), QueryTokenReceiver {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: PostViewModel by viewModels()
    // private lateinit var choiceAdapter: PostChoiceAdapter
    var users = listOf<UserNameForSearch>()

    private val BUCKET = "users"

    var choices: MutableList<choices> = mutableListOf(
        choices("University", false),
        choices("School", false),
        choices("Industry", false),
        choices("Students", false),
        choices("Research Scholars", false),
        choices("Professionals", false),
        choices("Professors", false)
    )

    var imageUri: Uri? = null

    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it!!
        binding.imageView.setImageURI(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.postToolBar.topPostAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.imageView.setOnClickListener {
            contract.launch("image/*")
        }

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

        postViewModel.getUserNames()

        postViewModel.usernames.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    val userNames = response.data?.data?.usernames?.map { user ->
                        UserNameForSearch(user.userName)
                    }
                    users = userNames!!
                }
                Status.LOADING -> {
                    // @TODO - ADD PROGRESS BAR HERE
                }
                Status.ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        binding.editTextTextMultiLine.displayTextCounter(false)
        binding.editTextTextMultiLine.setQueryTokenReceiver(this)
        binding.editTextTextMultiLine.setHint("Start Writing Thoughts")
//        cities = CityLoader(resources)

        val config = WordTokenizerConfig.Builder()
        config.setExplicitChars("@")
        config.setMaxNumKeywords(2)
        config.setWordBreakChars(" ")
        binding.editTextTextMultiLine.setTokenizer(WordTokenizer(config.build()))

        postViewModel.addPost.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    Toast.makeText(context, "Content Posted", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
                Status.LOADING -> {
                    // @TODO - ADD Loading Bar
                }
                Status.ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        postViewModel.imageUpload.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                SUCCESS -> {
                    Log.d("IMAGE URL ", "onViewCreated: ${response.data}")
                    postViewModel.getImageUrl(response.data?.data!!.toString())
                }
                LOADING -> {

                }
                ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        postViewModel.imageUrl.observe(viewLifecycleOwner) { response ->
            when(response.status) {
                SUCCESS -> {
                    Log.d("IMAGE WHAT ", "onViewCreated: ${response.data}")

                    val toUpload = getData()
                    postViewModel.addPost(PostBody(
                      toUpload.content,
                      toUpload.taggedUsers,
                      toUpload.hashTags,
                      getChoice(),
                      isImage = true,
                      imageUrl = response.data?.data!!
                    ))
                }
                LOADING -> {

                }
                ERROR -> {
                    DialogClass(view).showDialog(response.message!!)
                }
            }
        }

        // binding.choicesRv.layoutManager = GridLayoutManager(context, 2)
        // binding.choicesRv.setHasFixedSize(true)
        // choiceAdapter = PostChoiceAdapter { change, value ->
        //     Log.d("CHANGE ", "choice ${change} ${value}")
        //     // choices[pos].isMarked = !choices[pos].isMarked
        //     // choiceAdapter.changeList(choices)
        // }
        // choiceAdapter.choices = choices
        // binding.choicesRv.adapter = choiceAdapter
    }

    private fun postContent() {
        var choices = ""

        // for (ch in choiceAdapter.choices) {
        //     if (ch.isMarked) {
        //         choices += "${ch.category}"
        //     }
        // }

       val text = MentionsEditable(binding.editTextTextMultiLine.text)
       val mentionSpans: List<MentionSpan> = text.mentionSpans
       var taggedUsers = mutableListOf<String>()
       for (span in mentionSpans) {
           val start: Int = text.getSpanStart(span)
           val end: Int = text.getSpanEnd(span)
           val currentText: String = text.subSequence(start, end).toString()
           taggedUsers.add(currentText)
           text.replace(
               start,
               end,
               "@$currentText "
           )
       }

       val hashTags = binding.hastag.text.toString().trim().splitToSequence("#").toList()

       val finalContent = "$text \n \n \n${binding.hastag.text.toString().trim()}"
//        Toast.makeText(context, finalContent, Toast.LENGTH_LONG).show()

       val tags = mutableListOf<String>()

       for(tag in hashTags) {
           if (tag.isNotEmpty() && tag.isNotBlank()) {
               tags.add(tag.trim())
           }
       }


        Toast.makeText(context, "${finalContent} ${taggedUsers} ${tags} ${getChoice()}", Toast.LENGTH_LONG).show()

        //
        if (imageUri != null) {
            postViewModel.uploadImage(upload())
        } else {
            postViewModel.addPost(PostBody(
              finalContent,
              taggedUsers,
              tags,
              getChoice()
            ))
        }

    }

    private fun getData(): PostBody {
        val text = MentionsEditable(binding.editTextTextMultiLine.text)
        val mentionSpans: List<MentionSpan> = text.mentionSpans
        var taggedUsers = mutableListOf<String>()
        for (span in mentionSpans) {
            val start: Int = text.getSpanStart(span)
            val end: Int = text.getSpanEnd(span)
            val currentText: String = text.subSequence(start, end).toString()
            taggedUsers.add(currentText)
            text.replace(
              start,
              end,
              "@$currentText "
            )
        }

        val hashTags = binding.hastag.text.toString().trim().splitToSequence("#").toList()

        val finalContent = "$text \n \n \n${binding.hastag.text.toString().trim()}"
//        Toast.makeText(context, finalContent, Toast.LENGTH_LONG).show()

        val tags = mutableListOf<String>()

        for(tag in hashTags) {
            if (tag.isNotEmpty() && tag.isNotBlank()) {
                tags.add(tag.trim())
            }
        }

        return PostBody(
          finalContent,
          taggedUsers,
          tags,
          getChoice()
        )
    }

    private fun getChoice(): List<String> {
        val choicesList = mutableListOf<String>()

        with(binding) {
            if (cbSchools.isChecked) {
                choicesList.add("SCHOOLS")
            }
            if(cbStudents.isChecked) {
                choicesList.add("STUDENTS")
            }
            if (cbProfessors.isChecked) {
                choicesList.add("PROFESSORS")
            }
            if (cbUniversity.isChecked) {
                choicesList.add("UNIVERSITY")
            }
            if (cbProfessional.isChecked) {
                choicesList.add("PROFESSIONALS")
            }
            if (cbResearch.isChecked) {
                choicesList.add("RESEARCH SCHOLARS")
            }
            if (cbIndustry.isChecked) {
                choicesList.add("INDUSTRY")
            }
        }

        return choicesList
    }

    override fun onQueryReceived(queryToken: QueryToken): List<String> {
        val buckets: List<String> = Collections.singletonList(BUCKET)
        val suggestions: List<UserNameForSearch> =  getSuggestions(queryToken)
        val result = SuggestionsResult(queryToken, suggestions)
        binding.editTextTextMultiLine.onReceiveSuggestionsResult(result, BUCKET)
        return buckets
    }

    fun getSuggestions(queryToken: QueryToken): List<UserNameForSearch> {
        val prefix = queryToken.keywords.lowercase(Locale.getDefault())
        val suggestions: MutableList<UserNameForSearch> = ArrayList<UserNameForSearch>()
        if (users != null) {
            for (suggestion in users) {
                val name: String =
                    suggestion.suggestiblePrimaryText.lowercase(Locale.getDefault())
                if (name.startsWith(prefix)) {
                    suggestions.add(suggestion)
                }
            }
        }
        return suggestions
    }

    private fun upload(): Part {
        val filesDir = requireContext().filesDir
        val file = File(filesDir, "image.png")
        val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

        return Part.createFormData("image", file.name, requestBody)
    }
}