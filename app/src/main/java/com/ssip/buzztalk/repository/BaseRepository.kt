package com.ssip.buzztalk.repository

import android.util.Log
import com.google.gson.Gson
import com.ssip.buzztalk.models.failure.Failure
import com.ssip.buzztalk.utils.ErrorResponse
import okio.IOException
import com.ssip.buzztalk.utils.NetworkManager
import com.ssip.buzztalk.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

abstract class BaseRepository {

    @Inject
    lateinit var networkManager: NetworkManager

    @Inject
    lateinit var errorResponse: ErrorResponse

    suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Response<T>): NetworkResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                if (networkManager.hasInternetConnection()) {
                    val data: Response<T> = apiToBeCalled()
                    if (data.isSuccessful) {
                        NetworkResult.Success(data.body()!!)
                    } else {
                        val error = errorResponse.giveErrorResult(data.errorBody()!!)
                        NetworkResult.Error(error.message)
                    }
                } else {
                    NetworkResult.Error("No Internet Connection")
                }
            } catch (t: Throwable) {
                when(t) {
                    is IOException -> NetworkResult.Error("Network Failure")
                    else -> NetworkResult.Error("Some Error Occurred , Please Try Again Later")
                }
            }
        }
    }
}