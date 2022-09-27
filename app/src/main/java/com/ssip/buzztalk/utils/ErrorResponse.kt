package com.ssip.buzztalk.utils

import com.google.gson.Gson
import com.ssip.buzztalk.models.failure.Failure
import okhttp3.ResponseBody
import org.json.JSONObject
import javax.inject.Inject


class ErrorResponse @Inject constructor(private val gson: Gson) {

    fun giveErrorResult(errorBody: ResponseBody): Failure {
        val errorObject = JSONObject(errorBody.charStream().readText())
        return gson.fromJson(errorObject.toString(), Failure::class.java)
    }
}