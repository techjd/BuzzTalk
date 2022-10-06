package com.ssip.buzztalk.ui.fragments.auth.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssip.buzztalk.R
import com.ssip.buzztalk.databinding.FragmentAddEmailBinding
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

class AddEmailFragment : Fragment() {

    private var _binding: FragmentAddEmailBinding? = null
    private val binding get() = _binding!!
    val userSignUpViewModel: UserSignUpViewModel by activityViewModels()
    var citySpinner: Spinner? = null
    var stateSpinner: Spinner? = null
    var stringArrayState: ArrayList<String>? = null
    var stringArrayCity: ArrayList<String>? = null
    var spinnerStateValue: String? = null
    var city: String? = null
    var state: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        citySpinner = binding.citySpinner
        stateSpinner = binding.stateSpinner
        init()
        binding.next.setOnClickListener {
            val uname = binding.username.text.toString().trim()

            if (validateText(uname)) {
                if (validateUsername(uname)) {
                    findNavController().navigate(R.id.action_addEmailFragment_to_addPasswordFragment)
                } else {
                    Toast.makeText(context, "Not a Valid User name", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(context, "Please Fill User name", Toast.LENGTH_LONG).show()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_addEmailFragment_to_addFirstLastNameFragment)
                }
            })
    }

    private fun init() {
        stringArrayState = java.util.ArrayList()
        stringArrayCity = java.util.ArrayList()

        //set city adapter

        //set city adapter
        val adapterCity = ArrayAdapter(
            requireContext(), R.layout.spinnertextview,
            stringArrayCity!!
        )
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner!!.adapter = adapterCity


        //Get state json value from assets folder


        //Get state json value from assets folder
        try {
            val obj = JSONObject(loadJSONFromAssetState())
            val m_jArry = obj.getJSONArray("statelist")
            for (i in 0 until m_jArry.length()) {
                val jo_inside = m_jArry.getJSONObject(i)
                val state = jo_inside.getString("State")
                val id = jo_inside.getString("id")
                stringArrayState!!.add(state)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val adapter = ArrayAdapter(
            requireContext(), R.layout.spinnertextview,
            stringArrayState!!
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        stateSpinner!!.adapter = adapter


        //state spinner item selected listner with the help of this we get selected value
        stateSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val item = parent.getItemAtPosition(position)
                val Text = stateSpinner!!.selectedItem.toString()
                spinnerStateValue = stateSpinner!!.selectedItem.toString()
                state = spinnerStateValue
                Log.e("SpinnerStateValue", spinnerStateValue!!)
                stringArrayCity!!.clear()
                try {
                    val obj = JSONObject(loadJSONFromAssetCity())
                    val m_jArry = obj.getJSONArray("citylist")
                    for (i in 0 until m_jArry.length()) {
                        val jo_inside = m_jArry.getJSONObject(i)
                        val state = jo_inside.getString("State")
                        val cityid = jo_inside.getString("id")
                        if (spinnerStateValue.equals(state, ignoreCase = true)) {
                            val city = jo_inside.getString("city")
                            stringArrayCity!!.add(city)
                        }
                    }

                    //notify adapter city for getting selected value according to state
                    adapterCity.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        citySpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                val spinnerCityValue = citySpinner!!.selectedItem.toString()
                Log.e("SpinnerCityValue", spinnerCityValue)
                city = spinnerCityValue
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    fun loadJSONFromAssetState(): String? {
        var json: String?
        json = try {
            val state: InputStream = requireContext().assets.open("state.json")
            val size = state.available()
            val buffer = ByteArray(size)
            state.read(buffer)
            state.close()
            String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun loadJSONFromAssetCity(): String? {
        var json: String?
        json = try {
            val city: InputStream = requireContext().assets.open("cityState.json")
            val size = city.available()
            val buffer = ByteArray(size)
            city.read(buffer)
            city.close()
            String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun validateText(inputname: String): Boolean {
        if (inputname.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun validateUsername(inputname: String): Boolean {
        if (inputname.length > 5) {
            return true
        }
        return false
    }

}