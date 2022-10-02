package com.ssip.buzztalk.api

import com.ssip.buzztalk.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(): Interceptor {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", tokenManager.getTokenWithBearer()!!)
        return chain.proceed(request.build())
    }
}