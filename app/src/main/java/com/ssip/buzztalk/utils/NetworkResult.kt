package com.ssip.buzztalk.utils

sealed class NetworkResult<T>(val status: Status, val data: T?, val message: String?) {
    class Success<T>(data: T) : NetworkResult<T>(Status.SUCCESS, data, null)
    class Error<T>(message: String?, data: T? = null) :
        NetworkResult<T>(Status.ERROR, null, message)
    class Loading<T> : NetworkResult<T>(Status.LOADING, null, null)
}
